package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import datamodel.*;
import io.javalin.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import server.websocket.WebSocketHandler;
import service.GameService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;
    private final WebSocketHandler webSocketHandler;
    private DataAccess dataAccess;

    public Server() {
//        dataAccess = new MemoryDataAccess();
        try { dataAccess = new MySqlDataAccess(); }
        catch (DataAccessException e) { throw new RuntimeException("Error initializing database", e); }

        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);

        webSocketHandler = new WebSocketHandler(dataAccess);

        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", this::clear);
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);

        server.post("game", this::createGame);
        server.get("game", this::listGame);
        server.put("game", this::joinGame);

        server.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });

        server.exception(BadRequestResponse.class, this::badResponseHandler);
        server.exception(ForbiddenResponse.class, this::forbiddenResponseHandler);
        server.exception(UnauthorizedResponse.class, this::unauthorizedResponseHandler);
        server.exception(DataAccessException.class, this::mySQLReponseHandler);
        server.exception(RuntimeException.class, this::runtimeResponseHandler);

        // Register your endpoints and exception handlers here.

    }
    private void clear(Context ctx) {
        userService.clear();
        gameService.clear();

        ctx.result();
    }

    private void register(Context ctx) throws BadRequestResponse, ForbiddenResponse {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), User.class);
        RegistrationResult response = userService.register(req);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void login(Context ctx) throws BadRequestResponse, UnauthorizedResponse {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), User.class);
        LoginResult response = userService.login(req);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void logout(Context ctx) throws UnauthorizedResponse {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        LogoutResult response = userService.logout(authToken);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void listGame(Context ctx) {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        GameListResult response = gameService.listGame(authToken);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void createGame(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Game.class);
        String authToken = ctx.header("authorization");
        GameCreateResult response = gameService.createGame(req, authToken);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void joinGame(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), JoinGameRequest.class);
        String authToken = ctx.header("authorization");
        GameJoinResult response = gameService.joinGame(req, authToken);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void badResponseHandler(BadRequestResponse ex, Context ctx) {
        var serializer = new Gson();
        ctx.status(400);
        ctx.json(serializer.toJson(Map.of("message", ex.getMessage())));
    }

    private void unauthorizedResponseHandler(UnauthorizedResponse ex, Context ctx) {
        var serializer = new Gson();
        ctx.status(401);
        ctx.json(serializer.toJson(Map.of("message", ex.getMessage())));
    }

    private void forbiddenResponseHandler(ForbiddenResponse ex, Context ctx) {
        var serializer = new Gson();
        ctx.status(403);
        ctx.json(serializer.toJson(Map.of("message", ex.getMessage())));
    }

    private void mySQLReponseHandler(DataAccessException ex, Context ctx) {
        var serializer = new Gson();
        ctx.status(500);
        ctx.json(serializer.toJson(Map.of("message", ex.getMessage())));
    }

    private void runtimeResponseHandler(RuntimeException ex, Context ctx) {
        var serializer = new Gson();
        ctx.status(500);
        ctx.json(serializer.toJson(Map.of("message", ex.getMessage())));
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
