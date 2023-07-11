package de.janbnz.chat.rest.path;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.json.JSONObject;

public class LoginPath {

    public static Handler loginUser = ctx -> {
        if (ctx.body().isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }

        final JSONObject data = new JSONObject(ctx.body());

    };
}