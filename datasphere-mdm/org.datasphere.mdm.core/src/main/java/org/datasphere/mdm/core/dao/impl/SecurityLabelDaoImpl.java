/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import org.datasphere.mdm.core.dao.SecurityLabelDao;
import org.datasphere.mdm.core.po.security.LabelAttributePO;
import org.datasphere.mdm.core.po.security.LabelAttributeValuePO;
import org.datasphere.mdm.core.po.security.LabelPO;
import org.datasphere.mdm.core.type.security.SecurityLabel;
import org.datasphere.mdm.core.type.security.SecurityLabelAttribute;
import org.springframework.util.CollectionUtils;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

public class SecurityLabelDaoImpl extends BaseDAOImpl implements SecurityLabelDao {

    private final String selectSecurityLabelsByObject;
    private final String deleteAllObjectSecurityLabelsAttributes;
    private final String addSecurityLabelsAttributeToObject;
    private final String cleanUsersLabelsHavingRole;
    private final String selectObjectsSecurityLabels;


    public SecurityLabelDaoImpl(final String connectionTable, final DataSource dataSource, final Properties sql) {
        super(dataSource);


        selectSecurityLabelsByObject =
                String.format(sql.getProperty("SELECT_SECURITY_LABELS_BY_OBJECT"), connectionTable);
        deleteAllObjectSecurityLabelsAttributes =
                String.format(sql.getProperty("DELETE_ALL_OBJECT_S_LABEL_ATTRIBUTES"), connectionTable);
        addSecurityLabelsAttributeToObject =
                String.format(sql.getProperty("ADD_S_LABEL_ATTRIBUTE_TO_OBJECT"), connectionTable);
        cleanUsersLabelsHavingRole = sql.getProperty("CLEAN_USERS_LABELS_HAVING_ROLE");
        selectObjectsSecurityLabels = String.format(sql.getProperty("SELECT_OBJECTS_LABELS_VALUES"), connectionTable);
    }

    public void saveLabelsForObject(final int objectId, final List<SecurityLabel> securityLabels) {
        jdbcTemplate.update(deleteAllObjectSecurityLabelsAttributes, objectId);
        if (CollectionUtils.isEmpty(securityLabels)) {
            return;
        }
        int group = 0;
        for (final SecurityLabel securityLabel : securityLabels) {
            group++;
            final List<SecurityLabelAttribute> slas = securityLabel.getAttributes();
            if (CollectionUtils.isEmpty(slas)) {
                continue;
            }
            for (final SecurityLabelAttribute sla : slas) {
                final Map<String, Object> params = new HashMap<>();
                params.put("id", objectId);
                params.put("value", sla.getValue());
                params.put("attributeName", sla.getName());
                params.put("labelName", securityLabel.getName());
                params.put("labelGroup", group);
                namedJdbcTemplate.update(addSecurityLabelsAttributeToObject, params);
            }
        }
    }

    public List<LabelAttributeValuePO> findLabelsAttributesValuesForObject(final int objectId) {
        final List<Map<String, Object>> rows = namedJdbcTemplate.query(
                selectSecurityLabelsByObject,
                Collections.singletonMap("objectId", objectId),
                (rs, rowNum) -> {
                    final Map<String, Object> row = new HashMap<>();
                    row.put("labelId", rs.getInt("labelId"));
                    row.put("labelName", rs.getString("labelName"));
                    row.put("labelDisplayName", rs.getString("labelDisplayName"));
                    row.put("labelDescription", rs.getString("labelDescription"));
                    row.put("labelAttributeId", rs.getInt("labelAttributeId"));
                    row.put("labelAttributeName", rs.getString("labelAttributeName"));
                    row.put("labelAttributePath", rs.getString("labelAttributePath"));
                    row.put("labelAttributeDescription", rs.getString("labelAttributeDescription"));
                    row.put("labelAttributeValue", new LabelAttributeValuePO(
                            rs.getInt("labelAttributeValueId"),
                            rs.getString("labelAttributeValueValue"),
                            rs.getInt("labelAttributeValueGroup"),
                            null
                    ));
                    return row;
                }
        );
        final Map<Integer, LabelAttributePO> las = new HashMap<>();
        final Map<Integer, LabelPO> ls = new HashMap<>();
        for (final Map<String, Object> row : rows) {
            final LabelAttributeValuePO labelAttributeValuePO = (LabelAttributeValuePO) row.get("labelAttributeValue");
            final Integer labelAttributeId = (Integer) row.get("labelAttributeId");
            if (!las.containsKey(labelAttributeId)) {
                final LabelAttributePO labelAttributePO = new LabelAttributePO(
                        labelAttributeId,
                        (String) row.get("labelAttributeName"),
                        (String) row.get("labelAttributePath"),
                        (String) row.get("labelAttributeDescription")
                );
                las.put(labelAttributeId, labelAttributePO);
                final Integer labelId = (Integer) row.get("labelId");
                if (!ls.containsKey(labelId)) {
                    final LabelPO labelPO = new LabelPO(labelId);
                    labelPO.setName((String) row.get("labelName"));
                    labelPO.setDisplayName((String) row.get("labelDisplayName"));
                    labelPO.setDescription((String) row.get("labelDescription"));
                    ls.put(labelId, labelPO);
                }
                final LabelPO label = ls.get(labelId);
                labelAttributePO.setLabel(label);
                label.addLabelAttributePO(labelAttributePO);
            }
            final LabelAttributePO labelAttributePO = las.get(labelAttributeId);
            labelAttributeValuePO.setLabelAttribute(labelAttributePO);
            labelAttributePO.addLabelAttributeValue(labelAttributeValuePO);
        }
        return rows.stream().map(r -> (LabelAttributeValuePO) r.get("labelAttributeValue")).collect(Collectors.toList());
    }

    @Override
    public void cleanUsersLabels(String roleName) {
        namedJdbcTemplate.update(cleanUsersLabelsHavingRole, Collections.singletonMap("roleName", roleName));
    }

    @Override
    public Map<Integer, List<LabelAttributeValuePO>> fetchObjectsSecurityLabelsValues() {
        return jdbcTemplate.query(selectObjectsSecurityLabels, rs -> {
            final Map<Integer, List<LabelAttributeValuePO>> result = new HashMap<>();
            while (rs.next()) {
                final int oId = rs.getInt("objectId");
                result.computeIfAbsent(oId, (objectId) -> new ArrayList<>());
                final LabelPO label = new LabelPO();
                label.setName(rs.getString("labelName"));
                final LabelAttributePO labelAttribute = new LabelAttributePO();
                labelAttribute.setId(rs.getInt("labelAttributeId"));
                labelAttribute.setName(rs.getString("labelAttributeName"));
                labelAttribute.setLabel(label);
                final LabelAttributeValuePO labelAttributeValuePO = new LabelAttributeValuePO(
                        null,
                        rs.getString("labelAttributeValue"),
                        rs.getInt("labelAttributeValueGroup"),
                        labelAttribute
                );
                result.get(oId).add(labelAttributeValuePO);
            }
            return result;
        });
    }
}
