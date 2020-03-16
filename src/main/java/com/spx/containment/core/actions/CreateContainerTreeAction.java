package com.spx.containment.core.actions;

import static com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType.GET;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerEvent;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ContainerServices;
import com.spx.containment.core.services.ModelToViewAdaptor;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

public class CreateContainerTreeAction implements Callable<Void> {

  private final ModelToViewAdaptor mapper;
  private final ContainerServices containerServices;
  private final ContainerView[] containerViews;
  private final Set<ContainerEventListener> containerEventListeners;

  CreateContainerTreeAction(ModelToViewAdaptor modelToViewMapper,
      ContainerServices containerServices, ContainerView[] containerViews,
      Set<ContainerEventListener> containerEventListeners) {
    mapper = modelToViewMapper;
    this.containerServices = containerServices;
    this.containerViews = containerViews;
    this.containerEventListeners = containerEventListeners;
  }

  @Override
  public Void call() {
    Optional<Container> parentContainer = mapper.getContainerModel(containerViews);
    containerServices.saveTree(parentContainer.get());
    fireContainerEvent(parentContainer);
    return null;
  }

  private void fireContainerEvent(Optional<Container> parentContainer) {
    ContainerEvent containerEvent = new ContainerEvent();
    containerEvent.setReference(parentContainer.get()
        .getReference());
    containerEvent.setEvent(GET);
    containerEventListeners.stream()
        .forEach(l -> l.containerEvent(parentContainer.get(), containerEvent));
  }

}
