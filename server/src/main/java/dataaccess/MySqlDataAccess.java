package dataaccess;

import datamodel.Game;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Connect with statement and return primary keys from DB / SQL
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Couldn't prepare statement string for SQL");
        }
    }

    private final String[] createStatments = {
            """
            CREATE TABLE IF NOT EXISTS  user (
               `id` int NOT NULL AUTO_INCREMENT,
               `username` varchar(256) NOT NULL,
               `password` varchar(256) NOT NULL,
               `email` varchar(256) NOT NULL,
               PRIMARY KEY (`id`),
               INDEX(`username`)
             );
            """,
            """
            CREATE TABLE IF NOT EXISTS  auth (
               `id` int NOT NULL AUTO_INCREMENT,
               `username` varchar(256) NOT NULL,
               `authkey` varchar(256) NOT NULL,
               PRIMARY KEY (`id`),
               INDEX(`username`),
               INDEX(`authkey`)
             );
            """,
            """
            CREATE TABLE IF NOT EXISTS  game (
               `id` int NOT NULL AUTO_INCREMENT,
               `gameID` int NOT NULL,
               `whiteUser` varchar(256) DEFAULT NULL,
               `blackUser` varchar(256) DEFAULT NULL,
               `gameName` varchar(256) NOT NULL,
               `game` TEXT NOT NULL,
               PRIMARY KEY (`id`),
               INDEX(`gameID`),
               INDEX(`gameName`)
             );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatments) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
