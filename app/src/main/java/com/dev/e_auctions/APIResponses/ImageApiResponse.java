package com.dev.e_auctions.apiresponses;

import java.util.List;

public class ImageApiResponse extends GeneralResponse {

    private List<byte[]> data;

    public ImageApiResponse(String statusCode, String statusMsg, List<byte[]> data) {
        super(statusCode, statusMsg);
        this.data = data;
    }

    public List<byte[]> getData() {
        return data;
    }

    public void setData(List<byte[]> data) {
        this.data = data;
    }
}
