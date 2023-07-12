package de.janbnz.chat.chat;

public class ChatMessage {

    private final String chatId, message, userId;
    private final long sentAt;

    public ChatMessage(String chatId, String message, String userId, long sentAt) {
        this.chatId = chatId;
        this.message = message;
        this.userId = userId;
        this.sentAt = sentAt;
    }

    public String getMessage() {
        return message;
    }

    public String getChatId() {
        return chatId;
    }

    public long getSentAt() {
        return sentAt;
    }

    public String getUserId() {
        return userId;
    }
}