
package com.huahui.datasphere.mdm.rest.core.ro.libraries;

import java.util.List;

import com.huahui.datasphere.mdm.rest.system.ro.DetailedOutputRO;

/**
 * @author theseusyang on Feb 8, 2021
 */
public class GetLibrariesRO extends DetailedOutputRO {
    /**
     * Libraries list.
     */
    private List<UserLibraryRO> libraries;
    /**
     * Constructor.
     */
    public GetLibrariesRO() {
        super();
    }
    /**
     * Constructor.
     */
    public GetLibrariesRO(List<UserLibraryRO> libaries) {
        super();
        this.libraries = libaries;
    }
    /**
     * @return the libaries
     */
    public List<UserLibraryRO> getLibraries() {
        return libraries;
    }
    /**
     * @param libaries the libaries to set
     */
    public void setLibraries(List<UserLibraryRO> libaries) {
        this.libraries = libaries;
    }
}
