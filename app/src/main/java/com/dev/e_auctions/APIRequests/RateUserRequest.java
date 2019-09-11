package com.dev.e_auctions.APIRequests;

import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.User;

public class RateUserRequest {
    private String userToken;
    private User userToBeRated;
    private Auction auction;
    private int rate;

    public RateUserRequest(String userToken, User userToBeRated, Auction auction, int rate) {
        this.userToken = userToken;
        this.userToBeRated = userToBeRated;
        this.auction = auction;
        this.rate = rate;
    }

    public String getUserToken() {
        return userToken;
    }

    public User getUserToBeRated() {
        return userToBeRated;
    }

    public Auction getAuction() {
        return auction;
    }

    public int getRate() {
        return rate;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setUserToBeRated(User userToBeRated) {
        this.userToBeRated = userToBeRated;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
