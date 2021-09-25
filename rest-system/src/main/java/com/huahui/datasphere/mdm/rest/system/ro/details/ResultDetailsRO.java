package com.huahui.datasphere.mdm.rest.system.ro.details;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Details of the operation
 *
 * @author theseusyang
 * @since 23.09.2020
 **/
public class ResultDetailsRO {

    private List<InfoDetailsRO> info;
    private List<InfoDetailsRO> warning;
    private List<ErrorDetailsRO> error;

    public ResultDetailsRO withInfo(InfoDetailsRO details, InfoDetailsRO ...rest) {
        if (info == null) {
            info = new ArrayList<>();
        }
        info.add(details);
        Collections.addAll(info, rest);
        return this;
    }

    public ResultDetailsRO withWarning(InfoDetailsRO details, InfoDetailsRO ...rest) {
        if (warning == null) {
            warning = new ArrayList<>();
        }
        warning.add(details);
        Collections.addAll(warning, rest);
        return this;
    }

    public ResultDetailsRO withError(ErrorDetailsRO... details) {
        if (ArrayUtils.isNotEmpty(details)) {
            return withError(Arrays.asList(details));
        }
        return this;
    }

    public ResultDetailsRO withError(Collection<ErrorDetailsRO> details) {
        if (CollectionUtils.isNotEmpty(details)) {
            if (error == null) {
                error = new ArrayList<>();
            }
            error.addAll(details);
        }
        return this;
    }

    public List<InfoDetailsRO> getInfo() {
        if (info == null) {
            info = new ArrayList<>();
        }
        return info;
    }

    public void setInfo(List<InfoDetailsRO> info) {
        this.info = info;
    }

    public List<InfoDetailsRO> getWarning() {
        if (warning == null) {
            warning = new ArrayList<>();
        }
        return warning;
    }

    public void setWarning(List<InfoDetailsRO> warning) {
        this.warning = warning;
    }

    public List<ErrorDetailsRO> getError() {
        if (error == null) {
            error = new ArrayList<>();
        }
        return error;
    }

    public void setError(List<ErrorDetailsRO> error) {
        this.error = error;
    }
}
