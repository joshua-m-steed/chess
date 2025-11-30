package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.GameMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch ((command.getCommandType())) {
                case CONNECT -> join(command.getGameID(), ctx.session);
                case LEAVE -> exit(command.getAuthToken(), ctx.session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void join(Integer game, Session session) throws Exception {
        connections.add(session);
        GameMessage notification = new GameMessage(game.toString());
        connections.send(session, notification);
    }

    private void exit(String name, Session session) throws Exception {
        String message = String.format("%s left the chess tables", name);
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.DISCONNECT, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }
}