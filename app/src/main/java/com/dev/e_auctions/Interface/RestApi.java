package com.dev.e_auctions.Interface;

import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
import com.dev.e_auctions.APIRequests.NewAcutionRequest;
import com.dev.e_auctions.APIRequests.NewBidRequest;
import com.dev.e_auctions.APIRequests.SignInRequest;
import com.dev.e_auctions.APIRequests.SignUpRequest;
import com.dev.e_auctions.APIResponses.AllCategoriesResponse;
import com.dev.e_auctions.APIResponses.AuctionsResponses;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.APIResponses.UsersResponse;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.Bid;
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

    @POST("/users/authenticateuser")
    Call<UsersResponse> postSignIn(@Body SignInRequest signInRequest);

    @POST("/users/registernewuser")
    Call<UsersResponse> postSignUp(@Body SignUpRequest signUpRequest);

    @GET("/auctions/allauctions")
    Call<AuctionsResponses> getAllAuctions();

    @GET("/auctions/openauctions")
    Call<AuctionsResponses> getOpenAuctions();

    @POST("/auctions/newauction")
    Call<GeneralResponse> postNewAuction(@Body NewAcutionRequest newAcutionRequest);

    @GET("/auctions/getauctionbyid")
    Call<Auction> getAuctionsById(@Query("auctionId") String id);

    @POST("/auctions/deleteauctionbyid")
    Call<GeneralResponse> postDeleteAuction(@Body DeleteAuctionRequest deleteAuctionRequest);

    @GET("itemcategories/allcategories")
    Call<AllCategoriesResponse> getCategories();

    @POST("/bids/newbidin")
    Call<GeneralResponse> postNewBid(@Body NewBidRequest newBidRequest);


    //old
    @GET("auctions")
    Call<List<Auction>> getAuctionsByCategory(@Query("category") String categoryString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsBySellerId(@Query("seller_id") String tokenString);

    @GET("auctions")
    Call<List<Auction>> getAuctionsByBidderId(@Query("bidder_id") String tokenString);

    @POST("categories")
    Call<List<Category>> postCategories(@Body List<Category> categoryList);

    @POST("/scripts/uploadImage.php")
    Call<Image>  uploadImage(@Field("title") String title, @Field("image") String image);

}
