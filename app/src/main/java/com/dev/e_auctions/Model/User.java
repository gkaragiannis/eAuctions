package com.dev.e_auctions.Model;

public class User {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String address;
    private Double bidderRating;
    private Integer bidderRatingVotes;
    private Double sellerRating;
    private Double sellerRatingVotes;

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public Double getBidderRating() {
        return bidderRating;
    }

    public Integer getBidderRatingVotes() {
        return bidderRatingVotes;
    }

    public Double getSellerRating() {
        return sellerRating;
    }

    public Double getSellerRatingVotes() {
        return sellerRatingVotes;
    }
}
