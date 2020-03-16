package com.spx.containment.core.builders;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Global;
import com.spx.containment.core.services.ContainerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class GlobalContainerFactory implements SpecificContainerFactory<Global> {

  private final Global global;

  @Inject
  @Autowired
  GlobalContainerFactory(ContainerServices cam) {
    global = cam.getGlobal();
  }

  @Override
  public String getType() {

    return "GLOBAL";
  }

  @Override
  public Global createContainerFromView(ContainerView view) {
    return global;
  }

  @Override
  public Class<Global> getContainerClass() {

    return Global.class;
  }

  @Override
  public ContainerView createViewContainerContainer(Global container) {

    return new ContainerView();
  }

}
