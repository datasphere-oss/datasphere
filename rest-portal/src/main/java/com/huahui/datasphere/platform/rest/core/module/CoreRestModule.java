/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.module;

import java.util.Collection;
import java.util.Set;

import org.unidata.mdm.system.type.module.Dependency;
import org.unidata.mdm.system.type.module.Module;

/**
 * @author Alexander Malyshev
 */
public class CoreRestModule implements Module {

    private static final Set<Dependency> DEPENDENCIES = Set.of(
            new Dependency("org.unidata.mdm.core", "6.0"),
            new Dependency("org.unidata.mdm.rest.system", "6.0"));

    @Override
    public String getId() {
        return "org.unidata.mdm.rest.core";
    }

    @Override
    public String getVersion() {
        return "6.0";
    }

    @Override
    public String getName() {
        return "Core Rest Module";
    }

    @Override
    public String getDescription() {
        return "Core Rest Module";
    }

    @Override
    public Collection<Dependency> getDependencies() {
        return DEPENDENCIES;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getResourceBundleBasenames() {
        return new String[]{ "rest_core_messages" };
    }
}
