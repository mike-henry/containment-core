package com.spx.containment.core.builders;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationContainerFactory implements SpecificContainerFactory<Location> {

  @Override
  public String getType() {

    return "Location";
  }

  @Override
  public Location createContainerFromView(ContainerView view) {
    Location result = new Location();

    return result;
  }

  @Override
  public Class<Location> getContainerClass() {

    return Location.class;
  }

  @Override
  public ContainerView createViewContainerContainer(Location container) {
    ContainerView result = new ContainerView();

    return result;
  }

}
