package org.datasphere.mdm.core.service.impl.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.dao.RoleDao;
import org.datasphere.mdm.core.dto.RolePropertyDTO;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.po.security.RolePropertyPO;
import org.datasphere.mdm.core.type.security.CustomProperty;
import org.datasphere.mdm.core.type.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.datasphere.mdm.system.exception.PlatformValidationException;
import org.datasphere.mdm.system.exception.ValidationResult;

/**
 * validation service for role meta
 *
 * @author maria.chistyakova
 * @since 17.09.2019
 */
@Service
public class RoleValidationService {

    private static final String VIOLATION_NAME_PROPERTY_EMPTY = "app.role.property.validationError.name.property.empty";
    private static final String VIOLATION_NAME_PROPERTY_LENGTH = "app.role.property.validationError.name.property.length";
    private static final String VIOLATION_DISPLAY_NAME_PROPERTY_EMPTY = "app.role.property.validationError.displayName.property.empty";
    private static final String VIOLATION_DISPLAY_NAME_PROPERTY_LENGTH = "app.role.property.validationError.displayName.property.length";
    private static final String VIOLATION_NAME_PROPERTY_NOT_UNIQUE = "app.role.property.validationError.name.not.unique";
    private static final String VIOLATION_DISPLAY_NAME_PROPERTY_NOT_UNIQUE = "app.role.property.validationError.displayName.not.unique";
    private static final String VIOLATION_ROLE_NAME_EMPTY = "app.role.data.validationError.roleName.empty";
    private static final String VIOLATION_ROLE_NAME_LENGTH = "app.role.data.validationError.roleName.length";
    private static final String VIOLATION_DISPLAY_NAME_LENGTH = "app.role.data.validationError.displayName.length";
    private static final String VIOLATION_ROLE_TYPE_EMPTY = "app.role.data.validationError.roleType.empty";
    private static final String VIOLATION_ROLE_TYPE_LENGTH = "app.role.data.validationError.roleType.length";
    private static final String VIOLATION_REQUIRED_PROPERTY_VALUE = "app.role.property.validationError.value.not.set";

    // TODO: 17.09.2019 remove, use cache map<String propertyName, Property>
    @Autowired
    private RoleDao roleDAO;

    /**
     * Parameter name length limit.
     */
    private static final int PARAM_NAME_LIMIT = 2044;

    /**
     * Parameter display name length limit.
     */
    private static final int PARAM_DISPLAY_NAME_LIMIT = 2044;

    private static final int ROLE_FIELD_LIMIT = 255;

    public void validateRoleProperty(final RolePropertyDTO property) {

        final List<ValidationResult> validationResult = new ArrayList<>();

        property.setName(StringUtils.trim(property.getName()));
        property.setDisplayName(StringUtils.trim(property.getDisplayName()));

        if (StringUtils.isEmpty(property.getName())) {
            validationResult.add(new ValidationResult("Property 'name' is blank/empty. Rejected.",
                    VIOLATION_NAME_PROPERTY_EMPTY));
        } else if (property.getName().length() > PARAM_NAME_LIMIT) {
            validationResult.add(new ValidationResult("The lenght of the 'name' parameter is larger than {0} limit.",
                    VIOLATION_NAME_PROPERTY_LENGTH, PARAM_NAME_LIMIT));
        }

        if (StringUtils.isEmpty(property.getDisplayName())) {
            validationResult.add(new ValidationResult("Property 'displayName' is blank/empty. Rejected.",
                    VIOLATION_DISPLAY_NAME_PROPERTY_EMPTY));
        } else if (property.getDisplayName().length() > PARAM_DISPLAY_NAME_LIMIT) {
            validationResult.add(new ValidationResult("The lenght of the 'displayName' parameter is larger than {0} limit.",
                    VIOLATION_DISPLAY_NAME_PROPERTY_LENGTH, PARAM_DISPLAY_NAME_LIMIT));
        }

        if (validationResult.isEmpty()) {

            RolePropertyPO existProperty = roleDAO.loadPropertyByName(property.getName());
            if (existProperty != null && !existProperty.getId().equals(property.getId())) {
                validationResult.add(new ValidationResult("Role property 'name' must be unique. Found existing property with name {0}.",
                        VIOLATION_NAME_PROPERTY_NOT_UNIQUE, property.getName()));
            }

            existProperty = roleDAO.loadPropertyByDisplayName(property.getDisplayName());
            if (existProperty != null && !existProperty.getId().equals(property.getId())) {
                validationResult.add(new ValidationResult("Role property 'displayName' must be unique. Found existing property with displayName {0}.",
                        VIOLATION_DISPLAY_NAME_PROPERTY_NOT_UNIQUE, property.getDisplayName()));
            }
        }
        if (!CollectionUtils.isEmpty(validationResult)) {
            throw new PlatformValidationException("Role properties validation error.",
                    CoreExceptionIds.EX_ROLE_PROPERTY_VALIDATION_ERROR, validationResult);
        }
    }

