package de.janbnz.chat.user;

public class UserModel {

    private final String uuid, username, password;

    public UserModel(String uuid, String username, String password) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }
}