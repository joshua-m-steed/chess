package service;

import dataaccess.DataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

import java.util.ArrayList;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public GameListResult listGame(String authToken) {
        ArrayList<Game> games = this.dataAccess.listGame(authToken);
        if(games == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        return new GameListResult(games);
    }

    public GameCreateResult createGame(Game game, String authToken) {
        if(this.dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }

        if((game.gameName() == null || game.gameName().isBlank())) {
            throw new BadRequestResponse("Error: bad request");
        }

        Game newGame = this.dataAccess.createGame(game.gameName());
        return new GameCreateResult(newGame.gameID());
    }

    public GameJoinResult joinGame(JoinGameRequest gameRequest, String authToken) throws BadRequestResponse {
        User authUser = this.dataAccess.getAuth(authToken);
        // Verify AuthToken
        if(authUser == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }

        // Verify Given Data Exists
        if(gameRequest.playerColor() == null || gameRequest.playerColor().isBlank()) {
            throw new BadRequestResponse("Error: bad request");
        } else if (!gameRequest.playerColor().equals("WHITE") &&
                !gameRequest.playerColor().equals("BLACK") &&
                !gameRequest.playerColor().equals("OBSERVER")) {
            throw new BadRequestResponse("Error: bad request");
        } else if (gameRequest.gameID() == null) {
            throw new BadRequestResponse("Error: bad request");
        }

        if (gameRequest.playerColor().equals("OBSERVER")) {
            return new GameJoinResult();
        }

        ArrayList<Game> games = this.dataAccess.listGame(authToken);
        Game targetGame = null;
        for(Game game : games) {
            if(game.gameID() == gameRequest.gameID()) {
                targetGame = game;
                break;
            }
        }

        // Verify that there is a matching game
        if(targetGame == null) {
            throw new BadRequestResponse("Error: bad request");
        }

        switch (gameRequest.playerColor()) {
            case "WHITE":
                if(targetGame.whiteUsername() != null && !targetGame.whiteUsername().isBlank()) {
                    throw new ForbiddenResponse("Error: already taken");
                }
                break;
            case "BLACK":
                if(targetGame.blackUsername() != null && !targetGame.blackUsername().isBlank()) {
                    throw new ForbiddenResponse("Error: already taken");
                }
                break;
        }

        this.dataAccess.joinGame(authUser, targetGame, gameRequest.playerColor());

        return new GameJoinResult();
    }

//    public GameJoinResult observeGame(JoinGameRequest gameRequest, String authToken) throws BadRequestResponse {
//        User authUser = this.dataAccess.getAuth(authToken);
//        // Verify AuthToken
//        if (authUser == null) {
//            throw new UnauthorizedResponse("Error: unauthorized");
//        }
//
//        if (gameRequest.playerColor() == null) {
//            return new GameJoinResult();
//        } else {
//            throw new BadRequestResponse("Error: bad request");
//        }
//    }

    public void clear() {
        this.dataAccess.clearGames();
    }
}
