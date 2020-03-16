package com.spx.containment.core.api.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.websocket.EndpointConfig;

public class MessageEncoderDecoder implements javax.websocket.Decoder.Text<ContainerMonitorMessage>, javax.websocket.Encoder.Text<ContainerMonitorMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ContainerMonitorMessage decode(String jsonMessage) {
        try {
            return objectMapper.readValue(jsonMessage, ContainerMonitorMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean willDecode(String message) {
        return true;
    }

    @Override
    public String encode(ContainerMonitorMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
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
