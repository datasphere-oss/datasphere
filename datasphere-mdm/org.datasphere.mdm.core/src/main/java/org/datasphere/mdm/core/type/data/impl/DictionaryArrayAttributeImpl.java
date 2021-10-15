package org.datasphere.mdm.core.type.data.impl;

import java.util.List;

import org.datasphere.mdm.core.type.data.ArrayValue;

public class DictionaryArrayAttributeImpl extends AbstractArrayAttribute<String> {
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected DictionaryArrayAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name
     */
    public DictionaryArrayAttributeImpl(String name) {
        super(name);
    }

    /**
     * Constructor.
     * @param name
     * @param value
     */
    public DictionaryArrayAttributeImpl(String name, List<ArrayValue<String>> value) {
        super(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayDataType getDataType() {
        return ArrayDataType.DICTIONARY;
    }
}

