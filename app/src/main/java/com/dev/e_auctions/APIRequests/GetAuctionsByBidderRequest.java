package com.dev.e_auctions.apirequests;

public class GetAuctionsByBidderRequest {
    private String fieldName;
    private String fieldValue;

    public GetAuctionsByBidderRequest(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
