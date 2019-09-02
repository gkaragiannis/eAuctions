package com.dev.e_auctions.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.APIResponses.AuctionsResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Bid;
import com.dev.e_auctions.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import pl.polak.clicknumberpicker.ClickNumberPickerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionActivity extends AppCompatActivity {

    ImageView auctionImage;
    TextView auctionName, startingDate, endDate, auctionDesc;
    ProgressBar durationBar;
    ClickNumberPickerView btnNewBidValue;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnBid;

    String auctionId = "";


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
        Call<AuctionsResponse> request = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId);

        request.enqueue(new Callback<AuctionsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<AuctionsResponse> request, Response<AuctionsResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(AuctionActivity.this, "Auction not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (response.body().getAuctions().isEmpty() || response.body().getAuctions().size()!=1){
                    Toast.makeText(AuctionActivity.this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    Picasso.get().load(response.body().getAuctions().get(0).getImage()).into(auctionImage);
                    auctionName.setText(response.body().getAuctions().get(0).getNameOfItem());
                    startingDate.setText(response.body().getAuctions().get(0).getStartedTime());
                    endDate.setText(response.body().getAuctions().get(0).getEndingTime());
                    int progress = 0;
                    try {
                        progress = getProgress(response.body().getAuctions().get(0).getStartedTime(),
                                response.body().getAuctions().get(0).getEndingTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    durationBar.setProgress(progress);
                    //btnNewBidValue.setPickerValue(response.body().getAuctions().get(0).getBids());
                    response.body().getAuctions().get(0).getBids().sort(Comparator.comparingDouble(Bid::getBidPrice).reversed());
                    btnNewBidValue.setPickerValue(response.body().getAuctions().get(0).getBids().get(0).getBidPrice().floatValue());
                    auctionDesc.setText(response.body().getAuctions().get(0).getItemDescription());

                }
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
            public void onFailure(Call<AuctionsResponse> request, Throwable t) {
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
