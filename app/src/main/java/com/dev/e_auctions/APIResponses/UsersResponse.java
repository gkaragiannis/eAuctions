package com.dev.e_auctions.APIResponses;

import com.dev.e_auctions.Model.User;

public class UsersResponse extends GeneralResponse {

    /*private String statusCode;
    private String statusMsg;*/
    private String token;
    private User user;

/*public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }*/

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
