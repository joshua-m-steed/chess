package dataaccess;

import chess.ChessGame;
import datamodel.Game;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessSQLTest {
    @BeforeEach
    void freshTable() {
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();
    }

    @Test
    void clearPositive() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


        assertNull(sqlda.getUser(userSQL.username()));
        sqlda.createUser(userSQL);
        assertNotNull(sqlda.getUser(userSQL.username()));
        sqlda.clearUsers();
        sqlda.clearGames();
        assertNull(sqlda.getUser(userSQL.username()));
    }

    @Test
    void clearNegative() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var missingUserSQL = new User("Bilbo", "ofTheShire", "bilbo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertNull(sqlda.getUser(userSQL.username()));
        sqlda.createUser(userSQL);
        assertNotNull(sqlda.getUser(userSQL.username()));
        assertNull(sqlda.getUser(missingUserSQL.username()));
        sqlda.clearUsers();
        sqlda.clearGames();
        assertNull(sqlda.getUser(userSQL.username()));
        assertNull(sqlda.getUser(missingUserSQL.username()));

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public void clearUsers() {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.clearUsers();
        });
    }

    @Test
    void createUserPositive() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();
        RegistrationResult response = sqlda.createUser(userSQL);
        assertEquals(userSQL.username(), response.username());
        assertEquals(String.class, response.authToken().getClass());
        assertEquals(userSQL.password(), sqlda.getUser(userSQL.username()).password());
        assertEquals(userSQL.email(), sqlda.getUser(userSQL.username()).email());
    }

    @Test
    void createUserNegative() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public RegistrationResult createUser(User u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.createUser(userSQL);
        });
    }

    @Test
    void getUserPositive() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();
        sqlda.createUser(userSQL);

        User foundUser = sqlda.getUser(userSQL.username());
        assertNotNull(foundUser);
        assertEquals(User.class, foundUser.getClass());
        assertEquals(userSQL.username(), foundUser.username());
        assertEquals(userSQL.password(), foundUser.password());
        assertEquals(userSQL.email(), foundUser.email());
    }

    @Test
    void getUserNegative() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public User getUser(String u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.getUser(userSQL.username());
        });
    }

    @Test
    void getAuthPositive() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        sqlda.clearUsers();
        sqlda.clearGames();
        RegistrationResult response = sqlda.createUser(userSQL);
        String authToken = response.authToken();

        User foundUser = sqlda.getUser(userSQL.username());
        assertNotNull(foundUser);
        assertEquals(User.class, foundUser.getClass());
        assertEquals(userSQL.username(), foundUser.username());
        assertEquals(userSQL.password(), foundUser.password());
        assertEquals(userSQL.email(), foundUser.email());
    }

    @Test
    void getAuthNegative() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public User getAuth(String u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.getAuth(userSQL.username());
        });
    }

    @Test
    void authUserPositive() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();
        sqlda.createUser(userSQL);

        LoginResult authUser = sqlda.authUser(userSQL);
        assertNotNull(authUser);
        assertEquals(LoginResult.class, authUser.getClass());
        assertEquals(userSQL.username(), authUser.username());
        assertEquals(String.class, authUser.authToken().getClass());
    }

    @Test
    void authUserNegative() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public LoginResult authUser(User u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.authUser(userSQL);
        });
    }

    @Test
    void deleteUserPositive() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();
        RegistrationResult response = sqlda.createUser(userSQL);

        assertTrue(sqlda.deleteUser(response.authToken()));
    }

    @Test
    void deleteUserNegative() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        sqlda.clearUsers();
        sqlda.clearGames();
        RegistrationResult response = sqlda.createUser(userSQL);

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public boolean deleteUser(String u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.deleteUser(response.authToken());
        });
    }

    @Test
    void createGamePositive() {
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();

        Game response = sqlda.createGame("YouHaveMySword");
        assertNotNull(response);
        assertEquals(Game.class, response.getClass());
        assertEquals(Integer.class, response.gameID().getClass());
        assertEquals(ChessGame.class, response.game().getClass());
        assertNull(response.whiteUsername());
        assertNull(response.blackUsername());
        assertEquals("YouHaveMySword", response.gameName());
    }

    @Test
    void createGameNegative() {
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public Game createGame(String u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.createGame("You Have My Sword");
        });
    }

    @Test
    void joinGamePositive() {
        var userWhiteSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlackSQL = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();

        RegistrationResult resWhite = sqlda.createUser(userWhiteSQL);
        RegistrationResult resBlack = sqlda.createUser(userWhiteSQL);
        Game response = sqlda.createGame("YouHaveMySword");

        ArrayList<Game> list = sqlda.listGame(resWhite.authToken());
        Game onlyGame = list.getFirst();
        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());

        sqlda.joinGame(userWhiteSQL, response, "WHITE");
        list = sqlda.listGame(resWhite.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userWhiteSQL.username(), onlyGame.whiteUsername());

        sqlda.joinGame(userBlackSQL, response, "BLACK");
        list = sqlda.listGame(resBlack.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userBlackSQL.username(), onlyGame.blackUsername());


    }

    @Test
    void joinGameNegative() {
        var userWhiteSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlackSQL = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();

        RegistrationResult resWhite = sqlda.createUser(userWhiteSQL);
        RegistrationResult resBlack = sqlda.createUser(userWhiteSQL);
        Game response = sqlda.createGame("YouHaveMySword");

        ArrayList<Game> list = sqlda.listGame(resWhite.authToken());
        Game onlyGame = list.getFirst();
        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());

        sqlda.joinGame(userWhiteSQL, response, "WHITE");
        list = sqlda.listGame(resWhite.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userWhiteSQL.username(), onlyGame.whiteUsername());

        sqlda.joinGame(userBlackSQL, response, "BLACK");
        list = sqlda.listGame(resBlack.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userBlackSQL.username(), onlyGame.blackUsername());

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public void joinGame(User u, Game g, String c) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.joinGame(userWhiteSQL, response, "WHITE");
        });
    }

    @Test
    void listGamePositive() {
        var userWhiteSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlackSQL = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();

        RegistrationResult resWhite = sqlda.createUser(userWhiteSQL);
        RegistrationResult resBlack = sqlda.createUser(userWhiteSQL);

        ArrayList<Game> list = sqlda.listGame(resWhite.authToken());
        assertTrue(list.isEmpty());

        Game response = sqlda.createGame("YouHaveMySword");
        sqlda.joinGame(userWhiteSQL, response, "WHITE");

        list = sqlda.listGame(resWhite.authToken());
        Game game = list.getFirst();
        assertNotNull(game);
        assertEquals(Game.class, game.getClass());
        assertEquals("YouHaveMySword", game.gameName());
        assertEquals(userWhiteSQL.username(), game.whiteUsername());

        response = sqlda.createGame("DoYouHaveTheRing");
        sqlda.joinGame(userBlackSQL, response, "BLACK");

        list = sqlda.listGame(resWhite.authToken());
        game = list.get(1);
        assertNotNull(game);
        assertEquals(Game.class, game.getClass());
        assertEquals("DoYouHaveTheRing", game.gameName());
        assertEquals(userBlackSQL.username(), game.blackUsername());
    }

    @Test
    void listGameNegative() {
        var userWhiteSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess sqlda = null;
        try {
            sqlda = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        sqlda.clearUsers();
        sqlda.clearGames();

        RegistrationResult resWhite = sqlda.createUser(userWhiteSQL);

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public ArrayList<Game> listGame(String u) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.listGame(resWhite.authToken());
        });
    }
}