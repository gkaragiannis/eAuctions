package com.dev.e_auctions.Interface;

import com.dev.e_auctions.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {

    @GET("posts")
    Call<List<User>> getUser();
}
