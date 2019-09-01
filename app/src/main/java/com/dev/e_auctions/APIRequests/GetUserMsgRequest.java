package com.dev.e_auctions.apirequests;

public class GetUserMsgRequest {
    private String token;

    public GetUserMsgRequest(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
