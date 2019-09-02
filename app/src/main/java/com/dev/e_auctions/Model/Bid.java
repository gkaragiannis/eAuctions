package com.dev.e_auctions.Model;

public class Bid {
    Integer bid_id;
    User bidder;
    String bidTime;
    Double bidPrice;

    public Integer getBid_id() {
        return bid_id;
    }

    public User getBidder() {
        return bidder;
    }

    public String getBidTime() {
        return bidTime;
    }

    public Double getBidPrice() {
        return bidPrice;
    }
}
