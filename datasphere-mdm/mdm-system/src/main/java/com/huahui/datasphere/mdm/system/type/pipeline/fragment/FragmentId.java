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

package com.huahui.datasphere.mdm.system.type.pipeline.fragment;

/**
 * Generic fragment ID.
 * @author theseusyang on Dec 11, 2019
 */
public class FragmentId<F extends Fragment<F>> {
    /**
     * The name of the ID.
     */
    private final String name;
    /**
     * Constructor.
     * @param name the name of the ID
     */
    public FragmentId(String name) {
        super();
        this.name = name;
    }
    /**
     * Gets the name if this fragment.
     * @return fragment name
     */
    public String getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FragmentId<?> other = (FragmentId<?>) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder("FragmentId [").append(name).append("]").toString();
    }
}
