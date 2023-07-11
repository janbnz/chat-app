package de.janbnz.chat.user;

import de.janbnz.chat.database.SqlDatabase;
import de.janbnz.chat.rest.HttpResponse;
import de.janbnz.chat.utils.TokenGenerator;
import io.javalin.http.HttpStatus;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserController {

    public final SqlDatabase database;

    public UserController(SqlDatabase database) {
        this.database = database;
    }

    public CompletableFuture<HttpResponse> createUser(String username, String password) {
        CompletableFuture<HttpResponse> futureResponse = new CompletableFuture<>();

        this.isUsernameExisting(username).thenAcceptAsync(existing -> {
            if (existing) {
                String responseBody = new JSONObject().put("response", "A user with this name is already existing").toString();
                futureResponse.complete(new HttpResponse(HttpStatus.CONFLICT, responseBody));
                return;
            }

            final String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(6));
            final String userId = UUID.randomUUID().toString();

            this.database.executeUpdate("INSERT INTO accounts(user_id, username, password) VALUES ('" + userId + "', '" + username + "', '" + hashedPassword + "');");

            String responseBody = new JSONObject().put("response", "User created").toString();
            futureResponse.complete(new HttpResponse(HttpStatus.OK, responseBody));
        });

        return futureResponse;
    }

    public CompletableFuture<HttpResponse> loginUser(TokenGenerator generator, String username, String password) {
        CompletableFuture<HttpResponse> futureResponse = new CompletableFuture<>();

        this.isUsernameExisting(username).thenAcceptAsync(existing -> {
            if (!existing) {
                String responseBody = new JSONObject().put("response", "No user found").toString();
                futureResponse.complete(new HttpResponse(HttpStatus.CONFLICT, responseBody));
                return;
            }

            this.getHashedPassword(username).thenAcceptAsync(hashedPassword -> {
                if (!BCrypt.checkpw(password, hashedPassword)) {
                    String responseBody = new JSONObject().put("response", "Wrong password").toString();
                    futureResponse.complete(new HttpResponse(HttpStatus.UNAUTHORIZED, responseBody));
                    return;
                }

                CompletableFuture.supplyAsync(() -> {
                    String token = generator.getProvider().generateToken(new UserModel("", username, password));
                    String responseBody = new JSONObject().put("response", token).toString();
                    return futureResponse.complete(new HttpResponse(HttpStatus.OK, responseBody));
                });
            });
        });

        return futureResponse;
    }

    public CompletableFuture<String> getHashedPassword(String username) {
        final String sql = "SELECT password FROM accounts WHERE username = ?";
        return database.executeQuery(sql, username).thenApplyAsync(resultSet -> {
            try (resultSet) {
                if (resultSet == null || !resultSet.next()) return null;
                return resultSet.getString(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Boolean> isUsernameExisting(String username) {
        final String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        return database.executeQuery(sql, username).thenApplyAsync(resultSet -> {
            try (resultSet) {
                if (resultSet == null || !resultSet.next()) return false;
                int count = resultSet.getInt(1);
                return count > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }
}