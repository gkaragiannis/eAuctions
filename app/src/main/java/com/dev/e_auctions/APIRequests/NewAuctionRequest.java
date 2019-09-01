package com.dev.e_auctions.apirequests;

import com.dev.e_auctions.Model.Category;

import java.util.Set;

public class NewAuctionRequest {

    private String token;
    private String nameOfItem;
    private Set<Category> categories;
    private String startedTime;
    private String endingTime;
    private String itemDescription;
    private Double initialPrice;


    public void setToken(String token) {
        this.token = token;
    }

    public void setNameOfItem(String nameOfItem) {
        this.nameOfItem = nameOfItem;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void setStartedTime(String startedTime) {
        this.startedTime = startedTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setInitialPrice(Double initialPrice) {
        this.initialPrice = initialPrice;
    }
}
