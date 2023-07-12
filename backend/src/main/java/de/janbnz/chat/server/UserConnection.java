package de.janbnz.chat.server;

import org.java_websocket.WebSocket;

public class UserConnection {

    private final String uuid, username;
    private final WebSocket connection;

    public UserConnection(String uuid, String username, WebSocket connection) {
        this.uuid = uuid;
        this.username = username;
        this.connection = connection;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public WebSocket getConnection() {
        return connection;
    }
}