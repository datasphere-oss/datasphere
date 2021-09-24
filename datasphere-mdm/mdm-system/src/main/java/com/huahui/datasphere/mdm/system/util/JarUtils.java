/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.system.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;

/**
 * Utility class for JAR files.
 *
 * @author ilya.bykov
 */
public class JarUtils {

    public static final String UNIDATA_INTEGRATION = "unidata-integration";
    /**
     * This class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JarUtils.class);

    /**
     * Default constructor.
     */
    private JarUtils() {
        super();
    }

    /**
     * Find classes in jar.
     *
     * @param <T>
     *            the generic type
     * @param baseInterface
     *            the base interface
     * @param filePath
     *            the file path
     * @return the list
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> List<Class<T>> findClassesInJar(final Class<T> baseInterface, final String filePath)
            throws IOException, ClassNotFoundException {

        final List<Class<T>> classesTobeReturned = new ArrayList<>();
        if (!StringUtils.isBlank(filePath)) {

            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final URL url = new URL("jar:file:" + filePath + "!/");

            try (final URLClassLoader ucl = new URLClassLoader(new URL[] { url }, classLoader);
                 final JarInputStream jarFile = new JarInputStream(new FileInputStream(filePath))) {

                JarEntry jarEntry;
                while (true) {

                    jarEntry = jarFile.getNextJarEntry();
                    if (jarEntry == null)
                        break;

                    // check only compiled classes
                    if (jarEntry.getName().endsWith(".class")) {

                        String classname = jarEntry.getName().replace('/', '.');
                        classname = classname.substring(0, classname.length() - 6);

                        // skip sub classes
                        final Class<?> myLoadedClass = Class.forName(classname, true, ucl);
                        if (!classname.contains("$") && baseInterface.isAssignableFrom(myLoadedClass)) {
                            classesTobeReturned.add((Class<T>) myLoadedClass);
                        }
                    }
                }
            }
        }

        return classesTobeReturned;
    }

    /**
     * Save file to lib folder.
     *
     * @param attachment
     *            the attachment
     * @return the java.nio.file. path
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static final java.nio.file.Path saveFileToLibFolder(String fileName, InputStream in) throws IOException{
        //String fileName = attachment.getContentDisposition().getParameter("filename");
        Files.createDirectories(Paths.get(System.getProperty("catalina.base") + File.separator + UNIDATA_INTEGRATION+ File.separator + "custom_cf"));
        java.nio.file.Path path = Paths
                .get(System.getProperty("catalina.base") + File.separator + UNIDATA_INTEGRATION+ File.separator + "custom_cf" + File.separator + fileName);
        Files.deleteIfExists(path);
        //InputStream in = attachment.getObject(InputStream.class);
        Files.copy(in, path);
        return path;
    }
    /**
     * Extracts jar content to a map (without manifest).
     * @param jis the {@link JarInputStream} instance
     * @return map
     * @throws IOException
     */
    public static Map<String, byte[]> streamToMap(@Nonnull JarInputStream jis) throws IOException {

        Objects.requireNonNull(jis, "JarInputStream can not be null.");

        Map<String, byte[]> result = new HashMap<>();

        JarEntry je = null;
        while ((je = jis.getNextJarEntry()) != null) {

            String name = je.getName();
            byte[] payload = IOUtils.toByteArray(jis);

            result.put(name, payload);

            jis.closeEntry();
        }
        return result;
    }
    /**
     * @author theseusyang on Jan 21, 2021
     * A modified version of com.hazelcast.jet.impl.deployment.JetClassLoader.
     */
    public static class SingleJarClassLoader extends ClassLoader {
        /**
         * "Protocol" name for ClassLoader's findResource overriding.
         */
        private static final String LOCAL_STREAM_PROTOCOL = "local-stream";
        /**
         * Resources.
         */
        private final Map<String, byte[]> resources;
        /**
         * The manifest just for the case.
         */
        private final Manifest manifest;
        /**
         * Handler.
         */
        private SingleJarURLStreamHandler singleJarURLStreamHandler;
        /**
         * Constructor.
         * @param parent parent class loader
         * @param jis the {@link JarInputStream} instance
         */
        public SingleJarClassLoader(@Nonnull String name, @Nullable ClassLoader parent, @Nonnull JarInputStream jis) {
            super(name, parent == null ? Thread.currentThread().getContextClassLoader() : parent);

            try {
                this.resources = streamToMap(jis);
                this.manifest = jis.getManifest();
            } catch (IOException e) {
                throw new PlatformFailureException("Cannot read JarInputStream content.",
                        e, SystemExceptionIds.EX_SYSTEM_CANNOT_READ_JAR_CONTENT);
            }

            singleJarURLStreamHandler = new SingleJarURLStreamHandler();
        }
        /**
         * @return the manifest
         */
        public Manifest getManifest() {
            return manifest;
        }
        /**
         * Filters classes from the content hold.
         * @return list of java classes
         */
        public List<String> filterClasses() {
            return resources.keySet().stream()
                .filter(name -> name.endsWith(".class"))
                .collect(Collectors.toList());
        }
        /**
         * Finds classes,
         * which can be assigned to variables of a type,
         * given in the parameter.
         * This method lists only classes, served by this class loader.
         * No parent classloader calls are made.
         * @param check the class to check for assign ability
         * @return list of classes or empty list
         */
        public List<Class<?>> filterAssignable(Class<?> check) {
            return resources.keySet().stream()
                .filter(name -> name.endsWith(".class"))
                .map(name -> {

                    String binaryName = name.replace('/', '.');
                    Class<?> cl = findLoadedClass(binaryName);

                    if (cl == null) {
                        try {
                            cl = findClass(binaryName);
                        } catch (ClassNotFoundException cnfe) {
                            // Suppressed, since we're pretty
                            // satisfied with CNFE.
                        }
                    }

                    return cl;
                })
                .filter(Objects::nonNull)
                .filter(check::isAssignableFrom)
                .collect(Collectors.toList());
        }
        /**
         * Finds classes,
         * which can be assigned to variables of a type,
         * given in the parameter.
         * This method lists only classes, served by this class loader.
         * No parent classloader calls are made.
         * @param check the class to check for assign ability
         * @return list of class names (binary name) or empty list
         */
        public List<String> filterAssignableNames(Class<?> check) {
            return resources.keySet().stream()
                .filter(name -> name.endsWith(".class"))
                .map(name -> {

                    String binaryName = name
                            .replace('/', '.')
                            .substring(0, name.length() - 6);

                    Class<?> cl = findLoadedClass(binaryName);

                    if (cl == null) {
                        try {
                            cl = findClass(binaryName);
                        } catch (ClassNotFoundException cnfe){
                            // Suppressed, since we're pretty
                            // satisfied with CNFE.
                        }
                    }

                    return cl;
                })
                .filter(Objects::nonNull)
                .filter(check::isAssignableFrom)
                .map(Class::getName)
                .collect(Collectors.toList());
        }
        /**
         * Reserved for future use.
         */
        public void cleanup() {
            resources.clear();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {

            if (StringUtils.isBlank(name)) {
                return null;
            }

            byte[] classBytes = resources.get(name.replace('.', '/') + ".class");
            if (classBytes == null) {
                throw new ClassNotFoundException(name + ". Missing in jar file or on classpath?");
            }

            return defineClass(name, classBytes, 0, classBytes.length);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        protected URL findResource(String name) {

            if (StringUtils.isBlank(name) || !resources.containsKey(name)) {
                return null;
            }

            try {
                return new URL(LOCAL_STREAM_PROTOCOL, null, -1, name, singleJarURLStreamHandler);
            } catch (MalformedURLException e) {
                // this should never happen with custom URLStreamHandler
                LOGGER.warn("MalformedURLException caught in JarStreamClassLoader!", e);
            }

            return null;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        protected Enumeration<URL> findResources(String name) {

            return new Enumeration<URL>() {

                private URL url = findResource(name);

                @Override
                public boolean hasMoreElements() {
                    return url != null;
                }

                @Override
                public URL nextElement() {

                    if (url == null) {
                        throw new NoSuchElementException();
                    }

                    try {
                        return url;
                    } finally {
                        url = null;
                    }
                }
            };
        }
        /**
         * Local stream "protocol" URL handler.
         */
        private final class SingleJarURLStreamHandler extends URLStreamHandler {
            /**
             * {@inheritDoc}
             */
            @Override
            protected URLConnection openConnection(URL url) {
                return new SingleJarURLConnection(url);
            }
        }
        /**
         * Local URL connection.
         */
        private final class SingleJarURLConnection extends URLConnection {

            private SingleJarURLConnection(URL url) {
                super(url);
            }

            @Override
            public void connect() {
                // Nothing needed here.
            }

            @Override
            public InputStream getInputStream() {

                byte[] classData = resources.get(url.getFile());
                if (classData == null) {
                    return null;
                }

                return new ByteArrayInputStream(classData);
            }
        }
    }
}