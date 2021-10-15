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

package org.datasphere.mdm.core.exception;

import org.datasphere.mdm.core.module.CoreModule;
import org.datasphere.mdm.system.exception.ExceptionId;

/**
 * @author Mikhail Mikhailov
 * Exception IDs for this module.
 */
public class CoreExceptionIds {
    /**
     * Interval type does not support unlock.
     */
    public static final ExceptionId EX_DATA_TIMELINE_INTERVAL_READ_ONLY
            = new ExceptionId("EX_CORE_TIMELINE_INTERVAL_READ_ONLY", "app.data.timeline.interval.read.only");
    /**
     * Put string simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_STRING_NOT_SIMPLE
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_STRING_NOT_SIMPLE", "app.data.attribute.put.string.not.simple");
    /**
     * Put string simple attribute: Attribute exists and is not string.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_STRING_NOT_STRING
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_STRING_NOT_STRING", "app.data.attribute.put.string.not.string");
    /**
     * Put integer simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_INT_NOT_SIMPLE
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_INT_NOT_SIMPLE", "app.data.attribute.put.int.not.simple");
    /**
     * Put integer simple attribute: Attribute exists and is not integer.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_INT_NOT_INT
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_INT_NOT_INT", "app.data.attribute.put.int.not.int");
    /**
     * Put numeric simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_NUM_NOT_SIMPLE
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_NUM_NOT_SIMPLE", "app.data.attribute.put.num.not.simple");
    /**
     * Put numeric simple attribute: Attribute exists and is not numeric.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_NUM_NOT_NUM
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_NUM_NOT_NUM", "app.data.attribute.put.num.not.num");
    /**
     * Put boolean simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_BOOL_NOT_SIMPLE
            = new ExceptionId("EX_CORE_ATTRIBUTE_PUT_BOOL_NOT_SIMPLE", "app.data.attribute.put.bool.not.simple");
    /**
     * Put boolean simple attribute: Attribute exists and is not boolean.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_BOOL_NOT_BOOL
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_BOOL_NOT_BOOL", "app.data.attribute.put.bool.not.bool");
    /**
     * Put date simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_DATE_NOT_SIMPLE
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_DATE_NOT_SIMPLE", "app.data.attribute.put.date.not.simple");
    /**
     * Put date simple attribute: Attribute exists and is not date.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_DATE_NOT_DATE
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_DATE_NOT_DATE", "app.data.attribute.put.date.not.date");
    /**
     * Put time simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_TIME_NOT_SIMPLE
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_TIME_NOT_SIMPLE", "app.data.attribute.put.time.not.simple");
    /**
     * Put time simple attribute: Attribute exists and is not time.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_TIME_NOT_TIME
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_TIME_NOT_TIME", "app.data.attribute.put.time.not.time");
    /**
     * Put timestamp simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_TIMESTAMP_NOT_SIMPLE
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_TIMESTAMP_NOT_SIMPLE", "app.data.attribute.put.timestamp.not.simple");
    /**
     * Put timestamp simple attribute: Attribute exists and is not timestamp.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_TIMESTAMP_NOT_TIMESTAMP
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_TIMESTAMP_NOT_TIMESTAMP", "app.data.attribute.put.timestamp.not.timestamp");
    /**
     * Put BLOB simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_BLOB_NOT_SIMPLE
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_BLOB_NOT_SIMPLE", "app.data.attribute.put.blob.not.simple");
    /**
     * Put BLOB simple attribute: Attribute exists and is not BLOB.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_BLOB_NOT_BLOB
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_BLOB_NOT_BLOB", "app.data.attribute.put.blob.not.blob");
    /**
     * Put CLOB simple attribute: Attribute exists and is not simple.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_CLOB_NOT_SIMPLE
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_CLOB_NOT_SIMPLE", "app.data.attribute.put.clob.not.simple");
    /**
     * Put CLOB simple attribute: Attribute exists and is not CLOB.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_CLOB_NOT_CLOB
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_CLOB_NOT_CLOB", "app.data.attribute.put.clob.not.clob");
    /**
     * Put complex attribute: Attribute exists and is not complex.
     */
    public static final ExceptionId EX_DATA_ATTRIBUTE_PUT_NOT_COMPLEX
            = new ExceptionId("EX_DATA_ATTRIBUTE_PUT_NOT_COMPLEX", "app.data.attribute.put.not.complex");
    /**
     * Append to bulk set failed.
     */
    public static final ExceptionId EX_DATA_APPEND_BULK_SET_FAILED
            = new ExceptionId("EX_DATA_APPEND_BBULK_SET_FAILED", "app.data.append.batch.set.failed");
    /**
     * Before bulk set failed.
     */
    public static final ExceptionId EX_DATA_BEFORE_BULK_SET_FAILED
            = new ExceptionId("EX_DATA_BEFORE_BULK_SET_FAILED", "app.data.before.batch.set.failed");
    /**
     * After bulk set failed.
     */
    public static final ExceptionId EX_DATA_AFTER_BULK_SET_FAILED
            = new ExceptionId("EX_DATA_AFTER_BULK_SET_FAILED", "app.data.after.batch.set.failed");
    /**
     * Init bulk set failed.
     */
    public static final ExceptionId EX_DATA_INIT_BULK_SET_FAILED
            = new ExceptionId("EX_DATA_INIT_BULK_SET_FAILED", "app.data.init.batch.set.failed");
    /**
     * Finish bulk set failed.
     */
    public static final ExceptionId EX_DATA_FINISH_BULK_SET_FAILED
            = new ExceptionId("EX_DATA_FINISH_BULK_SET_FAILED", "app.data.finish.batch.set.failed");
    /**
     * Copy stream close failed.
     */
    public static final ExceptionId EX_DATA_COPY_STREAM_CLOSE_FAILED
            = new ExceptionId("EX_DATA_COPY_STREAM_CLOSE_FAILED", "app.data.copy.stream.close.failed");
    /**
     * Undefined error occurs.
     */
    public static final ExceptionId EX_SYSTEM_CONNECTION_UNWRAP
            = new ExceptionId("EX_SYSTEM_CONNECTION_UNWRAP", "app.system.connection.unwrap");
    /**
     * No query template found for given descriptor..
     */
    public static final ExceptionId EX_DATA_STORAGE_NO_QUERY_TEMPLATE_FOR_DECSRIPTOR
            = new ExceptionId("EX_DATA_STORAGE_NO_QUERY_TEMPLATE_FOR_DECSRIPTOR", "app.data.storage.no.query.template.for.descriptor");
    /**
     * Invalid input. Entity name or path blank.
     */
    public static final ExceptionId EX_UPATH_INVALID_INPUT_PATH_IS_BLANK
            = new ExceptionId("EX_UPATH_INVALID_INPUT_PATH_IS_BLANK", "app.upath.invalid.input.entity.or.path.blank");
    /**
     * Invalid state. Entity not found by name.
     */
    public static final ExceptionId EX_UPATH_MALFORMED_EXPRESSION
            = new ExceptionId("EX_UPATH_MALFORMED_EXPRESSION", "app.upath.malformed.expression");
    /**
     * Invalid input. Path was split to zero elements.
     */
    public static final ExceptionId EX_UPATH_INVALID_INPUT_SPLIT_TO_ZERO_ELEMENTS
            = new ExceptionId("EX_UPATH_INVALID_INPUT_SPLIT_TO_ZERO_ELEMENTS", "app.upath.invalid.input.split.to.zero.elements");
    /**
     * Invalid input. Malformed intro (namespace : typename).
     */
    public static final ExceptionId EX_UPATH_INVALID_INPUT_MORE_THEN_TWO_COLONS
            = new ExceptionId("EX_UPATH_INVALID_INPUT_MORE_THEN_TWO_COLONS", "app.upath.invalid.input.more.then.two.colons");
    /**
     * Invalid state. Attribute selected for an intermediate path element is not a complex attribute.
     */
    public static final ExceptionId EX_UPATH_NOT_A_COMPLEX_ATTRIBUTE_FOR_INTERMEDIATE_PATH_ELEMENT
            = new ExceptionId("EX_UPATH_NOT_A_COMPLEX_ATTRIBUTE_FOR_INTERMEDIATE_PATH_ELEMENT", "app.upath.invalid.state.not.complex.for.intermediate");
    /**
     * Invalid state. Attribute selected for an intermediate path element is not a complex attribute.
     */
    public static final ExceptionId EX_UPATH_INVALID_SET_NOT_A_COMPLEX_FOR_INTERMEDIATE
            = new ExceptionId("EX_UPATH_INVALID_SET_NOT_A_COMPLEX_FOR_INTERMEDIATE", "app.upath.invalid.set.not.complex.for.intermediate");
    /**
     * Invalid input. Upath for set operations must end with collecting element.
     */
    public static final ExceptionId EX_UPATH_INVALID_SET_WRONG_END_ELEMENT
            = new ExceptionId("EX_UPATH_INVALID_SET_WRONG_END_ELEMENT", "app.upath.invalid.set.wrong.end.element");
    /**
     * Invalid input. Last element of this UPath and target attribute have different value types.
     */
    public static final ExceptionId EX_UPATH_INVALID_SET_WRONG_TARGET_ATTRIBUTE_TYPE
            = new ExceptionId("EX_UPATH_INVALID_SET_WRONG_TARGET_ATTRIBUTE_TYPE", "app.upath.invalid.set.wrong.target.attribute.type");
    /**
     * Invalid input. Attribute and last UPath element have different names.
     */
    public static final ExceptionId EX_UPATH_INVALID_SET_WRONG_ATTRIBUTE_NAME
            = new ExceptionId("EX_UPATH_INVALID_SET_WRONG_ATTRIBUTE_NAME", "app.upath.invalid.set.wrong.attribute.name");
    /**
     * Invalid input. Root expression incorrect.
     */
    public static final ExceptionId EX_UPATH_INVALID_ROOT_EXPRESSION
            = new ExceptionId("EX_UPATH_INVALID_ROOT_EXPRESSION", "app.upath.invalid.root.expression");
    /**
     * Invalid input. Subscript expression incorrect.
     */
    public static final ExceptionId EX_UPATH_INVALID_SUBSCRIPT_EXPRESSION
            = new ExceptionId("EX_UPATH_INVALID_SUBSCRIPT_EXPRESSION", "app.upath.invalid.subscript.expression");
    /**
     * Invalid input. Filtering expression denotes attribute not found in model.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_MISSING_ATTRIBUTE
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_MISSING_ATTRIBUTE", "app.upath.invalid.filtering.expression.missing.attribute");
    /**
     * Invalid input. Filtering expression denotes complex attribute as filter attribute. Filter attribute may be either simple or code or array.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_COMPLEX_ATTRIBUTE
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_COMPLEX_ATTRIBUTE", "app.upath.invalid.filtering.expression.complex.attribute");
    /**
     * Invalid input. Filtering expression addresses invalid attribute type. Strings, numeric types and temporal types only are supported.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_ATTRIBUTE_TYPE
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_ATTRIBUTE_TYPE", "app.upath.invalid.filtering.expression.attribute.type");
    /**
     * Invalid input. Filtering expression denotes date value in wrong format. ISO date is expected.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_DATE_FORMAT
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_DATE_FORMAT", "app.upath.invalid.filtering.expression.date.format");
    /**
     * Invalid input. Filtering expression denotes time value in wrong format. ISO time is expected.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_TIME_FORMAT
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_TIME_FORMAT", "app.upath.invalid.filtering.expression.time.format");
    /**
     * Invalid input. Filtering expression denotes timestamp value in wrong format. ISO timestamp is expected.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_TIMESTAMP_FORMAT
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_TIMESTAMP_FORMAT", "app.upath.invalid.filtering.expression.timestamp.format");
    /**
     * Invalid input. Filtering expression denotes number value in wrong format. Unquoted numeric value in octal, hexadecimal, decimal possibly with type modifyer is expected.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_NUMBER_FORMAT
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_NUMBER_FORMAT", "app.upath.invalid.filtering.expression.number.format");
    /**
     * Invalid input. Filtering expression denotes string value in wrong format. Quoted 'value' is expected.
     */
    public static final ExceptionId EX_UPATH_INVALID_FILTERING_EXPRESSION_STRING_FORMAT
            = new ExceptionId("EX_UPATH_INVALID_FILTERING_EXPRESSION_STRING_FORMAT", "app.upath.invalid.filtering.expression.string.format");
    /**
     * Invalid input. Attribute not found by path.
     */
    public static final ExceptionId EX_UPATH_INVALID_INPUT_ATTRIBUTE_NOT_FOUND_BY_PATH
            = new ExceptionId("EX_UPATH_INVALID_INPUT_ATTRIBUTE_NOT_FOUND_BY_PATH", "app.upath.invalid.input.attribute.not.found.by.path");
    /**
     * Login attempt failed.
     */
    public static final ExceptionId EX_SECURITY_CANNOT_LOGIN
            = new ExceptionId("EX_SECURITY_CANNOT_LOGIN", "app.security.cannotLogin");
    /**
     * User has no rights for endpoint.
     */
    public static final ExceptionId EX_SECURITY_USER_HAS_NO_RIGHTS_FOR_ENDPOINT
            = new ExceptionId("EX_SECURITY_USER_HAS_NO_RIGHTS_FOR_ENDPOINT", "app.security.noRightsForEndpoint");
    /**
     * Password expired.
     */
    public static final ExceptionId EX_SECURITY_USER_PASSWORD_EXPIRED
            = new ExceptionId("EX_SECURITY_USER_PASSWORD_EXPIRED", "app.security.userPasswordExpired");
    /**
     * User inactive.
     */
    public static final ExceptionId EX_SECURITY_USER_NOT_ACTIVE
            = new ExceptionId("EX_SECURITY_USER_NOT_ACTIVE", "app.security.user.not.active");
    /**
     * Unable to change password for some reason.
     */
    public static final ExceptionId EX_SECURITY_USER_PASSWORD_CHANGE_FAILED
            = new ExceptionId("EX_SECURITY_USER_PASSWORD_CHANGE_FAILED", "app.security.user.password.change.failed");
    /**
     * Role property value validation error.
     */
    public static final ExceptionId EX_ROLE_PROPERTY_VALUES_VALIDATION_ERROR
            = new ExceptionId("EX_ROLE_PROPERTY_VALUES_VALIDATION_ERROR", "app.role.property.value.validationError");
    /**
     * Role property validation error.
     */
    public static final ExceptionId EX_ROLE_PROPERTY_VALIDATION_ERROR
            = new ExceptionId("EX_ROLE_PROPERTY_VALIDATION_ERROR", "app.role.property.validationError");
    /**
     * Role data validation error.
     */
    public static final ExceptionId EX_ROLE_DATA_VALIDATION_ERROR
            = new ExceptionId("EX_ROLE_DATA_VALIDATION_ERROR", "app.role.data.validationError");
    /**
     * Role already exists.
     */
    public static final ExceptionId EX_SECURITY_ROLE_ALREADY_EXISTS
            = new ExceptionId("EX_SECURITY_ROLE_ALREADY_EXISTS", "app.security.roleExists");
    /**
     * Can not create user for some reason.
     */
    public static final ExceptionId EX_SECURITY_CANNOT_CREATE_USER
            = new ExceptionId("EX_SECURITY_CANNOT_CREATE_USER", "app.security.cannotCreate");
    /**
     * User deactivation failed.
     */
    public static final ExceptionId EX_SECURITY_CANNOT_DEACTIVATE_USER
            = new ExceptionId("EX_SECURITY_CANNOT_DEACTIVATE_USER", "app.security.cannotDeactivate");
    /**
     * Password is too short, according to the current policy.
     */
    public static final ExceptionId EX_SECURITY_USER_PASSWORD_TOO_SHORT
            = new ExceptionId("EX_SECURITY_USER_PASSWORD_TOO_SHORT", "app.security.userPasswordTooSmall");
    /**
     * Password doesn't match.
     */
    public static final ExceptionId EX_SECURITY_USER_PASSWORD_DOESNT_MATCH
            = new ExceptionId("EX_SECURITY_USER_PASSWORD_DOESNT_MATCH", "app.security.userPasswordNotMatched");
    /**
     * New password is the same as the old one, what is prohibited by the policy.
     */
    public static final ExceptionId EX_SECURITY_USER_PASSWORD_UPDATE_SAME_PASSWORD
            = new ExceptionId("EX_SECURITY_USER_PASSWORD_UPDATE_SAME_PASSWORD", "app.security.userPasswordRepeatOld");
    /**
     * User property validation exception.
     */
    public static final ExceptionId EX_USER_PROPERTY_VALUES_VALIDATION_ERROR
            = new ExceptionId("EX_USER_PROPERTY_VALUES_VALIDATION_ERROR", "app.user.property.value.validationError");
    /**
     * Upsert of a user event failed. No user.
     */
    public static final ExceptionId EX_DATA_USER_EVENT_NO_USER
            = new ExceptionId("EX_DATA_USER_EVENT_NO_USER", "app.data.userEventUpsertNoUser");
    /**
     * Upsert of an external user failed.
     */
    public static final ExceptionId EX_DATA_USER_UPSERT_EXTERNAL_USER_FAILED
            = new ExceptionId("EX_DATA_USER_UPSERT_EXTERNAL_USER_FAILED", "app.data.user.upsert.external.user.failed");
    /**
     * User property validation error.
     */
    public static final ExceptionId EX_USER_PROPERTY_VALIDATION_ERROR
            = new ExceptionId("EX_USER_PROPERTY_VALIDATION_ERROR", "app.user.property.validationError");
    /**
     * User data validation error.
     */
    public static final ExceptionId EX_USER_DATA_VALIDATION_ERROR
            = new ExceptionId("EX_USER_DATA_VALIDATION_ERROR", "app.user.data.validationError");
    /**
     * User already exists.
     */
    public static final ExceptionId EX_SECURITY_USER_ALREADY_EXISTS
            = new ExceptionId("EX_SECURITY_USER_ALREADY_EXISTS", "app.security.userExist");

