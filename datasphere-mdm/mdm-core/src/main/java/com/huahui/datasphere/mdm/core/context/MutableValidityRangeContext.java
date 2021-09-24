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

package com.huahui.datasphere.mdm.core.context;

import java.util.Date;

/**
 * @author theseusyang
 * Adds mutability to validity range, needed in data upserts mostly.
 * It's a trade-off between immutability of the context and the need to modify period boundary according to metamodel in some cases.
 */
public interface MutableValidityRangeContext extends ValidityRangeContext {
    /**
     * Sets lower bound.
     * @param from the from timestamp
     */
    void setValidFrom(Date from);
    /**
     * Sets upper bound.
     * @param to the to timestamp
     */
    void setValidTo(Date to);
}
