package com.spx.containment.core;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spx.containment.core.api.mapping.SubClassObjectMapper;
import com.spx.containment.core.api.model.Box;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.api.model.Location;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class JsonMappingTest {

  private final SubClassObjectMapper mapper = new SubClassObjectMapper();


  private final Box box = new Box();
  private final Location location = new Location();


  @Test
  public void mappingIncludesType() throws JsonProcessingException {
    box.setName("b1");
    box.setReference("ref");
    log.info(mapper.writeValueAsString(box));
    Assert.assertTrue(mapper.writeValueAsString(box)
        .contains("\"type\":\"Box\""));

    location.setName("l1");
    location.setReference("ref");
    log.info(mapper.writeValueAsString(location));
    Assert.assertTrue(mapper.writeValueAsString(location)
        .contains("\"type\":\"Location\""));
  }

  @Test
  public void shouldDeserializeSubtypes() throws IOException {
    box.setName("b1");
    box.setReference("ref");
    String boxJson = mapper.writeValueAsString(box);

    final Container read = mapper.readValue(boxJson, Container.class);
    assertEquals(box, read);

    mapper.findClassesToRegister()
        .stream()
        .forEach(c -> {
          log.info("class found {}", c.getName());
        });


  }
}
