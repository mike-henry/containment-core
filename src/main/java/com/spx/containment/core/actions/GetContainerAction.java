package com.spx.containment.core.actions;

import static com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType.CREATE;

import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerEvent;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ModelToViewAdaptor;
import java.util.Set;
import java.util.concurrent.Callable;

public class GetContainerAction implements Callable<Container> {

  private final ModelToViewAdaptor mapper;
  private final String rootName;
  private final Set<ContainerEventListener> containerEventListeners;

  GetContainerAction(ModelToViewAdaptor modelToViewMapper, String rootName,
      Set<ContainerEventListener> containerEventListeners) {
    mapper = modelToViewMapper;
    this.rootName = rootName;
    this.containerEventListeners = containerEventListeners;
  }

  @Override
  public Container call() {

    Container result = mapper.getContainerByReference(rootName);
    fireContainerEvent(result);
    return result;
  }


  private void fireContainerEvent(Container container) {
    ContainerEvent containerEvent = new ContainerEvent();
    containerEvent.setReference(container.getReference());
    containerEvent.setEvent(CREATE);
    containerEventListeners.stream()
        .forEach(l -> l.containerEvent(container, containerEvent));
  }

}
