package org.datasphere.mdm.core.util;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.datasphere.mdm.system.util.AbstractJaxbUtils;

import com.datasphere.mdm.security.Security;

/**
 * @author Mikhail Mikhailov on Oct 4, 2019
 * JAXB stuff, related to meta model.
 * TODO: Get rid of JAXB.
 */
public final class SecurityJaxbUtils extends AbstractJaxbUtils {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityJaxbUtils.class);

    /**
     * XSD dateTime date format.
     */
    public static final String XSD_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";

    private static final com.datasphere.mdm.security.ObjectFactory SECURITY_FACTORY =
            new com.datasphere.mdm.security.ObjectFactory();

    public static com.datasphere.mdm.security.ObjectFactory getSecurityFactory() {
        return SECURITY_FACTORY;
    }

    private static final String SECURITY_ROOT_PACKAGE = "com.unidata.mdm.security";

    /**
     * Constructor.
     */
    private SecurityJaxbUtils() {
        super();
    }


    public static Security createSecurityFromInputStream(InputStream is) throws IOException {
        return unmarshalObject(Security.class, is);
    }

    public static String marshalSecurity(Security security) {
        return marshalObject(security);
    }
}
