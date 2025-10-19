package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.*;
import io.javalin.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.eclipse.jetty.util.TopologicalSort;
import service.UserService;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private final Javalin server;
    private UserService userService;
    private DataAccess dataAccess;

    public Server() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.get("game", this::listGame);
        server.exception(BadRequestResponse.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    private void register(Context ctx) throws BadRequestResponse {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), User.class);
        RegistrationResult response = userService.register(req);

        // req.put("authToken", "cow");
        // Call the service and register this

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void login(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), User.class);
        LoginResult response = userService.login(req);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void logout(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), User.class);
        LogoutResult response = userService.logout(req);

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void listGame(Context ctx) {
        var serializer = new Gson();
        GameListResult response = userService.gameList();

        var res = serializer.toJson(response);
        ctx.result(res);
    }

    private void exceptionHandler(BadRequestResponse ex, Context ctx) {
        var serializer = new Gson();
        ctx.status(400);
        ctx.json(serializer.toJson(Map.of("message", ex.getMessage(), "status", 400)));
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
