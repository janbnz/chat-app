package de.janbnz.chat.rest.path;

import de.janbnz.chat.ChatServer;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.json.JSONObject;

public class InfoPath {

    public static Handler getInfo(ChatServer server) {
        return ctx -> ctx.future(() -> server.getTokenGenerator().decodedJWT(ctx).thenAcceptAsync(jwt -> {
            if (jwt == null) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid token");
                return;
            }

            final JSONObject response = new JSONObject();
            response.put("name", jwt.getClaim("name"));
            response.put("uuid", jwt.getIssuer());

            ctx.status(HttpStatus.OK).result(response.toString());
        }).exceptionally(ex -> {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("An error occurred");
            return null;
        }));
    }
}