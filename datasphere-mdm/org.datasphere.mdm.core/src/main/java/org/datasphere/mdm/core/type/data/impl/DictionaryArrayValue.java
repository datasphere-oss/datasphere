package org.datasphere.mdm.core.type.data.impl;

public class DictionaryArrayValue extends StringArrayValue {

    public DictionaryArrayValue() {
        super();
    }
    /**
     * Constructor.
     * @param value
     * @param displayValue
     */
    public DictionaryArrayValue(String value, String displayValue) {
        super(value, displayValue);
    }
    /**
     * Constructor.
     * @param value the value
     * @param displayValue the display value
     * @param linkEtalonId link etalon id
     */
    public DictionaryArrayValue(String value, String displayValue, String linkEtalonId) {
        super(value, displayValue, linkEtalonId);
    }
    /**
     * Constructor.
     * @param value
     */
    public DictionaryArrayValue(String value) {
        super(value);
    }
}
