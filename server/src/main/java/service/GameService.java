package service;

import dataaccess.DataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;

import java.util.ArrayList;
import java.util.Map;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public GameListResult gameList(String authToken) {
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

    public GameJoinResult joinGame(JoinGameRequest gameRequest) throws BadRequestResponse {
        if(gameRequest.playerColor() == null || gameRequest.playerColor().isBlank()) {
            throw new BadRequestResponse("Error: bad request");
        } else if (!gameRequest.playerColor().equals("WHITE") && !gameRequest.playerColor().equals("BLACK")) {
            throw new BadRequestResponse("Error: bad request");
        }

        ArrayList<Game> games = this.dataAccess.listGame("Hobbits");
        Game targetGame = null;
        for(Game game : games) {
            if(game.gameID() == gameRequest.gameID()) {
                targetGame = game;
                break;
            }
        }

        if(targetGame == null) {
            throw new BadRequestResponse("Error: bad request");
        }

        return new GameJoinResult();




    }

    public void clear() {
        this.dataAccess.clearGames();
    }
}
