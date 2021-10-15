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

package org.datasphere.mdm.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import org.datasphere.mdm.core.context.AuditEventWriteContext;
import org.datasphere.mdm.core.service.AuditStorageService;
import org.datasphere.mdm.core.type.search.AuditHeaderField;
import org.datasphere.mdm.core.type.search.AuditIndexType;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.datasphere.mdm.search.context.IndexRequestContext;
import org.datasphere.mdm.search.context.MappingRequestContext;
import org.datasphere.mdm.search.service.SearchService;
import org.datasphere.mdm.search.type.indexing.Indexing;
import org.datasphere.mdm.search.type.indexing.IndexingField;
import org.datasphere.mdm.search.type.indexing.impl.IndexingRecordImpl;
import org.datasphere.mdm.search.type.mapping.Mapping;
import org.datasphere.mdm.search.type.mapping.impl.BooleanMappingField;
import org.datasphere.mdm.search.type.mapping.impl.CompositeMappingField;
import org.datasphere.mdm.search.type.mapping.impl.StringMappingField;
import org.datasphere.mdm.search.type.mapping.impl.TimestampMappingField;
import org.datasphere.mdm.system.type.annotation.ConfigurationRef;
import org.datasphere.mdm.system.type.configuration.ConfigurationValue;

/**
 * @author Alexander Malyshev
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
