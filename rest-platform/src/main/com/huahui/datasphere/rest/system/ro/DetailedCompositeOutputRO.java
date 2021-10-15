package com.huahui.datasphere.rest.system.ro;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import org.unidata.mdm.system.type.rendering.OutputRenderingAction;

/**
 * The base output composite object.
 * Contains fields for dynamic processing by system modules
 *
 * @author Alexandr Serov
 * @since 28.09.2020
 **/
public abstract class DetailedCompositeOutputRO extends DetailedOutputRO implements RestOutputSink {

    @JsonIgnore
    public abstract OutputRenderingAction getOutputRenderingAction();

    /**
     * Dynamic object fields
     */
    private final Map<String, JsonNode> payload = new HashMap<>();

    /**
     * Adds a part to output.
     *
     * @param name the property name
     * @param value the value
     */
    @Override
    @JsonAnySetter
    public void setAny(String name, JsonNode value) {
        payload.put(name, value);
    }

    /**
     * @return output parts
     */
    @JsonAnyGetter
    public Map<String, JsonNode> getAny() {
        return payload;
    }
}
