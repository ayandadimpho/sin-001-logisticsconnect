package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

public class IngestionServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO: read and clean src/main/resources/hubs-global.csv (hubs, sorting centers, regional districts data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.
    }

    public static String cleanHubId(String hubId) {
        String cleaned = hubId.trim().replaceAll("\\s+", " ");

        char firstCharacter = cleaned.charAt(0);
        firstCharacter = Character.toUpperCase(firstCharacter);
        String newString = firstCharacter + cleaned;
        return newString;
    }
}
