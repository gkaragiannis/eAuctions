package com.dev.e_auctions.apirequests;

import com.dev.e_auctions.Model.Auction;

public class NewMessageRequest {

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

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
