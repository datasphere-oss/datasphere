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

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.huahui.datasphere.mdm.search.context.IndexRequestContext;
import com.huahui.datasphere.mdm.search.context.MappingRequestContext;
import com.huahui.datasphere.mdm.search.service.SearchService;
import com.huahui.datasphere.mdm.search.type.indexing.Indexing;
import com.huahui.datasphere.mdm.search.type.indexing.IndexingField;
import com.huahui.datasphere.mdm.search.type.indexing.impl.IndexingRecordImpl;
import com.huahui.datasphere.mdm.search.type.mapping.Mapping;
import com.huahui.datasphere.mdm.search.type.mapping.impl.BooleanMappingField;
import com.huahui.datasphere.mdm.search.type.mapping.impl.CompositeMappingField;
import com.huahui.datasphere.mdm.search.type.mapping.impl.StringMappingField;
import com.huahui.datasphere.mdm.search.type.mapping.impl.TimestampMappingField;
import com.huahui.datasphere.mdm.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;

import com.huahui.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import com.huahui.datasphere.mdm.core.context.AuditEventWriteContext;
import com.huahui.datasphere.mdm.core.service.AuditStorageService;
import com.huahui.datasphere.mdm.core.type.search.AuditHeaderField;
import com.huahui.datasphere.mdm.core.type.search.AuditIndexType;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * @author theseusyang
 */
@Service("indexAuditStorageService")
public class IndexAuditStorageService implements AuditStorageService {

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_SYSTEM_SHARDS_NUMBER)
    private ConfigurationValue<Long> shardsNumber;

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_SYSTEM_REPLICAS_NUMBER)
    private ConfigurationValue<Long> replicasNumber;

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_AUDIT_ENABLED)
    private ConfigurationValue<Boolean> auditEnabled;

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_AUDIT_ENABLED_STORAGES)
    private ConfigurationValue<AuditStorageType[]> enabledStorages;

    private final SearchService searchService;

    public IndexAuditStorageService(final SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {

        final Mapping auditIndexMapping = new Mapping(AuditIndexType.AUDIT)
            .withFields(
                    new StringMappingField(AuditHeaderField.DOMAIN.getName()).withDocValue(true),
                    new StringMappingField(AuditHeaderField.TYPE.getName()).withDocValue(true),
                    new BooleanMappingField(AuditHeaderField.SUCCESS.getName()),
                    new StringMappingField(AuditHeaderField.THROWABLE.getName()).withAnalyzed(true),
                    new StringMappingField(AuditHeaderField.LOGIN.getName()).withDocValue(true),
                    new StringMappingField(AuditHeaderField.CLIENT_IP.getName()).withDocValue(true),
                    new StringMappingField(AuditHeaderField.SERVER_IP.getName()).withDocValue(true),
                    new StringMappingField(AuditHeaderField.ENDPOINT.getName()).withDocValue(true),
                    new TimestampMappingField(AuditHeaderField.WHEN_HAPPENED.getName()),
                    new CompositeMappingField(AuditHeaderField.PARAMETERS.getName())
                            .withNested(true)
                            .withFields(
                                    new StringMappingField(AuditHeaderField.PARAMETER_KEY.getName()).withDocValue(true),
                                    new StringMappingField(AuditHeaderField.PARAMETER_VALUE.getName()).withDocValue(true)));

        MappingRequestContext mCtx = MappingRequestContext.builder()
                .entity(AuditIndexType.INDEX_NAME)
                .storageId(SecurityUtils.getCurrentUserStorageId())
                .mapping(auditIndexMapping)
                .shards(shardsNumber.getValue().intValue())
                .replicas(replicasNumber.getValue().intValue())
                .build();

        searchService.process(mCtx);
    }

    @Override
    public void write(Collection<AuditEventWriteContext> auditEventWriteContexts) {

        // Audit is either disabled entirely or just the indexing is disabled
        if (!auditEnabled.getValue().booleanValue()
          || enabledStorages.getValue()[AuditStorageType.INDEX.ordinal()] == null) {
            return;
        }

        final IndexRequestContext indexRequestContext = IndexRequestContext.builder()
                .entity(AuditIndexType.INDEX_NAME)
                .index(auditEventWriteContexts.stream().map(this::createIndexing).collect(Collectors.toList()))
                .build();

        searchService.process(indexRequestContext);
    }

    private Indexing createIndexing(AuditEventWriteContext ctx) {

        return new Indexing(AuditIndexType.AUDIT, null)
            .withFields(
                    IndexingField.of(AuditHeaderField.DOMAIN.getName(), ctx.getDomain()),
                    IndexingField.of(AuditHeaderField.TYPE.getName(), ctx.getType()),
                    IndexingField.of(AuditHeaderField.SUCCESS.getName(), ctx.isSuccess()),
                    IndexingField.of(AuditHeaderField.THROWABLE.getName(), ctx.getThrowableDump()),
                    IndexingField.of(AuditHeaderField.LOGIN.getName(), ctx.getUserLogin()),
                    IndexingField.of(AuditHeaderField.CLIENT_IP.getName(), ctx.getClientIp()),
                    IndexingField.of(AuditHeaderField.SERVER_IP.getName(), ctx.getServerIp()),
                    IndexingField.of(AuditHeaderField.ENDPOINT.getName(), ctx.getEndpoint()),
                    IndexingField.of(AuditHeaderField.WHEN_HAPPENED.getName(), ctx.getWhenHappened()),
                    IndexingField.ofRecords(AuditHeaderField.PARAMETERS.getName(), ctx.getParameters().entrySet().stream()
                        .map(entry ->
                            List.of(
                                IndexingField.of(AuditHeaderField.PARAMETER_KEY.getName(), entry.getKey()),
                                IndexingField.of(AuditHeaderField.PARAMETER_VALUE.getName(), Objects.nonNull(entry.getValue()) ? String.valueOf(entry.getValue()) : StringUtils.EMPTY)))
                        .map(IndexingRecordImpl::new)
                        .collect(Collectors.toList())));
    }
}
