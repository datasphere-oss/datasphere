
package com.huahui.datasphere.mdm.rest.core.ro.libraries;

import com.huahui.datasphere.mdm.rest.system.ro.DetailedOutputRO;

/**
 * @author theseusyang on Feb 9, 2021
 */
public class DeleteLibraryPO extends DetailedOutputRO {

    /**
     * Success indicator.
     */
    private boolean success;

    /**
     * Constructor.
     */
    public DeleteLibraryPO() {
        super();
    }

    /**
     * Constructor.
     */
    public DeleteLibraryPO(boolean success) {
        super();
        this.success = success;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}