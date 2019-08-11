package com.dev.e_auctions.Client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    private static String domainURL = "https://my-json-server.typicode.com/gkaragiannis/testREST/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(domainURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}
