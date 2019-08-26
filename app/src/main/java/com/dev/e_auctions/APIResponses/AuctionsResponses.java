package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.Auction;

import java.util.List;

public class AuctionsResponses {

    private String statusCode;
    private String statusMsg;
    private List<Auction> auctions;

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }
}
