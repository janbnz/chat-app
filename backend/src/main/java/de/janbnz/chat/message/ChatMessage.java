package de.janbnz.chat.message;

public class ChatMessage {

    private String sender;
    private String receiver;
    private String message;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}