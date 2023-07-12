package de.janbnz.chat.rest.path;

import de.janbnz.chat.ChatServer;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class LogoutPath {

    public static Handler logout(ChatServer server) {
        return ctx -> ctx.future(() -> server.getTokenGenerator().decodedJWT(ctx).thenAcceptAsync(jwt -> {
            if (jwt == null) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid token");
                return;
            }

            server.getTokenInvalidator().invalidate(jwt.getToken());
            ctx.status(HttpStatus.OK).result("Successfully logged out");
        }).exceptionally(ex -> {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("An error occurred");
            return null;
        }));
    }
}