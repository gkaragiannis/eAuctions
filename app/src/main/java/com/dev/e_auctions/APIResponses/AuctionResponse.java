package com.dev.e_auctions.apiresponses;

import com.dev.e_auctions.Model.Auction;

public class AuctionResponse extends GeneralResponse {
    private Auction auction;

    public AuctionResponse(String statusCode, String statusMsg, Auction auction) {
        super(statusCode, statusMsg);
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }
}
