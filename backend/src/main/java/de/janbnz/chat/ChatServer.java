package de.janbnz.chat;

import de.janbnz.chat.server.SocketServer;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer {

    public static void main(String[] args) {
        WebSocketServer server = new SocketServer(new InetSocketAddress("localhost", 8887));
        server.run();
    }
}