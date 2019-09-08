package com.dev.e_auctions.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.APIResponses.AuctionListResponse;
import com.dev.e_auctions.APIResponses.AuctionResponse;
import com.dev.e_auctions.Adapter.ExpandableListAdapter;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Bid;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pl.polak.clicknumberpicker.ClickNumberPickerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionActivity extends AppCompatActivity {

    private ImageView auctionImage;
    private TextView auctionName, startingDate, endDate, auctionDesc, sellerRatingNum, sellerRatingVotes;
    private ProgressBar durationBar;
    private ClickNumberPickerView btnNewBidValue;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnBid;
    private ExpandableListView categoryListView;
    private ExpandableListAdapter categoryListAdapter;
    private RatingBar rtnBar;

    private ArrayList<String> categoryListHeader;
    private HashMap<String, List<String>> categoryListMap;
    private String auctionId = "";


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        //Initialize View
        btnBid = (FloatingActionButton) findViewById(R.id.btnBid);
        if (Common.currentUser==null)
            btnBid.setVisibility(View.GONE);
        else
            btnBid.setVisibility(View.VISIBLE);

        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pop up to approve new bid "R U Sure?"
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
        });

        auctionImage = (ImageView) findViewById(R.id.auctionImage);
        auctionName = (TextView) findViewById(R.id.auctionName);
        durationBar = (ProgressBar) findViewById(R.id.durationBar);
        startingDate = (TextView) findViewById(R.id.startingDate);
        endDate = (TextView) findViewById(R.id.endDate);
        auctionDesc = (TextView) findViewById(R.id.auctionDesc);
        rtnBar = (RatingBar) findViewById(R.id.sellerRating);
        sellerRatingNum = (TextView) findViewById(R.id.sellerRatingNum);
        sellerRatingVotes = (TextView) findViewById(R.id.sellerRatingVotes);
        categoryListView = (ExpandableListView)findViewById(R.id.auctionCategoriesELV);

        btnNewBidValue = (ClickNumberPickerView) findViewById(R.id.btnNewBidValue);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        //maybe there is no need for next 2 rows
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);



        //Get auction id from Intent
        if (getIntent() != null){
            auctionId = getIntent().getStringExtra("AuctionId");
        }
        if (!auctionId.isEmpty()){
            getAuction();
        }
    }

    private void postBid() {
        /*final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Your bid is submitting");
        mDialog.show();

        final Bid newBid = new Bid(Common.currentUser.getPassword(),
                btnNewBidValue.getValue(),
                Integer.parseInt(getIntent().getStringExtra("AuctionId")));

        Call<Bid> request = RestClient.getClient().create(RestApi.class).postNewBid(newBid);

        request.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> request, Response<Bid> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AuctionActivity.this, "Your bid is submitted", Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });*/
    }

    private void getAuction(){
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);

        mDialog.setMessage("Please wait...");
        mDialog.show();

        Call<AuctionResponse> request = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId);

        request.enqueue(new Callback<AuctionResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<AuctionResponse> request, Response<AuctionResponse> response) {
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
                    Picasso.get().load(response.body().getAuction().getImage()).into(auctionImage);
                    auctionName.setText(response.body().getAuction().getNameOfItem());
                    startingDate.setText(response.body().getAuction().getStartedTime());
                    endDate.setText(response.body().getAuction().getEndingTime());
                    int progress = 0;
                    try {
                        progress = getProgress(response.body().getAuction().getStartedTime(),
                                response.body().getAuction().getEndingTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    durationBar.setProgress(progress);
                    //btnNewBidValue.setPickerValue(response.body().getAuctions().get(0).getBids());
                    response.body().getAuction().getBids().sort(Comparator.comparingDouble(Bid::getBidPrice).reversed());
                    btnNewBidValue.setPickerValue(response.body().getAuction().getBids().get(0).getBidPrice().floatValue());
                    auctionDesc.setText(response.body().getAuction().getItemDescription());

                    initializeCategoryListViewData(response.body().getAuction().getCategories());

                    rtnBar.setRating(response.body().getAuction().getSeller().getSellerRating().floatValue());
                    sellerRatingNum.setText(Double.toString(response.body().getAuction().getSeller().getSellerRating()));
                    if (response.body().getAuction().getSeller().getSellerRatingVotes()!=null)
                        sellerRatingVotes.setText("out of " + Integer.toString(response.body().getAuction().getSeller().getSellerRatingVotes()) + " votes");
                    else
                        sellerRatingVotes.setText("0");
                    mDialog.dismiss();
                    return;
                }
            }

            private void initializeCategoryListViewData(List<Category> itemCategories) {
                categoryListHeader = new ArrayList<>();
                categoryListMap = new HashMap<>();

                categoryListHeader.add("Categories");

                List<String> categories = new ArrayList<>();
                for (Category category : itemCategories){
                    categories.add(category.getCategoryName());
                    System.out.println(category.getCategoryName());
                }

                categoryListMap.put(categoryListHeader.get(0), categories);

                categoryListAdapter = new ExpandableListAdapter(AuctionActivity.this, categoryListHeader, categoryListMap);
                categoryListView.setAdapter(categoryListAdapter);
            }

            private int getProgress(String created, String ends) throws ParseException {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                Date startingDate = format.parse(created);
                Date endDate = format.parse(ends);
                Date current = new Date();

                long diff1 = endDate.getTime() - startingDate.getTime();
                long diff2 = current.getTime() - startingDate.getTime();

                return (int) ((diff2*100)/diff1);
            }

            @Override
            public void onFailure(Call<AuctionResponse> request, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
