package de.janbnz.chat.utils;

import de.janbnz.chat.ChatServer;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class TokenInvalidator {

    private final ChatServer server;

    public TokenInvalidator(ChatServer server) {
        this.server = server;
    }

    public void invalidate(String token) {
        server.getTokenGenerator().decodedJWT(token).thenAcceptAsync(jwt -> {
            if (jwt == null) return;

            long expiresAt = jwt.getExpiresAt().getTime();
            this.server.getDatabase().executeUpdate("INSERT INTO dead_tokens(token, expires_at) VALUES ('" + token + "', '" + expiresAt + "');").join();
        });
    }

    public CompletableFuture<Boolean> isValid(String token) {
        return this.server.getDatabase().executeQuery("SELECT COUNT(*) FROM dead_tokens WHERE token = ?", token).thenApplyAsync(set -> {
            try (set) {
                if (set == null || !set.next()) return true;
                return set.getInt(1) == 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return true;
        });
    }
}