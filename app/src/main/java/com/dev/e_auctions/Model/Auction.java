package com.dev.e_auctions.Model;

public class Auction {
    private Integer id;
    private String name;
    private Integer total_bids;
    private Float first_bid;
    private Float last_bid;
    private String created;
    private String ends;
    private String seller_id;
    private String description;
    private String image;

    //Constructors
    public Auction(String name, Float first_bid, String created, String ends, String seller_id, String description, String image) {
        this.name = name;
        this.first_bid = first_bid;
        this.created = created;
        this.ends = ends;
        this.seller_id = seller_id;
        this.description = description;
        this.image = image;
    }

    //Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTotal_bids() {
        return total_bids;
    }

    public Float getFirst_bid() {
        return first_bid;
    }

    public Float getLast_bid() {
        return last_bid.floatValue();
    }

    public String getCreated() {
        return created;
    }

    public String getEnds() {
        return ends;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    //Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal_bids(Integer total_bids) {
        this.total_bids = total_bids;
    }

    public void setFirst_bid(Float first_bid) {
        this.first_bid = first_bid;
    }

    public void setLast_bid(Float last_bid) {
        this.last_bid = last_bid;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
