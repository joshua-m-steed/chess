package dataaccess;

import chess.ChessGame;
import datamodel.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {
    @Test
    void clear() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        assertNull(da.getUser(user.username()));
        da.createUser(user);
        assertNotNull(da.getUser(user.username()));
        da.clearUsers();
        da.clearGames();
        assertNull(da.getUser(user.username()));
    }

    @Test
    void createUser() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);
        assertEquals(user.username(), response.username());
        assertEquals(String.class, response.authToken().getClass());
        assertEquals(user.password(), da.getUser(user.username()).password());
        assertEquals(user.email(), da.getUser(user.username()).email());
    }

    @Test
    void getUser() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
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
    void getAuth() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
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
    void authUser() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
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
    void deleteUser() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);

        assertTrue(da.deleteUser(response.authToken()));
    }

    @Test
    void createGame() {
        DataAccess da = new MemoryDataAccess();
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
    void joinGame() {
        var userWhite = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlack = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
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
    void listGame() {
        var userWhite = new User("Frodo", "theOneRing", "frodo@baggins.com");
        var userBlack = new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
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
}