    public static final ExceptionId EX_JOB_NOT_FOUND = new ExceptionId("EX_JOB_NOT_FOUND", "app.job.notFound");
    public static final ExceptionId EX_JOB_BATCH_EXECUTION_FAILED = new ExceptionId("EX_JOB_BATCH_EXECUTION_FAILED", "app.job.batch.execution.failed");
    public static final ExceptionId EX_JOB_DISABLED = new ExceptionId("EX_JOB_DISABLED", "app.job.disabled");
    public static final ExceptionId EX_JOB_ALREADY_RUNNING = new ExceptionId("EX_JOB_ALREADY_RUNNING", "app.job.alreadyRunning");
    public static final ExceptionId EX_JOB_BATCH_RESTART_FAILED_ALREADY_RUNNING = new ExceptionId("EX_JOB_BATCH_RESTART_FAILED_ALREADY_RUNNING", "app.job.batch.restart.failed.already.running");
    public static final ExceptionId EX_JOB_BATCH_RESTART_FAILED = new ExceptionId("EX_JOB_BATCH_RESTART_FAILED", "app.job.batch.restart.failed");
    public static final ExceptionId EX_JOB_PARAMETERS_VALIDATION_ERRORS = new ExceptionId("EX_JOB_PARAMETERS_VALIDATION_ERRORS", "app.job.parameters.validation.errors");
    public static final ExceptionId EX_JOB_PARAMETER_INVALID_TYPE = new ExceptionId("EX_JOB_PARAMETER_INVALID_TYPE", "app.job.parameter.invalidType");
    public static final ExceptionId EX_JOB_EXECUTION_NOT_FOUND = new ExceptionId("EX_JOB_EXECUTION_NOT_FOUND", "app.job.execution.notFound");
    public static final ExceptionId EX_JOB_BATCH_STOP_FAILED = new ExceptionId("EX_JOB_BATCH_STOP_FAILED", "app.job.batch.stop.failed");
    public static final ExceptionId EX_JOB_STEP_SUBMIT_FAILED = new ExceptionId("EX_JOB_STEP_SUBMIT_FAILED", "app.job.general.step.submit.failed");
    public static final ExceptionId EX_JOB_BAD_CREDENTIALS = new ExceptionId("EX_JOB_BAD_CREDENTIALS", "app.job.authenticationFailed.badCredentials");
    public static final ExceptionId EX_JOB_DESCRIPTOR_MISSING = new ExceptionId("EX_JOB_DESCRIPTOR_MISSING", CoreModule.MODULE_ID + ".job.descriptor.missing");
    public static final ExceptionId EX_JOB_DESCRIPTOR_NOT_FOUND_AT_RUNTIME = new ExceptionId("EX_JOB_DESCRIPTOR_NOT_FOUND_AT_RUNTIME", CoreModule.MODULE_ID + ".job.descriptor.not.found.at.runtime");
    public static final ExceptionId EX_JOB_SAVE_TO_SYSTEM_JOB = new ExceptionId("EX_JOB_SAVE_TO_SYSTEM_JOB", CoreModule.MODULE_ID + ".job.save.to.system.job");
    public static final ExceptionId EX_JOB_UPSERT_VALIDATION_ERROR = new ExceptionId("EX_JOB_UPSERT_VALIDATION_ERROR", CoreModule.MODULE_ID + ".job.upsert.validation.error");
    /**
     *
     */
    public static final ExceptionId EX_JOB_IMPORT_FAILED
            = new ExceptionId("EX_JOB_IMPORT_FAILED", "app.job.importFailed");
    /**
     *
     */
    public static final ExceptionId EX_JOB_UPDATE_ERROR
            = new ExceptionId("EX_JOB_UPDATE_ERROR", "app.job.updateError");
    /**
     *
     */
    public static final ExceptionId EX_JOB_PARAMETER_PREPARE_UPSERT_ERROR
            = new ExceptionId("EX_JOB_PARAMETER_PREPARE_UPSERT_ERROR", "app.job.parameter.prepareUpsertError");
    /**
     *
     */
    public static final ExceptionId EX_JOB_DELETE_FAILED
            = new ExceptionId("EX_JOB_DELETE_FAILED", "app.job.deleteFailed");
    /**
     *
     */
    public static final ExceptionId EX_JOB_PARAMETER_PREPARE_VALIDATION_ERROR
            = new ExceptionId("EX_JOB_PARAMETER_PREPARE_VALIDATION_ERROR", "app.job.parameter.prepareValidationError");
    /**
     *
     */
    public static final ExceptionId EX_JOB_TRIGGER_UPDATE_ERROR
            = new ExceptionId("EX_JOB_TRIGGER_UPDATE_ERROR", "app.job.trigger.updateError");
    /**
     *
     */
    public static final ExceptionId EX_JOB_TRIGGER_DELETE_FAILED
            = new ExceptionId("EX_JOB_TRIGGER_DELETE_FAILED", "app.job.trigger.deleteFailed");
    /**
     *
     */
    public static final ExceptionId EX_JOB_EXPORT_FAILED
            = new ExceptionId("EX_JOB_EXPORT_FAILED", "app.job.exportFailed");
    /**
     *
     */
    public static final ExceptionId EX_JOB_PARAMETER_EXTRACT_ERROR
            = new ExceptionId("EX_JOB_PARAMETER_EXTRACT_ERROR", "app.job.parameter.extractError");
    /**
     * Job parameter definition is not a single value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_IS_NOT_SINGLE
           = new ExceptionId("EX_JOB_PARAMETER_IS_NOT_SINGLE", CoreModule.MODULE_ID + ".job.parameter.is.not.single");
    /**
     * Job parameter definition is not a collection value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_IS_NOT_COLLECTION
           = new ExceptionId("EX_JOB_PARAMETER_IS_NOT_COLLECTION", CoreModule.MODULE_ID + ".job.parameter.is.not.collection");
    /**
     * Job parameter definition is not a map value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_IS_NOT_MAP
           = new ExceptionId("EX_JOB_PARAMETER_IS_NOT_MAP", CoreModule.MODULE_ID + ".job.parameter.is.not.map");
    /**
     * Job parameter definition is not a custom value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_IS_NOT_CUSTOM
           = new ExceptionId("EX_JOB_PARAMETER_IS_NOT_CUSTOM", CoreModule.MODULE_ID + ".job.parameter.is.not.custom");
    /**
     * Job parameter definition is not a custom value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_VALIDATE_NOT_SINGLE_OR_CUSTOM
           = new ExceptionId("EX_JOB_PARAMETER_VALIDATE_NOT_SINGLE_OR_CUSTOM", CoreModule.MODULE_ID + ".job.parameter.validate.not.single.or.custom");
    /**
     * Job parameter definition is not a custom value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_VALIDATE_NOT_COLLECTION
           = new ExceptionId("EX_JOB_PARAMETER_VALIDATE_NOT_COLLECTION", CoreModule.MODULE_ID + ".job.parameter.validate.not.collection");
    /**
     * Job parameter definition is not a custom value.
     */
    public static final ExceptionId EX_JOB_PARAMETER_VALIDATE_NOT_MAP
           = new ExceptionId("EX_JOB_PARAMETER_VALIDATE_NOT_MAP", CoreModule.MODULE_ID + ".job.parameter.validate.not.map");
    /**
     * Job parameter definition is not a map value.
     */
    public static final ExceptionId EX_JOB_DEFINITION_FETCH_FAILURE
           = new ExceptionId("EX_JOB_DEFINITION_FETCH_FAILURE", CoreModule.MODULE_ID + ".job.definition.fetch.failure");
    /**
     * Job parameter definition is not a map value.
     */
    public static final ExceptionId EX_JOB_DEFINITION_PUT_FAILURE
           = new ExceptionId("EX_JOB_DEFINITION_PUT_FAILURE", CoreModule.MODULE_ID + ".job.definition.put.failure");
    /**
     * Order field not allowed.
     */
    public static final ExceptionId EX_JOB_DEFINITION_SORT_FIELD_NOT_ALLOWED
           = new ExceptionId("EX_JOB_DEFINITION_ORDER_FIELD_NOT_ALLOWED", CoreModule.MODULE_ID + ".job.order.field.not.allowed");
    /**
     * Order direction not allowed.
     */
    public static final ExceptionId EX_JOB_DEFINITION_ORDER_DIRECTION_NOT_ALLOWED
           = new ExceptionId("EX_JOB_DEFINITION_ORDER_DIRECTION_NOT_ALLOWED", CoreModule.MODULE_ID + ".job.order.direction.not.allowed");
    /**
     * Order direction not allowed.
     */
    public static final ExceptionId EX_JOB_DEFINITION_PARAMETERS_UNMARSHALING_FAILED
           = new ExceptionId("EX_JOB_DEFINITION_PARAMETERS_UNMARSHALING_FAILED", CoreModule.MODULE_ID + ".job.parameters.unmarshaling.failed");
    /**
     * Job name missing in job definition.
     */
    public static final ExceptionId EX_JOB_DEFINITION_JOB_NAME_MISSING
           = new ExceptionId("EX_JOB_DEFINITION_JOB_NAME_MISSING", CoreModule.MODULE_ID + ".job.definition.job.name.missing");
    /**
     * Job name missing in job definition.
     */
    public static final ExceptionId EX_JOB_DEFINITION_NAME_MISSING
           = new ExceptionId("EX_JOB_DEFINITION_NAME_MISSING", CoreModule.MODULE_ID + ".job.definition.name.missing");
    /**
     * Job name missing in job definition.
     */
    public static final ExceptionId EX_JOB_DEFINITION_INVALID_FIELD_SPEC
           = new ExceptionId("EX_JOB_DEFINITION_INVALID_FIELD_SPEC", CoreModule.MODULE_ID + ".job.definition.invalid.field.spec");
    /**
     * Both jobName and jobDefinitionId specified.
     */
    public static final ExceptionId EX_JOB_RUN_JOB_NAME_AND_DEFINITION_ID_SPECIFIED
           = new ExceptionId("EX_JOB_RUN_JOB_NAME_AND_DEFINITION_ID_SPECIFIED", CoreModule.MODULE_ID + ".job.name.and.definition.id.specified");
    /**
     * Both jobName and jobDefinitionId specified.
     */
    public static final ExceptionId EX_JOB_RUN_JOB_NAME_MISSING
           = new ExceptionId("EX_JOB_RUN_JOB_NAME_MISSING", CoreModule.MODULE_ID + ".job.name.missing");
    /**
     * Job definitions [x] have running executions [x] and cannot be removed.
     * Please, stop them first or wait until they are complete.
     */
    public static final ExceptionId EX_JOB_REMOVE_FAILED_RUNNING_EXECUTIONS
           = new ExceptionId("EX_JOB_REMOVE_FAILED_RUNNING_EXECUTIONS", CoreModule.MODULE_ID + ".job.remove.failed.running.executions");
    /**
     * Impossible convert to required type.
     */
    public static final ExceptionId EX_DATA_IMPORT_IMPOSSIBLE_CONVERT_TO_TYPE
            = new ExceptionId("EX_DATA_IMPORT_IMPOSSIBLE_CONVERT_TO_TYPE", "app.data.import.impossible.convert.type");
    /**
     * String has incorrect format
     */
    public static final ExceptionId EX_DATA_IMPORT_INCORRECT_STRING_TIME_FORMAT
            = new ExceptionId("EX_DATA_IMPORT_INCORRECT_STRING_TIME_FORMAT", "app.data.import.incorrect.string.time.format");
    /**
     * Time conversation support just calendar,string,date
     */
    public static final ExceptionId EX_DATA_IMPORT_UNSUPPORTED_TIME_FORMAT
            = new ExceptionId("EX_DATA_IMPORT_UNSUPPORTED_TIME_FORMAT", "app.data.import.unsupported.time.type");

