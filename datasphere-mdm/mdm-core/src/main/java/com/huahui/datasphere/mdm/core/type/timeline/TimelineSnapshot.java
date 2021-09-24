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

package com.huahui.datasphere.mdm.core.type.timeline;

import com.huahui.datasphere.mdm.core.type.calculables.Calculable;

/**
 * @author theseusyang
 * Simple next and previous timeline change view.
 */
public final class TimelineSnapshot <T extends Calculable> {
    /**
     * Previous TL state.
     */
    private final Timeline<T> previous;
    /**
     * Next TL state.
     */
    private final Timeline<T> next;
    /**
     * Constructor.
     * @param previous the former state
     * @param next the next state
     */
    public TimelineSnapshot(Timeline<T> previous, Timeline<T> next) {
        super();
        this.previous = previous;
        this.next = next;
    }
    /**
     * @return the previous
     */
    public Timeline<T> getPrevious() {
        return previous;
    }
    /**
     * @return the next
     */
    public Timeline<T> getNext() {
        return next;
    }

}
