package com.spx.containment.core.services;

import com.spx.containment.core.api.monitor.ContainerMonitorMessage.MessageActionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainerEvent {

    private String reference;
    private MessageActionType event;
}
