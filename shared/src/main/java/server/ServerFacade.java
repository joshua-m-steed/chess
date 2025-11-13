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
        HttpRequest request = buildRequest("POST", "/user", user);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, Auth.class);
    }

    public Auth login(User user) throws Exception {
        HttpRequest request = buildRequest("POST", "/session", user);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, Auth.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
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
                throw new Exception("Not authorized");
            }
            throw new Exception("Server failure");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }
}
