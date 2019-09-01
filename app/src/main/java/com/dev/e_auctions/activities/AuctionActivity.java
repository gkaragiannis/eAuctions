package com.dev.e_auctions.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.R;

import pl.polak.clicknumberpicker.ClickNumberPickerView;

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
        if (Common.currentUser == null)
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
        if (getIntent() != null) {
            auctionId = getIntent().getStringExtra("AuctionId");
        }
        if (!auctionId.isEmpty()) {
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

    private void getAuction() {
        /*Call<List<Auction>> request = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId);

        request.enqueue(new Callback<List<Auction>>() {
            @Override
            public void onResponse(Call<List<Auction>> request, Response<List<Auction>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(AuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (response.body().isEmpty() || response.body().size()!=1){
                    Toast.makeText(AuctionActivity.this, "404 - Not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    Picasso.get().load(response.body().get(0).getImage()).into(auctionImage);
                    auctionName.setText(response.body().get(0).getName());
                    startingDate.setText(response.body().get(0).getCreated());
                    endDate.setText(response.body().get(0).getEnds());
                    int progress = 0;
                    try {
                        progress = getProgress(response.body().get(0).getCreated(),
                                response.body().get(0).getEnds());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    durationBar.setProgress(progress);
                    btnNewBidValue.setPickerValue(response.body().get(0).getLast_bid());
                    auctionDesc.setText(response.body().get(0).getDescription());

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
            public void onFailure(Call<List<Auction>> request, Throwable t) {
                Toast.makeText(AuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });*/
    }
}
