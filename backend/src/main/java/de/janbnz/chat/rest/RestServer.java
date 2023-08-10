package de.janbnz.chat.rest;

import de.janbnz.chat.ChatServer;
import de.janbnz.chat.rest.path.*;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class RestServer {

    private Javalin app;

    public void start(ChatServer server) {
        this.app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        });

        this.app.post("/register", RegisterPath.registerUser(server));
        this.app.post("/login", LoginPath.loginUser(server));
        this.app.post("/logout", LogoutPath.logout(server));
        this.app.get("/info", InfoPath.getInfo(server));
        this.app.get("/messages", MessagesPath.getMessages(server));

        this.app.start(7070);

        System.out.println("RestServer started successfully");
    }

    public void stop() {
        this.app.stop();
    }
}