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

package com.huahui.datasphere.mdm.core.type.upath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author theseusyang
 * UPath object.
 */
public class UPath {
    /**
     * Elements.
     */
    private final List<UPathElement> elements = new ArrayList<>();
    /**
     * Constructor.
     */
    public UPath() {
        super();
    }
    /**
     * @return the elements
     */
    public List<UPathElement> getElements() {
        return elements;
    }
    /**
     * @return the segments
     */
    public List<UPathElement> getSegments() {
        List<UPathElement> view = new ArrayList<>(elements.size());
        for (int i = 0; i < elements.size(); i++) {

            UPathElement e = elements.get(i);
            if (e.getType() != UPathElementType.COLLECTOR) {
                continue;
            }

            view.add(e);
        }

        return view;
    }
    /**
     * Gets the size (i. e. number of path segments) of this UPath.
     * @return number of segments
     */
    public int getNumberOfSegments() {

        int result = 0;
        for (int i = 0; i < elements.size(); i++) {

            UPathElement e = elements.get(i);
            if (e.getType() != UPathElementType.COLLECTOR) {
                continue;
            }

            result++;
        }

        return result;
    }
    /**
     * Gets the last {@link UPathElement} or null, if empty.
     * @return last element
     */
    public UPathElement getTail() {
        return elements.isEmpty() ? null : elements.get(elements.size() - 1);
    }
    /**
     * Gets the sub segments UPath starting from segment index 'from'.
     * @param from the segment index to start
     * @return UPath
     */
    public UPath getSubSegmentsUPath(int from) {

        if (from < 0) {
            throw new ArrayIndexOutOfBoundsException("'From' segments index for UPath subtraction out of bounds.");
        }

        int done = 0;
        for (int i = 0; i < elements.size(); i++) {

            UPathElement e = elements.get(i);
            if (e.getType() != UPathElementType.COLLECTOR) {
                continue;
            }

            if (done++ == from) {
                UPath newPath = new UPath();
                newPath.elements.addAll(this.elements.subList(i, this.elements.size()));
                return newPath;
            }
        }

        throw new ArrayIndexOutOfBoundsException("'From' segments index for UPath subtraction out of bounds.");
    }
    /**
     * Tells whether this upath denotes the root element.
     * @return true, if so, false otherwise
     */
    public boolean isRoot() {
        return elements.size() == 1 && UPathConstants.UPATH_ROOT_NAME.equals(elements.get(0).getElement());
    }
    /**
     * Gets canonical meta model path.
     * @return meta model path
     */
    public String toPath() {

        if (elements.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder pb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {

            UPathElement e = elements.get(i);
            if (e.getType() != UPathElementType.COLLECTOR) {
                continue;
            }

            pb.append(i > 0 ? '.' : StringUtils.EMPTY)
              .append(e.getElement());
        }

        return pb.toString();
    }
    /**
     * Gets UPath path.
     * @return UPath path
     */
    public String toUPath() {

        if (elements.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder pb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {

            UPathElement e = elements.get(i);
            if (e.getType() == UPathElementType.COLLECTOR) {
                pb.append(i > 0 ? '.' : StringUtils.EMPTY);
            }

            pb.append(e.getElement());
        }

        return pb.toString();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toUPath();
    }
}