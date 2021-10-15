package org.datasphere.mdm.core.type.data.impl;

public class DictionarySimpleAttributeImpl extends StringSimpleAttributeImpl {


    public DictionarySimpleAttributeImpl(String name) {
        super(name);
    }

    public DictionarySimpleAttributeImpl(String name, String value) {
        super(name, value);
    }

    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.DICTIONARY;
    }

}

