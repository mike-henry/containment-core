package com.spx.containment.core.services;

import com.spx.containment.core.model.Container;

public interface ContainerEventListener {

    public void containerEvent(Container container, ContainerEvent containerEvent);
}
