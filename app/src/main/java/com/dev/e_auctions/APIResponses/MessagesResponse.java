package com.dev.e_auctions.apiresponses;

import com.dev.e_auctions.Model.Message;

import java.util.List;

public class MessagesResponse extends GeneralResponse {
    private List<Message> messages;


    public MessagesResponse(String statusCode, String statusMsg, List<Message> messages) {
        super(statusCode, statusMsg);
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
