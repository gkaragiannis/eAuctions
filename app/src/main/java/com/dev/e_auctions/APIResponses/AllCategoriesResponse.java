package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.Category;

import java.util.List;

public class AllCategoriesResponse extends GeneralResponse{

    /*private String statusCode;
    private String statusMsg;*/
    private List<Category> categories;

    /*public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }*/

    public List<Category> getCategories() {
        return categories;
    }
}
