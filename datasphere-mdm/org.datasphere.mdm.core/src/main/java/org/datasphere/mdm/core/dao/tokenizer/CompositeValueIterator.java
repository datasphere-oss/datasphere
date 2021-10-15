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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Mikhail Mikhailov
 * Simple forward iterator for {@link PGtokenizer}.
 */
public class CompositeValueIterator implements Iterator<String> {
    /**
     * The tokenizer.
     */
    private CompositeValueTokenizer cvt;
    /**
     * Current position.
     */
    private int cursor = 0;
    /**
     * Constructor.
     */
    public CompositeValueIterator(CompositeValueTokenizer cvt) {
        super();
        this.cvt = cvt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return cvt.getSize() > cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String next() {

        if (!hasNext()) {
            throw new NoSuchElementException("no (more) tokens in CompositeValueTokenizer");
        }

        return cvt.getToken(cursor++);
    }
}
