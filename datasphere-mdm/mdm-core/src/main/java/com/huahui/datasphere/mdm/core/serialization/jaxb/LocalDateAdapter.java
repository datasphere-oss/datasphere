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

package com.huahui.datasphere.mdm.core.serialization.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author theseusyang
 * Local date adapter.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    /**
     * Constructor.
     */
    public LocalDateAdapter() {
        super();
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public LocalDate unmarshal(String v) throws Exception {

        if (Objects.isNull(v)) {
            return null;
        }

        // UN-3876, try date first, then DT, if result is null
        LocalDate result = LocalDate.parse(v, DateTimeFormatter.ISO_DATE);
        if (Objects.isNull(result)) {
            result = LocalDate.parse(v, DateTimeFormatter.ISO_DATE_TIME);
        }

        return result;
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public String marshal(LocalDate v) throws Exception {

        if (Objects.isNull(v)) {
            return null;
        }

        return DateTimeFormatter.ISO_LOCAL_DATE.format(v);
    }

}
