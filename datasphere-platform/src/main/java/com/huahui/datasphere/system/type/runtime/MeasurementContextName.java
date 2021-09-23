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

package com.huahui.datasphere.system.type.runtime;

/**
 * @author Mikhail Mikhailov
 *         Measurement context names.
 * TODO: Divide into modules.
 */
public enum MeasurementContextName implements MeasurementContext {
    /**
     * SOAP Get.
     */
    MEASURE_SOAP_GET,
    /**
     * SOAP Get.
     */
    MEASURE_SOAP_GET_ALL_PERIODS,
    /**
     * SOAP Get info.
     */
    MEASURE_SOAP_GET_INFO,
    /**
     * SOAP Get.
     */
    MEASURE_SOAP_RELATIONS_GET,
    /**
     * SOAP upsert.
     */
    MEASURE_SOAP_UPSERT,
    /**
     * SOAP bulk upsert!
     */
    MEASURE_SOAP_BULK_UPSERT,
    /**
     * SOAP Relations upsert.
     */
    MEASURE_SOAP_RELATIONS_UPSERT,
    /**
     * SOAP soft delete.
     */
    MEASURE_SOAP_DELETE,
    /**
     * SOAP Relations delete.
     */
    MEASURE_SOAP_RELATIONS_DELETE,
    /**
     * SOAP Auth.
     */
    MEASURE_SOAP_AUTH,
    /**
     * SOAP Search.
     */
    MEASURE_SOAP_SEARCH,
    /**
     * SOAP apply model draft.
     */
    MEASURE_SOAP_APPLY_MODEL_DRAFT,
    /**
     * SOAP delete element.
     */
    MEASURE_SOAP_DELETE_MODEL_ELEMENT,
    /**
     * SOAP get element.
     */
    MEASURE_SOAP_GET_MODEL_ELEMENT,
    /**
     * SOAP get entity with deps.
     */
    MEASURE_SOAP_GET_ENTITY_WITH_DEPS,
    /**
     * SOAP upsert element.
     */
    MEASURE_SOAP_UPSERT_MODEL_ELEMENT,
    /**
     * SOAP get model.
     */
    MEASURE_SOAP_GET_MODEL,
    /**
     * REST Get.
     */
    MEASURE_UI_GET,
    /**
     * REST Get.
     */
    MEASURE_UI_CLASSIFIERS_GET,
    /**
     * REST Node add.
     */
    MEASURE_UI_CLASSIFIER_NODE_ADD,
    /**
     * REST Node update.
     */
    MEASURE_UI_CLASSIFIER_NODE_UPDATE,
    /**
     * REST Node delete.
     */
    MEASURE_UI_CLASSIFIER_NODE_DELETE,
    /**
     * REST Node get.
     */
    MEASURE_UI_CLASSIFIER_NODE_GET,
    /**
     * REST Get.
     */
    MEASURE_UI_CLASSIFIER_GET,
    /**
     * REST Get.
     */
    MEASURE_UI_CLASSIFIER_SEARCH,
    /**
     * REST Update.
     */
    MEASURE_UI_CLASSIFIER_UPDATE,
    /**
     * REST Delete.
     */
    MEASURE_UI_CLASSIFIER_DELETE,
    /**
     * REST Create.
     */
    MEASURE_UI_CREATE,
    /**
     * REST Create.
     */
    MEASURE_UI_CLASSIFIERS_CREATE,
    /**
     * REST Update.
     */
    MEASURE_UI_UPDATE,
    /**
     * REST Delete etalon
     */
    MEASURE_UI_DELETE_BY_ETALON,
    /**
     * REST Delete origin
     */
    MEASURE_UI_DELETE_BY_ORIGIN,
    /**
     * REST Delete period
     */
    MEASURE_UI_DELETE_BY_PERIOD,
    /**
     * REST Merge.
     */
    MEASURE_UI_MERGE,
    /**
     * REST Merge.
     */
    MEASURE_UI_MERGE_PREVIEW,
    /**
     * REST restore.
     */
    MEASURE_UI_RESTORE,
    /**
     * REST period restore.
     */
    MEASURE_UI_PERIOD_RESTORE,
    /**
     * REST relations Get.
     */
    MEASURE_UI_RELATIONS_GET,
    /**
     * REST relations contains upsert.
     */
    MEASURE_UI_RELATIONS_INTEGRAL_UPSERT,
    /**
     * REST RelTo upsert endpoint.
     */
    MEASURE_UI_RELATIONS_TO_UPSERT,
    /**
     * REST relations etalon delete.
     */
    MEASURE_UI_RELATIONS_ETALON_DELETE,
    /**
     * REST relations origin delete.
     */
    MEASURE_UI_RELATIONS_ORIGIN_DELETE,
    /**
     * REST relations version delete.
     */
    MEASURE_UI_RELATIONS_VERSION_DELETE,
    /**
     * REST Auth.
     */
    MEASURE_UI_AUTH,
    /**
     * REST Search and XLS export simple.
     */
    MEASURE_UI_SEARCH_EXPORT_SIMPLE,
    /**
     * REST Search and XLS export form.
     */
    MEASURE_UI_SEARCH_EXPORT_FORM,
    /**
     * REST Search simple.
     */
    MEASURE_UI_SEARCH_SIMPLE,
    /**
     * REST Search form.
     */
    MEASURE_UI_SEARCH_FORM,
    /**
     * REST Search combo.
     */
    MEASURE_UI_SEARCH_COMBO,
    /**
     * REST Search complex.
     */
    MEASURE_UI_SEARCH_COMPLEX,
    /**
     * REST Search SAYT.
     */
    MEASURE_UI_SEARCH_SAYT,
    /**
     * Record set upsert from utility.
     */
    MEASURE_UTIL_RECORDS_UPSERT,
    /**
     * Relation set upsert from utility.
     */
    MEASURE_UTIL_RELS_UPSERT,
    /**
     * REST complete.
     */
    MEASURE_UI_COMPLETE,
    /**
     * REST tasks.
     */
    MEASURE_UI_TASKS,
    /**
     * REST bulk operations list.
     */
    MEASURE_UI_BULK_LIST,
    /**
     * REST bulk operation configure.
     */
    MEASURE_UI_BULK_CONFIGURE,
    /**
     * REST bulk operation run.
     */
    MEASURE_UI_BULK_RUN,
    /**
     * REST user notifications.
     */
    MEASURE_UI_GET_USER_NOTIFICATIONS,
    /**
     * REST user notifications count.
     */
    MEASURE_UI_COUNT_USER_NOTIFICATIONS,
    /**
     * REST user notifications delete.
     */
    MEASURE_UI_DELETE_USER_NOTIFICATION,
    /**
     * REST selected user notifications delete.
     */
    MEASURE_UI_DELETE_SELECTED_USER_NOTIFICATIONS,
    /**
     * REST all user notifications delete.
     */
    MEASURE_UI_DELETE_ALL_USER_NOTIFICATIONS,
    /**
     * Matching fucntion
     */
    MEASURE_MATCHING,
    /**
     * Reapply step
     */
    MEASURE_STEP_REAPPLY,
    /**
     * Reindex step
     */
    MEASURE_STEP_REINDEX,
    /**
     * Reindex meta steps
     */
    MEASURE_STEP_META_REINDEX,
    /**
     * stat step
     */
    MEASURE_STEP_STAT,
    /**
     * duplicate step
     */
    MEASURE_STEP_DUPLICATE,
    /**
     * delete step
     */
    MEASURE_STEP_DELETE,
    /**
     * republish step
     */
    MEASURE_STEP_REPUBLISH,
    /**
     * modify step
     */
    MEASURE_STEP_MODIFY,
    /**
     * remove step
     */
    MEASURE_STEP_REMOVE,
    /**
     * remove step
     */
    MEASURE_STEP_REMOVE_RELATIONS,
    /**
     * import step
     */
    MEASURE_STEP_IMPORT_RECORDS,
    /**
     * import relations
     */
    MEASURE_STEP_IMPORT_RELATIONS,
    /**
     * export records
     */
    MEASURE_STEP_EXPORT_RECORDS,
    /**
     * matching records
     */
    MEASURE_STEP_MATCHING,
    /**
     * restore step
     */
    MEASURE_STEP_RESTORE,
    /**
     * restore record periods
     */
    MEASURE_STEP_RESTORE_RECORD_PERIODS,
    /**
     * SOAP apply dq.
     */
    MEASURE_DQ_SOAP_APPLY,
    /**
     * REST apply dq.
     */
    MEASURE_DQ_REST_APPLY,
    /**
     * Update indexes.
     */
    MEASURE_STEP_UPDATE_MAPPING,


    FILTER_BY_CRITERIA,
    /**
     * REST Create.
     */
    MEASURE_UI_COPY_RECORD;
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name();
    }
}
