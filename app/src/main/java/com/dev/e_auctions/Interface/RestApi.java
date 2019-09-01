package com.dev.e_auctions.Interface;

import com.dev.e_auctions.apirequests.DeleteAuctionRequest;
import com.dev.e_auctions.apirequests.GetAuctionsByBidderRequest;
import com.dev.e_auctions.apirequests.GetUserMsgRequest;
import com.dev.e_auctions.apirequests.NewAuctionRequest;
import com.dev.e_auctions.apirequests.NewBidRequest;
import com.dev.e_auctions.apirequests.NewMessageRequest;
import com.dev.e_auctions.apirequests.RateUserRequest;
import com.dev.e_auctions.apirequests.SignInRequest;
import com.dev.e_auctions.apirequests.SignUpRequest;
import com.dev.e_auctions.apiresponses.AllCategoriesResponse;
import com.dev.e_auctions.apiresponses.AuctionResponse;
import com.dev.e_auctions.apiresponses.AuctionsListResponse;
import com.dev.e_auctions.apiresponses.AuthenticateUserResponse;
import com.dev.e_auctions.apiresponses.GeneralResponse;
import com.dev.e_auctions.apiresponses.MessagesResponse;
import com.dev.e_auctions.apiresponses.UsersResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    //-------------- User API calls --------------
    @POST("/users/authenticateuser")
    Call<AuthenticateUserResponse> postSignIn(@Body SignInRequest signInRequest);

    @POST("/users/registernewuser")
    Call<UsersResponse> postSignUp(@Body SignUpRequest signUpRequest);

    @POST("/users/rateuser")
    Call<UsersResponse> rateUser(@Body RateUserRequest request);


    //-------------- Auction API calls --------------
    @GET("/auctions/allauctions")
    Call<AuctionsListResponse> getAllAuctions();

    @GET("/auctions/openauctions")
    Call<AuctionsListResponse> getOpenAuctions();

    @POST("/auctions/newauction")
    Call<GeneralResponse> postNewAuction(@Body NewAuctionRequest newAuctionRequest);

    @GET("/auctions/getauctionbyid")
    Call<AuctionResponse> getAuctionsById(@Query("auctionId") String id);

    @POST("/auctions/deleteauctionbyid")
    Call<GeneralResponse> postDeleteAuction(@Body DeleteAuctionRequest deleteAuctionRequest);

    //fieldNames : categoryId OR sellerId . fieldValues : the appropriate id
    //******** This is a GET Request *****************
    @GET("/auctions/getauctionsbyfield")
    Call<AuctionsListResponse> getAuctionsByField(@Query("fieldName") String fieldName, @Query("fieldValue") String fieldValue);

    //******** This is a POST Request *****************
    //Find all the auctions that a bidder has participated
    @POST("/auctions/getauctionsbyfield")
    Call<AuctionsListResponse> getAuctionsByFieldForBidder(@Body GetAuctionsByBidderRequest request);


    //-------------- Category API calls --------------
    @GET("itemcategories/allcategories")
    Call<AllCategoriesResponse> getCategories();

    //-------------- Bids API calls --------------
    @POST("/bids/newbidin")
    Call<GeneralResponse> postNewBid(@Body NewBidRequest newBidRequest);


    //-------------- Messages API calls --------------
    @POST("/message/newmessage")
    Call<GeneralResponse> newMessage(@Body NewMessageRequest request);

    @POST("/message/getinbox")
    Call<MessagesResponse> getInboxMessages(@Body GetUserMsgRequest request);

    @POST("/message/getsent")
    Call<MessagesResponse> getSentMessages(@Body GetUserMsgRequest request);

    //TODO: needs the logic implementation in the API side
    @POST("/message/getunreadmessages")
    Call<MessagesResponse> getUreadMessages(@Body GetUserMsgRequest request);

    //-------------- Images API calls --------------
    //TODO: need implementation to upload the image to the auction.

//    @POST("categories")
//    Call<List<Category>> postCategories(@Body List<Category> categoryList);
//
//    @POST("/scripts/uploadImage.php")
//    Call<Image>  uploadImage(@Field("title") String title, @Field("image") String image);
//
//
//    @GET("image/getimages")
//    Call<ImageApiResponse>  getImage(@Query("auctionId") Long auctionId);
//


    //TODO : needs to be deleted
    @GET("users/allusers")
    Call<AllUsersResponse> getAllUsers();


}
