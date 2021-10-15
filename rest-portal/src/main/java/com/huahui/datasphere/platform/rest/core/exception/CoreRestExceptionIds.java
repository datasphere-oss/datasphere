/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.exception;

import org.unidata.mdm.system.exception.ExceptionId;

/**
 * @author Alexander Malyshev
 */
public final class CoreRestExceptionIds {
    private CoreRestExceptionIds() { }

    public static final ExceptionId EX_SYSTEM_DATABASE_CANNOT_CONNECT =
            new ExceptionId("EX_SYSTEM_DATABASE_CANNOT_CONNECT", "app.system.database.cannotConnect");

    public static final ExceptionId EX_CORE_AUDIT_EXPORT_UNABLE_TO_EXPORT_XLS =
            new ExceptionId("EX_CORE_AUDIT_EXPORT_UNABLE_TO_EXPORT_XLS", "app.core.audit.export.unable.to.export.xls");

    public static final ExceptionId EX_CORE_LIBRARIES_UNABLE_TO_EXPORT_LIBRARY =
            new ExceptionId("EX_CORE_LIBRARIES_UNABLE_TO_EXPORT_LIBRARY", "app.core.libraries.unable.to.export.library");

    public static final ExceptionId EX_CORE_LIBRARIES_FAILED_TO_SEND_LIBRARY =
            new ExceptionId("EX_CORE_LIBRARIES_FAILED_TO_SEND_LIBRARY", "app.core.libraries.failed.to.send.library");
}
