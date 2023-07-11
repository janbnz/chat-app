package de.janbnz.chat.rest;

import de.janbnz.chat.rest.path.LoginPath;
import io.javalin.Javalin;

public class RestServer {

    private Javalin app;

    public void start() {
        this.app = Javalin.create();
        this.app.get("/login", LoginPath.loginUser);
        this.app.start(7070);

        System.out.println("RestServer started successfully");
    }

    public void stop() {
        this.app.stop();
    }
}