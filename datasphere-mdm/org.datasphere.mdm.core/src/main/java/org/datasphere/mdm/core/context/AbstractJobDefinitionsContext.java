package org.datasphere.mdm.core.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mikhail Mikhailov on Jul 5, 2021
 */
public abstract class AbstractJobDefinitionsContext {
    /**
     * The definition id.
     */
    protected final Set<Long> jobDifinitionIds;
    /**
     * Constructor.
     */
    protected AbstractJobDefinitionsContext(AbstractJobDefinitionsContextBuilder<?> b) {
        super();
        jobDifinitionIds = Objects.isNull(b.jobDefinitionIds) ? Collections.emptySet() : b.jobDefinitionIds;
    }
    /**
     * @return the jobDifinitionId
     */
    public Collection<Long> getJobDifinitionIds() {
        return jobDifinitionIds;
    }
    /**
     * @author Mikhail Mikhailov on Jul 5, 2021
     * Definition ID based ops builder base.
     */
    public abstract static class AbstractJobDefinitionsContextBuilder<X extends AbstractJobDefinitionsContextBuilder<X>> {
        /**
         * The definition id.
         */
        protected Set<Long> jobDefinitionIds;
        /**
         * Constructor.
         */
        protected AbstractJobDefinitionsContextBuilder() {
            super();
        }
        /**
         * Sets the definition id.
         * @param id the definition id
         * @return self
         */
        public X jobDefinitionId(Long... id) {
            if (ArrayUtils.isNotEmpty(id)) {
                return jobDefinitionIds(Arrays.asList(id));
            }
            return self();
        }
        /**
         * Sets the definition id.
         * @param id the definition id
         * @return self
         */
        public X jobDefinitionIds(Collection<Long> ids) {

            if (CollectionUtils.isNotEmpty(ids)) {

                for (Long id : ids) {

                    if (Objects.isNull(id)) {
                        continue;
                    }

                    if (Objects.isNull(jobDefinitionIds)) {
                        jobDefinitionIds = new HashSet<>();
                    }

                    jobDefinitionIds.add(id);
                }
            }
            return self();
        }
        /**
         * Define builder method.
         * @return object
         */
        public abstract AbstractJobDefinitionsContext build();
        /*
         * Cast self to X.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
    }
}
