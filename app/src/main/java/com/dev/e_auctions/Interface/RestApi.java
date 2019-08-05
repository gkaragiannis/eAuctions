package com.dev.e_auctions.Interface;

import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @GET("users")
    Call<List<User>> getUsers();
            //@Query("username") String username
    /*);*/

    @GET("users")
    Call<List<User>> getUserByUsername(@Query("username") String usernameString);

    @POST("users")
    Call<User> createNewUser(@Body User newUser);

    @GET("auctions")
    Call<List<Auction>> getAllAuctions();

    @GET("auctions")
    Call<List<Auction>> getAuctionsById(@Query("id") int id);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByCategory(@Query("category") String categoryString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsBySellerId(@Query("seller_id") String tokenString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByBidderId(@Query("bidder_id") String tokenString);

}
