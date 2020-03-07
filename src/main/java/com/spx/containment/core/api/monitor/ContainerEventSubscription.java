package com.spx.containment.core.api.monitor;


import com.spx.containment.core.model.Container;
import com.spx.containment.core.services.ContainerEvent;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ContainerServices;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContainerEventSubscription implements SessionEventListener, ContainerEventListener {

    private static final IdComparator<Session> SESSION_COMPARATOR = new IdComparator<Session>(s -> s.getId());
    private static final IdComparator<Container> CONTAINER_COMPARATOR = new IdComparator<Container>(c -> c.getReference());

    private final Map<Container, Set<Session>> containerSessions = new TreeMap<Container, Set<Session>>(CONTAINER_COMPARATOR);
    private final Map<Session, Set<Container>> sessionContainers = new TreeMap<Session, Set<Container>>(SESSION_COMPARATOR);
    private final ContainerServices containerServices;

    @Autowired
    public ContainerEventSubscription(ContainerServices containerServices, ClientSessionHandler clientSessionHandler) {
        this.containerServices = containerServices;

        clientSessionHandler.addSessionListener(this);
    }

    void addSubscription(Container container, Session session) {
        Set<Session> sessions = containerSessions.get(container);
        if (sessions == null) {
            sessions = new TreeSet<Session>(SESSION_COMPARATOR);
        }
        sessions.add(session);
        containerSessions.put(container, sessions);

        Set<Container> containers = sessionContainers.get(session);
        if (containers == null) {
            containers = new TreeSet<Container>(CONTAINER_COMPARATOR);
        }
        containers.add(container);
        sessionContainers.put(session, containers);
    }

    void removeSubscription(Container container, Session session) {
        Set<Session> sessions = containerSessions.get(container);
        if (sessions != null) {
            sessions.remove(session);
        }

        Set<Container> containers = sessionContainers.get(session);
        if (containers != null) {
            containers.remove(container);
        }

    }


    private void removeSession(final Session session) {
        Set<Container> containers = sessionContainers.get(session);
        if (containers == null) {
            return;
        }
        containers.stream()
            .map(containerSessions::get)
            .forEach(sessions -> sessions.remove(session));
        sessionContainers.remove(session);
    }

    Stream<Session> getSubscriberSessions(Container container) {
        return containerSessions.keySet()
            .stream()
            .filter(c -> isContainerChildOf(container, c))
            .map(this::doGetSubscriberSessions)
            .flatMap(ids -> ids);
    }

    private boolean isContainerChildOf(Container container, Container child) {
        return containerServices.isContainerChildOf(container, child);
    }

    private Stream<Session> doGetSubscriberSessions(Container container) {
        Set<Session> sessions = containerSessions.get(container);
        if (sessions == null) {
            return Stream.empty();
        }
        return sessions.stream();
    }

    @Override
    public void sessionCreated(Session session) {
    }

    @Override
    public void sessionRemoved(Session session) {
        removeSession(session);
    }

    @Override
    public void containerEvent(Container container, ContainerEvent containerEvent) {
        log.info("Event occurred {} {}", containerEvent.getReference(), containerEvent.getEvent());
        getSubscriberSessions(container).forEach(s -> {
            s.getAsyncRemote()
                .sendObject(containerEvent);
        });
    }


    static class IdComparator<O> implements Comparator<O> {

        final Function<O, String> mapToIdentifier;

        IdComparator(Function<O, String> mapToIdentifier) {
            this.mapToIdentifier = mapToIdentifier;
        }

        private String getId(O object) {
            if (object == null) {
                return null;
            }
            return mapToIdentifier.apply(object);
        }

        @Override
        public int compare(O o1, O o2) {
            if (getId(o1) == null && getId(o2) != null) {
                return 1;
            }
            if (getId(o1) != null && getId(o2) == null) {
                return -1;
            }
            if (getId(o1) == null && getId(o2) == null) {
                return 0;
            }
            return getId(o1).compareTo(getId(o2));
        }
    }


}
