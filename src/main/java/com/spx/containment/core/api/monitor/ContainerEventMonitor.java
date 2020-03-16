package com.spx.containment.core.api.monitor;


import com.spx.containment.core.WebSocketSpringConfigurator;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerServices;
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@ServerEndpoint(value = "/ws/container/events", configurator = WebSocketSpringConfigurator.class, encoders = {
    EventEncoder.class, MessageEncoderDecoder.class}, decoders = {MessageEncoderDecoder.class})
public class ContainerEventMonitor {

  private final ClientSessionHandler sessionHandler;
  private final ContainerEventSubscription containerEventSubscription;
  private final ContainerServices containerServices;

  @Autowired
  public ContainerEventMonitor(ClientSessionHandler sessionHandler,
      ContainerEventSubscription containerEventSubscription, ContainerServices containerServices) {
    this.sessionHandler = sessionHandler;
    this.containerEventSubscription = containerEventSubscription;
    this.containerServices = containerServices;
    log.debug("Containment activity monitor started");
  }

  @OnOpen
  public void onOpen(Session session) {
    log.debug("open session: {}", session.getId());
    sessionHandler.add(session);
  }

  @OnMessage
  public void onMessage(ContainerMonitorMessage message, Session session)
      throws IOException, EncodeException {

    log.debug("Message from session: {}", session.getId());
    log.debug("Message Message for: {}", message.getReference());
    try {
      Container container = containerServices.findByReference(message.getReference());
      switch (message.getAction()) {
        case CREATE:
          containerEventSubscription.addSubscription(container, session);
          break;
        case DELETE:
          containerEventSubscription.removeSubscription(container, session);
          break;
        default:
          log.error("Action not understood {}", message.getAction());
      }
    } catch (Exception e) {
      log.error("Error processing message ", e);
    }

    session.getBasicRemote()
        .sendObject(message);

  }

  @OnClose
  public void onClose(Session session) {
    log.info("onClose " + session.getId());
    sessionHandler.close(session);
  }

  @OnError
  public void onError(Throwable t) {
    log.error(t.getMessage(), t);
  }
}





