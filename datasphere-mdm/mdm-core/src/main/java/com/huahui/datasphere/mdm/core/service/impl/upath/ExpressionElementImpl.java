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
package com.huahui.datasphere.mdm.core.service.impl.upath;

import java.util.Map;
import java.util.Objects;

import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;

import com.huahui.datasphere.mdm.core.type.data.DataRecord;
import com.huahui.datasphere.mdm.core.type.upath.UPathConstants;
import com.huahui.datasphere.mdm.core.type.upath.UPathElementType;
import com.huahui.datasphere.mdm.core.type.upath.UPathFilterElement;

/**
 * @author theseusyang on Feb 26, 2021
 * MVEL Expression filter.
 */
public class ExpressionElementImpl extends AbstractElementImpl implements UPathFilterElement {
    /**
     * Compiled MVEL expression.
     */
    private final CompiledExpression expression;
    /**
     * Constructor.
     * @param element the source
     * @param expression compiled MVEL expression
     */
    public ExpressionElementImpl(String element, CompiledExpression expression) {
        super(element, UPathElementType.EXPRESSION);
        this.expression = expression;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(DataRecord record) {

        if (Objects.nonNull(record)) {
            return MVEL.executeExpression(expression, Map.of(UPathConstants.UPATH_RECORD_NAME, record), Boolean.class);
        }

        return false;
    }
}
