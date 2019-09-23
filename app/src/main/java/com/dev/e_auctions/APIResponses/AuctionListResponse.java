package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.Auction;

import java.util.List;

/**
 *
 */
public class AuctionListResponse extends GeneralResponse {

    private List<Auction> auctions;

    /**
     *
     * @return
     */
    public List<Auction> getAuctions() {
        return auctions;
    }
}
