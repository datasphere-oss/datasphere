package org.unidata.mdm.rest.system.exception;

import org.unidata.mdm.system.exception.ExceptionId;

/**
 * Common system errors
 *
 * @author Alexandr Serov
 * @since 09.12.2020
 **/
public class SystemRestExceptionIds {

    public static final ExceptionId EX_NOT_ALLOW = new ExceptionId("ACCESS_DENIED", "app.rest.notAllow");
}
