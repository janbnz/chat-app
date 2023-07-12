package de.janbnz.chat.rest.path;

import de.janbnz.chat.ChatServer;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessagesPath {

    public static Handler getMessages(ChatServer server) {
        return ctx -> ctx.future(() -> server.getTokenGenerator().decodedJWT(ctx).thenAcceptAsync(jwt -> {
            if (jwt == null) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid token");
                return;
            }

            final JSONObject body = new JSONObject(ctx.body());
            final String chatId = body.has("chatId") ? body.getString("chatId") : "";

            final JSONObject response = new JSONObject();
            final JSONArray array = new JSONArray();

            ctx.future(() -> server.getChatRegistry().getMessages(chatId).thenAcceptAsync(messages -> {
                messages.forEach(array::put);
                response.put("messages", array);
                ctx.status(HttpStatus.OK).result(response.toString());
            }));

        }).exceptionally(ex -> {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("An error occurred");
            return null;
        }));
    }
}