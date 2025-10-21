package dataaccess;

import chess.ChessGame;
import datamodel.Game;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess{

    // Idea? Make the usersByAuth using authData?
    private final HashMap<String, User> usersByName = new HashMap<>();
    private final HashMap<String, User> usersByAuth = new HashMap<>();
    private final HashMap<Integer, Game> games = new HashMap<>();

    @Override
    public void clear() {
        usersByName.clear();
        usersByAuth.clear();
        games.clear();
    }

    @Override
    public RegistrationResult createUser(User user) {
        usersByName.put(user.username(), user);
        String authToken = generateAuthToken();
        usersByAuth.put(authToken, user);
        return new RegistrationResult(user.username(), authToken);
    }

    @Override
    public User getUser(String username) {
        return usersByName.get(username);
    }

    @Override
    public LoginResult authUser(User user) {
        String authToken = generateAuthToken();
        usersByAuth.put(authToken, user);
        return new LoginResult(user.username(), authToken);
    }

    @Override
    public void deleteUser(String authToken) {
        User user = usersByAuth.get(authToken);
        usersByName.remove(user.username());
        usersByAuth.remove(authToken);
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

    @Override
    public void joinGame(String userName, int gameID) {

    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
