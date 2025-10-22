package dataaccess;

import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;
import datamodel.Game;

import java.util.ArrayList;

public interface DataAccess {
        void clearUsers();
        void clearGames();
        RegistrationResult createUser(User user);
        User getUser(String username);
        User getAuth(String authToken);
        LoginResult authUser(User user);
        boolean deleteUser(String authToken);
        ArrayList<Game> listGame(String authToken);
        Game createGame(String gameName);
//        void joinGame(String userName, int gameID);

        void joinGame(User authUser, Game targetGame, String s);
}
