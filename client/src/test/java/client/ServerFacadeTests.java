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

}
