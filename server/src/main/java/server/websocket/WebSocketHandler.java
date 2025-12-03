package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import datamodel.Game;
import datamodel.User;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.ArrayList;

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
                case MAKE_MOVE -> move(command, ctx.session);
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
        User authUser = dataAccess.getAuth(command.getAuthToken());
        Game game = null;
        if (checkAuth(authUser, session)) {
            return;
        }
        ArrayList<Game> gameList = dataAccess.listGame(command.getAuthToken());
        if (gameList == null || gameList.size() < command.getGameID()) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the game. Please try again!");
            connections.send(session, errorMessage);
            return;
        } else {
            for (Game gameItem : gameList) {
                if (gameItem.gameID() == command.getGameID());
                {
                    game = gameItem;
                }
            }
            if (game == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the game. Please try again!");
                connections.send(session, errorMessage);
                return;
            }
        }


        connections.add(session);
        LoadGameMessage message = new LoadGameMessage(command.getGameID().toString());
        connections.send(session, message);



        String notifMessage = String.format("%s joined the game", authUser.username(), command.getGameID());
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.JOIN, notifMessage);
        connections.broadcast(session, notification);
    }

    private void move(UserGameCommand command, Session session) throws Exception {
        User authUser = dataAccess.getAuth(command.getAuthToken());
        Game game = null;
        if (checkAuth(authUser, session)) {
            return;
        }
    }

    private void exit(String name, Session session) throws Exception {
        String message = String.format("%s left the chess tables", name);
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.DISCONNECT, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }

    private Boolean checkAuth(User authUser, Session session) throws Exception {
        if (authUser == null) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the user. Please try again, or reload!");
            connections.send(session, errorMessage);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}