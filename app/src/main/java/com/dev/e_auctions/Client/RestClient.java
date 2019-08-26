package com.dev.e_auctions.Client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    //private static String domainURL = "https://my-json-server.typicode.com/gkaragiannis/testREST/";
    //private static String domainURL = "http://83.212.109.213:8081/";
    private static String domainURL = "https://gkarag.free.beeceptor.com";
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