    /**
     * File name not valid(encoding).
     */
    public static final ExceptionId EX_DATA_INCORRECT_ENCODING
            = new ExceptionId("EX_DATA_INCORRECT_ENCODING", "app.data.incorrectEncoding");

    public static final ExceptionId EX_DATA_SAVE_FILE
            = new ExceptionId("EX_DATA_SAVE_FILE", "app.data.save.file");

    /**
     * Cannot load LOB data.
     */
    public static final ExceptionId EX_DATA_CANNOT_LOAD_LOB
            = new ExceptionId("EX_DATA_CANNOT_LOAD_LOB", "app.data.cannotLoadLOB");

    /**
     * Cannot save LOB data.
     */
    public static final ExceptionId EX_DATA_CANNOT_SAVE_LOB
            = new ExceptionId("EX_DATA_CANNOT_SAVE_LOB", "app.data.cannotSaveLOB");

    /**
     * Cannot save LOB data.
     */
    public static final ExceptionId EX_DATA_INVALID_LOB_UPDATE
            = new ExceptionId("EX_DATA_INVALID_LOB_UPDATE", "app.data.invalidLobUpdate");
    public static final ExceptionId EX_AUDIT_EVENT_JSON_SERIALIZATION_EXCEPTION =
            new ExceptionId("EX_AUDIT_EVENT_JSON_SERIALIZATION_EXCEPTION", "app.audit.parameters.json.serialization.error");

