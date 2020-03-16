package com.spx.containment.core.api.monitor;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainerMonitorMessage {

    private String type;
    private String reference;
    private MessageActionType action;


    public static enum MessageActionType {

        @JsonProperty("update") UPDATE("update"), @JsonProperty("get") GET("get"),  //This should  be removed as there is no change
        @JsonProperty("create") CREATE("create"), @JsonProperty("delete") DELETE("delete");

        private final String type;

        private MessageActionType(String type) {
            this.type = type;
        }

        @Override
        @JsonGetter
        public String toString() {
            return type;
        }

    }
}
