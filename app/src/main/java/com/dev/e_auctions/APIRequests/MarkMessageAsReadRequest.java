package com.dev.e_auctions.APIRequests;

/**
 *
 */
public class MarkMessageAsReadRequest {
    String token;
    String msgId;

    /**
     *
     * @param token
     * @param msgId
     */
    public MarkMessageAsReadRequest(String token, String msgId) {
        this.token = token;
        this.msgId = msgId;
    }
}
