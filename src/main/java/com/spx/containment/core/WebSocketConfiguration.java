package com.spx.containment.core;

import com.spx.containment.core.api.monitor.ClientSessionHandler;
import com.spx.containment.core.api.monitor.ContainerEventMonitor;
import com.spx.containment.core.api.monitor.ContainerEventSubscription;
import com.spx.containment.core.services.ContainerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
@ConditionalOnWebApplication
@EnableScheduling
public class WebSocketConfiguration {

    private final ClientSessionHandler sessionHandler;
    private final ContainerEventSubscription containerEventSubscription;
    private final ContainerServices containerServices;


    @Autowired
    public WebSocketConfiguration(ClientSessionHandler sessionHandler, ContainerEventSubscription containerEventSubscription, ContainerServices containerServices) {
        this.sessionHandler = sessionHandler;
        this.containerEventSubscription = containerEventSubscription;
        this.containerServices = containerServices;
    }

    @Bean
    public ContainerEventMonitor containmentActivityMonitor() {
        return new ContainerEventMonitor(sessionHandler, containerEventSubscription, containerServices);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocketSpringConfigurator customSpringConfigurator() {
        return new WebSocketSpringConfigurator(); // This is just to get context
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
