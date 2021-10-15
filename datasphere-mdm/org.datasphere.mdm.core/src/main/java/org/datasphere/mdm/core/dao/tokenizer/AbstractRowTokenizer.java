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

package org.datasphere.mdm.core.dao.tokenizer;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.po.ObjectPO;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mikhail Mikhailov
 * Row tokenization helper utility.
 */
public abstract class AbstractRowTokenizer<T extends ObjectPO> {
    /**
     * The logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRowTokenizer.class);
    /**
     * Constructor.
     */
    public AbstractRowTokenizer() {
        super();
    }
    /**
     * Processes PGObjet internal data.
     * @param val the PGObject instance
     * @return result
     */
    public T process(PGobject val) {

        if (val != null
         && val.getValue() != null
         && val.getValue().length() > 0) {
            return process(CompositeValueTokenizer.structTokenizer(val.getValue()));
        }

        return null;
    }
    /**
     * Number of columns in a record.
     * @return number of columns
     */
    protected int size() {
        return 0;
    }
    /**
     * Process line at whole.
     * @param fields the line
     * @return new object
     */
    protected abstract T process(CompositeValueTokenizer fields);
    /**
     * Process part of a line.
     * @param fields the line
     * @param from the from index
     * @param to the to index
     * @param t
     */
    protected abstract void process(CompositeValueIterator tk, T t);
    /**
     * Reads a portion from tokenizer iterator into the target T.
     * @param rti the tokenizer
     * @param t the target
     * @param fields the fields to read
     */
    protected void process(CompositeValueIterator rti, T t, RowTokenizerField<T>[] fields) {

        for (int i = 0; i < fields.length; i++) {

            String token = rti.next();
            if (StringUtils.isBlank(token)) {
                continue;
            }

            fields[i].accept(token, t);
        }
    }
}
