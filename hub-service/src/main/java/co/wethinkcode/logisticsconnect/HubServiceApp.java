package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class HubServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7051);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/hubs", ctx -> ctx.result(getHubsFromIngestion()));

        // TODO (Serves provinces and sorting centers (place-name source of truth).)
        // Add domain endpoints for hub-service here.
    }

    public static String getHubsFromIngestion() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:7050/hubs")).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        }catch (IOException | InterruptedException e) {
            return "Error conecting to ingestion service: " + e.getMessage();
        }
    }
}
