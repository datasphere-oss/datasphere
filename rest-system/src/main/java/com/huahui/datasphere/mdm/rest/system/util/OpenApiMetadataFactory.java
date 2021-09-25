package com.huahui.datasphere.mdm.rest.system.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;

import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxrs.model.ApplicationInfo;
import org.apache.cxf.jaxrs.openapi.OpenApiCustomizer;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;

import com.huahui.datasphere.mdm.rest.system.service.openapi.VersionedOpenApiReader;


public final class OpenApiMetadataFactory {
    /**
     * Swagger custom auth schema name.
     */
    public static final String AUTHENTICATION_SCHEMA_NAME = "datasphere-token";
    /**
     * Schema definition.
     */
    public static final SecurityScheme AUTHENTICATION_SCHEMA = new SecurityScheme()
        .type(Type.APIKEY)
        .in(In.HEADER)
        .name(HttpHeaders.AUTHORIZATION);

    /**
     * Constructor.
     */
    private OpenApiMetadataFactory() {
        super();
    }
    /**
     * Datasphere OpenAPI customizer.
     * @author theseusyang on Apr 14, 2020
     */
    private static class DatasphereOpenApiCustomizer extends OpenApiCustomizer {

        public DatasphereOpenApiCustomizer(Application application, Bus bus) {
            super();
            setApplicationInfo(new ApplicationInfo(application, bus));
            setDynamicBasePath(true);
        }

        @Override
        public OpenAPIConfiguration customize(final OpenAPIConfiguration configuration) {
            OpenAPIConfiguration result = super.customize(configuration);
            if (result instanceof SwaggerConfiguration) {
                SwaggerConfiguration swaggerConfiguration = (SwaggerConfiguration) result;
                swaggerConfiguration.setReaderClass(VersionedOpenApiReader.class.getName());
            }
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void customize(OpenAPI oas) {
            oas.setSecurity(List.of(new SecurityRequirement().addList(AUTHENTICATION_SCHEMA_NAME)));
            super.customize(oas);
        }
    }

    public static OpenApiFeature openApiFeature(String title, String description, Application application, Bus bus, String... packages) {

        final OpenApiFeature openApiFeature = new OpenApiFeature();

        // TODO Take all this from properties.
        openApiFeature.setContactName("Datasphere team");
        openApiFeature.setContactEmail("info@unidata-platform.ru");
        openApiFeature.setContactUrl("https://unidata-platform.ru/contacts/");
        openApiFeature.setLicense("Commercial license");
        openApiFeature.setLicenseUrl("https://unidata-platform.ru/contacts/");
        openApiFeature.setVersion("6.0.0");
        openApiFeature.setTitle(title);
        openApiFeature.setDescription(description);

        // Real stuff
        openApiFeature.setScan(false);
        openApiFeature.setUseContextBasedConfig(true);
        openApiFeature.setCustomizer(new DatasphereOpenApiCustomizer(application, bus));
        openApiFeature.setResourcePackages(Set.of(packages));
        openApiFeature.setSecurityDefinitions(Map.of(AUTHENTICATION_SCHEMA_NAME, AUTHENTICATION_SCHEMA));
        return openApiFeature;
    }
}
