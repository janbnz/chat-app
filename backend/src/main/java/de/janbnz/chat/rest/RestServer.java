package de.janbnz.chat.rest;

import de.janbnz.chat.ChatServer;
import de.janbnz.chat.rest.path.*;
import io.javalin.Javalin;

public class RestServer {

    private Javalin app;

    public void start(ChatServer server) {
        this.app = Javalin.create();

        this.app.get("/register", RegisterPath.registerUser(server));
        this.app.get("/login", LoginPath.loginUser(server));
        this.app.get("/logout", LogoutPath.logout(server));
        this.app.get("/info", InfoPath.getInfo(server));
        this.app.get("/messages", MessagesPath.getMessages(server));

        this.app.start(7070);

        System.out.println("RestServer started successfully");
    }

    public void stop() {
        this.app.stop();
    }
}