    public static final ExceptionId EX_FAIL_ROUTE_DUMP =
            new ExceptionId("EX_FAIL_ROUTE_DUMP", "app.bus.fail.dump.route");

    public static final ExceptionId EX_JAXB_CONTEXT_FAILURE =
            new ExceptionId("EX_JAXB_CONTEXT_FAILURE", "app.jabx.context.init.failure");
    public static final ExceptionId EX_MARSHAL_SECURITY_META_EXCEPTION =
            new ExceptionId("EX_MARSHAL_SECURITY_META_EXCEPTION", "app.marshal.security.exception");

    public static final ExceptionId EX_ERROR_SECURITY_LABEL_NAME_IS_NULL =
            new ExceptionId("EX_ERROR_SECURITY_LABEL_NAME_IS_NULL", "app.security.label.name.is.null");
    public static final ExceptionId EX_ERROR_SECURITY_LABEL_DISPLAY_NAME_IS_NULL =
            new ExceptionId("EX_ERROR_SECURITY_LABEL_DISPLAY_NAME_IS_NULL", "app.security.label.display.name.is.null");
    public static final ExceptionId EX_ERROR_SECURITY_LABEL_NAME_NOT_UNIQUE =
            new ExceptionId("EX_ERROR_SECURITY_LABEL_NAME_NOT_UNIQUE", "app.security.label.name.not.unique");
    public static final ExceptionId EX_ERROR_SECURITY_LABEL_ATTRIBUTES_IS_EMPTY =
            new ExceptionId("EX_ERROR_SECURITY_LABEL_ATTRIBUTES_IS_EMPTY", "app.security.label.attributes.is.empty");
    public static final ExceptionId EX_ERROR_SECURITY_LABEL_NAME_NOT_FOUND =
            new ExceptionId("EX_ERROR_SECURITY_LABEL_NAME_NOT_FOUND", "app.security.label.name.not.found");
    public static final ExceptionId EX_ERROR_SECURITY_DATASOURCE_REGISTERED =
            new ExceptionId("EX_ERROR_SECURITY_DATASOURCE_REGISTERED", "app.security.datasource.registered");
    public static final ExceptionId EX_META_INIT_METADATA_FAILED =
            new ExceptionId("EX_META_INIT_METADATA_FAILED", "app.meta.init.metadata.failed");
    public static final ExceptionId EX_META_STORAGE_MODEL_EMPTY =
            new ExceptionId("EX_META_STORAGE_MODEL_EMPTY", "app.meta.storage.model.empty");

