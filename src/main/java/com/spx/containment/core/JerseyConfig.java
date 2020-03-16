package com.spx.containment.core;

import com.spx.containment.core.api.mapping.ContainerJsonProvider;
import com.spx.containment.core.api.monitor.ContainerEventMonitor;
import com.spx.containment.core.security.filters.OpenIdConnectAuthenticationFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
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
    register(ContainerEventMonitor.class);
    register(JacksonFeature.class);
    register(OpenIdConnectAuthenticationFilter.class);
    //   register(CORSFilter.class);
    packages(true, getClass().getPackage()
        .getName(), ApiListingResource.class.getPackage()
        .getName());
  }

  static class JacksonFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
      context.register(new ContainerJsonProvider(), MessageBodyReader.class,
          MessageBodyWriter.class);
      return true;
    }
  }

}



