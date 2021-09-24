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

package com.huahui.datasphere.mdm.system.type.module;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.job.ModularBatchJobFraction;
import com.huahui.datasphere.mdm.system.type.messaging.DomainType;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.type.rendering.RenderingAction;
import com.huahui.datasphere.mdm.system.type.rendering.RenderingProvider;

/**
 * @author theseusyang
 * Module contract.
 */
public interface Module {
    /**
     * Gets module ID, i. e. 'org.unidata.mdm.core'.
     * @return ID
     */
    String getId();
    /**
     * Gets module version, consisting of major.minor.rev, i. e. '5.4.3'.
     * @return version
     */
    String getVersion();
    /**
     * Gets module localized name, 'Unidata Core'.
     * @return name
     */
    String getName();
    /**
     * Gets module localized description, i. e. 'This outstanding module is for all the good things on earth...'.
     * @return description
     */
    String getDescription();
    /**
     * Gets module 'tag', i. e. 'super_fast_edition'.
     * @return module tag
     */
    @Nullable
    default String getTag() {
        return null;
    }

    default Collection<Dependency> getDependencies() {
        return Collections.emptyList();
    }
    /**
     * Gets the segments this module exports.
     * @return collection of segments
     */
    default Collection<Segment> getSegments() {
        return Collections.emptyList();
    }
    /**
     * Gets resource bundle basenames - i. e. 'my_resources' for localized resources
     * such as (my_resources_en, my_resources_fr, my_resources_fi).
     * Resources may be java or XML properties.
     * Files must be in classpath.
     * Property names should be prefixed with MODULE_ID, i. e. (for 'org.unidata.mdm.system'
     * org.unidata.mdm.system.information.message = The weather is nice today!)
     * @return array of resource bundle base names
     */
    default String[] getResourceBundleBasenames() {
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    /**
     * Gets the rendering resolver for this module or null, if none defined.
     * @return rendering resolver or null
     */
    @Nonnull
    default Collection<RenderingProvider> getRenderingProviders() {
        return Collections.emptyList();
    }
    /**
     * Gets the module's exported rendering actions.
     * @return collection of actions
     */
    default Collection<RenderingAction> getRenderingActions() {
        return Collections.emptyList();
    }
    /**
     * Gets a collection batch set post processors, which can be used in jobs.
     * @return collection of post-processors
     */
    default Collection<ModularBatchJobFraction> getBatchJobFractions() {
        return Collections.emptyList();
    }
    /**
     * Gets configuration properties, exported by this module.
     * @return configuration properties
     */
    default ConfigurationProperty<?>[] getConfigurationProperties() {
        return ConfigurationProperty.EMPTY_PROPERTIES_ARRAY;
    }
    /**
     * Gets messaging domain types, exported by this module.
     * @return messaging domain types
     */
    default DomainType[] getMessagingDomains() {
        return DomainType.EMPTY_DOMAINS_ARRAY;
    }
    /**
     * Runs module's install/upgrade procedure.
     * Can be used to init / mgirate DB schema or other similar tasks.
     */
    default void install() {
        // Override
    }
    /**
     * Runs module's uninstall procedure.
     * Can be used to drop schema or similar tasks.
     */
    default void uninstall() {
        // Override
    }
    /**
     * Runs module's start procedure. Happens upon each application startup.
     */
    default void start() {
        // Override
    }
    /**
     * Runs after all modules execute method start in same order as dependencies
     */
    default void ready() {
        // Override
    }
    /**
     * Runs module's stop procedure. Happens upon each application shutdown.
     */
    default void stop() {
        // Override
    }
}
