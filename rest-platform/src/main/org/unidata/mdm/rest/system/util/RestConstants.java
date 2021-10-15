/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
/**
 *
 */
package org.unidata.mdm.rest.system.util;


/**
 * @author Mikhail Mikhailov
 * String constants common to various rest services.
 */
public final class RestConstants {
    private RestConstants() { }
    /**
     * Draft ID param.
     */
    public static final String QUERY_PARAM_DRAFT_ID = "draftId";
    /**
     * Parent draft ID param.
     */
    public static final String QUERY_PARAM_PARENT_DRAFT_ID = "parentDraftId";
    /**
     * ID param.
     */
    public static final String DATA_PARAM_ID = "id";
    /**
     * External ID param.
     */
    public static final String DATA_PARAM_EXT_ID = "externalId";
    /**
     * Operation ID param.
     */
    public static final String DATA_PARAM_OPERATION_ID = "operationId";
    /**
     * Source system param.
     */
    public static final String DATA_PARAM_SOURCE_SYSTEM = "sourceSystem";
    /**
     * Task ID param.
     */
    public static final String DATA_PARAM_TASK_ID = "taskId";
    /**
     * Task data param.
     */
    public static final String DATA_PARAM_TASKS = "tasks";
    /**
     * ID winner param.
     */
    public static final String DATA_PARAM_WINNER_ID = "winnerEtalonId";
    /**
     * Attribute name param.
     */
    public static final String DATA_PARAM_ATTR = "attr";
    /**
     * Name param.
     */
    public static final String DATA_PARAM_NAME = "name";
    /**
     * Type param.
     */
    public static final String DATA_PARAM_TYPE = "type";
    /**
     * Date param.
     */
    public static final String DATA_PARAM_DATE = "date";
    /**
     * Last update date param.
     */
    public static final String DATA_PARAM_LUD = "lud";
    /**
     * Date param from.
     */
    public static final String DATA_PARAM_FROM = "from";
    /**
     * Date param time stamps.
     */
    public static final String DATA_PARAM_TIMESTAMPS = "timestamps";
    /**
     * Date param to.
     */
    public static final String DATA_PARAM_TO = "to";
    /**
     * Upload file attachment param.
     */
    public static final String DATA_PARAM_FILE = "file";
    /**
     * Upload file name attachment param.
     */
    public static final String DATA_PARAM_FILENAME = "filename";
    /**
     * Merged param.
     */
    public static final String DATA_PARAM_MERGED = "merged";
    /**
     * Finished param.
     */
    public static final String DATA_PARAM_FINISHED = "finished";
    /**
     * Include inactive param.
     */
    public static final String DATA_PARAM_INCLUDE_INACTIVE = "inactive";
    /**
     * Include drafts param.
     */
    public static final String DATA_PARAM_INCLUDE_DRAFTS = "drafts";
    /**
     * Include diff to draft param.
     */
    public static final String DATA_PARAM_DIFF_TO_DRAFT = "diffToDraft";
    /**
     * Include diff to previous state param.
     */
    public static final String DATA_PARAM_DIFF_TO_PREVIOUS = "diffToPrevious";
    /**
     * Include drafts param.
     */
    public static final String DATA_PARAM_RETURN_FIELDS = "returnFields";
    /**
     * Data path tag.
     */
    public static final String PATH_PARAM_DATA = "data";
    /**
     * Blob path tag.
     */
    public static final String PATH_PARAM_BLOB = "blob";
    /**
     * Clob path tag.
     */
    public static final String PATH_PARAM_CLOB = "clob";
    /**
     * Path param id.
     */
    public static final String PATH_PARAM_ID = "id";
    /**
     * Path param keys.
     */
    public static final String PATH_PARAM_KEYS = "keys";
    /**
     * Etalon path component.
     */
    public static final String PATH_PARAM_ETALON = "etalon";
    /**
     * Origin path component.
     */
    public static final String PATH_PARAM_ORIGIN = "origin";
    /**
     * Origin id path component.
     */
    public static final String PATH_PARAM_ORIGIN_ID = "originId";
    /**
     * External path component.
     */
    public static final String PATH_PARAM_EXTERNAL = "external";
    /**
     * Version path component.
     */
    public static final String PATH_PARAM_VERSION = "version";
    /**
     * Upload file name attachment param.
     */
    public static final String PATH_PARAM_FILENAME = "filename";
    /**
     * Time line.
     */
    public static final String PATH_PARAM_TIMELINE = "timeline";
    /**
     * Merge path component.
     */
    public static final String PATH_PARAM_MERGE = "merge";
    /**
     * Workflow.
     */
    public static final String PATH_PARAM_WORKFLOW = "workflow";
    /**
     * Complete task.
     */
    public static final String PATH_PARAM_COMPLETE = "complete";
    /**
     * Approve.
     */
    public static final String PATH_PARAM_APPROVE = "approve";
    /**
     * Decline.
     */
    public static final String PATH_PARAM_DECLINE = "decline";
    /**
     * Range.
     */
    public static final String PATH_PARAM_RANGE = "range";
    /**
     * Run.
     */
    public static final String PATH_PARAM_RUN = "run";
    /**
     * Wipe a record.
     */
    public static final String PATH_PARAM_WIPE = "wipe";
    /**
     * Wipe a record.
     */
    public static final String QUERY_PARAM_WIPE = "wipe";
    /**
     * Soft-delete a record.
     */
    public static final String QUERY_PARAM_INACTIVATE_ETALON = "inactivateEtalon";
    /**
     * Inactivate origin.
     */
    public static final String QUERY_PARAM_INACTIVATE_ORIGIN = "inactivateOrigin";
    /**
     * Inactivate period.
     */
    public static final String QUERY_PARAM_INACTIVATE_PERIOD = "inactivatePeriod";
    /**
     * Configure.
     */
    public static final String PATH_PARAM_CONFIGURE = "configure";
    /**
     * Notifications.
     */
    public static final String PATH_PARAM_NOTIFICATIONS = "notifications";
    /**
     * Tasks.
     */
    public static final String PATH_PARAM_TASKS = "tasks";
    /**
     * Tasks.
     */
    public static final String PATH_PARAM_TASK_STATS = "stat";
    /**
     * Types.
     */
    public static final String PATH_PARAM_TYPES = "types";
    /**
     * Assignments.
     */
    public static final String PATH_PARAM_ASSIGNMENTS = "assignments";
    /**
     * Assignable entities.
     */
    public static final String PATH_PARAM_ASSIGNABLE_ENTITIES = "assignable-entities";
    /**
     * Assignable classifiers.
     */
    public static final String PATH_PARAM_ASSIGNABLE_CLASSIFIERS = "assignable-classifiers";
    /**
     * Processes.
     */
    public static final String PATH_PARAM_PROCESSES = "processes";
    /**
     * Attachments id.
     */
    public static final String PATH_PARAM_ATTACHMENT_ID = "attachmentId";
    /**
     * Download parameter.
     */
    public static final String PATH_PARAM_DOWNLOAD = "download";
    /**
     * Assign.
     */
    public static final String PATH_PARAM_ASSIGN = "assign";
    /**
     * Assign.
     */
    public static final String PATH_PARAM_UNASSIGN = "unassign";
    /**
     * Process comments.
     */
    public static final String PATH_PARAM_PROCESS_INSTANCE_ID = "processInstanceId";
    /**
     * Process comments.
     */
    public static final String PATH_PARAM_TASK_ID = "taskId";
    /**
     * Process comments.
     */
    public static final String PATH_PARAM_PROCESS_COMMENT = "comment";
    /**
     * Process attaches.
     */
    public static final String PATH_PARAM_PROCESS_ATTACH = "attach";
    /**
     * History.
     */
    public static final String PATH_PARAM_HISTORY = "history";
    /**
     * Copy.
     */
    public static final String PATH_PARAM_COPY = "copy";
    /**
     * Apply draft.
     */
    public static final String PATH_PARAM_APPLY_DRAFT = "apply-draft";
    /**
     * Item types filter fro History.
     */
    public static final String  PATH_PARAM_ITEM_TYPE = "itemType";
    /**
     * Diagram.
     */
    public static final String PATH_PARAM_DIAGRAM = "diagram";
    /**
     * Security.
     */
    public static final String PATH_PARAM_SECURITY = "security";
    /**
     * User.
     */
    public static final String PATH_PARAM_USER = "user";
    /**
     * Role.
     */
    public static final String PATH_PARAM_ROLE = "role";
    /**
     * System.
     */
    public static final String PATH_PARAM_SYSTEM = "system";
    /**
     * Logs.
     */
    public static final String PATH_PARAM_LOGS = "logs";
    /**
     * Record restore.
     */
    public static final String PATH_PARAM_RESTORE = "restore";
    /**
     * Period restore.
     */
    public static final String PATH_PARAM_PERIOD_RESTORE = "period-restore";
    /**
     * Input timestamp regex.
     */
    public static final String DEFAULT_TIMESTAMP_PATTERN = "(([0-9T\\-\\:\\.]{23})?)";
    /**
     * Entity name.
     */
	public static final String DATA_IMPORT_PARAMS = "importParams";
    /**
     * Sort by date asc or desc.
     */
    public static final String QUERY_PARAM_SORT_DATE_ASC = "sortDateAsc";
    /**
     * Lookup entity.
     */
    public static final String LOOKUP_ENTITY_TYPE = "LookupEntity";
    /**
     * Lookup entity.
     */
    public static final String REGISTER_ENTITY_TYPE = "Entity";
    /**
     *
     */
    public static final String PATH_PARAM_RELATION_BULK = "relation-bulk";

    /**
     * Task form properties
     */
    public static final String PATH_PARAM_FORM_PROPERTIES = "form_properties";
    /**
     * Bulk operation types
     */
    public static final String PATH_PARAM_GET_BULK_OPERATIONS = "get_bulk_operations";
    /**
     * Bulk operation types
     */
    public static final String PATH_PARAM_GET_BULK_OPERATION_PARAMETERS = "get_bulk_operation_parameters";
    /**
     * Apply operation type
     */
    public static final String PATH_PARAM_APPLY_BULK_OPERATION = "apply_bulk_operation";

    public static final int REST_DEFAULT_PAGE_SIZE = 100;

    public static final String HTTP_HEADER_REQUEST_UUID = "X-EGAIS-Request-Uuid";

    public static final String HTTP_HEADER_TOKEN_VALIDITY = "X-EGAIS-Token-Validity-Seconds";

    public static final String USER_LOGIN_JOB = "timed-job";
}
