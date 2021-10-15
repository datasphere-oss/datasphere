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
package org.datasphere.mdm.core.service.impl.upath;

import java.util.Map;
import java.util.Objects;

import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.upath.UPathConstants;
import org.datasphere.mdm.core.type.upath.UPathElementType;
import org.datasphere.mdm.core.type.upath.UPathFilterElement;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;

/**
 * @author Mikhail Mikhailov on Feb 26, 2021
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
