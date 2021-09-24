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

package com.huahui.datasphere.mdm.core.configuration.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.util.Assert;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * // TODO: Switch to work on Jackson implementation org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer
 * Custom serializer based on "com.thoughtworks.xstream:xstream:1.4.11.1"
 * Custom code created from default used in spring-batch '3.0.9.RELEASE'
 * https://github.com/spring-projects/spring-batch/blob/3.0.9.RELEASE/spring-batch-core/src/main/java/org/springframework/batch/core/repository/dao/XStreamExecutionContextStringSerializer.java
 *
 * @author theseusyang
 * Extend default XStream serializer to overcome java.time.* serialization problem.
 */
@Deprecated
public class CustomJobExecutionContextSerializer implements ExecutionContextSerializer, InitializingBean {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJobExecutionContextSerializer.class);
    /**
     * Supertype stuff.
     */
    private ReflectionProvider reflectionProvider = null;
    /**
     * Supertype stuff.
     */
    private HierarchicalStreamDriver hierarchicalStreamDriver;
    /**
     * Supertype stuff.
     */
    private XStream xstream;
    /**
     * Constructor.
     */
    public CustomJobExecutionContextSerializer() {
        super();
    }

    public void setReflectionProvider(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    public void setHierarchicalStreamDriver(HierarchicalStreamDriver hierarchicalStreamDriver) {
        this.hierarchicalStreamDriver = hierarchicalStreamDriver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public synchronized void init() throws Exception {

        if (hierarchicalStreamDriver == null) {
            this.hierarchicalStreamDriver = new JettisonMappedXmlDriver();
        }

        if (reflectionProvider == null) {
            xstream =  new XStream(hierarchicalStreamDriver);
        } else {
            xstream = new XStream(reflectionProvider, hierarchicalStreamDriver);
        }

        xstream.registerConverter(new LocalDateConverter());
        xstream.registerConverter(new LocalTimeConverter());
        xstream.registerConverter(new LocalDateTimeConverter());
    }

    /**
     * Serializes the passed execution context to the supplied OutputStream.
     *
     * @param context
     * @param out
     * @see Serializer#serialize(Object, OutputStream)
     */
    @Override
    public void serialize(Map<String, Object> context, OutputStream out) throws IOException {
        Assert.notNull(context);
        Assert.notNull(out);

        out.write(xstream.toXML(context).getBytes());
    }

    /**
     * Deserializes the supplied input stream into a new execution context.
     *
     * @param in
     * @return a reconstructed execution context
     * @see Deserializer#deserialize(InputStream)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> deserialize(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return (Map<String, Object>) xstream.fromXML(sb.toString());
    }

    /**
     * @author theseusyang
     * {@linkplain LocalDate} support.
     */
    public static class LocalDateConverter implements Converter {

        @Override
        public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
            return LocalDate.class.isAssignableFrom(type);
        }

        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            LocalDate ld = (LocalDate) source;
            writer.setValue(ld.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            try {
                return LocalDate.parse(reader.getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                LOGGER.warn("Cannot unmarshal local date value {}. Exception caught.", reader.getValue(), e);
            }
            return null;
        }
    }

    /**
     * @author theseusyang
     * {@linkplain LocalTime} support.
     */
    public static class LocalTimeConverter implements Converter {
        @Override
        public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
            return LocalTime.class.isAssignableFrom(type);
        }

        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            LocalTime lt = (LocalTime) source;
            writer.setValue(lt.format(DateTimeFormatter.ISO_LOCAL_TIME));
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            try {
                return LocalTime.parse(reader.getValue(), DateTimeFormatter.ISO_LOCAL_TIME);
            } catch (Exception e) {
                LOGGER.warn("Cannot unmarshal local time value {}. Exception caught.", reader.getValue(), e);
            }
            return null;
        }
    }

    /**
     * @author theseusyang
     * {@linkplain LocalDateTime} support.
     */
    public static class LocalDateTimeConverter implements Converter {
        @Override
        public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
            return LocalDateTime.class.isAssignableFrom(type);
        }

        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            LocalDateTime ldt = (LocalDateTime) source;
            writer.setValue(ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            try {
                return LocalDateTime.parse(reader.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e) {
                LOGGER.warn("Cannot unmarshal local date-time value {}. Exception caught.", reader.getValue(), e);
            }
            return null;
        }
    }
}
