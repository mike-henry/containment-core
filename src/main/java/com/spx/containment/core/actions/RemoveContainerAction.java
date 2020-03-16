package com.spx.containment.core.actions;

import static com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType.DELETE;

import com.spx.containment.core.exceptions.NotFoundException;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerEvent;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ContainerServices;
import java.util.Set;
import java.util.concurrent.Callable;

public class RemoveContainerAction implements Callable<Void> {

  private final ContainerServices containerServices;
  private final String containerName;
  private final Set<ContainerEventListener> containerEventListeners;


  RemoveContainerAction(ContainerServices containerServices, String containerName,
      Set<ContainerEventListener> containerEventListeners) {
    this.containerServices = containerServices;
    this.containerName = containerName;
    this.containerEventListeners = containerEventListeners;
  }

  @Override
  public Void call() {
    Container container = containerServices.fetchContainerByName(containerName)
        .orElseThrow(() -> new NotFoundException(containerName));
    containerServices.remove(container);
    fireContainerEvent(container);
    return null;
  }


  private void fireContainerEvent(Container container) {
    ContainerEvent containerEvent = new ContainerEvent();
    containerEvent.setReference(container.getReference());
    containerEvent.setEvent(DELETE);
    containerEventListeners.stream()
        .forEach(l -> l.containerEvent(container, containerEvent));
  }
}
