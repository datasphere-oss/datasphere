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

package com.huahui.datasphere.mdm.core.dao;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * todo: JavaDoc
 *
 * @author maria.chistyakova
 * @since 30.05.2019
 */
public class DaoSqlQueryMapper {

    private DaoSqlQueryMapper() {
    }

    public static void fill(Class<?> clazz,
                            Object fillerObject,
                            Properties sql) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotationsByType(SqlQuery.class).length != 0) {
                SqlQuery[] annotationsByType = field.getAnnotationsByType(SqlQuery.class);

                String query = sql.getProperty(annotationsByType[0].value());

                try {
                    field.setAccessible(true);
                    field.set(fillerObject, query);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("cannot fill " + query, e);
                }
            }
        }
    }

}
