package com.spx.containment.core.builders;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.api.model.Box;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BoxContainerFactory implements SpecificContainerFactory<Box> {

  private static String HEIGHT = "height";

  private static String WIDTH = "width";

  private static String DEPTH = "depth";

  @Override
  public String getType() {
    return "Box";
  }

  @Override
  public Box createContainerFromView(ContainerView view) {
    Box result = new Box();
    result.setHeight(getDoubleAdditionalProperty(view, HEIGHT));
    result.setDepth(getDoubleAdditionalProperty(view, DEPTH));
    result.setWidth(getDoubleAdditionalProperty(view, WIDTH));
    return result;
  }

  @Override
  public ContainerView createViewContainerContainer(Box box) {
    ContainerView result = new ContainerView();
    result.getAdditionalProperties()
        .put(HEIGHT, box.getHeight()
            .toPlainString());
    result.getAdditionalProperties()
        .put(DEPTH, box.getDepth()
            .toPlainString());
    result.getAdditionalProperties()
        .put(WIDTH, box.getWidth()
            .toPlainString());

    return result;
  }

  private BigDecimal getDoubleAdditionalProperty(ContainerView view, String propertyName) {

    return BigDecimal.valueOf(Double.parseDouble((String) view.getAdditionalProperties()
        .getOrDefault(propertyName, "0")));
  }

  @Override
  public Class<Box> getContainerClass() {

    return Box.class;
  }

}
