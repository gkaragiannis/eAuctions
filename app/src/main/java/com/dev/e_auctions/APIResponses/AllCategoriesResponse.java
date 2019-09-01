package com.dev.e_auctions.apiresponses;

import com.dev.e_auctions.Model.Category;

import java.util.List;

public class AllCategoriesResponse extends GeneralResponse {

    private List<Category> categories;

    public AllCategoriesResponse(String statusCode, String statusMsg, List<Category> categories) {
        super(statusCode, statusMsg);
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
