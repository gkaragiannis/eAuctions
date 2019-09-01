package com.dev.e_auctions.apirequests;

public class DeleteAuctionRequest {

    private String auctionId;
    private String token;

    public DeleteAuctionRequest(String auctionId, String token) {
        this.auctionId = auctionId;
        this.token = token;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
