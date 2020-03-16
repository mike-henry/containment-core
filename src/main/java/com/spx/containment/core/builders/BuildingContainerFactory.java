package com.spx.containment.core.builders;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Building;
import org.springframework.stereotype.Component;

@Component
public class BuildingContainerFactory implements SpecificContainerFactory<Building> {

  private static String LONGITUDE = "longitude";

  private static String LATITUDE = "latitude";

  private static String ADDRESS_1 = "address1";

  private static String ADDRESS_2 = "address2";

  private static String CODE = "code";

  private static String COUNTRY = "country";

  @Override
  public String getType() {

    return "Building";
  }

  @Override
  public Building createContainerFromView(ContainerView view) {
    Building result = new Building();
    result.setLongitude(Double.parseDouble((String) view.getAdditionalProperties()
        .getOrDefault(LONGITUDE, "0.00")));
    result.setLatitude(Double.parseDouble((String) view.getAdditionalProperties()
        .getOrDefault(LATITUDE, "53.20")));
    result.setAddress1((String) view.getAdditionalProperties()
        .getOrDefault(ADDRESS_1, "N/A"));
    result.setAddress2((String) view.getAdditionalProperties()
        .getOrDefault(ADDRESS_2, "N/A"));
    result.setCode((String) view.getAdditionalProperties()
        .getOrDefault(CODE, "N/A"));
    result.setCountry((String) view.getAdditionalProperties()
        .getOrDefault(COUNTRY, "N/A"));

    return result;
  }

  @Override
  public Class<Building> getContainerClass() {

    return Building.class;
  }

  @Override
  public ContainerView createViewContainerContainer(Building container) {
    ContainerView result = new ContainerView();
    result.getAdditionalProperties()
        .put(LONGITUDE, container.getLongitude());
    result.getAdditionalProperties()
        .put(LATITUDE, container.getLatitude());
    result.getAdditionalProperties()
        .put(ADDRESS_1, container.getAddress1());
    result.getAdditionalProperties()
        .put(ADDRESS_2, container.getAddress2());
    result.getAdditionalProperties()
        .put(CODE, container.getCode());
    result.getAdditionalProperties()
        .put(COUNTRY, container.getCountry());
    return result;
  }

}
