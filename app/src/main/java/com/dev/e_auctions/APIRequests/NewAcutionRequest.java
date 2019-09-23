package com.dev.e_auctions.APIRequests;

import com.dev.e_auctions.Model.Category;

import java.util.List;

/**
 *
 */
public class NewAcutionRequest {

    private String token;
    private String nameOfItem;
    private List<Category> categories;
    private String startedTime;
    private String endingTime;
    private String itemDescription;
    private String initialPrice;

    /**
     *
     * @param token
     * @param nameOfItem
     * @param categories
     * @param startedTime
     * @param endingTime
     * @param itemDescription
     * @param initialPrice
     */
    public NewAcutionRequest(String token, String nameOfItem, List<Category> categories, String startedTime, String endingTime, String itemDescription, String initialPrice) {
        this.token = token;
        this.nameOfItem = nameOfItem;
        this.categories = categories;
        this.startedTime = startedTime;
        this.endingTime = endingTime;
        this.itemDescription = itemDescription;
        this.initialPrice = initialPrice;
    }
}
