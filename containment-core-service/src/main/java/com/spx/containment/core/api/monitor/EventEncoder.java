package com.spx.containment.core.api.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spx.containment.core.services.ContainerEvent;
import javax.websocket.EndpointConfig;

public class EventEncoder implements javax.websocket.Encoder.Text<ContainerEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String encode(ContainerEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
