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
package com.huahui.datasphere.platform.rest.core.ro;


import com.huahui.datasphere.rest.system.ro.security.RightRO;

/**
 * @author Mikhail Mikhailov
 * Resource specific, justified by security level resource access rights.
 */
public class ResourceSpecificRightRO extends RightRO {

    /** Virtual "restore" right */
    private boolean restore;

    /** Virtual "merge" right */
    private boolean merge;

    /**
     * Constructor.
     */
    public ResourceSpecificRightRO() {
        super();
    }

    /**
     * @return the restore
     */
    public boolean isRestore() {
        return restore;
    }

    /**
     * @param restore the restore to set
     */
    public void setRestore(boolean restore) {
        this.restore = restore;
    }


    /**
     * @return the merge
     */
    public boolean isMerge() {
        return merge;
    }


    /**
     * @param merge the merge to set
     */
    public void setMerge(boolean merge) {
        this.merge = merge;
    }
}
