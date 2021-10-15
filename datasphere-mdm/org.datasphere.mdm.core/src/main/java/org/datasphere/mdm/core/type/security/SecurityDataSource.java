package org.datasphere.mdm.core.type.security;

import javax.annotation.Nullable;

import org.datasphere.mdm.core.type.security.provider.AuthenticationProvider;
import org.datasphere.mdm.core.type.security.provider.AuthorizationProvider;
import org.datasphere.mdm.core.type.security.provider.InterceptionProvider;
import org.datasphere.mdm.core.type.security.provider.ProfileProvider;

/**
 * The security data source interface.
 * @author Mikhail Mikhailov on Jun 23, 2020
 */
public interface SecurityDataSource {
    /**
     * Name of security provider.
     *
     * @return security provider name.
     */
    String getName();
    /**
     * Description of the sec. data source.
     *
     * @return description
     */
    String getDescription();
    /**
     * Gets interception provider,
     * that allows to implement some complex authentication schemas, such as SAML or RADIUS.
     *
     * @return interception provider or null
     */
    @Nullable
    InterceptionProvider getInterceptionProvider();
    /**
     * Authentication provider to check username/password pair and/or fill other user specific information.
     *
     * @return authentication provider or null.
     */
    @Nullable
    AuthenticationProvider getAuthenticationProvider();
    /**
     * Authorization provider to get rights for secured resources.
     *
     * @return authorization provider or null.
     */
    @Nullable
    AuthorizationProvider getAuthorizationProvider();
    /**
     * Profile provider to fetch user specific information.
     *
     * @return profile provider or null.
     */
    @Nullable
    ProfileProvider getProfileProvider();
}
