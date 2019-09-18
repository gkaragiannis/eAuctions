package com.dev.e_auctions.APIRequests;

public class MarkMessageAsReadRequest {
    String token;
    String msgId;

    public MarkMessageAsReadRequest(String token, String msgId) {
        this.token = token;
        this.msgId = msgId;
    }
}
