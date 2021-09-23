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
 * @author Mikhail Mikhailov
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
