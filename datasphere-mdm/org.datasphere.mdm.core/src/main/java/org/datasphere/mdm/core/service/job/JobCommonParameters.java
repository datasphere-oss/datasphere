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

package org.datasphere.mdm.core.service.job;


/**
 * @author Mikhail Mikhailov
 * Parameters, shared by many jobs.
 */
public abstract class JobCommonParameters {
    /**
     * Parameter 'partitionGroup'.
     */
    public static final String PARAM_PARTITION_GROUP = "partitionGroup";
    /**
     * Parameter 'entityName'.
     */
    public static final String PARAM_ENTITY_NAME = "entityName";
    /**
     * Parameter 'relationName'.
     */
    public static final String PARAM_RELATION_NAME = "relationName";
    /**
     * Parameter 'classifierName'.
     */
    public static final String PARAM_CLASSIFIER_NAME = "classifierName";
    /**
     * Parameter 'definition'.
     */
    public static final String PARAM_DEFINITION = "definition";
    /**
     * Parameter 'blockSize'.
     */
    public static final String PARAM_BLOCK_SIZE = "blockSize";
    /**
     * Parameter 'offset'.
     */
    public static final String PARAM_OFFSET = "offset";
    /**
     * Parameter 'startGsn'.
     */
    public static final String PARAM_START_LSN = "startLsn";
    /**
     * Parameter 'endGsn'.
     */
    public static final String PARAM_END_LSN = "endLsn";

    public static final String PARAM_SHARD_NUMBER = "dataShard";
    /**
     * Parameter 'filters'.
     */
    public static final String PARAM_FILTERS = "filters";
    /**
     * Parameter 'databaseUrl'.
     */
    public static final String PARAM_DATABASE_URL = "databaseUrl";
    /**
     * Parameter 'operationId'.
     */
    public static final String PARAM_OPERATION_ID = "operationId";
    /**
     * Parameter 'runId'.
     */
    public static final String PARAM_RUN_ID = "runId";

    public static final String PARAM_PARENT_JOB_EXECUTION_ID = "parentJobExecutionId";
    /**
     * Parameter 'paritionId'.
     */
    public static final String PARAM_PARTITION_ID = "paritionId";
    /**
     * Parameter 'exchangeObjectId'.
     */
    public static final String PARAM_EXCHANGE_OBJECT_ID = "exchangeObjectId";
    /**
     * Parameter 'timestamp'.
     */
    public static final String PARAM_START_TIMESTAMP = "timestamp";
    /**
     * Parameter 'userName'.
     */
    public static final String PARAM_USER_NAME = "userName";
    /**
     * Parameter 'userToken'.
     */
    public static final String PARAM_USER_TOKEN = "userToken";
    /**
     * Parameter 'userSelector'.
     */
    public static final String PARAM_USER_SELECTOR = "usersSelector";
    /**
     * Parameter 'reportCharSet'.
     */
    public static final String PARAM_REPORT_CHAR_SET = "reportCharSet";
    /**
     * Parameter 'reportSeparator'.
     */
    public static final String PARAM_REPORT_SEPARATOR = "reportSeparator";
    /**
     * Parameter 'reportName'.
     */
    public static final String PARAM_REPORT_NAME = "reportName";
    /**
     * Parameter 'description' (job decsription).
     */
    public static final String PARAM_JOB_DESCRIPTION = "description";
    /**
     * Previous success start date.
     */
    public static final String PARAM_PREVIOUS_SUCCESS_START_DATE = "previousSuccessStartDate";
    /**
     * Parameter 'jobUser'.
     */
    public static final String PARAM_JOB_USER = "jobUser";
    /**
     * The name of the job (as job's bean name).
     */
    public static final String PARAM_JOB_NAME = "jobName";
    /**
     * The step name param.
     */
    public static final String PARAM_STEP_NAME = "stepName";
    /**
     * Constructor.
     */
    protected JobCommonParameters() {
       super();
    }

}
