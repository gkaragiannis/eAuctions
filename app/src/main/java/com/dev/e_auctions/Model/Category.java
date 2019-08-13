package com.dev.e_auctions.Model;

public class Category {
    private Integer Id;
    private String name;
    private String image;

    //Constructos
    public Category(Integer id, String name, String image) {
        Id = id;
        this.name = name;
        this.image = image;
    }

    //Getters
    public Integer getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    //Setters
    public void setId(Integer id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
