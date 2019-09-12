package com.dev.e_auctions.Interface;

import com.dev.e_auctions.APIRequests.AuctionByFieldRequest;
import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
import com.dev.e_auctions.APIRequests.NewAcutionRequest;
import com.dev.e_auctions.APIRequests.NewBidRequest;
import com.dev.e_auctions.APIRequests.NewMessageRequest;
import com.dev.e_auctions.APIRequests.RateUserRequest;
import com.dev.e_auctions.APIRequests.AuthenticateUserRequest;
import com.dev.e_auctions.APIRequests.RegisterNewUserRequest;
import com.dev.e_auctions.APIResponses.AllCategoriesResponse;
import com.dev.e_auctions.APIResponses.AuctionListResponse;
import com.dev.e_auctions.APIResponses.AuctionResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.APIResponses.NewAuctionResponse;
import com.dev.e_auctions.APIResponses.AuthenticateUserResponse;
import com.dev.e_auctions.APIResponses.RegisterNewUserResponse;
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
    @POST("users/authenticateuser")
    Call<AuthenticateUserResponse> postSignIn(@Body AuthenticateUserRequest signInRequest);

    @POST("users/registernewuser")
    Call<RegisterNewUserResponse> postSignUp(@Body RegisterNewUserRequest signUpRequest);

    @POST("users/rateuser")
    Call<GeneralResponse> postRateUser(@Body RateUserRequest rateUserRequest);


    /*-- AUCTIONS --*/
    @GET("auctions/allauctions")
    Call<AuctionListResponse> getAllAuctions();

    @GET("auctions/openauctions")
    Call<AuctionListResponse> getOpenAuctions();

    @POST("auctions/newauction")
    Call<NewAuctionResponse> postNewAuction(@Body NewAcutionRequest newAcutionRequest);

    @GET("auctions/getauctionbyid")
    Call<AuctionResponse> getAuctionsById(@Query("auctionId") String id);

    @POST("/auctions/getauctionsbyfield")
    Call<AuctionListResponse> getAuctionsByField(@Body AuctionByFieldRequest auctionByFieldRequest);

    @POST("auctions/deleteauctionbyid")
    Call<GeneralResponse> postDeleteAuction(@Body DeleteAuctionRequest deleteAuctionRequest);


    /*-- ITEMCATEGORIES --*/
    @GET("itemcategories/allcategories")
    Call<AllCategoriesResponse> getCategories();


    /*-- BIDS --*/
    @POST("bids/newbid")
    Call<GeneralResponse> postNewBid(@Body NewBidRequest newBidRequest);

    /*-- MESSAGES --*/
    @POST("/message/newmessage")
    Call<GeneralResponse> postNewMessage(@Body NewMessageRequest newMessageRequest);


    //old
    @GET("auctions")
    Call<List<Auction>> getAuctionsByCategory(@Query("category") String categoryString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByBidderId(@Query("bidder_id") String tokenString);

    @POST("categories")
    Call<List<Category>> postCategories(@Body List<Category> categoryList);

    @POST("scripts/uploadImage.php")
    Call<Image>  uploadImage(@Field("title") String title, @Field("image") String image);

}
