package com.dev.e_auctions.Client;

import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dev.e_auctions.Client.RestClient.getUnsafeOkHttpClient;

public class UploadImageClient {


    private static OkHttpClient client = getUnsafeOkHttpClient();


    public static void uploadImage(String token, String auctionId, File file) {

//        final Uri imageUri = data.getData();
//
//        final File file = new File(getPath(this, imageUri));
        Log.d("Auction ", "before the body builder ");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("auctionId", auctionId)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse ("image/jpeg"), file))
                .build();

        Log.d("Auction ", "Before the request builder");
        Request request = new Request.Builder()
                .url("https://83.212.109.213:8080/image/upload")
                .post(requestBody)
                .build();

        try  {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                Log.d("TEST", response.toString());
            }

            System.out.println(response.body().string());
        } catch (Exception e) {
            Log.d("Auction ", "Not successful upload");
            e.printStackTrace();
        }
    }





}
