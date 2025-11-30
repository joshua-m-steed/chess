package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

    public void add(Session session) {
        connections.put(session, session);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage message) throws Exception {
        String msg = new Gson().toJson(message);
        for (Session conn : connections.values()) {
            if (conn.isOpen()) {
                if (!conn.equals(excludeSession)) {
                    conn.getRemote().sendString(msg);
                }
            }
        }
    }

    public void send(Session onlySession, ServerMessage message) throws Exception {
        String msg = new Gson().toJson(message);
        if (onlySession.isOpen()) {
            onlySession.getRemote().sendString(msg);
        }
    }
}
