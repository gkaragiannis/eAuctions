package com.dev.e_auctions.APIRequests;

/**
 *
 */
public class AuthenticateUserRequest {

    private String username;
    private String password;

    /**
     *
     * @param username
     * @param password
     */
    public AuthenticateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
