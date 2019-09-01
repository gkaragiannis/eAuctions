package com.dev.e_auctions.apiresponses;

import com.dev.e_auctions.Model.Auction;

import java.util.List;

public class AuctionsListResponse extends GeneralResponse {

    private List<Auction> auctions;

    public AuctionsListResponse(String statusCode, String statusMsg, List<Auction> auctions) {
        super(statusCode, statusMsg);
        this.auctions = auctions;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }
}
