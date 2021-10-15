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

/**
 *
 */
package com.huahui.datasphere.mdm.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author theseusyang
 * Deletes the temp file upon close.
 */
public class AutodeleteTempFileInputStream extends FileInputStream {
    /**
     * Temp file.
     */
    private File file;
    /**
     * Constructor.
     * @param name
     * @throws FileNotFoundException
     */
    public AutodeleteTempFileInputStream(String name) throws FileNotFoundException {
        this(new File(name));
    }
    /**
     * Constructor.
     * @param file
     * @throws FileNotFoundException
     */
    public AutodeleteTempFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (file != null) {
                Files.delete(file.toPath());
            }
        }
    }
}