    public void onCreate(final Role role) {
        if (roleDAO.findByName(role.getName()) != null) {
            throw new PlatformValidationException("Role with name '{}' already exists.",
                    CoreExceptionIds.EX_SECURITY_ROLE_ALREADY_EXISTS, null,
                    role.getName());
        }
        onUpdate(role);

        validateRolePropertyValues(role.getProperties());
    }

    public void onUpdate(final Role role) {
        final List<ValidationResult> validationResult = new ArrayList<>();

        String roleName = StringUtils.trim(role.getName());
        String roleDisplayName = StringUtils.trim(role.getDisplayName());


        if (StringUtils.isBlank(roleName)) {
            validationResult.add(new ValidationResult("Role name is empty.",
                    VIOLATION_ROLE_NAME_EMPTY));
        } else if (roleName.length() > ROLE_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("Role name is larger than the field limit.",
                    VIOLATION_ROLE_NAME_LENGTH, roleName, ROLE_FIELD_LIMIT));
        }
        if (StringUtils.isBlank(roleDisplayName)) {
            validationResult.add(new ValidationResult("Role display name is empty.",
                    VIOLATION_ROLE_NAME_EMPTY));
        }
        if (!StringUtils.isEmpty(roleDisplayName) && roleDisplayName.length() > ROLE_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("Role display name is larger than the field limit.",
                    VIOLATION_DISPLAY_NAME_LENGTH, roleDisplayName, ROLE_FIELD_LIMIT));
        }

        if (role.getRoleType() == null) {
            validationResult.add(new ValidationResult("Role type is empty.",
                    VIOLATION_ROLE_TYPE_EMPTY));
        } else if (role.getRoleType().toString().length() > ROLE_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("Role type exceeds field limit.",
                    VIOLATION_ROLE_TYPE_LENGTH, role.getRoleType(), ROLE_FIELD_LIMIT));
        }

        if (!validationResult.isEmpty()) {
            throw new PlatformValidationException("Role {} parameter validation error",
                    CoreExceptionIds.EX_ROLE_DATA_VALIDATION_ERROR, validationResult, roleName);
        }
    }

    private void validateRolePropertyValues(List<CustomProperty> toCheck) {
        final List<ValidationResult> validationResult = new ArrayList<>();
        List<RolePropertyPO> allProperties = roleDAO.loadAllProperties();

        if (CollectionUtils.isEmpty(allProperties)) {
            return;
        }

        Map<String, CustomProperty> newRolesProps = toCheck.stream().collect(Collectors.toMap(CustomProperty::getName, n -> n));
        List<RolePropertyPO> mustBeSetProperties = allProperties.stream().filter(RolePropertyPO::isRequired).collect(Collectors.toList());

        mustBeSetProperties.stream()
                .filter(propertyDTO -> !newRolesProps.containsKey(propertyDTO.getName()) || StringUtils.isBlank(newRolesProps.get(propertyDTO.getName()).getValue()))
                .forEach(property -> {
                    validationResult.add(
                            new ValidationResult(
                                    "Required value for role property '{}' not set.",
                                    VIOLATION_REQUIRED_PROPERTY_VALUE,
                                    property.getDisplayName()));
                });

        if (!CollectionUtils.isEmpty(validationResult)) {
            throw new PlatformValidationException("Role property values validation error.",
                    CoreExceptionIds.EX_ROLE_PROPERTY_VALUES_VALIDATION_ERROR, validationResult);
        }
    }
}
