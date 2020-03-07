package com.spx.containment.core.api.monitor;

import javax.websocket.Session;

public interface SessionEventListener {

    public void sessionCreated(Session session);

    public void sessionRemoved(Session session);

}
