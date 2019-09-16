package com.dev.e_auctions.Model;

public class Message {
    private int messageId;
    private String messageTime;
    private User sender;
    private User receiver;
    private String subject;
    private String message;
    private boolean read;

    public int getMessageId() {
        return messageId;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }
}
