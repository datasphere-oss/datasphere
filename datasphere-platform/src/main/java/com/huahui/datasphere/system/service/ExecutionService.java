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

package com.huahui.datasphere.system.service;

import com.huahui.datasphere.system.type.pipeline.Pipeline;
import com.huahui.datasphere.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.system.type.pipeline.Start;

/**
 * @author Mikhail Mikhailov on Oct 1, 2019
 * Pipeline execution service.
 */
public interface ExecutionService {
    /**
     * Calls {@link Start#subject(PipelineInput)}
     * to select a pre-configured pipeline for the context and than
     * calls the variant below.
     *
     * @param <C> the type of the request
     * @param <R> the type of the result
     * @param ctx the request context
     * @return result
     */
    <C extends PipelineInput, R extends PipelineOutput> R execute(C ctx);
    /**
     * Calls the pipeline 'p' for the supplied context 'c'.
     *
     * @param <C> the type of the request
     * @param <R> the type of the result
     * @param p a ready to execute pipeline instance
     * @param ctx the context
     * @return result
     */
    <C extends PipelineInput, R extends PipelineOutput> R execute(Pipeline p, C ctx);
}
