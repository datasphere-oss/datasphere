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

package com.huahui.datasphere.system.type.pipeline.fragment;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import com.huahui.datasphere.system.context.InputContainer;
import com.huahui.datasphere.system.context.InputFragmentHolder;
/**
 * Gets input fragments.
 * @author Mikhail Mikhailov on Dec 11, 2019
 */
public interface InputFragmentContainer extends InputContainer {
    /**
     * Gets a fragment from this composite.
     * @param r the fragment DTO
     */
    <F extends InputFragment<F>> F fragment(FragmentId<F> f);
    /**
     * Gets multiple fragments of the same type from this composite.
     * @param r fragments
     */
    <F extends InputFragment<F>> Collection<F> fragments(FragmentId<F> f);
    /**
     * Gets all fragments carried by this container.
     * @return collection
     */
    default Collection<InputFragmentHolder> fragments() {
        return Collections.emptyList();
    }
    /**
     * Executes a worker on fragments tree recursively.
     * @param worker the worker to execute
     */
    default void recursive(Consumer<InputFragment<?>> worker) {

        for (InputFragmentHolder ifh : fragments()) {

            ifh.stream().forEach(f -> {

                worker.accept(f);
                if (f instanceof InputFragmentContainer) {
                    ((InputFragmentContainer) f).recursive(worker);
                }
            });
        }
    }
}
