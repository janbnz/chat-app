package de.janbnz.chat.rest.path;

import com.auth0.jwt.interfaces.DecodedJWT;
import de.janbnz.chat.ChatServer;
import de.janbnz.chat.utils.TokenGenerator;
import io.javalin.http.Handler;
import javalinjwt.JavalinJWT;
import org.json.JSONObject;

import java.util.Optional;

public class InfoPath {

    public static Handler getInfo(ChatServer server, TokenGenerator tokenGenerator) {
        return ctx -> {
            Optional<DecodedJWT> decodedJWT = JavalinJWT.getTokenFromHeader(ctx).flatMap(tokenGenerator.getProvider()::validateToken);

            if (decodedJWT.isEmpty()) {
                ctx.status(401).result("Missing or invalid token");
            } else {
                ctx.result("Hi " + decodedJWT.get().getIssuer());
            }
        };
    }
}