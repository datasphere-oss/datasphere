

package com.huahui.datasphere.system.util;

import javax.servlet.http.HttpServletRequest;

public final class IpUtils {

    private IpUtils() { }

    private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";

    public static String clientIp(final HttpServletRequest hsr) {

        if (hsr == null) {
            return null;
        }

        return hsr.getHeader(X_FORWARDED_FOR_HEADER) != null ?
                hsr.getHeader(X_FORWARDED_FOR_HEADER).split(",")[0] :
                hsr.getRemoteAddr();
    }

    public static String serverIp(final HttpServletRequest hsr) {
        return hsr.getLocalAddr();
    }
}
