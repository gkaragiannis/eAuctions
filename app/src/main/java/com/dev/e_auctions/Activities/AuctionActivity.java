package com.dev.e_auctions.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
import com.dev.e_auctions.APIRequests.NewBidRequest;
import com.dev.e_auctions.APIRequests.NewMessageRequest;
import com.dev.e_auctions.APIRequests.RateUserRequest;
import com.dev.e_auctions.APIResponses.AuctionResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Adapter.ExpandableBidListAdapter;
import com.dev.e_auctions.Adapter.ExpandableCategoryListAdapter;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.Bid;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.R;
import com.dev.e_auctions.Utilities.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionActivity extends AppCompatActivity {

    private ImageView auctionImage;
    private TextView auctionName, startingDate, endDate, auctionDesc, auctionLocation, sellerRatingNum, sellerRatingVotes;
    private ProgressBar durationBar;
    private ClickNumberPickerView btnNewBidValue;
    private ImageButton btnAuctionBidEditBtn;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnFAB;
    private ExpandableListView categoryListView;
    private ExpandableCategoryListAdapter categoryListAdapter;
    private ExpandableListView bidListView;
    private ExpandableBidListAdapter bidListAdapter;
    private RatingBar rtnBar;

    private String auctionId = "";
    private ArrayList<String> categoryListHeader;
    private HashMap<String, List<String>> categoryListMap;
    private ArrayList<String> bidListHeader;
    private HashMap<String, List<Bid>> bidListMap;

    /**
     * AuctionActivity's onCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        //Initialize View
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        auctionImage = (ImageView) findViewById(R.id.auctionImage);

        btnNewBidValue = (ClickNumberPickerView) findViewById(R.id.btnNewBidValue);

        auctionName = (TextView) findViewById(R.id.auctionName);
        durationBar = (ProgressBar) findViewById(R.id.durationBar);
        startingDate = (TextView) findViewById(R.id.startingDate);
        endDate = (TextView) findViewById(R.id.endDate);

        bidListView = (ExpandableListView)findViewById(R.id.auctionBidsELV);
        btnAuctionBidEditBtn = (ImageButton) findViewById(R.id.auctionBidEditBtn);
        btnAuctionBidEditBtn.setOnClickListener(EditBidClickListener);

        auctionDesc = (TextView) findViewById(R.id.auctionDesc);
        categoryListView = (ExpandableListView)findViewById(R.id.auctionCategoriesELV);
        auctionLocation = (TextView) findViewById(R.id.auctionLocation);

        rtnBar = (RatingBar) findViewById(R.id.sellerRating);
        sellerRatingNum = (TextView) findViewById(R.id.sellerRatingNum);
        sellerRatingVotes = (TextView) findViewById(R.id.sellerRatingVotes);

        //maybe there is no need for next 2 rows
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        btnFAB = (FloatingActionButton) findViewById(R.id.btnFAB);

        //Get auction id from Intent
        if (getIntent() != null){
            auctionId = getIntent().getStringExtra("AuctionId");
        }
        if (!auctionId.isEmpty()){
            getAuction();
        }
    }


    //UI methods

    /**
     * Method to display auction item
     * @param auction The auction item to display
     * @throws ParseException
     */
    private void viewAuction(Auction auction) throws ParseException {
        configureFAB(auction);
        Picasso.get().load(auction.getImage()).into(auctionImage);
        auctionName.setText(auction.getNameOfItem());
        startingDate.setText(auction.getStartedTime());
        endDate.setText(auction.getEndingTime());
        int progress = 0;
        try {
            progress = getProgress(auction.getStartedTime(),
                    auction.getEndingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        durationBar.setProgress(progress);
        if (auction.getBids().size() > 0) {
            Collections.sort(auction.getBids(), Collections.reverseOrder());
            btnNewBidValue.setPickerValue(auction.getBids().get(0).getBidPrice().floatValue());
            initializeBidListViewData(auction.getBids());
        }
        else{
            btnNewBidValue.setPickerValue(auction.getInitialPrice().floatValue());
        }
        auctionDesc.setText(auction.getItemDescription());
        initializeCategoryListViewData(auction.getCategories());
        auctionLocation.setText(auction.getItemLocation() + ", " + auction.getItemCountry());
        rtnBar.setRating(auction.getSeller().getSellerRating().floatValue());
        sellerRatingNum.setText(Double.toString(auction.getSeller().getSellerRating()));
        if (auction.getSeller().getSellerRatingVotes()!=null)
            sellerRatingVotes.setText("out of " + Integer.toString(auction.getSeller().getSellerRatingVotes()) + " votes");
        if (progress >= 100)
            ratingDialog(auction);
    }

    /**
     * Method to configure FAB upon user's role
     * <p>User Role: bidder, buyer, seller</p>
     * @param auction The displayed auction item
     * @throws ParseException
     */
    @SuppressLint("RestrictedApi")
    private void configureFAB(Auction auction) throws ParseException {
        Date currentDate = new Date();
        Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(auction.getEndingTime());

        //Check user role
        if (Common.currentUser == null){
            //guest
            btnFAB.setVisibility(View.GONE);
            return;
        }
        else if (endDate.compareTo(currentDate) > 0){
            //auction is stil active
            if (Common.currentUser.getUsername().equals(auction.getSeller().getUsername())) {
                //user is seller
                if (auction.getBids().size() > 0) {
                    btnFAB.setVisibility(View.GONE);
                    return;
                } else {
                    btnFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_forever_white_24dp));
                    btnFAB.setOnClickListener(seller_FAB_ClickListener);
                }
            }
            else{
                //user is bidder
                btnFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_gavel_white_24dp));
                btnFAB.setOnClickListener(bidder_FAB_ClickListener);
            }
            btnFAB.setVisibility(View.VISIBLE);
            return;
        }
        else {
            //auction is inactive
            if (auction.getBids().size()==0){
                btnFAB.setVisibility(View.GONE);
            }
            else if ((Common.currentUser.getUsername().equals(auction.getSeller().getUsername()) ||
                    Common.currentUser.getUsername().equals(auction.getBids().get(0).getBidder().getUsername())) &&
                    !auction.getBids().isEmpty()) {
//                    auction.getBids().size() > 0) {
                //if user is seller or buyer and there are bids
                btnFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_bubble_white_24dp));
                //btnFAB.setOnClickListener(messenger_FAB_ClickListener);
                btnFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageDialog(auction);
                    }
                });
                btnFAB.setVisibility(View.VISIBLE);
            }
            else{
                btnFAB.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Method to display messaging dialog and send new message on auction's partner
     * <p>User Role: buyer, seller</p>
     * @param auction The auction
     */
    private void messageDialog(Auction auction) {
        Dialog messagingDialog = new Dialog(AuctionActivity.this);
        messagingDialog.setContentView(R.layout.messaging_dialog);
        messagingDialog.setCancelable(true);
        messagingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView messageReceiver = (TextView) messagingDialog.findViewById(R.id.messageReceiver);
        if (Common.currentUser.getUsername().equals(auction.getSeller().getUsername())){
            messageReceiver.setText("To " + auction.getBids().get(0).getBidder().getUsername());
        }
        else if (Common.currentUser.getUsername().equals(auction.getBids().get(0).getBidder().getUsername())){
            messageReceiver.setText("To " + auction.getSeller().getUsername());
        }
        EditText messageSubject = (EditText) messagingDialog.findViewById(R.id.messageSubject);
        EditText messageBody = (EditText) messagingDialog.findViewById(R.id.messageBody);

        Button messageSendBtn = (Button) messagingDialog.findViewById(R.id.messageSendBtn);
        messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(auction, messageSubject.getText().toString(), messageBody.getText().toString());
                messagingDialog.dismiss();
            }
        });
        messagingDialog.show();
    }

    /**
     * Method to view bids on ExpandableListAdapter
     * @param itemBids
     */
    private void initializeBidListViewData(List<Bid> itemBids) {
        bidListHeader = new ArrayList<>();
        bidListMap = new HashMap<>();

        bidListHeader.add("LatestBids");

        List<Bid> bids = new ArrayList<>();
        if (itemBids.size() == 0){
            bids = null;
        }
        else if (itemBids.size() <= 4) {
            for (Bid bid : itemBids) {
                bids.add(bid);
            }
        }
        else {
            for (int position = 0; position < 5; position++){
                bids.add(itemBids.get(position));
            }
        }

        bidListMap.put(bidListHeader.get(0), bids);

        bidListAdapter = new ExpandableBidListAdapter(AuctionActivity.this, bidListHeader, bidListMap);
        bidListView.setAdapter(bidListAdapter);
    }

    /**
     * Method to view categories on ExpandableListAdapter
     * @param itemCategories
     */
    private void initializeCategoryListViewData(List<Category> itemCategories) {
        categoryListHeader = new ArrayList<>();
        categoryListMap = new HashMap<>();

        categoryListHeader.add("Categories");

        List<String> categories = new ArrayList<>();
        for (Category category : itemCategories){
            categories.add(category.getCategoryName());
        }

        categoryListMap.put(categoryListHeader.get(0), categories);

        categoryListAdapter = new ExpandableCategoryListAdapter(AuctionActivity.this, categoryListHeader, categoryListMap);
        categoryListView.setAdapter(categoryListAdapter);
    }

    /**
     * Method to estimate auction's progress in percentage
     * @param created Auction's starting date as String with yyyy-MM-dd HH:mm
     * @param ends Auction's end date as String with yyyy-MM-dd HH:mm
     * @return An integer represents the progress
     * @throws ParseException
     */
    private int getProgress(String created, String ends) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date startingDate = format.parse(created);
        Date endDate = format.parse(ends);
        Date current = new Date();

        long diff1 = endDate.getTime() - startingDate.getTime();
        long diff2 = current.getTime() - startingDate.getTime();

        return (int) ((diff2*100)/diff1);
    }

    /**
     * Method to display a rating dialog to rate auction's partner
     * <p>User Role: buyer, seller</p>
     * @param auction
     */
    private void ratingDialog(Auction auction) {
        Dialog rankDialog = new Dialog(AuctionActivity.this);

        if (Common.currentUser == null || auction.getBids().size() == 0){
            return;
        }

        if (Common.currentUser.getUsername().equals(auction.getSeller().getUsername()) ||
                Common.currentUser.getUsername().equals(auction.getBids().get(0).getBidder().getUsername())) {
            //if user is seller or buyer
            rankDialog.setContentView(R.layout.rating_dialog);
            rankDialog.setCancelable(true);
            rankDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.ratingDialogBar);

            TextView ratingDialogHeader = (TextView) rankDialog.findViewById(R.id.ratingDialogHeader);

            Button btnRankDialog = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
            btnRankDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: make rating call
                    postRateUser(auction, ratingBar.getRating());
                    rankDialog.dismiss();
                }
            });

            if (Common.currentUser.getUsername().equals(auction.getSeller().getUsername())) {
                //rate bidder
                ratingDialogHeader.setText("Please rate the buyer");
            }
            else{
                //rate seller
                ratingDialogHeader.setText("Please rate the seller");
            }
            rankDialog.show();
        }
    }

    //UI listeners
    /**
     * FAB's ClickListener for bidder
     * <p>User Role: bidder</p>
     */
    View.OnClickListener bidder_FAB_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //pop up to approve action
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AuctionActivity.this);
            alertDialog.setCancelable(true)
                    .setMessage(R.string.BidSubmission)
                    //on yes post bid
                    .setPositiveButton(R.string.YesButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postBid();
                        }
                    })
                    //on no return
                    .setNegativeButton(R.string.NoButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    };

    /**
     * FAB's ClickListener for seller
     * <p>User Role: seller</p>
     */
    View.OnClickListener seller_FAB_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AuctionActivity.this);
            alertDialog.setCancelable(true)
                    .setMessage(R.string.DeleteSubmission)
                    //on yes post bid
                    .setPositiveButton(R.string.YesButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postDelete();
                        }
                    })
                    //on no return
                    .setNegativeButton(R.string.NoButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    };

    /**
     *
     */
    View.OnClickListener EditBidClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Dialog edtPriceDialog = new Dialog(AuctionActivity.this);

            edtPriceDialog.setContentView(R.layout.price_dialog);
            edtPriceDialog.setCancelable(true);
            edtPriceDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            EditText edtPriceValue = (EditText) edtPriceDialog.findViewById(R.id.edtPriceValue);
            edtPriceValue.setText(Float.toString(btnNewBidValue.getValue()));
            Button edtPriceBtn = (Button) edtPriceDialog.findViewById(R.id.edtPriceDialogBtn);
            edtPriceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnNewBidValue.setPickerValue(Float.valueOf(String.valueOf(edtPriceValue.getText())));
                    edtPriceDialog.dismiss();
                }
            });
            edtPriceDialog.show();
        }
    };

    //REST call methods

    /**
     * Implements the GET method to request an auction by auctionId
     * <p>User Role: bidder, buyer, guest, seller</p>
     */
    private void getAuction(){
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);

        mDialog.setMessage("Please wait...");
        mDialog.show();

        Call<AuctionResponse> call = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId);

        call.enqueue(new Callback<AuctionResponse>() {
            @Override
            public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                if (!response.isSuccessful()){
                    mDialog.dismiss();
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    mDialog.dismiss();
                    Toast.makeText(AuctionActivity.this, "Auction not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (response.body().getAuction()==null){
                    mDialog.dismiss();
                    Toast.makeText(AuctionActivity.this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    try {
                        viewAuction(response.body().getAuction());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        mDialog.dismiss();
                        Toast.makeText(AuctionActivity.this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDialog.dismiss();
                    return;
                }
            }


            @Override
            public void onFailure(Call<AuctionResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    /**
     * Implements the POST method to delete an auction by auctionId
     * <p>User Role: seller</p>
     */
    private void postDelete() {
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Αuction is being deleted");
        mDialog.show();

        DeleteAuctionRequest deleteAuctionRequest = new DeleteAuctionRequest(auctionId, Common.token);

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postDeleteAuction(deleteAuctionRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(AuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AuctionActivity.this, "Your auction deleted successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AuctionActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return;

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Implements the POST method to create a new bid for auction
     * <p>User Role: bidder</p>
     */
    private void postBid() {
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Your bid is submitting");
        mDialog.show();

        DecimalFormat df = new DecimalFormat("#.00");
        String price = String.valueOf(df.format(btnNewBidValue.getValue())).replace(",",".");
        final NewBidRequest newBidRequest = new NewBidRequest(Common.token,
                price,getIntent().getStringExtra("AuctionId"));

        System.out.println("The request is "+ Utils.gson.toJson(newBidRequest));

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postNewBid(newBidRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(AuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AuctionActivity.this, "Your bid submitted", Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    /**
     * Implements the POST method to rate a user (seller/bidder)
     * <p>User Role: buyer, seller</p>
     * @param auction Related auction item
     * @param rating Rating value
     */
    private void postRateUser(Auction auction, float rating) {
        int ratedUserId = 0;
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Your rating is submitting");
        mDialog.show();

        if (Common.currentUser.getUsername().equals(auction.getSeller().getUsername())){
            ratedUserId = auction.getBids().get(0).getBidder().getId();
        }
        else if (Common.currentUser.getUsername().equals(auction.getBids().get(0).getBidder().getUsername())){
            ratedUserId = auction.getSeller().getId();
        }
        else{
            Toast.makeText(AuctionActivity.this, "Sorry, you have not permission to send message for this auction", Toast.LENGTH_SHORT).show();
            return;
        }
        RateUserRequest rateUserRequest = new RateUserRequest(Common.token, ratedUserId, Integer.valueOf(auctionId), (int) rating);

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postRateUser(rateUserRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();
                //TODO: 400
                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(AuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AuctionActivity.this, "Your rating submitted", Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Implements the POST method to send a new message
     * <p>User Role: buyer, seller</p>
     * @param auction Related auction item
     * @param messageSubject The messages subject
     * @param messageBody The actual message
     */
    private void sendMessage(Auction auction, String messageSubject, String messageBody) {
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Your message is sending");
        mDialog.show();

        NewMessageRequest newMessageRequest = new NewMessageRequest(Common.token, Integer.valueOf(auctionId), messageSubject, messageBody);

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postNewMessage(newMessageRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(AuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AuctionActivity.this, "Your message sent", Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
