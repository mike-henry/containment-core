package com.spx.containment.core.actions;

import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerServices;
import java.util.concurrent.Callable;

public class CreateContainerAction implements Callable<Void> {

  private final ContainerServices containerServices;
  private final String parentReference;
  private final Container container;

  CreateContainerAction(String parentReference, Container container,
      ContainerServices containerServices) {
    this.parentReference = parentReference;
    this.container = container;
    this.containerServices = containerServices;
  }

  @Override
  public Void call() throws Exception {
    Container parent = containerServices.findByReference(parentReference);
    parent.addChild(container);
    containerServices.save(container);
    containerServices.save(parent);
    return null;
  }
}
