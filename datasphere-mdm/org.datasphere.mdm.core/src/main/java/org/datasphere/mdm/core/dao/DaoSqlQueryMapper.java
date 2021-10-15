/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.dao;

import java.lang.reflect.Field;
import java.util.Properties;

import org.springframework.util.ReflectionUtils;

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

                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, fillerObject, query);
                field.setAccessible(false);
            }
        }
    }
}
