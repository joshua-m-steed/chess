package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import datamodel.User;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.GameMessage;
import service.GameService.*;
import service.UserService.*;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final DataAccess dataAccess;

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

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
                case CONNECT -> join(command, ctx.session);
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

    private void join(UserGameCommand command, Session session) throws Exception {
        connections.add(session);
        GameMessage message = new GameMessage(command.getGameID().toString());
        connections.send(session, message);

        User authUser = dataAccess.getAuth(command.getAuthToken());
        String notifMessage = String.format("%s joined the game", authUser.username(), command.getGameID());
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.JOIN, notifMessage);
        connections.broadcast(session, notification);
    }

    private void exit(String name, Session session) throws Exception {
        String message = String.format("%s left the chess tables", name);
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.DISCONNECT, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }
}