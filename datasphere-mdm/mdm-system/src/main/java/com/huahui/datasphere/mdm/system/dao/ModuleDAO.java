

package com.huahui.datasphere.mdm.system.dao;

import java.util.Collection;
import java.util.List;

import com.huahui.datasphere.mdm.system.dto.ModuleInfo;

public interface ModuleDAO {
    boolean moduleInfoTableExists();
    List<ModuleInfo> fetchModulesInfo();
    void saveModulesInfo(Collection<ModuleInfo> modulesInfo);
}
