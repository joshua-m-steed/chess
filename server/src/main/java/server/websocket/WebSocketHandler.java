package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import datamodel.Game;
import datamodel.User;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
            Gson gson = new Gson();
            UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);

            switch ((command.getCommandType())) {
                case CONNECT -> join(command, ctx.session);
                case MAKE_MOVE -> move(gson.fromJson(ctx.message(), MakeMoveCommand.class), ctx.session);
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
        LoadGameMessage loadGame = new LoadGameMessage(game.game());
        connections.send(session, loadGame);



        String notifMessage = String.format("%s joined the game", authUser.username(), command.getGameID());
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.JOIN, notifMessage);
        connections.broadcast(session, notification);
    }

    private void move(MakeMoveCommand command, Session session) throws Exception {
        User authUser = dataAccess.getAuth(command.getAuthToken());
        Game game = null;
        ChessMove move = command.getMove();
        if (checkAuth(authUser, session)) {
            return;
        }

        // Verify GameID
        ArrayList<Game> gameList = dataAccess.listGame(command.getAuthToken());
        if (gameList == null || gameList.size() < command.getGameID()) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the game. Please try again!");
            connections.send(session, errorMessage);
            return;
        } else {
            for (Game gameItem : gameList) {
                if (gameItem.gameID() == command.getGameID()) {
                    game = gameItem;
                }
            }
            if (game == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the game. Please try again!");
                connections.send(session, errorMessage);
                return;
            }
        }


        ChessGame chessGame = game.game();
        ChessBoard board = chessGame.getBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());
        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, move.getStartPosition());

        // Check Game Concluded
        if (chessGame.getWinCondition() != ChessGame.WinCondition.IN_PLAY) {
            ErrorMessage errorMessage = new ErrorMessage("Error: The game has ended.");
            connections.send(session, errorMessage);
            return;
        }

        // Check Observer
        if (!Objects.equals(authUser.username(), game.whiteUsername()) && !Objects.equals(authUser.username(), game.blackUsername())) {
            ErrorMessage errorMessage = new ErrorMessage("Error: You are currently observing the game and can't move pieces.");
            connections.send(session, errorMessage);
            return;
        }

        // Check Wrong Turn
        if (authUser.username().equals(game.whiteUsername()) && chessGame.getTeamTurn() != ChessGame.TeamColor.WHITE) {
            ErrorMessage errorMessage = new ErrorMessage("Error: It is not your turn to move just yet.");
            connections.send(session, errorMessage);
            return;
        } else if (authUser.username().equals(game.blackUsername()) && chessGame.getTeamTurn() != ChessGame.TeamColor.BLACK) {
            ErrorMessage errorMessage = new ErrorMessage("Error: It is not your turn to move just yet.");
            connections.send(session, errorMessage);
            return;
        }

        // Check Move Opponent piece
        if (authUser.username().equals(game.blackUsername()) && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ErrorMessage errorMessage = new ErrorMessage("Error: You aren't allowed to move that piece!");
            connections.send(session, errorMessage);
            return;
        } else if (authUser.username().equals(game.whiteUsername()) && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ErrorMessage errorMessage = new ErrorMessage("Error: You aren't allowed to move that piece!");
            connections.send(session, errorMessage);
            return;
        }

        // Check Move for Validity
        if (!pieceMoves.contains(move)) {
            ErrorMessage errorMessage = new ErrorMessage("Error: This is not a valid move!");
            connections.send(session, errorMessage);
            return;
        }

        chessGame.makeMove(move);

        LoadGameMessage loadGame = new LoadGameMessage(chessGame);
        connections.send(session, loadGame);
        connections.broadcast(session, loadGame);

        String message = String.format("%s moved their %s from %s to %s.",
                authUser.username(),
                piece.getPieceType(),
                move.getStartPosition(),
                move.getEndPosition());
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.Type.MOVE, message);
        connections.broadcast(session, notificationMessage);
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