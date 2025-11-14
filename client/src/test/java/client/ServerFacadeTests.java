package client;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.ServerFacade.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerFacadeTests {

    private static Server server;
    private static String url;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        url = "http://localhost:" + port;
    }

    @BeforeEach
    void clear() {clearDatabase();}

    @AfterAll
    static void stopServer() {
        server.stop();
    }


//    @Test
//    public void sampleTest() {
//        Assertions.assertTrue(true);
//    }

    @Test
    public void registerCorrectInfo() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        User newUser = new User("MiniJosh", "TheOneRing", "AnEmail");

        Auth newAuth = facade.register(newUser);
        Assertions.assertNotNull(newAuth);
        Assertions.assertInstanceOf(String.class, newAuth.authToken());
        Assertions.assertEquals(newUser.username(), newAuth.username());
    }

    @Test
    public void registerDuplicateUsers() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        User newUser = new User("MiniJosh", "TheOneRing", "AnEmail");

        Auth newAuth = facade.register(newUser);
        Assertions.assertNotNull(newAuth);

        Assertions.assertThrows(Exception.class, () -> {
            Auth secondAuth = facade.register(newUser);
        });
    }

    @Test
    public void loginCorrectInfo() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);

        Auth newAuth = facade.login(newUser);
        Assertions.assertNotNull(newAuth);
        Assertions.assertInstanceOf(String.class, newAuth.authToken());
        Assertions.assertEquals(newUser.username(), newAuth.username());
    }

    @Test
    public void loginIncorrectPassword() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        User newUser = new User("MiniJosh", "ILostTheRing", null);

        Assertions.assertThrows(Exception.class, () -> {
            facade.login(newUser);
        });
    }

    @Test
    public void loginUserNotFound() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        User newUser = new User("NotAPerson", "IsThisReal", null);

        Assertions.assertThrows(Exception.class, () -> {
            facade.login(newUser);
        });
    }

    @Test
    public void logoutCorrectInfo() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        Assertions.assertNotNull(newAuth);
        Assertions.assertInstanceOf(String.class, newAuth.authToken());

        facade.logout(newAuth.authToken());

        // Attempt without auth
        Assertions.assertThrows(Exception.class, () -> {
            facade.list(newAuth.authToken());
        });
    }

    @Test
    public void logoutFakeAuthToken() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        Assertions.assertThrows(Exception.class, () -> {
            facade.logout("I'm using a fake auth token");
        });
    }

    @Test
    public void logoutDoubleUser() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        facade.logout(newAuth.authToken());

        // Attempt without auth
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout(newAuth.authToken());
        });
    }

    @Test
    public void listBlankList() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        GameList games = facade.list(newAuth.authToken());
        Assertions.assertNotNull(games);
        Assertions.assertEquals(0, games.games().size());
    }

    @Test
    public void listNewGame() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        facade.create(new Game(null, null, null, "Test", new ChessGame()), newAuth.authToken());

        GameList games = facade.list(newAuth.authToken());
        Assertions.assertNotNull(games);
        Assertions.assertEquals(1, games.games().size());
        Assertions.assertInstanceOf(Game.class, games.games().getFirst());
        Assertions.assertEquals("Test", games.games().getFirst().gameName());
    }

    @Test
    public void listMissingAuth() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        facade.login(newUser);

        Assertions.assertThrows(Exception.class, () -> {
            facade.list(null);
        });
    }

    @Test
    public void createCorrectInfo() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        Game game = new Game(null, null, null, "TrialGame", new ChessGame());
        Game newGame = facade.create(game, newAuth.authToken());
        Assertions.assertNotNull(newGame);
        Assertions.assertInstanceOf(Integer.class, newGame.gameID());
    }

    @Test
    public void createAndList() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        Game game = new Game(null, null, null, "TrialGame", new ChessGame());
        Game newGame = facade.create(game, newAuth.authToken());
        Assertions.assertNotNull(newGame);
        Assertions.assertInstanceOf(Integer.class, newGame.gameID());

        GameList list = facade.list(newAuth.authToken());
        Assertions.assertEquals(game.gameName(), list.games().getFirst().gameName());
    }

    @Test
    public void createMissingInfo() throws Exception {
        ServerFacade facade = new ServerFacade(url);
        registerUsers(facade);

        User newUser = new User("MiniJosh", "TheOneRing", null);
        Auth newAuth = facade.login(newUser);

        Game game = new Game(null, null, null, null, new ChessGame());
        Assertions.assertThrows(Exception.class, () -> {
            facade.create(game, "IncorrectAuth");
        });
    }

    private static void clearDatabase() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/db"))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (!(response.statusCode() / 100 == 2)) {
                throw new RuntimeException("Failed to clear the database");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while attempting to clear the database", ex);
        }
    }

    private static void registerUsers(ServerFacade facade) throws Exception {
        User userOne = new User("MiniJosh", "TheOneRing", "AnEmail");
        User userTwo = new User("Frodo", "AnotherRing?", "ThisEmail");

        try {
            facade.register(userOne);
            facade.register(userTwo);
        } catch (Exception ex) {
            throw new Exception("Error with registering pre-users");
        }

    }

}
