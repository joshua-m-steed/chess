package dataaccess;

import datamodel.Game;
import datamodel.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{

    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<String, Game> games = new HashMap<>();

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
        users.remove(authToken);
    }

    @Override
    public ArrayList<Game> listGame(String authToken) {
        ArrayList<Game> gameList = new ArrayList<>();
        for(Map.Entry<String, Game> game : games.entrySet())
            gameList.add(game.getValue());
        return gameList;
    }
}
