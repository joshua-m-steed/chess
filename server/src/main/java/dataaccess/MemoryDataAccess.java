package dataaccess;

import datamodel.User;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{

    final private HashMap<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        users.put(user.username(), user);
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }
}
