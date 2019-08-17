package com.dev.e_auctions;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Auction;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAuctionActivity extends AppCompatActivity {

    private Calendar mCalendar = Calendar.getInstance();
    private EditText edtTextStartingDate, edtTextEndDate, edtTextName, edtTextDescription;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_auction);

        edtTextName = (EditText) findViewById(R.id.newAuctionName);
        edtTextDescription = (EditText) findViewById(R.id.newAuctionDescription);
        //Set Calendary elements
        day=mCalendar.get(Calendar.DAY_OF_MONTH);
        month=mCalendar.get(Calendar.MONTH);
        year=mCalendar.get(Calendar.YEAR);

        edtTextStartingDate = (EditText) findViewById(R.id.newAuctionStartingDate);
        edtTextStartingDate.setOnClickListener(StartingDateClickListener);
        edtTextEndDate = (EditText) findViewById(R.id.newAuctionEndDate);
        edtTextEndDate.setOnClickListener(EndDateClickListener);

        Button btnSubmitAuction = (Button) findViewById(R.id.btnSubmit);
        btnSubmitAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(NewAuctionActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                final Auction newAuction = new Auction(edtTextName.getText().toString(),
                        (float) 0.01,
                        edtTextStartingDate.getText().toString(),
                        edtTextEndDate.getText().toString(),
                        Common.currentUser.getPassword(),
                        edtTextDescription.getText().toString(),
                        "image");

                Call<Auction> request = RestClient.getClient().create(RestApi.class).postNewAuction(newAuction);

                request.enqueue(new Callback<Auction>() {
                    @Override
                    public void onResponse(Call<Auction> call, Response<Auction> response) {
                        mDialog.dismiss();

                        if (!response.isSuccessful()){
                            Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(NewAuctionActivity.this, "Auction submitted successfully", Toast.LENGTH_SHORT).show();
                        Intent NewAuctionIntent = new Intent(NewAuctionActivity.this, HomeActivity.class);
                        startActivity(NewAuctionIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Auction> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }

    View.OnClickListener StartingDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    edtTextStartingDate.setText(year + "-" + month + "-" + dayOfMonth);
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAuctionActivity.this, dateSetListener, year, month, day);
            datePickerDialog.show();
        }
    };

    View.OnClickListener EndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    edtTextEndDate.setText(year + "-" + month + "-" + dayOfMonth);
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAuctionActivity.this, dateSetListener, year, month, day);
            datePickerDialog.show();
        }
    };
}
