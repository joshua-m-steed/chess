package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceSQLTest {
    private String existingAuthSQL;
    private Integer existingGameIDSQL;
    private MySqlDataAccess da;

    @BeforeEach
    public void setup() throws DataAccessException {
        User existingUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");
        da = new MySqlDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        da.clearUsers();
        da.clearGames();

        RegistrationResult regResult = userService.register(existingUserSQL);
        existingAuthSQL = regResult.authToken();
        Game tempSQL = new Game(null, null, null, "ToMordor", null);
        GameCreateResult existingGame = gameService.createGame(tempSQL, existingAuthSQL);
        existingGameIDSQL = existingGame.gameID();
    }

    @Test
    void createGame() {
        var service = new GameService(da);
        service.clear();
        Game gameNameSQL = new Game(null, null, null, "ToMordor", null);

        GameCreateResult gameResult = service.createGame(gameNameSQL, existingAuthSQL);

        assertNotNull(gameResult);
        assertEquals(Integer.class, gameResult.gameID().getClass());
        assertEquals(1, gameResult.gameID());
    }

    @Test
    void createGameMissingGameInfo() {
        var service = new GameService(da);
        service.clear();
        Game gameNullSQL = new Game(null, null, null, null, null);
        Game gameBlankSQL = new Game(null, null, null, null, null);

        assertThrows(BadRequestResponse.class, () -> {
            service.createGame(gameNullSQL, existingAuthSQL);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.createGame(gameBlankSQL, existingAuthSQL);
        });
    }

    @Test
    void createGameMissingAuth() {
        var service = new GameService(da);
        service.clear();
        Game gameNameSQL = new Game(null, null, null, "ToMordor", null);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.createGame(gameNameSQL, null);
        });

        assertThrows(UnauthorizedResponse.class, () -> {
            service.createGame(gameNameSQL, "");
        });
    }

    @Test
    void joinGame() {
        var service = new GameService(da);
        JoinGameRequest gameWhiteRequestSQL = new JoinGameRequest("WHITE", existingGameIDSQL);
        JoinGameRequest gameBlackRequestSQL = new JoinGameRequest("BLACK", existingGameIDSQL);

        GameJoinResult whiteResponse = service.joinGame(gameWhiteRequestSQL, existingAuthSQL);

        assertNotNull(whiteResponse);
        assertEquals(new GameJoinResult(), whiteResponse);

        GameJoinResult blackResponse = service.joinGame(gameBlackRequestSQL, existingAuthSQL);

        assertNotNull(blackResponse);
        assertEquals(new GameJoinResult(), blackResponse);
    }

    @Test
    void joinGameMissingPlayerInfo() {
        var service = new GameService(da);
        JoinGameRequest gameEmptyRequestSQL = new JoinGameRequest("", existingGameIDSQL);
        JoinGameRequest gameYellowRequestSQL = new JoinGameRequest("YELLOW", existingGameIDSQL);
        JoinGameRequest gameNullRequestSQL = new JoinGameRequest(null, existingGameIDSQL);
        JoinGameRequest gameNoIDRequestSQL = new JoinGameRequest("BLACK", null);

        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameEmptyRequestSQL, existingAuthSQL);
        });
        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameYellowRequestSQL, existingAuthSQL);
        });
        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameNullRequestSQL, existingAuthSQL);
        });
        assertThrows(BadRequestResponse.class, () -> {
            service.joinGame(gameNoIDRequestSQL, existingAuthSQL);
        });
    }

    @Test
    void joinGameMissingAuth() {
        var service = new GameService(da);
        JoinGameRequest gameWhiteRequestSQL = new JoinGameRequest("WHITE", existingGameIDSQL);
        JoinGameRequest gameBlackRequestSQL = new JoinGameRequest("BLACK", existingGameIDSQL);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.joinGame(gameWhiteRequestSQL, null);
        });
        assertThrows(UnauthorizedResponse.class, () -> {
            service.joinGame(gameBlackRequestSQL, "");
        });
    }

    @Test
    void joinGameAlreadyTaken() {
        var service = new GameService(da);
        JoinGameRequest gameWhiteRequestSQL = new JoinGameRequest("WHITE", existingGameIDSQL);
        JoinGameRequest gameBlackRequestSQL = new JoinGameRequest("BLACK", existingGameIDSQL);

        GameJoinResult whiteResponse = service.joinGame(gameWhiteRequestSQL, existingAuthSQL);

        assertNotNull(whiteResponse);
        assertEquals(new GameJoinResult(), whiteResponse);

        assertThrows(ForbiddenResponse.class, () -> {
            service.joinGame(gameWhiteRequestSQL, existingAuthSQL);
        });

        GameJoinResult blackResponse = service.joinGame(gameBlackRequestSQL, existingAuthSQL);

        assertNotNull(blackResponse);
        assertEquals(new GameJoinResult(), blackResponse);

        assertThrows(ForbiddenResponse.class, () -> {
            service.joinGame(gameBlackRequestSQL, existingAuthSQL);
        });
    }

    @Test
    void listGame() {
        var service = new GameService(da);
        GameListResult listResponseSQL = service.listGame(existingAuthSQL);

        assertNotNull(listResponseSQL);
        assertEquals(ArrayList.class, listResponseSQL.games().getClass());
        assertEquals(1, listResponseSQL.games().size());
        Game gameOne = listResponseSQL.games().get(0);

        assertEquals("ToMordor", gameOne.gameName());
        assertNull(gameOne.blackUsername());
        assertNull(gameOne.whiteUsername());
        assertEquals(ChessGame.class, gameOne.game().getClass());

        Game gameNameSQL = new Game(null, null, null, "AnEnchantedForest", null);
        service.createGame(gameNameSQL, existingAuthSQL);

        GameListResult listResponseTwo = service.listGame(existingAuthSQL);
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

        Game gameNameSQL = new Game(null, null, null, "AnEnchantedForest", null);
        service.createGame(gameNameSQL, existingAuthSQL);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.listGame("");
        });
    }
}
