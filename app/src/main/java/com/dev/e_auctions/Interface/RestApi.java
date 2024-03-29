package com.dev.e_auctions.Interface;

import com.dev.e_auctions.APIRequests.AuctionByFieldRequest;
import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
import com.dev.e_auctions.APIRequests.GetMessagesRequest;
import com.dev.e_auctions.APIRequests.MarkMessageAsReadRequest;
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
import com.dev.e_auctions.APIResponses.GetMessagesResponse;
import com.dev.e_auctions.APIResponses.NewAuctionResponse;
import com.dev.e_auctions.APIResponses.AuthenticateUserResponse;
import com.dev.e_auctions.APIResponses.RegisterNewUserResponse;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.Model.Image;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RestApi {

    /*-- USERS --*/

    /**
     *
     * @param signInRequest
     * @return
     */
    @POST("users/authenticateuser")
    Call<AuthenticateUserResponse> postSignIn(@Body AuthenticateUserRequest signInRequest);

    /**
     *
     * @param signUpRequest
     * @return
     */
    @POST("users/registernewuser")
    Call<RegisterNewUserResponse> postSignUp(@Body RegisterNewUserRequest signUpRequest);

    /**
     *
     * @param rateUserRequest
     * @return
     */
    @POST("users/rateuser")
    Call<GeneralResponse> postRateUser(@Body RateUserRequest rateUserRequest);


    /*-- AUCTIONS --*/

    /**
     *
     * @return
     */
    @GET("auctions/allauctions")
    Call<AuctionListResponse> getAllAuctions();

    /**
     *
     * @return
     */
    @GET("auctions/openauctions")
    Call<AuctionListResponse> getOpenAuctions();

    /**
     *
     * @param newAcutionRequest
     * @return
     */
    @POST("auctions/newauction")
    Call<NewAuctionResponse> postNewAuction(@Body NewAcutionRequest newAcutionRequest);

    /**
     *
     * @param id
     * @return
     */
    @GET("auctions/getauctionbyid")
    Call<AuctionResponse> getAuctionsById(@Query("auctionId") String id);

    /**
     *
     * @param auctionByFieldRequest
     * @return
     */
    @POST("/auctions/getauctionsbyfield")
    Call<AuctionListResponse> getAuctionsByField(@Body AuctionByFieldRequest auctionByFieldRequest);

    /**
     *
     * @param deleteAuctionRequest
     * @return
     */
    @POST("auctions/deleteauctionbyid")
    Call<GeneralResponse> postDeleteAuction(@Body DeleteAuctionRequest deleteAuctionRequest);


    /*-- ITEMCATEGORIES --*/

    /**
     *
     * @return
     */
    @GET("itemcategories/allcategories")
    Call<AllCategoriesResponse> getCategories();


    /*-- BIDS --*/

    /**
     *
     * @param newBidRequest
     * @return
     */
    @POST("bids/newbid")
    Call<GeneralResponse> postNewBid(@Body NewBidRequest newBidRequest);

    /*-- MESSAGES --*/

    /**
     *
     * @param newMessageRequest
     * @return
     */
    @POST("/message/newmessage")
    Call<GeneralResponse> postNewMessage(@Body NewMessageRequest newMessageRequest);

    /**
     *
     * @param token
     * @return
     */
    @POST("/message/getinbox")
    Call<GetMessagesResponse> postGetInbox(@Body GetMessagesRequest token);

    /**
     *
     * @param token
     * @return
     */
    @POST("/message/getsent")
    Call<GetMessagesResponse> postGetOutbox(@Body GetMessagesRequest token);

    /**
     *
     * @param markMessageAsReadRequest
     * @return
     */
    @POST("/message/markmessageasread")
    Call<GeneralResponse> postMarkMessageAsRead(@Body MarkMessageAsReadRequest markMessageAsReadRequest);

    /*-- IMAGES --*/

    /**
     *
     * @param content
     * @param file
     * @param token
     * @param auctionId
     * @return
     */
    @Multipart
    @POST("/image/upload")
//    Call<GeneralResponse> postUploadImage (@Part("file") RequestBody file , @Part("token") RequestBody token, @Part("auctionId") RequestBody auctionId);
    Call<GeneralResponse> postUploadImage (@Header("Content-Type") String content,
                                           @Part MultipartBody.Part file ,
                                           @Part("token") RequestBody token,
                                           @Part("auctionId") RequestBody auctionId);
//    Call<GeneralResponse> postUploadImage (@PartMap Map<String, RequestBody> map);


}
