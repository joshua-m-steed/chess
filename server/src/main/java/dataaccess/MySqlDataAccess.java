package dataaccess;

import datamodel.Game;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clearUsers() {

    }

    @Override
    public void clearGames() {

    }

    @Override
    public RegistrationResult createUser(User user) {
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public User getAuth(String authToken) {
        return null;
    }

    @Override
    public LoginResult authUser(User user) {
        return null;
    }

    @Override
    public boolean deleteUser(String authToken) {
        return false;
    }

    @Override
    public ArrayList<Game> listGame(String authToken) {
        return null;
    }

    @Override
    public Game createGame(String gameName) {
        return null;
    }

    @Override
    public void joinGame(User authUser, Game targetGame, String s) {

    }

    private final String[] createStatments = {

    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
//            for (String statement : createStatments);
            System.out.println("MADE IT");
//                try (var prepared)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}