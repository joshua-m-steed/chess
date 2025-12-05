package server;

import com.google.gson.Gson;

import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public Auth register(User user) throws Exception {
        HttpRequest request = buildRequest("POST", "/user", user, null);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, Auth.class);
    }

    public Auth login(User user) throws Exception {
        HttpRequest request = buildRequest("POST", "/session", user, null);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, Auth.class);
    }

    public void logout(String authToken) throws Exception {
        HttpRequest request = buildRequest("DELETE", "/session", null, authToken);
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null);
    }

    public GameList list(String authToken) throws Exception {
        HttpRequest request = buildRequest("GET", "/game", null, authToken);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, GameList.class);
    }

    public Game create(Game game, String authToken) throws Exception {
        HttpRequest request = buildRequest("POST", "/game", game, authToken);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, Game.class);
    }

    public void join(GameJoin gameRequest, String authToken) throws Exception {
        HttpRequest request = buildRequest("PUT", "/game", gameRequest, authToken);
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }

        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }

        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        int status = response.statusCode();
        if (!(status / 100 == 2)) {
            String body = response.body();
            if (body != null) {
                throw new Exception("You don't have authorization");
            }
            throw new Exception("The Server failed to connect");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }
}
