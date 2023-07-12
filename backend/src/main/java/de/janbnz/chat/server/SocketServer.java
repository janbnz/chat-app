package de.janbnz.chat.server;

import de.janbnz.chat.ChatServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketServer extends WebSocketServer {

    private final ChatServer server;

    private final ArrayList<String> messages = new ArrayList<>();
    private final HashMap<String, WebSocket> tokenConnections = new HashMap<>();

    public SocketServer(ChatServer server, InetSocketAddress address) {
        super(address);
        this.server = server;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress());
        messages.forEach(conn::send);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from " + conn.getRemoteSocketAddress() + ": " + message);

        if (message.isEmpty()) return;
        final JSONObject data = new JSONObject(message);

        final String chatId = data.getString("chatId");
        final String token = data.getString("token");

        this.server.getTokenGenerator().decodedJWT(token).thenAcceptAsync(jwt -> {
            if (jwt == null) return;

            // User ID and WebSocket connection
            tokenConnections.put(jwt.getIssuer(), conn);

            final String username = jwt.getClaim("name").asString();
            data.remove("token");
            data.put("name", username);

            // TODO: store to database
            if ("public".equals(chatId)) {
                broadcast(data.toString());
                messages.add(data.toString());
            } else {
                // TODO: send to chat members
            }
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