package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestAuthResult;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private String existingAuth;
    private User existingUser;
    private MemoryDataAccess da;

    @BeforeEach
    public void setup() {
        existingUser = new User("Frodo", "theOneRing", "frodo@baggins.com");
        da = new MemoryDataAccess();
        var service = new UserService(da);

        RegistrationResult regResult = service.register(existingUser);
        existingAuth = regResult.authToken();
    }

    @Test
    void createGame() {
        var service = new GameService(da);
        Game gameName = new Game(null, null, null, "ToMordor", null);

        GameCreateResult gameResult = service.createGame(gameName, existingAuth);

        assertNotNull(gameResult);
        assertEquals(Integer.class, gameResult.gameID().getClass());
        assertEquals(1, gameResult.gameID());
    }

    @Test
    void createGameMissingGameInfo() {
        var service = new GameService(da);
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
        Game gameName = new Game(null, null, null, "ToMordor", null);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.createGame(gameName, null);
        });

        assertThrows(UnauthorizedResponse.class, () -> {
            service.createGame(gameName, "");
        });
    }
}
