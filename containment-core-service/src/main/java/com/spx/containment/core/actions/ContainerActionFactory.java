package com.spx.containment.core.actions;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ContainerServices;
import com.spx.containment.core.services.ModelToViewAdaptor;
import java.util.Set;
import javax.inject.Inject;
import org.springframework.stereotype.Component;


@Component
public class ContainerActionFactory {

  private final ModelToViewAdaptor mapper;
  private final ContainerServices containerServices;
  private final Set<ContainerEventListener> containerEventListeners;

  @Inject
  public ContainerActionFactory(ModelToViewAdaptor modelToViewMapper,
      ContainerServices containerServices, Set<ContainerEventListener> containerEventListeners) {
    mapper = modelToViewMapper;
    this.containerServices = containerServices;
    this.containerEventListeners = containerEventListeners;
  }

  public CreateContainerTreeAction buildCreateContainerTreeAction(ContainerView[] views) {
    return new CreateContainerTreeAction(mapper, containerServices, views, containerEventListeners);
  }

  public GetContainerAction buildGetContainerAction(String rootName) {
    return new GetContainerAction(mapper, rootName, containerEventListeners);
  }

  public GetContainerTreeAction buildGetContainerTreeAction(String rootName) {
    return new GetContainerTreeAction(mapper, rootName, containerEventListeners);
  }

  public GetContainerTreeNodeAction buildGetContainerTreeNodeAction(String rootName) {
    return new GetContainerTreeNodeAction(mapper, rootName, containerEventListeners);
  }

  public RemoveContainerAction buildRemoveContainerTreeAction(String containerName) {
    return new RemoveContainerAction(containerServices, containerName, containerEventListeners);
  }

  public CreateContainerAction buildCreateContainerAction(String parentName, Container container) {
    return new CreateContainerAction(parentName, container, containerServices);
  }
}
