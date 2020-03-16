package com.spx.containment.core.api.monitor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.services.ContainerServices;
import javax.websocket.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContainerEventSubscriptionTest {

  private ContainerServices mockContainerService;
  private ContainerEventSubscription subject;
  private ClientSessionHandler mockClientSessionHandler;


  @Before
  public void setup() {
    mockContainerService = mock(ContainerServices.class);
    mockClientSessionHandler = mock(ClientSessionHandler.class);
    subject = new ContainerEventSubscription(mockContainerService, mockClientSessionHandler);

  }

  @Test
  public void addSubscription() {

    Session session = mock(Session.class);
    Container container = new Container();
    container.setReference("one");
    when(mockContainerService.isContainerChildOf(container, container)).thenReturn(true);
    subject = new ContainerEventSubscription(mockContainerService, mockClientSessionHandler);
    when(session.getId()).thenReturn("1");

    subject.addSubscription(container, session);
    Assert.assertTrue(subject.getSubscriberSessions(container)
        .anyMatch(s -> "1".equals(s.getId())));
  }

  @Test
  public void addTwoSubscriptionSameContainer() {
    Session session1 = mock(Session.class);
    Session session2 = mock(Session.class);
    Container container = new Container();
    container.setReference("one");
    when(mockContainerService.isContainerChildOf(container, container)).thenReturn(true);
    subject = new ContainerEventSubscription(mockContainerService, mockClientSessionHandler);
    when(session1.getId()).thenReturn("1");
    when(session2.getId()).thenReturn("2");

    subject.addSubscription(container, session1);
    subject.addSubscription(container, session2);
    Assert.assertTrue(subject.getSubscriberSessions(container)
        .anyMatch(s -> "1".equals(s.getId())));
    Assert.assertTrue(subject.getSubscriberSessions(container)
        .anyMatch(s -> "2".equals(s.getId())));
    Assert.assertEquals(2, subject.getSubscriberSessions(container)
        .count());
  }

  @Test
  public void addOneSubscriptionChildContainer() {
    Session session1 = mock(Session.class);
    when(mockContainerService.isContainerChildOf(any(Container.class),
        any(Container.class))).thenReturn(true);
    when(session1.getId()).thenReturn("1");

    Container container = new Container();
    container.setReference("container");
    Container containerChild = new Container();
    containerChild.setReference("container-child");
    container.addChild(containerChild);
    subject.addSubscription(container, session1);
    Assert.assertTrue(subject.getSubscriberSessions(containerChild)
        .anyMatch(s -> "1".equals(s.getId())));
  }


  @Test
  public void addNonChildSubscriptionChildContainer() {
    mockContainerService = mock(ContainerServices.class);
    subject = new ContainerEventSubscription(mockContainerService, mockClientSessionHandler);
    Session session1 = mock(Session.class);

    when(session1.getId()).thenReturn("1");

    Container container = new Container();
    container.setReference("container");
    Container containerChild = new Container();
    containerChild.setReference("container-child");
    // container.addChild(containerChild);  //  <--- NOT ADDED!!!

    when(mockContainerService.isContainerChildOf(containerChild, container)).thenReturn(false);
    subject.addSubscription(container, session1);

    Assert.assertTrue(subject.getSubscriberSessions(containerChild)
        .noneMatch(s -> "1".equals(s)));

  }


}
