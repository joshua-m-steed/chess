package service;

import dataaccess.DataAccess;
import datamodel.*;

import java.util.ArrayList;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public GameListResult gameList() {
        ArrayList<Game> games = new ArrayList<>();
//        games.add(new Game(5555, "MiniJosh", "Frodo", "Uhhh", null));
//        games.add(new Game(2, "Me", "Myself", "AndI", null));
        return new GameListResult(games);
    }

    public GameCreateResult createGame(String gameName) {
        Game game = this.dataAccess.createGame(gameName);
        return new GameCreateResult(game.gameID());
    }
}
