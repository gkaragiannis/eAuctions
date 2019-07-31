package com.dev.e_auctions.Interface;

import com.dev.e_auctions.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    /*@GET("users")
    Call<List<User>> getUsers(
            //@Query("username") String username
    );*/

    @GET("users")
    Call<List<User>> getUserByUsername(@Query("username") String usernameString);

    //@GET("auctions")


    @POST("users")
    Call<User> createNewUser(@Body User newUser);

}
