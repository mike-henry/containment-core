package com.spx.containment.core.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.id.UuidStrategy;
import org.neo4j.ogm.typeconversion.AttributeConverter;


@Getter
@Setter
@EqualsAndHashCode
@NodeEntity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
public abstract class AbstractReferencable {

  @Id
  @GeneratedValue(strategy = UuidStrategy.class)
  @Convert(UUIDConverter.class) //same as above
  @JsonIgnore
  protected UUID id;

  @Index(unique = true)
  protected String name;
  @Index(unique = true)
  protected String reference;


  public static class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public String toGraphProperty(UUID value) {
      return value == null ? null : value.toString();
    }

    @Override
    public UUID toEntityAttribute(String value) {
      return value == null ? null : UUID.fromString(value);
    }
  }
}
