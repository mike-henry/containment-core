package com.spx.containment.core.services;

import com.spx.containment.core.api.model.Container;

public interface ContainerEventListener {

  void containerEvent(Container container, ContainerEvent containerEvent);
}
