package com.dev.e_auctions.apiresponses;

public class UsersResponse extends GeneralResponse {
    private String token;

    public UsersResponse(String statusCode, String statusMsg, String token) {
        super(statusCode, statusMsg);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
