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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author theseusyang
 * Local time adapter.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    /**
     * Constructor.
     */
    public LocalTimeAdapter() {
        super();
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public LocalTime unmarshal(String v) throws Exception {

        if (Objects.isNull(v)) {
            return null;
        }

        // UN-3876, try time first, then DT, if result is null
        LocalTime result = LocalTime.parse(v, DateTimeFormatter.ISO_TIME);
        if (Objects.isNull(result)) {
            result = LocalTime.parse(v, DateTimeFormatter.ISO_DATE_TIME);
        }

        return result;
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public String marshal(LocalTime v) throws Exception {

        if (Objects.isNull(v)) {
            return null;
        }

        return DateTimeFormatter.ISO_LOCAL_TIME.format(v);
    }

}
