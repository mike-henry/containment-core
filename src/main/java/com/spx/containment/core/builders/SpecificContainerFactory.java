package com.spx.containment.core.builders;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Container;

public interface SpecificContainerFactory<T extends Container> {

  String getType();

  Class<T> getContainerClass();

  T createContainerFromView(ContainerView view);

  ContainerView createViewContainerContainer(T container);

}
