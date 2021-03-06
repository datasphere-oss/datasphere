package com.huahui.datasphere.rest.system.ro;

import com.huahui.datasphere.rest.system.ro.details.ResultDetailsRO;

/**
 * Detailed result object
 *
 * @author Alexandr Serov
 * @since 09.11.2020
 **/
public abstract class DetailedOutputRO {

    /**
     * Result details
     */
    protected ResultDetailsRO details = new ResultDetailsRO();

    /**
     * @return result details
     */
    public ResultDetailsRO getDetails() {
        return details;
    }

    /**
     * Setup output details
     *
     * @param details result details
     */
    public void setDetails(ResultDetailsRO details) {
        this.details = details;
    }


}
