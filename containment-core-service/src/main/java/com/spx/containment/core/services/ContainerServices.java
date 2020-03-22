package com.spx.containment.core.services;

import com.spx.containment.core.exceptions.NotFoundException;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.api.model.Global;
import com.spx.containment.core.persistance.ContainerRepository;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ContainerServices {

  private final ContainerRepository repository;

  @Inject
  @Autowired
  public ContainerServices(ContainerRepository repository) {
    this.repository = repository;
  }

  public Global getGlobal() {
    Container container = fetchContainerByName(Global.NAME).orElseGet(this::buildGlobal);
    return (Global) container;
  }

  private Global buildGlobal() {
    Global result = new Global();
    result.setName(Global.NAME);
    result.setReference(Global.NAME);
    result.setParent(result);
    result = repository.save(result);
    return result;
  }

  public Optional<Container> fetchContainerByName(String name) {

    Optional<Container> result = repository.findByName(name, 1);
    if (!result.isPresent() && Global.NAME.equals(name)) {
      return Optional.of(buildGlobal());
    }

    return result;
  }

  public Container findByReference(String reference) {

    int c1 = repository.countParentPaths("BC Hallway 21", "Bristol Construction House");
    int c2 = repository.countParentPaths("BC Hallway 21", "Manchester Factory");
    log.error("Count 1 " + c1);
    log.debug("Count 2 " + c2);

    return repository.findByReference(reference, 1)
        .orElseThrow(
            () -> new NotFoundException(String.format("reference not found :%s", reference)));
  }

  public <C extends Container> Optional<Container> fetchContainerByName(String name,
      Class<C> type) {
    return fetchContainerByName(name).filter(type::isInstance);
  }

  public Container save(Container container) {
    Container result = repository.save(container);
    repository.save(container.getParent()
        .get());
    return result;
  }

  @Transactional
  public Container saveTree(Container container) {
    Container newParent = container.getParent()
        .orElseGet(() -> getGlobal());
    List<Container> done = new LinkedList<>();
    container.setParent(newParent);
    return doSaveTree(container, done);
  }

  private Container doSaveTree(Container container, List<Container> done) {
    log.debug("saving  {}", container);

    if (done.contains(container)) {  //TODO remove done parameter
      log.error("Found  a done  container {}", container.getReference());
      return container;
    }
    container.getChildren()
        .stream()
        .filter(c -> !Global.NAME.equalsIgnoreCase(c.getName()))
        .forEach(c -> doSaveTree(c, done));
    Container result = save(container);
    log.debug("saved container:{}", container.getReference());
    return result;
  }

  public void remove(Container c) {
    repository.delete(c);

  }

  private Set<Container> getDecendants(Container container) {
    Set<Container> result = new HashSet<>();
    result.add(container);
    container.getChildren()
        .forEach(child -> result.addAll(getDecendants(child)));
    return result;
  }

  public boolean isContainerChildOf(Container child, Container containerChild) {
    return repository.countParentPaths(child.getName(), containerChild.getName()) != 0
        || child.getName()
        .equals(containerChild.getName());
  }

}
