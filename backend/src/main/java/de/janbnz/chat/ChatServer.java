package de.janbnz.chat;

import de.janbnz.chat.config.ServiceConfig;
import de.janbnz.chat.database.SqlDatabase;
import de.janbnz.chat.rest.RestServer;
import de.janbnz.chat.server.SocketServer;
import de.janbnz.chat.user.UserController;
import de.janbnz.chat.utils.TokenGenerator;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer {

    private final SqlDatabase database;
    private final RestServer restServer;
    private final WebSocketServer socketServer;
    private final UserController userController;
    private final TokenGenerator tokenGenerator;

    public ChatServer() {
        final ServiceConfig config = new ServiceConfig();

        this.tokenGenerator = new TokenGenerator();

        this.database = new SqlDatabase(config);
        this.restServer = new RestServer();
        this.socketServer = new SocketServer(new InetSocketAddress("localhost", 8887));

        this.userController = new UserController(this.database);
    }

    public void start() {
        this.database.connect().join();
        this.database.executeUpdate("CREATE TABLE IF NOT EXISTS accounts(user_id varchar(64) NOT NULL, username varchar(64), password varchar(96));").join();

        this.restServer.start(this, this.tokenGenerator);
        this.socketServer.run();
    }

    public UserController getUserController() {
        return userController;
    }

    public TokenGenerator getTokenGenerator() {
        return tokenGenerator;
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}