package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.Message;

import java.util.List;

public class GetMessagesResponse extends GeneralResponse {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }
}
