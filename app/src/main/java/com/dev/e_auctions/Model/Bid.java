package com.dev.e_auctions.Model;

public class Bid {
    String bidder_id;
    Float bid_value;
    Integer auction_id;

    public Bid(String bidder_id, Float bid_value, Integer auction_id) {
        this.bidder_id = bidder_id;
        this.bid_value = bid_value;
        this.auction_id = auction_id;
    }
}
