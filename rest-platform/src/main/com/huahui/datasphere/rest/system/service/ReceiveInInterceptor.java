package com.huahui.datasphere.rest.system.service;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Inteceptor, tageted to {@linkplain Phase#RECEIVE} phase.
 * Includes phase interceptors from
 * @author Mikhail Mikhailov on Apr 24, 2020
 */
public interface ReceiveInInterceptor extends PhaseInterceptor<Message>, BeanPostProcessor {}
