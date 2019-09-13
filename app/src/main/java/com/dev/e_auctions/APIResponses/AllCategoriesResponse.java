package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.Category;

import java.util.List;

public class AllCategoriesResponse extends GeneralResponse{

    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }
}
