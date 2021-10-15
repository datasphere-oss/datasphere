package org.datasphere.mdm.core.type.formless;

import java.util.Objects;

import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.system.type.variables.Variables;

/**
 * @author Mikhail Mikhailov on Sep 14, 2020
 * Container for a data record and variables.
 */
public class DataBundle {
    /**
     * The data record.
     */
    private DataRecord record;
    /**
     * The variables.
     */
    private Variables variables;
    /**
     * Constructor.
     */
    public DataBundle() {
        super();
    }
    /**
     * @return the record
     */
    public DataRecord getRecord() {
        return record;
    }
    /**
     * @param record the record to set
     */
    public void setRecord(DataRecord record) {
        this.record = record;
    }
    /**
     * @return the variables
     */
    public Variables getVariables() {
        return variables;
    }
    /**
     * @param variables the variables to set
     */
    public void setVariables(Variables variables) {
        this.variables = variables;
    }
    /**
     * Sets record and returns self.
     * @param record the record to set
     * @return self
     */
    public DataBundle withRecord(DataRecord record) {
        setRecord(record);
        return this;
    }
    /**
     * Sets variables and returns self.
     * @param variables the variables to set
     * @return self
     */
    public DataBundle withVariables(Variables variables) {
        setVariables(variables);
        return this;
    }
    /**
     * Returns true, if some variables have been set.
     * @return true, if variables set, false otherwise
     */
    public boolean hasVariables() {
        return Objects.nonNull(variables) && !variables.isEmpty();
    }
    /**
     * Returns true, if record has been set.
     * @return true, if record set, false otherwise
     */
    public boolean hasRecord() {
        return Objects.nonNull(record);
    }
}
