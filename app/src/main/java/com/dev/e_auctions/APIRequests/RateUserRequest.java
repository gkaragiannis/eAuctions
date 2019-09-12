package com.dev.e_auctions.APIRequests;

import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.User;

public class RateUserRequest {
    private String userToken;
    private int userToBeRatedId;
    private int auctionId;
    private int rate;

    public RateUserRequest(String userToken, int userToBeRatedId, int auctionId, int rate) {
        this.userToken = userToken;
        this.userToBeRatedId = userToBeRatedId;
        this.auctionId = auctionId;
        this.rate = rate;
    }

    public String getUserToken() {
        return userToken;
    }

    public int getUserToBeRatedId() {
        return userToBeRatedId;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public int getRate() {
        return rate;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setUserToBeRated(int userToBeRatedId) {
        this.userToBeRatedId = userToBeRatedId;
    }

    public void setAuction(int auctionId) {
        this.auctionId = auctionId;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
