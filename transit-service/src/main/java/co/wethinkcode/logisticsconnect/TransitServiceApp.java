package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;
import org.eclipse.jetty.util.IO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class TransitServiceApp {

    public static String getHubFromHubService(String hubId) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:7051/hubs/" + hubId)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            return "Error connecting to hub service " + e.getMessage();
        }
    }

    public static String getDelayStageFromDelayService(String hubId) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:7052/delay-stage/" + hubId)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {
            return "Error connecting to delay stage service " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7053);

        app.get("/health", ctx -> ctx.result("OK"));


        // TODO (Calculates estimated arrival windows based on hub and delay stage.)
        // Add domain endpoints for transit-service here.
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
