package client;

import chess.ChessPiece;
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
        clearDatabase();
    }

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
