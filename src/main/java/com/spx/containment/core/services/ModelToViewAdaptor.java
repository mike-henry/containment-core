package com.spx.containment.core.services;

import com.spx.containment.core.api.ContainerTreeNode;
import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.builders.GeneralContainerViewFactory;
import com.spx.containment.core.exceptions.NotFoundException;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.api.model.Global;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ModelToViewAdaptor {

  private final ContainerServices containerAccessManager;

  private final GeneralContainerViewFactory containerFactory;

  @Inject
  @Autowired
  public ModelToViewAdaptor(GeneralContainerViewFactory containerFactory,
      ContainerServices containerAccessManager) {
    this.containerFactory = containerFactory;
    this.containerAccessManager = containerAccessManager;
  }


  private Map<String, Container> getViewMap(ContainerView[] containerViews) {
    return Stream.of(containerViews)
        .map(containerFactory::createLooseContainer)
        .collect(Collectors.toMap(container -> container.getName(), c -> c));
  }


  private void attachChildrenToContainers(Map<String, Container> containers,
      ContainerView[] containerViews) {
    Stream.of(containerViews)
        .forEach((view) -> attachChildrenToContainer(containers, view));
  }


  @Transactional
  private void attachChildrenToContainer(Map<String, Container> containers, ContainerView view) {
    Container container = containers.get(view.getName());
    view.getChildren()
        .stream()
        .map(presentContainer -> containers.get(presentContainer))
        .peek(presentContainer -> setParent(view, presentContainer))
        .forEach(presentContainer -> container.addChild(presentContainer));
  }


  private void setParent(ContainerView view, Container presentContainer) {
    presentContainer.setParent(containerAccessManager.fetchContainerByName(view.getParent())
        .orElse(containerAccessManager.getGlobal()));
  }


  private Optional<Container> findTopTree(Collection<Container> containers) {

    List<String> names = containers.stream()
        .map(container -> container.getName())
        .collect(Collectors.toList());

    Optional<Container> result = containers.stream()
        .filter(container -> isParentUnknown(names, container))
        .findFirst();
    if (result.get()
        .getParent()
        .isPresent() == false) {
      result.get()
          .setParent(containerAccessManager.getGlobal());
    }

    return result;
  }


  private boolean isParentUnknown(List<String> names, Container container) {
    return !container.getParent()
        .isPresent() || names.contains(container.getParent()
        .get()
        .getName()) == false;
  }

  @Transactional
  public Optional<Container> getContainerModel(ContainerView[] containerViews) {
    Map<String, Container> containers = getViewMap(containerViews);
    attachChildrenToContainers(containers, containerViews);
    Optional<Container> topContainer = findTopTree(containers.values());
    return topContainer;
  }

  @Deprecated
  public ContainerView[] getViewArray(String name) {
    return getViews(name).toArray(new ContainerView[]{});
  }

  public ContainerTreeNode[] getTreeNodeArray(String name) {
    return getTreeNodes(name).toArray(new ContainerTreeNode[]{});
  }


  private Collection<ContainerView> getViews(String reference) {
    Container container = getContainerByReference(reference);
    List<ContainerView> result = new ArrayList<ContainerView>();
    buildViews(result, container);
    return result;
  }

  private Collection<ContainerTreeNode> getTreeNodes(String name) {
    Container container = getContainerByName(name);
    List<ContainerTreeNode> result = new ArrayList<>();
    buildTreeNodes(result, container);
    return result;
  }

  private Container getContainerByName(String name) {
    return containerAccessManager.fetchContainerByName(name)
        .orElseThrow(() -> new NotFoundException(String.format("name not found :%s", name)));
  }

  public Container getContainerByReference(String reference) {
    return containerAccessManager.findByReference(reference);
  }

  @Deprecated
  private void buildViews(Collection<ContainerView> result, Container container) {
    container.getChildren()
        .stream()
        .filter(c -> !c.getClass()
            .equals(Global.class))
        .map(c -> getContainerByName(c.getName()))
        .forEach(child -> buildViews(result, child));
    ContainerView view = containerFactory.createView(container);
    result.add(view);
  }

  private void buildTreeNodes(Collection<ContainerTreeNode> result, Container container) {
    container.getChildren()
        .stream()
        .filter(c -> !c.getClass()
            .equals(Global.class))
        .map(c -> getContainerByName(c.getName()))
        .forEach(child -> buildTreeNodes(result, child));
    ContainerTreeNode node = containerFactory.createTreeNode(container);
    result.add(node);
  }

}
