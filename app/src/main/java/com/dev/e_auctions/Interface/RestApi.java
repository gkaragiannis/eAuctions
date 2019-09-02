package com.dev.e_auctions.Interface;

import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
import com.dev.e_auctions.APIRequests.NewAcutionRequest;
import com.dev.e_auctions.APIRequests.NewBidRequest;
import com.dev.e_auctions.APIRequests.SignInRequest;
import com.dev.e_auctions.APIRequests.SignUpRequest;
import com.dev.e_auctions.APIResponses.AllCategoriesResponse;
import com.dev.e_auctions.APIResponses.AuctionsResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.APIResponses.UsersResponse;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.Model.Image;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    /*-- USERS --*/
    @POST("/users/authenticateuser")
    Call<UsersResponse> postSignIn(@Body SignInRequest signInRequest);

    @POST("/users/registernewuser")
    Call<UsersResponse> postSignUp(@Body SignUpRequest signUpRequest);


    /*-- AUCTIONS --*/
    @GET("/auctions/allauctions")
    Call<AuctionsResponse> getAllAuctions();

    @GET("/auctions/openauctions")
    Call<AuctionsResponse> getOpenAuctions();

    @POST("/auctions/newauction")
    Call<GeneralResponse> postNewAuction(@Body NewAcutionRequest newAcutionRequest);

    @GET("/auctions/getauctionbyid")
    Call<AuctionsResponse> getAuctionsById(@Query("auctionId") String id);

    @GET("auctions")
    Call<AuctionsResponse> getAuctionsBySellerId(@Query("seller_id") String tokenString);

    @POST("/auctions/deleteauctionbyid")
    Call<GeneralResponse> postDeleteAuction(@Body DeleteAuctionRequest deleteAuctionRequest);


    /*-- ITEMCATEGORIES --*/
    @GET("itemcategories/allcategories")
    Call<AllCategoriesResponse> getCategories();


    /*-- BIDS --*/
    @POST("/bids/newbidin")
    Call<GeneralResponse> postNewBid(@Body NewBidRequest newBidRequest);


    //old
    @GET("auctions")
    Call<List<Auction>> getAuctionsByCategory(@Query("category") String categoryString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByBidderId(@Query("bidder_id") String tokenString);

    @POST("categories")
    Call<List<Category>> postCategories(@Body List<Category> categoryList);

    @POST("/scripts/uploadImage.php")
    Call<Image>  uploadImage(@Field("title") String title, @Field("image") String image);

}
