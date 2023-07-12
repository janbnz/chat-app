package de.janbnz.chat.rest.path;

import de.janbnz.chat.ChatServer;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class InfoPath {

    public static Handler getInfo(ChatServer server) {
        return ctx -> ctx.future(() -> server.getTokenGenerator().decodedJWT(ctx).thenAcceptAsync(jwt -> {
            if (jwt == null) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid token");
                return;
            }

            ctx.status(HttpStatus.OK).result("Hello " + jwt.getClaim("name").asString());
        }).exceptionally(ex -> {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("An error occurred");
            return null;
        }));
    }
}