package com.dev.e_auctions.apiresponses;

import com.dev.e_auctions.Model.User;

public class AuthenticateUserResponse extends GeneralResponse {
    private String token;
    private User user;

    public AuthenticateUserResponse(String statusCode, String statusMsg, String token, User user) {
        super(statusCode, statusMsg);
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
