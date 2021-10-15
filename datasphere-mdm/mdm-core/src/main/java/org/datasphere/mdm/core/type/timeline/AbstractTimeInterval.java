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

import java.util.Date;

import com.huahui.datasphere.mdm.core.type.calculables.Calculable;
import com.huahui.datasphere.mdm.core.type.calculables.CalculationResult;

/**
 * @author theseusyang
 * Common part of the interval API.
 */
public abstract class AbstractTimeInterval<T extends Calculable> implements TimeInterval<T> {
    /**
     * Valid from.
     */
    protected Date validFrom;
    /**
     * Valid to.
     */
    protected Date validTo;
    /**
     * The calculation result, if applicable.
     */
    protected CalculationResult<T> calculationResult;
    /**
     * Interval is active or not.
     */
    protected boolean active;

    /**
     * Constructor.
     */
    protected AbstractTimeInterval() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Date getValidFrom() {
        return validFrom;
    }
    /**
     * Sets the validity interval start.
     * @param validFrom the validFrom to set
     */
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }
    /**
     * Sets the validity interval end.
     * @param validTo the validTo to set
     */
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public<V extends CalculationResult<T>> V getCalculationResult() {
        return (V) calculationResult;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setCalculationResult(CalculationResult<T> result) {
        this.calculationResult = result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return active;
    }
}
