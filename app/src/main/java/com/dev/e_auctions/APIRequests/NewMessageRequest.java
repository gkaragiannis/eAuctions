package com.dev.e_auctions.APIRequests;

import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Model.Auction;

public class NewMessageRequest{
    private String token;
    private Auction auction;
    private String subject;
    private String message;

    public NewMessageRequest(String token, Auction auction, String subject, String message) {
        this.token = token;
        this.auction = auction;
        this.subject = subject;
        this.message = message;
    }
}
