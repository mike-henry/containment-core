package com.spx.containment.core.actions;

import static com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType.CREATE;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerEvent;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ModelToViewAdaptor;
import java.util.Set;
import java.util.concurrent.Callable;

public class GetContainerTreeAction implements Callable<ContainerView[]> {

  private final ModelToViewAdaptor mapper;
  private final String rootName;
  private final Set<ContainerEventListener> containerEventListeners;

  GetContainerTreeAction(ModelToViewAdaptor modelToViewMapper, String rootName,
      Set<ContainerEventListener> containerEventListeners) {
    mapper = modelToViewMapper;
    this.rootName = rootName;
    this.containerEventListeners = containerEventListeners;
  }

  @Override
  public ContainerView[] call() {
    ContainerView[] result = mapper.getViewArray(rootName);
    Container root = mapper.getContainerByReference(rootName);
    fireContainerEvent(root);
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
