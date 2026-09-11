package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HubServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7051);

        app.get("/health", ctx -> ctx.result("OK"));
        // TODO (Serves provinces and sorting centers (place-name source of truth).)
        // Add domain endpoints for hub-service here.
        app.get("/hubs", ctx -> ctx.result(getHubsFromIngestion()));
        app.get("/hubs/{hubId}", ctx -> {
            String hubId = ctx.pathParam("hubId");
            Hub hub = findHubById(hubId);
            ctx.json(hub);
        });
    }

    public static String getHubsFromIngestion() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:7050/hubs")).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {
            return "Error conecting to ingestion service: " + e.getMessage();
        }
    }

    public static Hub findHubById(String hubId) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Hub[] hubs = mapper.readValue(getHubsFromIngestion(), Hub[].class);

            for (Hub hub : hubs) {
                if (hub.getHubId().equals(hubId)) {
                    return hub;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
