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

package com.huahui.datasphere.mdm.system.type.runtime;

import java.lang.StackWalker.StackFrame;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * @author theseusyang
 * Measurement point.
 */
public class MeasurementPoint {
    /**
     * Collection of steps.
     */
    private final Deque<Pair<String, Split>> steps = new ArrayDeque<>();
    /**
     * Context.
     */
    private final MeasurementContext ctx;
    /**
     * The sole option we use for technical reasons.
     */
    private static final Set<StackWalker.Option> OPTIONS = Set.of(StackWalker.Option.SHOW_HIDDEN_FRAMES);
    /**
     * Thread local context.
     */
    private static final ThreadLocal<MeasurementPoint> MEASUREMENT_CONTEXT
        = new ThreadLocal<>();
    /**
     * Measurement generally (enabled or not).
     */
    private static volatile boolean enabled;
    /**
     * Constructor.
     */
    private MeasurementPoint(MeasurementContext ctx) {
        super();
        this.ctx = ctx;
    }

    /**
     * Starts new split.
     * Takes custom label instead of calling method ctx.
     * @param category a possibly given label category
     * @param label the label to take
     */
    public static void start(@Nullable String category, @Nonnull String label) {

        MeasurementPoint point = MEASUREMENT_CONTEXT.get();
        if (point == null) {
            return;
        }

        final String path;
        final Pair<String, Split> step = point.steps.peek();
        if (Objects.isNull(step)) {
            path = point.ctx.getName();
        } else {
            path = step.getLeft()
                    + Manager.HIERARCHY_DELIMITER
                    + (Objects.nonNull(category) ? ("[" + category + "]") : "[UNSPECIFIED]")
                    + "->"
                    + ("[" + label + "]");
        }

        Split split = SimonManager.getStopwatch(path).start();
        point.steps.push(Pair.of(path, split));
    }

    /**
     * Starts new split.
     */
    public static void start() {

        MeasurementPoint point = MEASUREMENT_CONTEXT.get();
        if (point == null) {
            return;
        } else if (!enabled) {
            MEASUREMENT_CONTEXT.remove();
            return;
        }

        // 4 is a sort of magic number.
        // We need stack depth 2 - the very last method called (except this method) + 2 frames, which are reserved by the platform.
        // We also have to supply SHOW_HIDDEN_FRAMES for this to work.
        final Pair<String, Split> step = point.steps.peek();
        final StackFrame frame = Objects.isNull(step)
                ? null
                : StackWalker
                    .getInstance(OPTIONS, 4)
                    .walk(frames -> frames.limit(2).skip(1).findFirst().orElse(null));

        String path = Objects.isNull(frame)
                ? point.ctx.getName()
                : step.getLeft()
                    + Manager.HIERARCHY_DELIMITER
                    + StringUtils.substringAfterLast(frame.getClassName(), ".")
                    + "->"
                    + frame.getMethodName();

        Split split = SimonManager.getStopwatch(path).start();
        point.steps.push(Pair.of(path, split));
    }

    /**
     * Stops current split.
     */
    public static void stop() {
        MeasurementPoint point = MEASUREMENT_CONTEXT.get();
        if (point == null) {
            return;
        }

        Pair<String, Split> step = point.steps.pop();
        if (step != null) {
            step.getRight().stop();
        }
    }

    /**
     * Sets the context and initializes measurement point.
     * @param ctx the the current measurement context
     */
    public static void init(MeasurementContext ctx) {

        if (!enabled) {
            MEASUREMENT_CONTEXT.remove();
            return;
        }

        MEASUREMENT_CONTEXT.set(new MeasurementPoint(ctx));
    }

    /**
     * Tells, whether this thread has been initialized with a measurement point.
     * @return true, if so, false otherwise
     */
    public static boolean initialized() {
        return MEASUREMENT_CONTEXT.get() != null;
    }

    /**
     * @return the enabled
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public static void setEnabled(boolean enabled) {
        MeasurementPoint.enabled = enabled;
    }
}
