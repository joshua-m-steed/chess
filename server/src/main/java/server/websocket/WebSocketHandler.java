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
            switch (command.getCommandType()) {
                case CONNECT -> join(command, ctx.session);
                case MAKE_MOVE -> move(gson.fromJson(ctx.message(), MakeMoveCommand.class), ctx.session);
                case LEAVE -> leave(command, ctx.session);
                case RESIGN -> resign(command, ctx.session);
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
        Game game = checkInputErrors(command, session, authUser);
        if (game == null) {
            return;
        }


        connections.add(session, game.gameID());
        LoadGameMessage loadGame = new LoadGameMessage(game.game());
        connections.send(session, loadGame);

        String notifMessage;
        if(!Objects.equals(authUser.username(), game.whiteUsername()) && !Objects.equals(authUser.username(), game.blackUsername())) {
            notifMessage = String.format("%s is watching the game", authUser.username());
        } else {
            if (authUser.username().equals(game.whiteUsername())) {
                notifMessage = String.format("%s joined the game as %s", authUser.username(), "WHITE");
            } else {
                notifMessage = String.format("%s joined the game as %s", authUser.username(), "BLACK");
            }
        }


        NotificationMessage notification = new NotificationMessage(NotificationMessage.NotificationType.JOIN, notifMessage);
        connections.broadcast(session, notification, game.gameID());
    }

    private void move(MakeMoveCommand command, Session session) throws Exception {
        User authUser = dataAccess.getAuth(command.getAuthToken());
        Game game = checkInputErrors(command, session, authUser);
        if (game == null) {return;}
        ChessMove move = command.getMove();
        ChessGame chessGame = game.game();
        ChessBoard board = chessGame.getBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null) {
            ErrorMessage errorMessage = new ErrorMessage("Error: There's no piece there. Try again.");
            connections.send(session, errorMessage);
            return;
        }

        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, move.getStartPosition());

        if (chessGame.getWinCondition() != ChessGame.WinCondition.IN_PLAY) {
            ErrorMessage errorMessage = new ErrorMessage("Error: The game has ended.");
            connections.send(session, errorMessage);
            return;
        }
        if (!Objects.equals(authUser.username(), game.whiteUsername()) &&
                !Objects.equals(authUser.username(), game.blackUsername())) {
            ErrorMessage errorMessage = new ErrorMessage("Error: You are currently observing the game and can't move pieces.");
            connections.send(session, errorMessage);
            return;
        }
        if (authUser.username().equals(game.whiteUsername()) && chessGame.getTeamTurn() != ChessGame.TeamColor.WHITE) {
            ErrorMessage errorMessage = new ErrorMessage("Error: It is not your turn to move just yet.");
            connections.send(session, errorMessage);
            return;
        } else if (authUser.username().equals(game.blackUsername()) && chessGame.getTeamTurn() != ChessGame.TeamColor.BLACK) {
            ErrorMessage errorMessage = new ErrorMessage("Error: It is not your turn to move just yet.");
            connections.send(session, errorMessage);
            return;
        }
        if (authUser.username().equals(game.blackUsername()) && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ErrorMessage errorMessage = new ErrorMessage("Error: You aren't allowed to move that piece!");
            connections.send(session, errorMessage);
            return;
        } else if (authUser.username().equals(game.whiteUsername()) && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ErrorMessage errorMessage = new ErrorMessage("Error: You aren't allowed to move that piece!");
            connections.send(session, errorMessage);
            return;
        }
        if (!pieceMoves.contains(move)) {
            ErrorMessage errorMessage = new ErrorMessage("Error: This is not a valid move!");
            connections.send(session, errorMessage);
            return;
        }

        chessGame.makeMove(move);
        chessGame.updateGameState();
        dataAccess.updateGame(game.gameID(), chessGame);
        LoadGameMessage loadGame = new LoadGameMessage(chessGame);
        connections.send(session, loadGame);
        connections.broadcast(session, loadGame, game.gameID());

        String message = String.format("%s moved their %s from %s to %s.",
                authUser.username(),
                piece.getPieceType(),
                move.getStartPosition(),
                move.getEndPosition());
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.NotificationType.MOVE, message);
        connections.broadcast(session, notificationMessage, game.gameID());
        if (chessGame.getWinCondition() == ChessGame.WinCondition.CHECKMATE) {
            if (chessGame.getWhiteState() == ChessGame.PlayerState.WON) {
                NotificationMessage checkmateMsg = new NotificationMessage(NotificationMessage.NotificationType.CHECKMATE,
                        authUser.username() + " [WHITE] placed " + game.blackUsername() + " in CHECKMATE!");
                connections.send(session, checkmateMsg);
                connections.broadcast(session, checkmateMsg, game.gameID());
            } else if (chessGame.getBlackState() == ChessGame.PlayerState.WON) {
                NotificationMessage checkmateMsg = new NotificationMessage(NotificationMessage.NotificationType.CHECKMATE,
                        authUser.username() + " [WHITE] placed " + game.whiteUsername() + " in CHECKMATE!");
                connections.send(session, checkmateMsg);
                connections.broadcast(session, checkmateMsg, game.gameID());
            }
            return;
        }
        if(chessGame.getWinCondition() == ChessGame.WinCondition.STALEMATE) {
            NotificationMessage staleMsg = new NotificationMessage(NotificationMessage.NotificationType.STALEMATE,
                    "Both " + game.whiteUsername() + " and " + game.blackUsername() + " have ended in a stalemate!");
            connections.send(session, staleMsg);
            connections.broadcast(session, staleMsg, game.gameID());
            return;
        }
        if (chessGame.getWhiteState() == ChessGame.PlayerState.IN_CHECK) {
            NotificationMessage checkMsg = new NotificationMessage(NotificationMessage.NotificationType.CHECK,
                    authUser.username() + " [WHITE] is in check!");
            connections.send(session, checkMsg);
            connections.broadcast(session, checkMsg, game.gameID());
        } else if (chessGame.getBlackState() == ChessGame.PlayerState.IN_CHECK) {
            NotificationMessage checkMsg = new NotificationMessage(NotificationMessage.NotificationType.CHECK,
                    authUser.username() + " [BLACK] is in check!");
            connections.send(session, checkMsg);
            connections.broadcast(session, checkMsg, game.gameID());
        }
    }

    private void resign(UserGameCommand command, Session session) throws Exception {
        User authUser = dataAccess.getAuth(command.getAuthToken());
        Game game = checkInputErrors(command, session, authUser);
        if (game == null) {
            return;
        }

        ChessGame chessGame = game.game();
        if (chessGame.getWinCondition() == ChessGame.WinCondition.RESIGN) {
            ErrorMessage errorMessage = new ErrorMessage("Error: A player has already resigned!");
            connections.send(session, errorMessage);
            return;
        }

        if (authUser.username().equals(game.whiteUsername())) {
            chessGame.resign(ChessGame.TeamColor.WHITE);
            String message = String.format("%s has resigned from the game! %s wins by default!",
                    game.whiteUsername(),
                    game.blackUsername());
            NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.NotificationType.RESIGN, message);
            connections.broadcast(session, notificationMessage, game.gameID());
            connections.send(session, notificationMessage);
        } else if (authUser.username().equals(game.blackUsername())) {
            chessGame.resign(ChessGame.TeamColor.BLACK);
            String message = String.format("%s has resigned from the game! %s wins by default!",
                    game.blackUsername(),
                    game.whiteUsername());
            NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.NotificationType.RESIGN, message);
            connections.broadcast(session, notificationMessage, game.gameID());
            connections.send(session, notificationMessage);
        } else {
            ErrorMessage errorMessage = new ErrorMessage("Error: You can't resign as an Observer.");
            connections.send(session, errorMessage);
        }

        dataAccess.updateGame(game.gameID(), chessGame);
    }

    private void leave(UserGameCommand command, Session session) throws Exception {
        User authUser = dataAccess.getAuth(command.getAuthToken());
        Game game = checkInputErrors(command, session, authUser);
        if (game == null) {
            return;
        }

        String message;
        if (game.game().getWinCondition() != ChessGame.WinCondition.IN_PLAY) {
            if (authUser.username().equals(game.whiteUsername())) {
                message = String.format("%s [Team White] left the chess table", authUser.username());
            } else if (authUser.username().equals(game.blackUsername())) {
                message = String.format("%s [Team Black] left the chess table", authUser.username());
            } else {
                message = String.format("%s [Observer] left the chess table", authUser.username());
            }
        } else {
            if (authUser.username().equals(game.whiteUsername())) {
                dataAccess.updateGameUser(game.gameID(), ChessGame.TeamColor.WHITE, game.game());
                message = String.format("%s [Team White] left the chess table", authUser.username());
            } else if (authUser.username().equals(game.blackUsername())) {
                dataAccess.updateGameUser(game.gameID(), ChessGame.TeamColor.BLACK, game.game());
                message = String.format("%s [Team Black] left the chess table", authUser.username());
            } else {
                message = String.format("%s [Observer] left the chess table", authUser.username());
            }
        }


        NotificationMessage notification = new NotificationMessage(NotificationMessage.NotificationType.LEAVE, message);

        connections.broadcast(session, notification, game.gameID());
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

    private Game grabGame(UserGameCommand command, ArrayList<Game> gameList) {
        Game game = null;
        for (Game gameItem : gameList) {
            if (gameItem.gameID().equals(command.getGameID())) {
                game = gameItem;
            }
        }
        return game;
    }

    private Game checkInputErrors(UserGameCommand command, Session session, User authUser) throws Exception {
        if (checkAuth(authUser, session)) {
            return null;
        }

        // Verify GameID
        Game game = null;
        ArrayList<Game> gameList = dataAccess.listGame(command.getAuthToken());
        if (gameList == null || gameList.size() < command.getGameID()) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the game. Please try again!");
            connections.send(session, errorMessage);
            return game;
        } else {
            game = grabGame(command, gameList);
            if (game == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Could not find the game. Please try again!");
                connections.send(session, errorMessage);
                return game;
            }
        }
        return game;
    }
}