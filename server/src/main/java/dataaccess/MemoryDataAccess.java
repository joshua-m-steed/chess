package dataaccess;

import chess.ChessGame;
import datamodel.Game;
import datamodel.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{

    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Integer, Game> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(User user) {
        users.put(user.username(), user);
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void deleteUser(String authToken) {
        System.out.println(users);
        users.remove(authToken);
    }

    @Override
    public ArrayList<Game> listGame(String authToken) {
        ArrayList<Game> gameList = new ArrayList<>();
        for(Map.Entry<Integer, Game> game : games.entrySet())
            gameList.add(game.getValue());
        return gameList;
    }

    @Override
    public Game createGame(String gameName) {
        Game newGame = new Game(1234, "", "", gameName, new ChessGame());
        games.put(newGame.gameID(), newGame);
        return newGame;
    }
}
