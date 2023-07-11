package de.janbnz.chat;

import de.janbnz.chat.config.ServiceConfig;
import de.janbnz.chat.database.SqlDatabase;
import de.janbnz.chat.rest.RestServer;
import de.janbnz.chat.server.SocketServer;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer {

    public static void main(String[] args) {
        final ServiceConfig config = new ServiceConfig();

        final SqlDatabase database = new SqlDatabase(config);
        database.connect();

        final RestServer rest = new RestServer();
        rest.start();

        final WebSocketServer server = new SocketServer(new InetSocketAddress("localhost", 8887));
        server.run();
    }
}