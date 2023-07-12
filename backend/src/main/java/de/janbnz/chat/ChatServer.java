package de.janbnz.chat;

import de.janbnz.chat.chat.ChatRegistry;
import de.janbnz.chat.config.ServiceConfig;
import de.janbnz.chat.database.SqlDatabase;
import de.janbnz.chat.rest.RestServer;
import de.janbnz.chat.server.SocketServer;
import de.janbnz.chat.user.UserController;
import de.janbnz.chat.utils.TokenGenerator;
import de.janbnz.chat.utils.TokenInvalidator;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer {

    private final SqlDatabase database;
    private final RestServer restServer;
    private final WebSocketServer socketServer;
    private final UserController userController;

    private final TokenGenerator tokenGenerator;
    private final TokenInvalidator tokenInvalidator;

    private final ChatRegistry chatRegistry;

    public ChatServer() {
        final ServiceConfig config = new ServiceConfig();

        this.tokenGenerator = new TokenGenerator(this);
        this.tokenInvalidator = new TokenInvalidator(this);

        this.database = new SqlDatabase(config);
        this.restServer = new RestServer();
        this.socketServer = new SocketServer(this, new InetSocketAddress("localhost", 8887));

        this.chatRegistry = new ChatRegistry(this);

        this.userController = new UserController(this.database);
    }

    public void start() {
        this.database.connect().join();
        this.database.executeUpdate("CREATE TABLE IF NOT EXISTS accounts(user_id varchar(64) NOT NULL, username varchar(64), password varchar(96));").join();
        this.database.executeUpdate("CREATE TABLE IF NOT EXISTS dead_tokens(token varchar(100), expires_at long);").join();

        this.database.executeUpdate("CREATE TABLE IF NOT EXISTS chats(chat_id varchar(64) NOT NULL, chat_name varchar(64), PRIMARY KEY(chat_id));").join();
        this.database.executeUpdate("CREATE TABLE IF NOT EXISTS chat_members(chat_id varchar(64), user_id varchar(64), FOREIGN KEY (chat_id) REFERENCES chats(chat_id), FOREIGN KEY (user_id) REFERENCES accounts(user_id));").join();
        this.database.executeUpdate("CREATE TABLE IF NOT EXISTS chat_messages(message_id varchar(64), chat_id varchar(64), message text(1000), user_id varchar(64), sent_at long, FOREIGN KEY (chat_id) REFERENCES chats(chat_id), FOREIGN KEY (user_id) REFERENCES accounts(user_id));").join();

        this.restServer.start(this);
        this.socketServer.run();
    }

    public UserController getUserController() {
        return userController;
    }

    public TokenGenerator getTokenGenerator() {
        return tokenGenerator;
    }

    public TokenInvalidator getTokenInvalidator() {
        return tokenInvalidator;
    }

    public ChatRegistry getChatRegistry() {
        return chatRegistry;
    }

    public SqlDatabase getDatabase() {
        return database;
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}