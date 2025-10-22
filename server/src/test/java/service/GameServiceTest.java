package service;

import chess.ChessGame;
import dataaccess.MemoryDataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private String existingAuth;
    private Integer existingGameID;
    private MemoryDataAccess da;

    @BeforeEach
    public void setup() {
        User existingUser = new User("Frodo", "theOneRing", "frodo@baggins.com");
        da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);

        RegistrationResult regResult = userService.register(existingUser);
        existingAuth = regResult.authToken();
        Game temp = new Game(null, null, null, "ToMordor", null);
        GameCreateResult existingGame = gameService.createGame(temp, existingAuth);
        existingGameID = existingGame.gameID();
    }

    @Test
    void createGame() {
        var service = new GameService(da);
        service.clear();
        Game gameName = new Game(null, null, null, "ToMordor", null);

        GameCreateResult gameResult = service.createGame(gameName, existingAuth);

        assertNotNull(gameResult);
        assertEquals(Integer.class, gameResult.gameID().getClass());
        assertEquals(2, gameResult.gameID());
    }

    @Test
    void createGameMissingGameInfo() {
        var service = new GameService(da);
        service.clear();
        Game gameNull = new Game(null, null, null, null, null);
        Game gameBlank = new Game(null, null, null, null, null);

        assertThrows(BadRequestResponse.class, () -> {
            service.createGame(gameNull, existingAuth);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.createGame(gameBlank, existingAuth);
        });
    }

    @Test
    void createGameMissingAuth() {
        var service = new GameService(da);
        service.clear();
        Game gameName = new Game(null, null, null, "ToMordor", null);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.createGame(gameName, null);
        });

        assertThrows(UnauthorizedResponse.class, () -> {
            service.createGame(gameName, "");
        });
    }

    @Test
    void joinGame() {
        var service = new GameService(da);
        JoinGameRequest gameWhiteRequest = new JoinGameRequest("WHITE", existingGameID);
        JoinGameRequest gameBlackRequest = new JoinGameRequest("BLACK", existingGameID);

        GameJoinResult whiteResponse = service.joinGame(gameWhiteRequest, existingAuth);

        assertNotNull(whiteResponse);
        assertEquals(new GameJoinResult(), whiteResponse);

        GameJoinResult blackResponse = service.joinGame(gameBlackRequest, existingAuth);

        assertNotNull(blackResponse);
        assertEquals(new GameJoinResult(), blackResponse);
    }

    @Test
    void joinGameMissingPlayerInfo() {
        var service = new GameService(da);
        JoinGameRequest gameEmptyRequest = new JoinGameRequest("", existingGameID);
        JoinGameRequest gameYellowRequest = new JoinGameRequest("YELLOW", existingGameID);
        JoinGameRequest gameNullRequest = new JoinGameRequest(null, existingGameID);
        JoinGameRequest gameNoIDRequest = new JoinGameRequest("BLACK", null);

        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameEmptyRequest, existingAuth);
        });
        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameYellowRequest, existingAuth);
        });
        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameNullRequest, existingAuth);
        });
        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameNoIDRequest, existingAuth);
        });
    }

    @Test
    void joinGameMissingAuth() {
        var service = new GameService(da);
        JoinGameRequest gameWhiteRequest = new JoinGameRequest("WHITE", existingGameID);
        JoinGameRequest gameBlackRequest = new JoinGameRequest("BLACK", existingGameID);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.joinGame(gameWhiteRequest, null);
        });
        assertThrows(UnauthorizedResponse.class, () -> {
            service.joinGame(gameBlackRequest, "");
        });
    }

    @Test
    void joinGameAlreadyTaken() {
        var service = new GameService(da);
        JoinGameRequest gameWhiteRequest = new JoinGameRequest("WHITE", existingGameID);
        JoinGameRequest gameBlackRequest = new JoinGameRequest("BLACK", existingGameID);

        GameJoinResult whiteResponse = service.joinGame(gameWhiteRequest, existingAuth);

        assertNotNull(whiteResponse);
        assertEquals(new GameJoinResult(), whiteResponse);

        assertThrows(ForbiddenResponse.class, () -> {
            service.joinGame(gameWhiteRequest, existingAuth);
        });

        GameJoinResult blackResponse = service.joinGame(gameBlackRequest, existingAuth);

        assertNotNull(blackResponse);
        assertEquals(new GameJoinResult(), blackResponse);

        assertThrows(ForbiddenResponse.class, () -> {
            service.joinGame(gameBlackRequest, existingAuth);
        });
    }

    @Test
    void listGame() {
        var service = new GameService(da);
        GameListResult listResponse = service.listGame(existingAuth);

        assertNotNull(listResponse);
        assertEquals(ArrayList.class, listResponse.games().getClass());
        assertEquals(1, listResponse.games().size());
        Game gameOne = listResponse.games().get(0);

        assertEquals("ToMordor", gameOne.gameName());
        assertNull(gameOne.blackUsername());
        assertNull(gameOne.whiteUsername());
        assertEquals(ChessGame.class, gameOne.game().getClass());

        Game gameName = new Game(null, null, null, "AnEnchantedForest", null);
        service.createGame(gameName, existingAuth);

        GameListResult listResponseTwo = service.listGame(existingAuth);
        assertNotNull(listResponseTwo);
        assertEquals(ArrayList.class, listResponseTwo.games().getClass());
        assertEquals(2, listResponseTwo.games().size());
        Game gameTwo = listResponseTwo.games().get(1);

        assertEquals("AnEnchantedForest", gameTwo.gameName());
    }

    @Test
    void listGameMissingAuth() {
        var service = new GameService(da);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.listGame(null);
        });

        Game gameName = new Game(null, null, null, "AnEnchantedForest", null);
        service.createGame(gameName, existingAuth);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.listGame("");
        });
    }
}
