package com.dev.e_auctions.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
import com.dev.e_auctions.APIRequests.NewBidRequest;
import com.dev.e_auctions.APIResponses.AuctionListResponse;
import com.dev.e_auctions.APIResponses.AuctionResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Adapter.ExpandableListAdapter;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Bid;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private FloatingActionButton btnFAB;
    private ExpandableListView categoryListView;
    private ExpandableListAdapter categoryListAdapter;
    private RatingBar rtnBar;

    private String auctionId = "";
    private ArrayList<String> categoryListHeader;
    private HashMap<String, List<String>> categoryListMap;
    private boolean isSeller, canDelete;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        //Initialize View
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

        btnFAB = (FloatingActionButton) findViewById(R.id.btnFAB);
        btnFAB.setOnClickListener(FAB_ClickListener);

        //Get auction id from Intent
        if (getIntent() != null){
            auctionId = getIntent().getStringExtra("AuctionId");
        }
        if (!auctionId.isEmpty()){
            getAuction();
        }
    }

    View.OnClickListener FAB_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //pop up to approve action
            if (isSeller){
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
            else{
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
        }
    };

    private void postDelete() {
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Αuction is being deleted");
        mDialog.show();

        DeleteAuctionRequest deleteAuctionRequest = new DeleteAuctionRequest(auctionId, Common.token);

        Call<GeneralResponse> request = RestClient.getClient().create(RestApi.class).postDeleteAuction(deleteAuctionRequest);

        request.enqueue(new Callback<GeneralResponse>() {
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

    private void postBid() {
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);
        mDialog.setMessage("Your bid is submitting");
        mDialog.show();

        DecimalFormat df = new DecimalFormat("#.00");
        final NewBidRequest newBidRequest = new NewBidRequest(Common.token,
                Double.parseDouble(df.format((double) btnNewBidValue.getValue())),
                getIntent().getStringExtra("AuctionId"));

        System.out.println(newBidRequest.getAuctionId() + " " + newBidRequest.getBidderToken() + " " + newBidRequest.getBidderValue());

        Call<GeneralResponse> request = RestClient.getClient().create(RestApi.class).postNewBid(newBidRequest);

        request.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> request, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(AuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AuctionActivity.this, "Your bid is submitted", Toast.LENGTH_LONG).show();
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

    private void getAuction(){
        final ProgressDialog mDialog = new ProgressDialog(AuctionActivity.this);

        mDialog.setMessage("Please wait...");
        mDialog.show();

        Call<AuctionResponse> request = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId);

        request.enqueue(new Callback<AuctionResponse>() {
            @SuppressLint("RestrictedApi")
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

                    //Configure FAB
                    if (Common.currentUser == null){
                        isSeller = false;
                        btnFAB.setVisibility(View.GONE);
                    }
                    else if (Common.currentUser.getUsername().equals(response.body().getAuction().getSeller().getUsername())) {
                        isSeller = true;
                        if (response.body().getAuction().getBids().size() > 0) {
                            btnFAB.setVisibility(View.GONE);
                        } else {
                            btnFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_forever_white_24dp));
                            btnFAB.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        isSeller = false;
                        btnFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_gavel_white_24dp));
                        btnFAB.setVisibility(View.VISIBLE);
                    }

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
                    Collections.sort(response.body().getAuction().getBids(), Collections.reverseOrder());
                    btnNewBidValue.setPickerValue(response.body().getAuction().getBids().get(0).getBidPrice().floatValue());
                    auctionDesc.setText(response.body().getAuction().getItemDescription());
                    initializeCategoryListViewData(response.body().getAuction().getCategories());
                    rtnBar.setRating(response.body().getAuction().getSeller().getSellerRating().floatValue());
                    sellerRatingNum.setText(Double.toString(response.body().getAuction().getSeller().getSellerRating()));
                    if (response.body().getAuction().getSeller().getSellerRatingVotes()!=null)
                        sellerRatingVotes.setText("out of " + Integer.toString(response.body().getAuction().getSeller().getSellerRatingVotes()) + " votes");
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
