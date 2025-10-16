package dataaccess;

import datamodel.User;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{

    private final HashMap<String, User> users = new HashMap<>();

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
}
