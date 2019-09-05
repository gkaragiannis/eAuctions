package com.dev.e_auctions.apiresponses;

import com.google.gson.annotations.SerializedName;

public class ImageApiResponse extends GeneralResponse {

    @SerializedName(value = "data")
    private byte[] data;

    public ImageApiResponse(String statusCode, String statusMsg, byte[] data) {
        super(statusCode, statusMsg);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        System.out.println("Setter gia to image");

    }

    @Override
    public String toString() {
        return "ImageApiResponse{" +
                "data=" + data +
                ", statusCode='" + statusCode + '\'' +
                ", statusMsg='" + statusMsg + '\'' +
                '}';
    }
}
