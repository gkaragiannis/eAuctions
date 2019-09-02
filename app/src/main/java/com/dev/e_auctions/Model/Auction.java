package com.dev.e_auctions.Model;

import java.util.List;

public class Auction {
    private Integer id;
    private String nameOfItem;
    private String image;
    private User seller;
    private String startedTime;
    private String endingTime;
    private String itemDescription;
    private String itemLocation;
    private String itemCountry;
    private Double initialPrice;
    private List<Bid> bids;
    private List<Category> categories;

    public Integer getId() {
        return id;
    }

    public String getNameOfItem() {
        return nameOfItem;
    }

    public String getImage() {
        return image;
    }

    public User getSeller() {
        return seller;
    }

    public String getStartedTime() {
        return startedTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getItemCountry() {
        return itemCountry;
    }

    public Double getInitialPrice() {
        return initialPrice;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
