package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.User;

public class AuthenticateUserResponse extends RegisterNewUserResponse {

    private User user;

    public User getUser() {
        return user;
    }
}
