package com.huahui.datasphere.mdm.system.service.impl.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author theseusyang on May 19, 2020
 */
public class ModularAnnotationConfigWebApplicationContext extends AnnotationConfigWebApplicationContext {
    /**
     * Modules stack.
     */
    private final List<AbstractApplicationContext> stack = new ArrayList<>(32);
    /**
     * Was already refreshed.
     */
    private boolean ready;
    /**
     * Constructor.
     */
    public ModularAnnotationConfigWebApplicationContext() {
        super();
    }
    /**
     * Gets the first context in the chain.
     * @return context
     */
    public AbstractApplicationContext getFirst() {
        return stack.get(0);
    }
    /**
     * Gets the last context in the chain.
     * @return context
     */
    public AbstractApplicationContext getLast() {
        return stack.get(stack.size() - 1);
    }
    /**
     * Adds a child.
     * @param context the context
     */
    public void addChild(AbstractApplicationContext context) {
        stack.add(context);
    }
    /**
     * Gets a child by id.
     * @param id the id
     * @return child or null
     */
    @Nullable
    public AbstractApplicationContext getChild(String id) {
        return stack.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    /**
     * All children as a collection.
     * @return children
     */
    public Collection<AbstractApplicationContext> getChildren() {
        return stack;
    }
    /**
     * Gets the number of contexts currently running.
     * @return number of contexts
     */
    public int getSize() {
        return stack.size();
    }
    /**
     * All ids as a collection.
     * @return children ids
     */
    public Collection<String> getIds() {
        return stack.stream()
                .map(AbstractApplicationContext::getId)
                .collect(Collectors.toList());
    }
    /**
     * @return the ready
     */
    public boolean isReady() {
        return ready;
    }
    /**
     * @param ready the ready to set
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
