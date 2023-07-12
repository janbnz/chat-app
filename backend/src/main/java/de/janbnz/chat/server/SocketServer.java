package de.janbnz.chat.server;

import de.janbnz.chat.ChatServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class SocketServer extends WebSocketServer {

    private final ChatServer server;
    private final ArrayList<UserConnection> userConnections = new ArrayList<>();

    public SocketServer(ChatServer server, InetSocketAddress address) {
        super(address);
        this.server = server;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        userConnections.removeIf(userConnection -> userConnection.getConnection().equals(conn));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from " + conn.getRemoteSocketAddress() + ": " + message);

        if (message.isEmpty()) return;
        final JSONObject data = new JSONObject(message);

        final String chatId = data.has("chatId") ? data.getString("chatId") : "";
        final String token = data.getString("token");

        this.server.getTokenGenerator().decodedJWT(token).thenAcceptAsync(jwt -> {
            if (jwt == null) return;

            final String userId = jwt.getIssuer();
            final String username = jwt.getClaim("name").asString();

            // User ID and WebSocket connection
            if (userConnections.stream().filter(user -> user.getUuid().equals(userId)).findAny().stream().findFirst().isEmpty())
                userConnections.add(new UserConnection(userId, username, conn));

            data.remove("token");
            data.put("name", username);
            data.put("sentAt", System.currentTimeMillis());

            if ("".equals(chatId)) {
                broadcast(data.toString());
            } else {
                this.server.getChatRegistry().getMembers(chatId).thenAccept(memberIds ->
                        this.userConnections.stream().filter(userConnection -> memberIds.contains(userConnection.getUuid()))
                                .forEach(userConnection -> userConnection.getConnection().send(data.toString())));
            }

            this.server.getChatRegistry().storeMessage(chatId, data.getString("message"), userId, System.currentTimeMillis());
        });
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket started successfully");
    }
}