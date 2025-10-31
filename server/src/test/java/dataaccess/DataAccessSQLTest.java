package dataaccess;

import chess.ChessGame;
import datamodel.Game;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


        assertNull(da.getUser(user.username()));
        da.createUser(user);
        assertNotNull(da.getUser(user.username()));
        da.clearUsers();
        da.clearGames();
        assertNull(da.getUser(user.username()));
    }

    @Test
    void clearNegative() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var missingUser = new User("Bilbo", "ofTheShire", "bilbo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertNull(da.getUser(user.username()));
        da.createUser(user);
        assertNotNull(da.getUser(user.username()));
        assertNull(da.getUser(missingUser.username()));
        da.clearUsers();
        da.clearGames();
        assertNull(da.getUser(user.username()));
        assertNull(da.getUser(missingUser.username()));

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
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);
        assertEquals(user.username(), response.username());
        assertEquals(String.class, response.authToken().getClass());
        assertEquals(user.password(), da.getUser(user.username()).password());
        assertEquals(user.email(), da.getUser(user.username()).email());
    }

    @Test
    void createUserNegative() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
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
            msa.createUser(user);
        });
    }

    @Test
    void getUserPositive() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();
        da.createUser(user);

        User foundUser = da.getUser(user.username());
        assertNotNull(foundUser);
        assertEquals(User.class, foundUser.getClass());
        assertEquals(user.username(), foundUser.username());
        assertEquals(user.password(), foundUser.password());
        assertEquals(user.email(), foundUser.email());
    }

    @Test
    void getUserNegative() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
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
            msa.getUser(user.username());
        });
    }

    @Test
    void getAuthPositive() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);
        String authToken = response.authToken();

        User foundUser = da.getUser(user.username());
        assertNotNull(foundUser);
        assertEquals(User.class, foundUser.getClass());
        assertEquals(user.username(), foundUser.username());
        assertEquals(user.password(), foundUser.password());
        assertEquals(user.email(), foundUser.email());
    }

    @Test
    void getAuthNegative() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
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
            msa.getAuth(user.username());
        });
    }

    @Test
    void authUserPositive() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();
        da.createUser(user);

        LoginResult authUser = da.authUser(user);
        assertNotNull(authUser);
        assertEquals(LoginResult.class, authUser.getClass());
        assertEquals(user.username(), authUser.username());
        assertEquals(String.class, authUser.authToken().getClass());
    }

    @Test
    void authUserNegative() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
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
            msa.authUser(user);
        });
    }

    @Test
    void deleteUserPositive() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);

        assertTrue(da.deleteUser(response.authToken()));
    }

    @Test
    void deleteUserNegative() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);

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
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();

        Game response = da.createGame("YouHaveMySword");
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
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();

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
        var userWhite = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlack = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();

        RegistrationResult resWhite = da.createUser(userWhite);
        RegistrationResult resBlack = da.createUser(userWhite);
        Game response = da.createGame("YouHaveMySword");

        ArrayList<Game> list = da.listGame(resWhite.authToken());
        Game onlyGame = list.getFirst();
        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());

        da.joinGame(userWhite, response, "WHITE");
        list = da.listGame(resWhite.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userWhite.username(), onlyGame.whiteUsername());

        da.joinGame(userBlack, response, "BLACK");
        list = da.listGame(resBlack.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userBlack.username(), onlyGame.blackUsername());


    }

    @Test
    void joinGameNegative() {
        var userWhite = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlack = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();

        RegistrationResult resWhite = da.createUser(userWhite);
        RegistrationResult resBlack = da.createUser(userWhite);
        Game response = da.createGame("YouHaveMySword");

        ArrayList<Game> list = da.listGame(resWhite.authToken());
        Game onlyGame = list.getFirst();
        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());

        da.joinGame(userWhite, response, "WHITE");
        list = da.listGame(resWhite.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userWhite.username(), onlyGame.whiteUsername());

        da.joinGame(userBlack, response, "BLACK");
        list = da.listGame(resBlack.authToken());
        onlyGame = list.getFirst();

        assertNotNull(onlyGame);
        assertEquals(Game.class, onlyGame.getClass());
        assertEquals("YouHaveMySword", onlyGame.gameName());
        assertEquals(userBlack.username(), onlyGame.blackUsername());

        assertThrows(RuntimeException.class, () -> {
            MySqlDataAccess msa = new MySqlDataAccess() {
                @Override
                public void joinGame(User u, Game g, String c) {
                    throw new RuntimeException("Server is down");
                }
            };
            msa.joinGame(userWhite, response, "WHITE");
        });
    }

    @Test
    void listGamePositive() {
        var userWhite = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlack = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();

        RegistrationResult resWhite = da.createUser(userWhite);
        RegistrationResult resBlack = da.createUser(userWhite);

        ArrayList<Game> list = da.listGame(resWhite.authToken());
        assertTrue(list.isEmpty());

        Game response = da.createGame("YouHaveMySword");
        da.joinGame(userWhite, response, "WHITE");

        list = da.listGame(resWhite.authToken());
        Game game = list.getFirst();
        assertNotNull(game);
        assertEquals(Game.class, game.getClass());
        assertEquals("YouHaveMySword", game.gameName());
        assertEquals(userWhite.username(), game.whiteUsername());

        response = da.createGame("DoYouHaveTheRing");
        da.joinGame(userBlack, response, "BLACK");

        list = da.listGame(resWhite.authToken());
        game = list.get(1);
        assertNotNull(game);
        assertEquals(Game.class, game.getClass());
        assertEquals("DoYouHaveTheRing", game.gameName());
        assertEquals(userBlack.username(), game.blackUsername());
    }

    @Test
    void listGameNegative() {
        var userWhite = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = null;
        try {
            da = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        da.clearUsers();
        da.clearGames();

        RegistrationResult resWhite = da.createUser(userWhite);

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