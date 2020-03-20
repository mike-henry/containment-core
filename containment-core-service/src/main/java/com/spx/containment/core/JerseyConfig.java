package com.spx.containment.core;

import com.spx.containment.core.api.mapping.ContainerJsonProvider;
import com.spx.containment.core.api.monitor.ContainerEventMonitor;
import com.spx.containment.core.security.filters.ApplicationRequestFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath(JerseyConfig.BASE_PATH)
public class JerseyConfig extends ResourceConfig {

  static final String BASE_PATH = "/api";
  private static final String BASE_PACKAGE = "com.spx.containment.core.api";

  @Autowired
  public JerseyConfig() {

    BeanConfig swaggerConfig = new BeanConfig();
    swaggerConfig.setBasePath(BASE_PATH);
    swaggerConfig.setResourcePackage(BASE_PACKAGE);
    SwaggerConfigLocator.getInstance()
        .putConfig(SwaggerContextService.CONFIG_ID_DEFAULT, swaggerConfig);
    register(ContainerEventMonitor.class);
    register(JacksonFeature.class);

    findTypes(ApplicationRequestFilter.class).forEach(type -> register(type));
    packages(true, getClass().getPackage()
        .getName(), ApiListingResource.class.getPackage()
        .getName());
  }

  private <T> Set<Class<? extends T>> findTypes(Class<T> superType) {
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
        ClasspathHelper.forPackage(getClass().getPackage()
            .getName())));
    return reflections.getSubTypesOf(superType);
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



