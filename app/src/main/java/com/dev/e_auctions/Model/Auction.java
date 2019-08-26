package com.dev.e_auctions.Model;

import java.util.List;

public class Auction {
    private Integer id;
    private String nameOfItem;
    private User seller;
    private Double initialPrice;
    private String startedTime;
    private String endingTime;
    private String itemDescription;
    private String itemLocation;
    private String itemCountry;
    private List<Bid> bids;
    private String image;


}