    public static final ExceptionId EX_LIBRARIES_MIME_TYPE_MISSING =
            new ExceptionId("EX_LIBRARIES_MIME_TYPE_MISSING", "app.libraries.mime.type.missing");

    public static final ExceptionId EX_LIBRARIES_IO_INTERACTION_FAILED =
            new ExceptionId("EX_LIBRARIES_IO_INTERACTION_FAILED", "app.libraries.io.interaction.failed");

    public static final ExceptionId EX_LIBRARIES_VERSION_MISSING =
            new ExceptionId("EX_LIBRARIES_VERSION_MISSING", "app.libraries.version.missing");

    public static final ExceptionId EX_LOB_RESULT_READ_FAILED =
            new ExceptionId("EX_LOB_RESULT_READ_FAILED", "app.lob.result.read.failed");

    public static final ExceptionId EX_DATA_LOAD_HANDLER_NOT_FOUND =
            new ExceptionId("EX_DATA_LOAD_HANDLER_NOT_FOUND", "ex.data.load.handler.not.found");

    public static final ExceptionId EX_DATA_LOAD_FORMAT_NOT_SUPPORTED =
            new ExceptionId("EX_DATA_LOAD_FORMAT_NOT_SUPPORTED", "ex.data.load.format.not.supported");

    public static final ExceptionId EX_CORE_LOB_INVALID_CLOB_MEDIATYPE =
            new ExceptionId("EX_CORE_LOB_INVALID_CLOB_MEDIATYPE", CoreModule.MODULE_ID + ".lob.invalid.character.mediatype");

    public static final ExceptionId EX_CORE_LOB_INVALID_ID_FORMAT =
            new ExceptionId("EX_CORE_LOB_INVALID_ID_FORMAT", CoreModule.MODULE_ID + ".lob.invalid.id.format");
    /**
     * Constructor.
     */
    private CoreExceptionIds() {
        super();
    }
}
