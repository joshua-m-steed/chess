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
    public void clearUsers() {
        usersByName.clear();
        usersByAuth.clear();
    }

    @Override
    public void clearGames() {
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
    public User getAuth(String authToken) {
        return usersByAuth.get(authToken);
    }

    @Override
    public LoginResult authUser(User user) {
        String authToken = generateAuthToken();
        usersByAuth.put(authToken, user);
        return new LoginResult(user.username(), authToken);
    }

    @Override
    public boolean deleteUser(String authToken) {
//        User user = usersByAuth.get(authToken);
//        usersByName.remove(user.username());
        User removedUser = usersByAuth.remove(authToken);
        return removedUser != null;
    }

    @Override
    public ArrayList<Game> listGame(String authToken) {
        User authUser = usersByAuth.get(authToken);

        if(authUser == null) {
            return null;
        }

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
