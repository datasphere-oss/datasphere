package com.huahui.datasphere.mdm.rest.core.converter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.huahui.datasphere.mdm.core.type.messaging.CoreSubsystems;
import com.huahui.datasphere.mdm.system.type.messaging.DomainInstance;
import com.huahui.datasphere.mdm.system.type.messaging.Header;
import com.huahui.datasphere.mdm.system.type.messaging.MessageType;
import com.huahui.datasphere.mdm.system.type.messaging.SystemHeaders;

import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditDomainRO;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditDomainsRO;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditMessageHeaderRO;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditMessageTypeRO;

/**
 * @author theseusyang on Aug 21, 2020
 */
public class AuditConverter {
    /**
     * Constructor.
     */
    private AuditConverter() {
        super();
    }

    public static AuditDomainsRO to(Collection<DomainInstance> in) {

        AuditDomainsRO result = new AuditDomainsRO();
        result.setDomains(in.stream()
                .map(AuditConverter::to)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        result.setSystemHeaders(Stream.of(SystemHeaders.all())
                .map(AuditConverter::to)
                .collect(Collectors.toList()));

        return result;
    }

    public static AuditDomainRO to(DomainInstance in) {

        List<AuditMessageTypeRO> types = in.getMessageTypes().stream()
                .map(AuditConverter::to)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(types)) {
            return null;
        }

        AuditDomainRO result = new AuditDomainRO();
        result.setName(in.getId());
        result.setDescription(in.getDescription());
        result.setTypes(types);

        return result;
    }

    public static AuditMessageTypeRO to(MessageType type) {

        if (!StringUtils.equals(type.getSubsystem(), CoreSubsystems.AUDIT_SUBSYSTEM)) {
            return null;
        }

        AuditMessageTypeRO result = new AuditMessageTypeRO();
        result.setId(type.getId());
        result.setDescription(type.getDescription());
        result.setHeaders(type.getHeaders().stream()
                .map(AuditConverter::to)
                .collect(Collectors.toList()));

        return result;
    }

    public static AuditMessageHeaderRO to(Header header) {
        AuditMessageHeaderRO result = new AuditMessageHeaderRO();
        result.setName(header.getName());
        result.setType(header.getType().name());
        return result;
    }
}
