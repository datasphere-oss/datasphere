package com.huahui.datasphere.mdm.rest.system.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;

import com.huahui.datasphere.mdm.rest.system.service.ReceiveInInterceptor;

/**
 * @author theseusyang on Apr 24, 2020
 */
public class ReceiveInInterceptorImpl extends AbstractPhaseInterceptor<Message> implements ReceiveInInterceptor {
    /**
     * Sub-interceptor chain.
     */
    private final List<PhaseInterceptor<?>> chain = new ArrayList<>();
    /**
     * Constructor.
     */
    public ReceiveInInterceptorImpl() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) {
        // NOP. The only goal of this class is the entry point for interceptor our local inteceptor chain.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof PhaseInterceptor) {
            PhaseInterceptor<?> pi = (PhaseInterceptor<?>) bean;
            if (Phase.RECEIVE.equals(pi.getPhase())) {
                chain.add(pi);
            }
        }

        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
        return chain;
    }
}
