

package com.huahui.datasphere.mdm.system.dao;

import java.util.List;
import java.util.Map;

public interface ConfigurationDAO {

    Map<String, byte[]> fetchAllProperties();

    Map<String, byte[]> fetch(List<String> names);

    void save(Map<String, byte[]> properties);

    boolean isBootstrap();
}
