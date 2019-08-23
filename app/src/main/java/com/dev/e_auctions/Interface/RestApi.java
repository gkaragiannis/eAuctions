package com.dev.e_auctions.Interface;

import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.Bid;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.Model.Image;
import com.dev.e_auctions.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @GET("users")
    Call<List<User>> getUserByUsername(@Query("username") String usernameString, @Query("password") String passwordString);

    @POST("users")
    Call<User> postNewUser(@Body User newUser);

    @GET("auctions")
    Call<List<Auction>> getAllAuctions();

    @GET("auctions")
    Call<List<Auction>> getAuctionsById(@Query("id") String id);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByCategory(@Query("category") String categoryString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsBySellerId(@Query("seller_id") String tokenString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByBidderId(@Query("bidder_id") String tokenString);

    @POST("auctions")
    Call<Auction> postNewAuction(@Body Auction newAuction);

    @GET("categories")
    Call<List<Category>> getCategories();

    @POST("categories")
    Call<List<Category>> postCategories(@Body List<Category> categoryList);

    @POST("bids")
    Call<Bid> postNewBid(@Body Bid newBid);

    @POST("/scripts/uploadImage.php")
    Call<Image>  uploadImage(@Field("title") String title, @Field("image") String image);

}
