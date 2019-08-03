package com.dev.e_auctions.Model;

public class Auction {
    private Integer id;
    private String name;
    private Integer total_bids;
    private Integer first_bid_id;
    private Integer last_bid_id;
    private String created;
    private String ends;
    private Integer seller_id;
    private String description;
    private String image;

    //Constructors
    public Auction(Integer id) {
        this.id = id;
    }

    public Auction(Integer id, String name, Integer total_bids, Integer first_bid_id, Integer last_bid_id, String created, String ends, Integer seller_id, String description, String image) {
        this.id = id;
        this.name = name;
        this.total_bids = total_bids;
        this.first_bid_id = first_bid_id;
        this.last_bid_id = last_bid_id;
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

    public Integer getFirst_bid_id() {
        return first_bid_id;
    }

    public Integer getLast_bid_id() {
        return last_bid_id;
    }

    public String getCreated() {
        return created;
    }

    public String getEnds() {
        return ends;
    }

    public Integer getSeller_id() {
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

    public void setFirst_bid_id(Integer first_bid_id) {
        this.first_bid_id = first_bid_id;
    }

    public void setLast_bid_id(Integer last_bid_id) {
        this.last_bid_id = last_bid_id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
