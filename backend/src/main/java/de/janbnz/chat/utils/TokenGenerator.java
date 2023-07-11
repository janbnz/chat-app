package de.janbnz.chat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import de.janbnz.chat.user.UserModel;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TokenGenerator {

    private static final String JWT_SECRET = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    private final Algorithm algorithm;
    private final JWTGenerator<UserModel> generator;
    private final JWTVerifier verifier;
    private final JWTProvider provider;

    public TokenGenerator() {
        this.algorithm = Algorithm.HMAC256(JWT_SECRET);

        this.generator = (user, alg) -> {
            Date now = new Date(System.currentTimeMillis());

            JWTCreator.Builder token = JWT.create().withIssuer(user.getUsername()).withIssuedAt(now).withExpiresAt(new Date(now.getTime() + TimeUnit.DAYS.toMillis(90)));
            return token.sign(alg);
        };

        this.verifier = JWT.require(algorithm).build();
        this.provider = new JWTProvider(algorithm, generator, verifier);
    }

    public JWTProvider getProvider() {
        return provider;
    }
}