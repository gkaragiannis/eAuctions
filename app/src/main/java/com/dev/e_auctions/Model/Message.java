package com.dev.e_auctions.Model;

public class Message {

    private Long messageId;
    private String messageTime;
    private User sender;
    private User receiver;
    private String subject; //The subject of the message is the auction id
    private String message;
    private boolean isRead;

    public Message(Long messageId, String messageTime, User sender, User receiver, String subject, String message, boolean isRead) {
        this.messageId = messageId;
        this.messageTime = messageTime;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.message = message;
        this.isRead = isRead;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
