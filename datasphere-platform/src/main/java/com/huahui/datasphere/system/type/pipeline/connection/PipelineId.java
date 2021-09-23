package com.huahui.datasphere.system.type.pipeline.connection;

import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.system.type.pipeline.Pipeline;

/**
 * @author Mikhail Mikhailov on Dec 29, 2020
 * The pipeline "id".
 */
public class PipelineId {
    /**
     * Start segment id.
     */
    private final String startId;
    /**
     * Subject name/id.
     */
    private final String subjectId;
    /**
     * Constructor.
     */
    public PipelineId(String startId, String subjectId) {
        super();

        Objects.requireNonNull(startId, "startId must not be null.");

        this.startId = startId;
        this.subjectId = subjectId;
    }
    /**
     * @return the startId
     */
    public String getStartId() {
        return startId;
    }
    /**
     * @return the subjectId
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * Returns true, if subject id has been set.
     * @return true, if subject id has been set
     */
    public boolean hasSubjectId() {
        return Objects.nonNull(subjectId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(startId, subjectId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        PipelineId other = (PipelineId) obj;
        return Objects.equals(this.startId, other.startId)
            && Objects.equals(this.subjectId, other.subjectId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.nonNull(subjectId)
                ? String.join("@", subjectId, startId)
                : startId;
    }
    /**
     * Returns serializable id of the pipeline or null.
     * @param p the pipeline
     * @return serializable id of the pipeline or null if the pipeline is null
     */
    @Nullable
    public static String toSerializable(Pipeline p) {

        if (Objects.isNull(p)) {
            return null;
        }

        return p.getPipelineId().toString();
    }
    /**
     * Creates pipeline id from serialized string.
     * @param id the serialized string
     * @return pipeline id or null
     */
    @Nullable
    public static PipelineId fromSerializable(String id) {

        final String[] parts = StringUtils.split(id, '@');
        if (ArrayUtils.isEmpty(parts)) {
            return null;
        }

        return parts.length == 1
                ? new PipelineId(parts[0], null)
                : new PipelineId(parts[1], parts[0]);
    }
}
