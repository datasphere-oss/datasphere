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

package com.huahui.datasphere.mdm.core.convert.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.huahui.datasphere.mdm.core.dto.RightDTO;
import com.huahui.datasphere.mdm.core.dto.SecuredResourceDTO;
import com.huahui.datasphere.mdm.core.po.security.ResourcePO;
import com.huahui.datasphere.mdm.core.po.security.ResourceRightPO;
import com.huahui.datasphere.mdm.core.po.security.RightPO;
import com.huahui.datasphere.mdm.core.type.security.Right;
import com.huahui.datasphere.mdm.core.type.security.SecuredResourceCategory;
import com.huahui.datasphere.mdm.core.type.security.SecuredResourceType;

/**
 * todo: JavaDoc
 *
 * @author maria.chistyakova
 * @since 31.05.2019
 */
public class RightConverter {


    /**
     * Create.
     */
    public static final String CREATE_LABEL = "CREATE";
    /**
     * Update.
     */
    public static final String UPDATE_LABEL = "UPDATE";
    /**
     * Delete.
     */
    public static final String DELETE_LABEL = "DELETE";
    /**
     * Read.
     */
    public static final String READ_LABEL = "READ";
    /**
     * Convert rights.
     *
     * @param source
     *            the source
     * @return the list
     */
    public static List<Right> convertRightsPoToDto(List<ResourceRightPO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<Right> target = new ArrayList<>();
        Map<ResourcePO, List<RightPO>> map = new HashMap<>();
        for (ResourceRightPO rr : source) {
            if (map.containsKey(rr.getResource())) {
                map.get(rr.getResource()).add(rr.getRight());
            } else {
                List<RightPO> list = new ArrayList<>();
                list.add(rr.getRight());
                map.put(rr.getResource(), list);
            }
        }

        Set<ResourcePO> pos = map.keySet();
        for (ResourcePO po : pos) {
            RightDTO dto = new RightDTO();
            SecuredResourceDTO ssd = new SecuredResourceDTO();
            ssd.setName(po.getName());
            ssd.setDisplayName(po.getDisplayName());
            ssd.setType(SecuredResourceType.valueOf(po.getRType()));
            ssd.setCategory(SecuredResourceCategory.valueOf(po.getCategory()));
            dto.setSecuredResource(ssd);
            List<RightPO> list = map.get(po);
            for (RightPO rightPO : list) {
                if (CREATE_LABEL.equals(rightPO.getName())) {
                    dto.setCreate(true);
                } else if (READ_LABEL.equals(rightPO.getName())) {
                    dto.setRead(true);
                } else if (DELETE_LABEL.equals(rightPO.getName())) {
                    dto.setDelete(true);
                } else if (UPDATE_LABEL.equals(rightPO.getName())) {
                    dto.setUpdate(true);
                }
            }
            target.add(dto);
        }

        return target;
    }



}
