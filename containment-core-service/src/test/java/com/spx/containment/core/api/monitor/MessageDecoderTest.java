package com.spx.containment.core.api.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType;
import org.junit.Test;

public class MessageDecoderTest {

    private final String inputCreate = "{\"action\":\"create\",\"type\":\"container\",\"reference\":\"Manchester Factory\"}";

    private final MessageEncoderDecoder subject = new MessageEncoderDecoder();


    @Test
    public void decode() {
        ContainerMonitorMessage result = subject.decode(inputCreate);
        assertEquals(result.getReference(), "Manchester Factory");
        assertEquals(result.getType(), "container");
        assertTrue(result.getAction() == MessageActionType.CREATE);
    }


}
