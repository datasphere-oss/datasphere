package com.huahui.datasphere.mdm.rest.system.ro;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.huahui.datasphere.mdm.system.type.rendering.InputRenderingAction;

/**
 * The base input composite object.
 * Contains fields for dynamic processing by system modules
 *
 * @author theseusyang
 * @since 28.09.2020
 **/
public abstract class CompositeInputRO implements RestInputSource {
    /**
     * Dynamic object fields
     */
    private final Map<String, JsonNode> payload = new HashMap<>();

    @JsonIgnore
    public abstract InputRenderingAction getInputRenderingAction();

    /**
     * Adds a part to output.
     *
     * @param name the property name
     * @param value the value
     */
//        @Override
    @JsonAnySetter
    public void setAny(String name, JsonNode value) {
        payload.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, JsonNode> getAny() {
        return payload;
    }
}
