package com.dev.e_auctions.APIRequests;

/**
 * Implements a request for
 */
public class AuctionByFieldRequest {
    private String fieldName, fieldValue;

    /**
     *
     * @param fieldName
     * @param fieldValue
     */
    public AuctionByFieldRequest(String fieldName, String fieldValue) {
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
