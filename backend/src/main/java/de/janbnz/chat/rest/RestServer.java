package de.janbnz.chat.rest;

import de.janbnz.chat.ChatServer;
import de.janbnz.chat.rest.path.InfoPath;
import de.janbnz.chat.rest.path.LoginPath;
import de.janbnz.chat.rest.path.RegisterPath;
import de.janbnz.chat.utils.TokenGenerator;
import io.javalin.Javalin;

public class RestServer {

    private Javalin app;

    public void start(ChatServer server, TokenGenerator generator) {
        this.app = Javalin.create();

        this.app.get("/login", LoginPath.loginUser(server));
        this.app.get("/register", RegisterPath.registerUser(server));
        this.app.get("/info", InfoPath.getInfo(server, generator));

        this.app.start(7070);

        System.out.println("RestServer started successfully");
    }

    public void stop() {
        this.app.stop();
    }
}