package com.dev.e_auctions.APIRequests;

public class NewBidRequest {

    private String bidderToken;
    private String bidderValue;
    private String auctionId;

    public NewBidRequest(String bidderToken, String bidderValue, String auctionId) {
        this.bidderToken = bidderToken;
        this.bidderValue = bidderValue;
        this.auctionId = auctionId;
    }

    public String getBidderToken() {
        return bidderToken;
    }

    public String getBidderValue() {
        return bidderValue;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setBidderToken(String bidderToken) {
        this.bidderToken = bidderToken;
    }

    public void setBidderValue(String bidderValue) {
        this.bidderValue = bidderValue;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }
}
