package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.*;
import io.javalin.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import service.GameService;
import service.UserService;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

public class Server {

    private final Javalin server;
    private UserService userService;
    private GameService gameService;
    private DataAccess dataAccess;

    public Server() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);

        server.post("game", this::createGame);
        server.get("game", this::listGame);
        server.put("game", this::joinGame);

        server.exception(BadRequestResponse.class, this::badResponseHandler);
        server.exception(ForbiddenResponse.class, this::forbiddenResponseHandler);
        server.exception(UnauthorizedResponse.class, this::unauthorizedResponseHandler);

        // Register your endpoints and exception handlers here.

    }

    private void register(Context ctx) throws BadRequestResponse, ForbiddenResponse {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), User.class);
        RegistrationResult response = userService.register(req);


        // req.put("authToken", "cow");
        // Call the service and register this

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
        var req = serializer.fromJson(ctx.body(), User.class);
//        var authToken = ctx.header("authorization");
        LogoutResult response = userService.logout(req);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void listGame(Context ctx) {
        var serializer = new Gson();
        GameListResult response = gameService.gameList();

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void createGame(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Game.class);
        GameCreateResult response = gameService.createGame(req);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void joinGame(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), JoinGameRequest.class);
        GameJoinResult response = gameService.joinGame(req);

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

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
