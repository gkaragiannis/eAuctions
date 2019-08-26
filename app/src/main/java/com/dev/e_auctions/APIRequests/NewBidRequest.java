package com.dev.e_auctions.APIRequests;

public class NewBidRequest {

    private String bidderToken;
    private Double bidderValue;
    private String auctionId;

    public NewBidRequest(String bidderToken, Double bidderValue, String auctionId) {
        this.bidderToken = bidderToken;
        this.bidderValue = bidderValue;
        this.auctionId = auctionId;
    }

    public void setBidderToken(String bidderToken) {
        this.bidderToken = bidderToken;
    }

    public void setBidderValue(Double bidderValue) {
        this.bidderValue = bidderValue;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }
}
