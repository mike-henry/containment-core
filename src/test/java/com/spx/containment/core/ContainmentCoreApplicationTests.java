package com.spx.containment.core;

import static org.mockito.Mockito.mock;

import com.spx.containment.core.model.Global;
import com.spx.containment.core.persistance.ContainerRepository;
import java.util.Optional;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootTest
public class ContainmentCoreApplicationTests {


  @Test
  public void contextLoads() {

  }

  @TestConfiguration
  public static class TestConfig {

    @Bean
    @Primary
    public ContainerRepository mockSystemTypeDetector() {
      ContainerRepository repository = mock(ContainerRepository.class);
      Mockito.when(repository.findByName("global", 1))
          .thenReturn(Optional.of(new Global()));
      return repository;
    }
  }


}
