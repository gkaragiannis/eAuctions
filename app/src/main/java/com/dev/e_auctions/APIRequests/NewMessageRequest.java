package com.dev.e_auctions.APIRequests;

import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Model.Auction;

public class NewMessageRequest{
    private String token;
    private int auctionId;
    private String subject;
    private String message;

    public NewMessageRequest(String token, int auctionId, String subject, String message) {
        this.token = token;
        this.auctionId = auctionId;
        this.subject = subject;
        this.message = message;
    }
}
