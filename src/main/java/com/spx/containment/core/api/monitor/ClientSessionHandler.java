package com.spx.containment.core.api.monitor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientSessionHandler {

    private final Map<String, SessionHolder> sessions;
    private final int timeoutMilliseconds;
    private final Set<SessionEventListener> sessionEventListeners = new HashSet<>();

    public ClientSessionHandler(@Value("${application.websocket.session.period}") int timeoutMilliseconds) {
        sessions = new ConcurrentHashMap<String, SessionHolder>();
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    @Scheduled(fixedDelayString = "${application.websocket.session.period}")
    public void clearInactiveSessions() {

        log.debug("clearing inactive sessions");
        sessions.keySet()
            .stream()
            .filter(this::inActive)
            .map(id -> sessions.get(id))
            .forEach(sessionHolder -> {
                closeAndRemove(sessionHolder.getSession());
            });
    }


    void close(Session session) {
        closeAndRemove(session);
    }

    private void close(String id) {
        try {
            if (sessions.get(id) == null) {
                return;
            }
            sessions.get(id)
                .getSession()
                .close();
        } catch (Exception e) {
            log.error("session closing failure", e);
        }
    }

    private void closeAndRemove(Session session) {
        close(session.getId());
        removeSession(session);
    }

    private void removeSession(Session session) {
        sessions.remove(session.getId());
        sessionEventListeners.forEach(l -> l.sessionRemoved(session));
    }

    private boolean inActive(String id) {
        Date date = getMinutesAgo(timeoutMilliseconds);
        SessionHolder holder = sessions.get(id);
        return holder != null && holder.getLastActivity()
            .before(date);
    }

    private Date getMinutesAgo(int milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 0 - milliseconds);
        return calendar.getTime();
    }

    public Optional<Session> getSession(String id) {
        return Optional.ofNullable(sessions.get(id))
            .map(s -> s.ping())
            .map(s -> s.getSession());
    }

    void add(Session session) {
        sessions.put(session.getId(), new SessionHolder(session));
    }

    public void addSessionListener(SessionEventListener sessionEventListener) {
        sessionEventListeners.add(sessionEventListener);
    }


    @Getter
    private static class SessionHolder {

        final private Session session;

        @Setter
        private Date lastActivity;

        private SessionHolder(Session session) {
            this.session = session;
            ping();
        }

        SessionHolder ping() {
            lastActivity = new Date();
            return this;
        }
    }
}
