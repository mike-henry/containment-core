package com.spx.containment.core.actions;

import static com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType.CREATE;

import com.spx.containment.core.api.ContainerTreeNode;
import com.spx.containment.core.model.Container;
import com.spx.containment.core.services.ContainerEvent;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ModelToViewAdaptor;
import java.util.Set;
import java.util.concurrent.Callable;

public class GetContainerTreeNodeAction implements Callable<ContainerTreeNode[]> {

  private final ModelToViewAdaptor mapper;
  private final String rootName;
  private final Set<ContainerEventListener> containerEventListeners;

  GetContainerTreeNodeAction(ModelToViewAdaptor modelToViewMapper, String rootName,
      Set<ContainerEventListener> containerEventListeners) {
    mapper = modelToViewMapper;
    this.rootName = rootName;
    this.containerEventListeners = containerEventListeners;
  }

  @Override
  public ContainerTreeNode[] call() {
    ContainerTreeNode[] result = mapper.getTreeNodeArray(rootName);
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
