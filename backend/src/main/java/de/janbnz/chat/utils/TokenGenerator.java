package de.janbnz.chat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.janbnz.chat.ChatServer;
import de.janbnz.chat.user.UserModel;
import io.javalin.http.Context;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TokenGenerator {

    private static final String JWT_SECRET = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    private final ChatServer server;

    private final Algorithm algorithm;
    private final JWTGenerator<UserModel> generator;
    private final JWTVerifier verifier;
    private final JWTProvider provider;

    public TokenGenerator(ChatServer server) {
        this.server = server;

        this.algorithm = Algorithm.HMAC256(JWT_SECRET);

        this.generator = (user, alg) -> {
            Date now = new Date(System.currentTimeMillis());

            final JWTCreator.Builder token = JWT.create().withIssuer(user.getUuid()).withClaim("name", user.getUsername())
                    .withIssuedAt(now).withExpiresAt(new Date(now.getTime() + TimeUnit.DAYS.toMillis(90)));
            return token.sign(alg);
        };

        this.verifier = JWT.require(algorithm).build();
        this.provider = new JWTProvider(algorithm, generator, verifier);
    }

    public CompletableFuture<DecodedJWT> decodedJWT(Context context) {
        Optional<String> token = JavalinJWT.getTokenFromHeader(context);
        Optional<DecodedJWT> decodedJWT = token.flatMap(this.provider::validateToken);
        if (decodedJWT.isEmpty()) return null;

        final DecodedJWT jwt = decodedJWT.get();
        return this.server.getTokenInvalidator().isValid(jwt.getToken()).thenApplyAsync(valid -> {
            if (valid) return jwt;
            return null;
        });
    }

    public CompletableFuture<DecodedJWT> decodedJWT(String token) {
        DecodedJWT jwt = this.verifier.verify(token);
        if (jwt == null) return null;

        return this.server.getTokenInvalidator().isValid(token).thenApplyAsync(valid -> {
            if (valid) return jwt;
            return null;
        });
    }

    public JWTProvider getProvider() {
        return provider;
    }
}