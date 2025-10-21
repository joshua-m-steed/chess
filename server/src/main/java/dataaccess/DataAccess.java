package dataaccess;

import datamodel.User;
import datamodel.Game;

import java.util.ArrayList;

public interface DataAccess {
        void clear();
        void createUser(User user);
        User getUser(String username);
        void deleteUser(String authToken);
        ArrayList<Game> listGame(String authToken);
        Game createGame(String gameName);
        void joinGame(String userName, int gameID);
}
