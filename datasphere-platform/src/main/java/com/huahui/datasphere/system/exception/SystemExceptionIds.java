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

package com.huahui.datasphere.system.exception;

import org.unidata.mdm.system.module.SystemModule;

/**
 * @author Mikhail Mikhailov
 * Exception IDs for this module.
 */
public class SystemExceptionIds {
    /**
     * Spring context failed to load.
     */
    public static final ExceptionId EX_SYSTEM_CONTAINER_INIT_FAILED
        = new ExceptionId("EX_SYSTEM_CONTAINER_INIT_FAILED", "app.system.container.init.failed");
    /**
     * JAXB data type factory init failure.
     */
    public static final ExceptionId EX_SYSTEM_JAXB_TYPE_FACTORY_INIT_FAILURE
        = new ExceptionId("EX_SYSTEM_JAXB_TYPE_FACTORY_INIT_FAILURE", "app.data.jaxbTypeFactoryInitFailure");
    /**
     * Node id undefined.
     */
    public static final ExceptionId EX_SYSTEM_NODE_ID_UNDEFINED
        = new ExceptionId("EX_SYSTEM_NODE_ID_UNDEFINED", "app.system.node.id.undefined");
    /**
     * Platform version undefined.
     */
    public static final ExceptionId EX_SYSTEM_PLATFORM_VERSION_UNDEFINED
        = new ExceptionId("EX_SYSTEM_PLATFORM_VERSION_UNDEFINED", "app.system.platform.version.undefined");
    /**
     * Platform version invalid.
     */
    public static final ExceptionId EX_SYSTEM_PLATFORM_VERSION_INVALID
        = new ExceptionId("EX_SYSTEM_PLATFORM_VERSION_INVALID", "app.system.platform.version.invalid");

    public static final ExceptionId EX_CONFIGURATION_PROPERTIES_INVALID =
            new ExceptionId("EX_CONFIGURATION_PROPERTIES_INVALID", "");

    public static final ExceptionId EX_MODULE_CANNOT_BE_INSTALLED = new ExceptionId("EX_MODULE_CANNOT_BE_INSTALLED", "app.module.cannot.be.installed");

    public static final ExceptionId EX_MODULE_CANNOT_BE_UNINSTALLED = new ExceptionId("EX_MODULE_CANNOT_BE_UNINSTALLED", "app.module.cannot.be.uninstalled");

    public static final ExceptionId EX_SYSTEM_CANNOT_INITIALIZE_NON_XA_FACTORY
        = new ExceptionId("EX_SYSTEM_CANNOT_INITIALIZE_NON_XA_FACTORY", "app.module.cannot.initialize.non.xa.factory");

    public static final ExceptionId EX_SYSTEM_CANNOT_INITIALIZE_XA_FACTORY
        = new ExceptionId("EX_SYSTEM_CANNOT_INITIALIZE_XA_FACTORY", "app.module.cannot.initialize.xa.factory");

    public static final ExceptionId EX_SYSTEM_CANNOT_CREATE_NON_XA_DATASOURCE
        = new ExceptionId("EX_SYSTEM_CANNOT_CREATE_NON_XA_DATASOURCE", "app.module.cannot.create.non.xa.datasource");

