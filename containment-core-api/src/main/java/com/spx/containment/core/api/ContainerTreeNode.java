package com.spx.containment.core.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerTreeNode {

  private Set<String> children = new HashSet<>();

  private String parent;

  private String name = "N/A";

  private String type;

  private String reference;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<String> getChildren() {
    return Collections.unmodifiableSet(children);
  }

  public void setChildren(Set<String> k) {
    children = k;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String newParent) {
    parent = newParent;
  }

  public void addChild(String child) {
    children.add(child);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }
}
