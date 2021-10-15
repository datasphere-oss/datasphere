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

package com.huahui.datasphere.mdm.core.exception;

import com.huahui.datasphere.mdm.system.exception.ExceptionId;

/**
 * @author theseusyang
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
    public static final ExceptionId EX_JOB_UNKNOWN_PARAMETERS = new ExceptionId("EX_JOB_UNKNOWN_PARAMETERS", "app.job.unknown.parameters");
    public static final ExceptionId EX_JOB_PARAMETERS_NOT_SET = new ExceptionId("EX_JOB_PARAMETERS_NOT_SET", "app.job.parameters.not.set");
    public static final ExceptionId EX_JOB_SAME_NAME = new ExceptionId("EX_JOB_SAME_NAME", "app.job.sameName");
    public static final ExceptionId EX_JOB_PARAMETER_VALIDATION_ERROR = new ExceptionId("EX_JOB_PARAMETER_VALIDATION_ERROR", "app.job.parameter.validationError");
    public static final ExceptionId EX_JOB_PARAMETER_INVALID_TYPE = new ExceptionId("EX_JOB_PARAMETER_INVALID_TYPE", "app.job.parameter.invalidType");
    public static final ExceptionId EX_JOB_SAME_PARAMETERS = new ExceptionId("EX_JOB_SAME_PARAMETERS", "app.job.sameParameters");
    public static final ExceptionId EX_JOB_TRIGGER_NOT_FOUND = new ExceptionId("EX_JOB_TRIGGER_NOT_FOUND", "app.job.trigger.notFound");
    public static final ExceptionId EX_JOB_EXECUTION_NOT_FOUND = new ExceptionId("EX_JOB_EXECUTION_NOT_FOUND", "app.job.execution.notFound");
    public static final ExceptionId EX_JOB_CRON_SUSPICIOUS_SECOND = new ExceptionId("EX_JOB_CRON_SUSPICIOUS_SECOND", "app.job.cronExpression.suspicious.second");
    public static final ExceptionId EX_JOB_CRON_SUSPICIOUS_MINUTE = new ExceptionId("EX_JOB_CRON_SUSPICIOUS_MINUTE", "app.job.cronExpression.suspicious.minute");
    public static final ExceptionId EX_JOB_CRON_SUSPICIOUS_SHORT_CYCLES_DOM = new ExceptionId("EX_JOB_CRON_SUSPICIOUS_SHORT_CYCLES_DOM", "app.job.cronExpression.suspicious.cycles_dom");
    public static final ExceptionId EX_JOB_CRON_EXPRESSION = new ExceptionId("EX_JOB_CRON_EXPRESSION", "app.job.cronExpression");
    public static final ExceptionId EX_JOB_TRIGGER_SAME_NAME = new ExceptionId("EX_JOB_TRIGGER_SAME_NAME", "app.job.trigger.sameName");
    public static final ExceptionId EX_JOB_TRIGGER_START_JOB_NOT_FOUND = new ExceptionId("EX_JOB_TRIGGER_START_JOB_NOT_FOUND", "app.job.trigger.startJob.notFound");
    public static final ExceptionId EX_JOB_TRIGGER_RECURSIVE_CALL = new ExceptionId("EX_JOB_TRIGGER_RECURSIVE_CALL", "app.job.trigger.recursiveCall");
    public static final ExceptionId EX_JOB_TRIGGER_PARAMETER_VALIDATION_ERROR = new ExceptionId("EX_JOB_TRIGGER_PARAMETER_VALIDATION_ERROR", "app.job.trigger.parameter.validationError");
    public static final ExceptionId EX_JOB_BATCH_STOP_FAILED = new ExceptionId("EX_JOB_BATCH_STOP_FAILED", "app.job.batch.stop.failed");
    public static final ExceptionId EX_JOB_STEP_SUBMIT_FAILED = new ExceptionId("EX_JOB_STEP_SUBMIT_FAILED", "app.job.general.step.submit.failed");
    public static final ExceptionId EX_JOB_BAD_CREDENTIALS = new ExceptionId("EX_JOB_BAD_CREDENTIALS", "app.job.authenticationFailed.badCredentials");
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
    /**
     * Constructor.
     */
    private CoreExceptionIds() {
        super();
    }
}