    public static final ExceptionId EX_SYSTEM_CANNOT_CREATE_XA_DATASOURCE
        = new ExceptionId("EX_SYSTEM_CANNOT_CREATE_XA_DATASOURCE", "app.module.cannot.create.xa.datasource");
    /**
     * Cannot parse date string. Incorrect date format.
     */
    public static final ExceptionId EX_SYSTEM_CANNOT_PARSE_LOCAL_TIMESTAMP
            = new ExceptionId("EX_SYSTEM_CANNOT_PARSE_LOCAL_TIMESTAMP", "app.system.cannot.parse.local.timestamp");
    /**
     * Undefined error occurs.
     */
    public static final ExceptionId EX_SYSTEM_CONNECTION_GET
            = new ExceptionId("EX_SYSTEM_CONNECTION_GET", "app.system.connection.get");
    /**
     * The pipeline is already closed.
     */
    public static final ExceptionId EX_PIPELINE_ALREADY_FINISHED
        = new ExceptionId("EX_PIPELINE_ALREADY_FINISHED", "app.pipeline.already.finished");
    /**
     * Attempt to add a non batched segment to a batched pipeline or vice versa.
     */
    public static final ExceptionId EX_PIPELINE_BATCHED_MISMATCH
        = new ExceptionId("EX_PIPELINE_BATCHED_MISMATCH", "app.pipeline.batched.not.batched");
    /**
     * The pipeline is not finished yet.
     */
    public static final ExceptionId EX_PIPELINE_IS_NOT_FINISHED
        = new ExceptionId("EX_PIPELINE_IS_NOT_FINISHED", "app.pipeline.is.not.finished");
    /**
     * Pipeline start type [{}] not found.
     */
    public static final ExceptionId EX_PIPELINE_START_TYPE_NOT_FOUND
        = new ExceptionId("EX_PIPELINE_START_TYPE_NOT_FOUND", "app.pipeline.start.type.not.found");
    /**
     * Pipeline not found by id [{}], subject [{}].
     */
    public static final ExceptionId EX_PIPELINE_NOT_FOUND_BY_ID_AND_SUBJECT
        = new ExceptionId("EX_PIPELINE_NOT_FOUND_BY_ID_AND_SUBJECT", "app.pipeline.not.found.by.id.and.subject");
    /**
     * Pipeline builder has empty input.
     */
    public static final ExceptionId EX_PIPELINE_BUILDER_EMPTY_INPUT
        = new ExceptionId("EX_PIPELINE_BUILDER_EMPTY_INPUT", "app.pipeline.builder.empty.input");
    /**
     * Pipeline builder has ambiguous input.
     */
    public static final ExceptionId EX_PIPELINE_BUILDER_AMBIGUOUS_INPUT
        = new ExceptionId("EX_PIPELINE_BUILDER_AMBIGUOUS_INPUT", "app.pipeline.builder.ambiguous.input");
    /**
     * Start segment not found by id [{}].
     */
    public static final ExceptionId EX_PIPELINE_START_SEGMENT_NOT_FOUND_BY_ID
        = new ExceptionId("EX_PIPELINE_START_SEGMENT_NOT_FOUND_BY_ID", "app.pipeline.start.segment.not.found.by.id");
    /**
     * Outcome segment not found by id [{}].
     */
    public static final ExceptionId EX_PIPELINE_OUTCOME_SEGMENT_NOT_FOUND_BY_ID
        = new ExceptionId("EX_PIPELINE_OUTCOME_SEGMENT_NOT_FOUND_BY_ID", "app.pipeline.outcome.segment.not.found.by.id");
    /**
     * Segment not found by id [{}].
     */
    public static final ExceptionId EX_PIPELINE_SEGMENT_NOT_FOUND_BY_ID
        = new ExceptionId("EX_PIPELINE_SEGMENT_NOT_FOUND_BY_ID", "app.pipeline.segment.not.found.by.id");
    /**
     * Outcome not found by id [{}] in [{}].
     */
    public static final ExceptionId EX_PIPELINE_OUTCOME_NOT_FOUND_BY_ID
        = new ExceptionId("EX_PIPELINE_OUTCOME_NOT_FOUND_BY_ID", "app.pipeline.outcome.not.found.by.id");
    /**
     * Segment found, but is of different type [{}].
     */
    public static final ExceptionId EX_PIPELINE_SEGMENT_OF_WRONG_TYPE
        = new ExceptionId("EX_PIPELINE_SEGMENT_OF_WRONG_TYPE", "app.pipeline.segment.of.wrong.type");
    /**
     * Invalid number of segments. A pipeline must contain at least 2 points of type 'start' and 'finish'.
     */
    public static final ExceptionId EX_PIPELINE_INVALID_NUMBER_OF_SEGMENTS
        = new ExceptionId("EX_PIPELINE_INVALID_NUMBER_OF_SEGMENTS", "app.pipeline.invalid.number.of.segments");
    /**
     * Invalid pipeline layout. A pipeline must start with a point of type 'start' and end with a point of type 'finish'.
     */
    public static final ExceptionId EX_PIPELINE_HAS_NO_START_OR_FINISH_OR_BOTH
        = new ExceptionId("EX_PIPELINE_HAS_NO_START_OR_FINISH_OR_BOTH", "app.pipeline.has.no.start.or.finish.or.both");
    /**
     * Attempt to add a unrelated elements to outcomes map.
     */
    public static final ExceptionId EX_PIPELINE_OUTCOMES_NOT_FULLY_COVERED
        = new ExceptionId("EX_PIPELINE_OUTCOMES_NOT_FULLY_COVERED", "app.pipeline.outcomes.not.fully.covered");
    /**
     * Attempt to add a unrelated elements to outcomes map.
     */
    public static final ExceptionId EX_PIPELINE_NOT_AN_OUTCOMES_SEGMENT
        = new ExceptionId("EX_PIPELINE_NOT_AN_OUTCOMES_SEGMENT", "app.pipeline.not.an.outcomes.segment");
    /**
     * Outcome not found by name [{}].
     */
    public static final ExceptionId EX_PIPELINE_OUTCOME_NOT_FOUND_BY_NAME
        = new ExceptionId("EX_PIPELINE_OUTCOME_NOT_FOUND_BY_NAME", "app.pipeline.outcome.not.found.by.name");
    /**
     * Finish/Splitter/Selector output type does not match Pipeline's output type.
     */
    public static final ExceptionId EX_PIPELINE_OUTPUT_TYPES_DONT_MATCH
        = new ExceptionId("EX_PIPELINE_OUTPUT_TYPES_DONT_MATCH", "app.pipeline.output.types.dont.match");
    /**
     * Pipeline execution failed.
     */
    public static final ExceptionId EX_PIPELINE_EXECUTION_FAILED
        = new ExceptionId("EX_PIPELINE_EXECUTION_FAILED", "app.pipeline.execution.failed");
    /**
     * Pipeline execution failed.
     */
    public static final ExceptionId EX_PIPELINE_NO_START_AND_TYPES_FOR_ANONYMOUS
        = new ExceptionId("EX_PIPELINE_NO_START_AND_TYPES_FOR_ANONYMOUS", "app.pipeline.no.start.and.types.for.anonymous");
    /**
     * Pipeline execution failed.
     */
    public static final ExceptionId EX_PIPELINE_NO_FINISH_AND_TYPES_FOR_ANONYMOUS
        = new ExceptionId("EX_PIPELINE_NO_FINISH_AND_TYPES_FOR_ANONYMOUS", "app.pipeline.no.finish.and.types.for.anonymous");
    /**
     * Pipeline execution failed.
     */
    public static final ExceptionId EX_PIPELINE_START_SEGMENT_NOT_SUPPORTED
        = new ExceptionId("EX_PIPELINE_START_SEGMENT_NOT_SUPPORTED", "app.pipeline.start.segment.not.supported");
    /**
     * Outcome and the pipeline have incompatible input types.
     */
    public static final ExceptionId EX_PIPELINE_OUTCOME_INPUT_TYPE_MISMATCH
        = new ExceptionId("EX_PIPELINE_OUTCOME_INPUT_TYPE_MISMATCH", "app.pipeline.outcome.input.type.mismatch");
    /**
     * Outcome and the pipeline have incompatible input types.
     */
    public static final ExceptionId EX_PIPELINE_OUTCOME_OUTPUT_TYPE_MISMATCH
        = new ExceptionId("EX_PIPELINE_OUTCOME_OUTPUT_TYPE_MISMATCH", "app.pipeline.outcome.output.type.mismatch");

