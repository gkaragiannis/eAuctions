package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.Auction;

public class AuctionResponse extends GeneralResponse {

    private Auction auction;

    public Auction getAuction() {
        return auction;
    }
}
