package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Integer> connections = new ConcurrentHashMap<>();

    public void add(Session session, Integer gameID) {
        connections.put(session, gameID);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage message, Integer gameID) throws Exception {
        String msg = new Gson().toJson(message);

        for (Session conn : getSessionWithGameID(gameID)) {
            if (conn.isOpen() && conn != excludeSession) {
                conn.getRemote().sendString(msg);
            }
        }
    }

    public void send(Session onlySession, ServerMessage message) throws Exception {
        String msg = new Gson().toJson(message);
        if (onlySession.isOpen()) {
            onlySession.getRemote().sendString(msg);
        }
    }

    private List<Session> getSessionWithGameID(Integer gameID) {
        return connections.entrySet().stream()
                .filter(e -> e.getValue().equals(gameID))
                .map(Map.Entry::getKey)
                .toList();
    }
}