    public static final ExceptionId EX_EVENT_NO_VALID_ID
        = new ExceptionId("EX_EVENT_NO_VALID_ID", "app.event.invalid.id");

    public static final ExceptionId EX_EVENT_ALREADY_WAITING
        = new ExceptionId("EX_EVENT_ALREADY_WAITING", "app.event.is.waiting.yet");

    public static final ExceptionId EX_SYSTEM_SECURITY_UTILS_CLASS
        = new ExceptionId("EX_SYSTEM_SECURITY_UTILS_CLASS", SystemModule.MODULE_ID + ".security.utils.class");
    /**
     * Migrate schema failed.
     */
    public static final ExceptionId EX_SYSTEM_MIGRATE_SCHEMA_FAILED =
            new ExceptionId("EX_SYSTEM_MIGRATE_SCHEMA_FAILED", "app.system.migrate.schema.failed");
    /**
     * Domain initialization failed.
     */
    public static final ExceptionId EX_SYSTEM_DOMAIN_INITIALIZATION_FAILED
            = new ExceptionId("EX_SYSTEM_DOMAIN_INITIALIZATION_FAILED", "app.system.domain.initialization.failed");
    /**
     * Domain startup failed.
     */
    public static final ExceptionId EX_SYSTEM_DOMAIN_STARTUP_FAILED
            = new ExceptionId("EX_SYSTEM_DOMAIN_STARTUP_FAILED", "app.system.domain.startup.failed");
    /**
     * Messaging startup failed.
     */
    public static final ExceptionId EX_SYSTEM_MESSAGING_STARTUP_FAILED
            = new ExceptionId("EX_SYSTEM_MESSAGING_STARTUP_FAILED", "app.system.messaging.startup.failed");
    /**
     * Cannot read jar content.
     */
    public static final ExceptionId EX_SYSTEM_CANNOT_READ_JAR_CONTENT
            = new ExceptionId("EX_SYSTEM_CANNOT_READ_JAR_CONTENT", "app.system.cannot.read.jar.content");

    /**
     * Constructor.
     */
    private SystemExceptionIds() {
        super();
    }
}
