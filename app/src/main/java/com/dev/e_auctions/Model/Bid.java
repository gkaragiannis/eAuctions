package com.dev.e_auctions.Model;

/**
 *
 */
public class Bid implements Comparable<Bid>{
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

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Bid o) {
        return this.getBidPrice().compareTo(o.getBidPrice());
    }
}
