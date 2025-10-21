package dataaccess;

import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;
import datamodel.Game;

import java.util.ArrayList;

public interface DataAccess {
        void clear();
        RegistrationResult createUser(User user);
        User getUser(String username);
        LoginResult authUser(User user);
        void deleteUser(String authToken);
        ArrayList<Game> listGame(String authToken);
        Game createGame(String gameName);
        void joinGame(String userName, int gameID);
}
