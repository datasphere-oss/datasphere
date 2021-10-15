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
package org.datasphere.mdm.core.convert.job;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.po.job.JobDefinitionPO;
import org.datasphere.mdm.core.service.impl.job.JobParameterFactory;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.type.job.JobDefinition;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.JobParameterDescriptor;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.convert.Converter;
import org.datasphere.mdm.system.util.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mikhail Mikhailov on Jul 6, 2021
 */
@Component
public class JobDefinitionConverter extends Converter<JobDefinition, JobDefinitionPO> {
    /**
     * The OM.
     */
    @Autowired
    private ObjectMapper objectMapper;
    /**
     * The CJR.
     */
    @Autowired
    private CustomJobRegistry registry;
    /**
     * The JPF.
     */
    @Autowired
    private JobParameterFactory parameterFactory;
    /**
     * Constructor.
     */
    public JobDefinitionConverter() {
        super();
        this.to = this::convert;
        this.from = this::convert;
    }

    private JobDefinitionPO convert(JobDefinition jd) {

        final JobDefinitionPO po = new JobDefinitionPO();
        final String user = SecurityUtils.getCurrentUserName();

        po.setId(jd.getId());
        po.setName(jd.getDisplayName());
        po.setEnabled(jd.isEnabled());
        po.setError(jd.isError());
        po.setDescription(jd.getDescription());
        po.setCronExpression(jd.getCronExpression());
        po.setJobName(jd.getJobName());
        po.setTags(jd.getTags());
        po.setParametersAsString(parameters(jd.getParametersMap()));
        po.setUpdateDate(new Date(System.currentTimeMillis()));
        po.setUpdatedBy(user);
        po.setCreatedBy(user);

        return po;
    }

    private JobDefinition convert(JobDefinitionPO po) {

        // 1. The definition itself
        final JobDefinition result = new JobDefinition();

        result.setId(po.getId());
        result.setJobName(po.getJobName());
        result.setDisplayName(po.getName());
        result.setDescription(po.getDescription());
        result.setEnabled(po.isEnabled());
        result.setError(po.isError());
        result.setCronExpression(po.getCronExpression());
        result.setTags(po.getTags());
        result.setParameters(parameters(po.getJobName(), po.getParametersAsString()));
        result.setCreateDate(po.getCreateDate());
        result.setCreatedBy(po.getCreatedBy());
        result.setUpdateDate(po.getUpdateDate());
        result.setUpdatedBy(po.getUpdatedBy());

        return result;
    }

    private String parameters(Map<String, JobParameterDefinition<?>> i) {

        if (MapUtils.isEmpty(i)) {
            return null;
        }

        Map<String, Object> values = new HashMap<>();
        for (JobParameterDefinition<?> v : i.values()) {

            if (v.isSingle()) {
                values.put(v.getName(), v.single());
            } else if (v.isCollection()) {
                values.put(v.getName(), v.collection());
            } else if (v.isMap()) {
                values.put(v.getName(), v.map());
            } else if (v.isCustom()) {
                values.put(v.getName(), v.asCustom());
            }
        }

        return JsonUtils.write(values);
    }

    private Map<String, JobParameterDefinition<?>> parameters(String jobName, String val) {

        if (StringUtils.isBlank(val)) {
            return Collections.emptyMap();
        }

        try {

            JsonNode n = objectMapper.readTree(val);
            if (Objects.isNull(n) || !n.isObject()) {
                return Collections.emptyMap();
            }

            JobDescriptor descriptor = registry.getDescriptor(jobName);

            Map<String, JobParameterDefinition<?>> result = new HashMap<>();
            Iterator<Entry<String, JsonNode>> i = n.fields();
            while (i.hasNext()) {

                Entry<String, JsonNode> entry = i.next();
                JobParameterDefinition<?> jpd = parameter(descriptor.findParameter(entry.getKey()), entry.getValue());
                if (Objects.nonNull(jpd)) {
                    result.put(jpd.getName(), jpd);
                }
            }

            return result;
        } catch (Exception e) {
            throw new JobException("Failed to unmarshal parameters. Exception caught: ", e,
                    CoreExceptionIds.EX_JOB_DEFINITION_PARAMETERS_UNMARSHALING_FAILED);
        }
    }

    private JobParameterDefinition<?> parameter(JobParameterDescriptor<?> jpd, JsonNode value) {

        if (Objects.isNull(jpd)) {
            return null;
        }

        return parameterFactory.fromValue(jpd, parameterFactory.fromNode(jpd, value));
    }
}
