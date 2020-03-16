package com.spx.containment.core.api.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.math.BigDecimal;

@NodeEntity

public class Box extends Container {

  private BigDecimal height = BigDecimal.ZERO;

  private BigDecimal width = BigDecimal.ZERO;

  private BigDecimal depth = BigDecimal.ZERO;

  public BigDecimal getHeight() {
    if (height == null) {
      height = BigDecimal.ZERO;
    }
    return height;
  }

  public void setHeight(BigDecimal value) {
    height = value;

  }

  public BigDecimal getWidth() {
    if (width == null) {
      width = BigDecimal.ZERO;
    }
    return width;
  }

  public void setWidth(BigDecimal value) {
    width = value;

  }

  public BigDecimal getDepth() {
    if (depth == null) {
      depth = BigDecimal.ZERO;
    }
    return depth;
  }

  public void setDepth(BigDecimal value) {
    depth = value;
  }

}
