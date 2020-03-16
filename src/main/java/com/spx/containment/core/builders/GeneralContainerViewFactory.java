package com.spx.containment.core.builders;

import com.spx.containment.core.api.ContainerTreeNode;
import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.AbstractReferencable;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.StreamSupport;

@Component
public class GeneralContainerViewFactory {

  private final Set<SpecificContainerFactory> services;


  private final ContainerServices cam;

  @Autowired
  public GeneralContainerViewFactory(Set<SpecificContainerFactory> services,
      ContainerServices cam) {
    this.services = services;
    this.cam = cam;
  }

  public Container createLooseContainer(ContainerView view) {
    Container container = cam.fetchContainerByName(view.getName())
        .orElseGet(() -> createFromFactory(view));
    return container;
  }

  private Container createFromFactory(ContainerView view) {
    SpecificContainerFactory<? extends Container> factory = getFactory(view);
    Container container = factory.createContainerFromView(view);
    Container parent = cam.fetchContainerByName(view.getParent())
        .orElseThrow(() -> new RuntimeException("Parent not found"));
    container.setName(view.getName());
    container.setParent(parent);
    container.setReference(view.getReference());
    container = cam.save(container);

    return container;
  }

  private SpecificContainerFactory<? extends Container> getFactory(ContainerView view) {
    return StreamSupport.stream(services.spliterator(), false)
        .filter((factory) -> factory.getType()
            .equals(view.getType()))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Unknown Type to construct " + view.getType()));
  }

  private SpecificContainerFactory<? extends Container> getFactory(AbstractReferencable container) {
    return services.stream()
        .filter((factory) -> factory.getContainerClass()
            .isAssignableFrom(container.getClass()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "Unknown Type to construct " + container.getClass()));
  }

  public <T extends Container> ContainerView createView(T container) {
    @SuppressWarnings("rawtypes") SpecificContainerFactory factory = getFactory(container);
    @SuppressWarnings("unchecked") ContainerView view = factory.createViewContainerContainer(
        container);

    view.setName(container.getName());
    view.setReference(container.getReference());
    view.setParent(container.getParent()
        .orElse(new Container())
        .getName());
    container.getChildren()
        .forEach(child -> view.addChild(child.getName()));
    view.setType(factory.getType());

    return view;
  }

  public ContainerTreeNode createTreeNode(Container container) {
    SpecificContainerFactory factory = getFactory(container);
    ContainerTreeNode node = new ContainerTreeNode();
    node.setName(container.getName());
    node.setReference(container.getReference());
    node.setParent(container.getParent()
        .orElse(new Container())
        .getName());
    container.getChildren()
        .forEach(child -> node.addChild(child.getName()));
    node.setType(factory.getType());
    return node;
  }


}
