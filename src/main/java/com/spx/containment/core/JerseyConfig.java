package com.spx.containment.core;

import com.spx.containment.core.api.ContainerResource;
import com.spx.containment.core.api.monitor.ContainerEventMonitor;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        BeanConfig swaggerConfig = new BeanConfig();
        swaggerConfig.setBasePath("/api");
        swaggerConfig.setResourcePackage("com.spx.containment.core.api");
        SwaggerConfigLocator.getInstance()
            .putConfig(SwaggerContextService.CONFIG_ID_DEFAULT, swaggerConfig);
        register(LoggingFeature.class);
        register(ContainerResource.class);
        register(ContainerEventMonitor.class);
//        register(AuthenticationFilter.class);
        // register(CORSFilter.class);
        packages(true, getClass().getPackage()
            .getName(), ApiListingResource.class.getPackage()
            .getName());
    }


}



