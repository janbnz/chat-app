package de.janbnz.chat.rest.path;

import de.janbnz.chat.ChatServer;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.json.JSONObject;

public class LoginPath {

    public static Handler loginUser(ChatServer server) {
        return ctx -> {
            if (ctx.body().isEmpty()) {
                ctx.status(HttpStatus.NO_CONTENT).result("Please specify a username and password");
                return;
            }

            final JSONObject data = new JSONObject(ctx.body());
            if (!data.has("username") || !data.has("password")) {
                ctx.status(HttpStatus.BAD_REQUEST).result("Please specify a username and password");
                return;
            }

            final String username = data.getString("username");
            final String password = data.getString("password");

            ctx.future(() -> server.getUserController().loginUser(server.getTokenGenerator(), username, password)
                    .thenAcceptAsync(response -> ctx.status(response.getStatus()).json(response.getResponse()))
                    .exceptionally(ex -> {
                        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("An error occurred");
                        return null;
                    }));
        };
    }
}