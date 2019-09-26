package com.dev.e_auctions.Model;

/**
 *
 */
public class Category {
    private Integer categoryId;
    private String categoryName;
    private String imagePath;

    /**
     *
     * @param categoryId
     * @param categoryName
     */
    public Category(Integer categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return imagePath;
    }
}
