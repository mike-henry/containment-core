package com.spx.containment.core.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.Relationship;


@Getter
@Setter
@Slf4j

public class Container extends AbstractReferencable {


  @Relationship(type = "PARENTING", direction = Relationship.INCOMING)
  @JsonIgnore
  private Set<Container> children = new HashSet<>();

  @Relationship(type = "PARENTING", direction = Relationship.OUTGOING)
  @JsonIgnore
  private Container parent;

  public Set<Container> getChildren() {
    return Collections.unmodifiableSet(children);
  }

  public Optional<Container> getParent() {
    return Optional.ofNullable(parent);
  }

  public void setParent(Container newParent) {
    newParent.addChild(this);
  }

  public void addChild(Container child) {

    if (isAncestor(child)) {
      throw new IllegalArgumentException("Container in Ancestor");
    }

    if (child.getParent()
        .isPresent()) {
      child.getParent()
          .get().children.remove(child);
    }
    child.parent = this;
    children.add(child);
  }

  private boolean isAncestor(Container child) {
    return getAncestors().contains(child);
  }

  @JsonIgnore
  private List<Container> getAncestors() {
    Container presentAncestor = this;
    List<Container> result = new ArrayList<Container>();
    // result.add(presentAncestor);
    while (presentAncestor.getParent()
        .isPresent()) {
      log.debug(presentAncestor.getName());
      presentAncestor = presentAncestor.getParent()
          .get();
      result.add(presentAncestor);
      if (presentAncestor instanceof Global) {
        break;
      }
    }
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Container other = (Container) obj;
    if (children == null) {
      if (other.children != null) {
        return false;
      }
    } else if (!children.equals(other.children)) {
      return false;
    }
    if (parent == null) {
      return other.parent == null;
    } else {
      return parent.equals(other.parent);
    }
  }


}
