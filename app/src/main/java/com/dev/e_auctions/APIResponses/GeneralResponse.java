package com.dev.e_auctions.apiresponses;

public class GeneralResponse {

    protected String statusCode;
    protected String statusMsg;

    public GeneralResponse(String statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public GeneralResponse() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }
}
