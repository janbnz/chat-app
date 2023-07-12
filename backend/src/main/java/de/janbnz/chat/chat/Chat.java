package de.janbnz.chat.chat;

import java.util.List;

public class Chat {

    private final String id;
    private final String chatName;
    private final List<String> members;

    public Chat(String id, String chatName, List<String> members) {
        this.id = id;
        this.chatName = chatName;
        this.members = members;
    }

    public String getChatName() {
        return chatName;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }
}