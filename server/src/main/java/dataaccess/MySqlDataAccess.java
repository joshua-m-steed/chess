package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import datamodel.Game;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clearUsers() {
        try {
            String userStatement = "TRUNCATE TABLE user";
            executeUpdate(userStatement);
            String authStatement = "TRUNCATE TABLE auth";
            executeUpdate(authStatement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearGames() {

    }

    @Override
    public RegistrationResult createUser(User user) {
        String authToken = generateAuthToken();
        try {
            String userStatement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            executeUpdate(userStatement, user.username(), user.password(), user.email());
            String authStatement = "INSERT INTO auth (username, authkey) VALUES (?, ?)";
            executeUpdate(authStatement, user.username(), authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new RegistrationResult(user.username(), authToken);
    }

    @Override
    public User getUser(String username) {
        User foundUser = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            String userStatement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(userStatement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        foundUser = new User(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email")
                        );
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return foundUser;
    }

    @Override
    public User getAuth(String authToken) {
        User foundUser = null;
        String username = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            String authStatement = "SELECT username, authkey FROM auth WHERE authkey=?";
            try (PreparedStatement ps = conn.prepareStatement(authStatement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        foundUser = getUser(username);

        return foundUser;
    }

    @Override
    public LoginResult authUser(User user) {
        String authToken = generateAuthToken();
        String authStatement = "INSERT INTO auth (username, authkey) VALUES (?, ?)";
        try {
            executeUpdate(authStatement, user.username(), authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new LoginResult(user.username(), authToken);
    }

    @Override
    public boolean deleteUser(String authToken) {
        boolean deleted = false;
        try (Connection conn = DatabaseManager.getConnection()) {
            String delStatement = "DELETE FROM auth WHERE authkey=?";
            try (PreparedStatement ps = conn.prepareStatement(delStatement)) {
                ps.setString(1, authToken);
                int interacted = ps.executeUpdate();
                if(interacted > 0) {
                    deleted = true;
                }
            }
            executeUpdate(delStatement, authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return deleted;
    }

    @Override
    public ArrayList<Game> listGame(String authToken) {
        User authUser = getAuth(authToken);
        if(authUser == null) {
            return null;
        }

        ArrayList<Game> gameList = new ArrayList<>();
        var serializer = new Gson();

        try (Connection conn = DatabaseManager.getConnection()) {
            String listStatement = "SELECT * FROM game";
            try (PreparedStatement ps = conn.prepareStatement(listStatement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameList.add(
                                new Game(rs.getInt("gameID"),
                                        rs.getString("whiteUser"),
                                        rs.getString("blackUser"),
                                        rs.getString("gameName"),
                                        serializer.fromJson(rs.getString("game"), ChessGame.class)
                                )
                        );
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gameList;
    }

    @Override
    public Game createGame(String gameName) {
        Integer gameID = null;
        var serializer = new Gson();
        String gameText = serializer.toJson(new ChessGame(), ChessGame.class);

        String createStatement = "INSERT INTO game (gameName, game) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(createStatement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameName);
                ps.setString(2, gameText);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        gameID = keys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ChessGame game = serializer.fromJson(gameText, ChessGame.class);

        return new Game(gameID, null, null, gameName, game);
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
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
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
               `gameID` int NOT NULL AUTO_INCREMENT,
               `whiteUser` varchar(256) DEFAULT NULL,
               `blackUser` varchar(256) DEFAULT NULL,
               `gameName` varchar(256) NOT NULL,
               `game` TEXT NOT NULL,
               PRIMARY KEY (`gameID`),
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
