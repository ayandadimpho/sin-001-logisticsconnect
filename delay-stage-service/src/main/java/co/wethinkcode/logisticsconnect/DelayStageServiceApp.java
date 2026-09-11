package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.mq.DelayStage;
import io.javalin.Javalin;

import java.util.HashMap;

public class DelayStageServiceApp {
    private static HashMap<String, Integer> delayStages = new HashMap<>();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7052);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO (Tracks the Transit Delay Stage (0-8, e.g. weather shutdowns).)
        // Add domain endpoints for delay-stage-service here.

        app.get("/delay-stage/{hubId}", ctx -> {
            String hubId = ctx.pathParam("hubId");

            Integer stage = delayStages.get(hubId);

            if (stage == null) {
                ctx.status(404).result("No delay stage found for " + hubId);
                return;
            }

            ctx.json(stage);
        });

        app.post("/delay-stage/{hubId}", ctx -> {
            String hubId = ctx.pathParam("hubId");

            DelayStage delayStage = ctx.bodyAsClass(DelayStage.class);

            delayStages.put(hubId, delayStage.getStage());

            ctx.json(delayStage);
        });
    }
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
