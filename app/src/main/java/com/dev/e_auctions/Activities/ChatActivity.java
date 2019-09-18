package com.dev.e_auctions.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.NewMessageRequest;
import com.dev.e_auctions.APIResponses.AuctionResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Adapter.MessageAdapter;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.R;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;

    RecyclerView recyclerView;

    Intent intent;

    String subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, MailBoxActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = findViewById(R.id.chat_send_btn);
        btn_send.setOnClickListener(sendMessage_ClickListener);
        text_send = findViewById(R.id.chat_new_text);

        if (getIntent() != null){
            subject = getIntent().getStringExtra("subject");
        }
        if (!subject.isEmpty()){
            String[] words=subject.split("\\s");
            getAuction(words[2]);
            getSupportActionBar().setTitle("Mitsos");
        }

    }

    private void getAuction(String auctionId) {
        Call<AuctionResponse> call = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId);

        call.enqueue(new Callback<AuctionResponse>() {
            @Override
            public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ChatActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(ChatActivity.this, "Auction not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (response.body().getAuction()==null){
                    Toast.makeText(ChatActivity.this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    toolbar.setTitle(response.body().getAuction().getNameOfItem());
//                    viewAuction(response.body().getAuction());
                    return;
                }
            }


            @Override
            public void onFailure(Call<AuctionResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    View.OnClickListener sendMessage_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            sendMessage();
        }
    };

    /*private void sendMessage(Auction auction, String messageSubject, String messageBody) {
        final ProgressDialog mDialog = new ProgressDialog(ChatActivity.this);
        mDialog.setMessage("Your message is sending");
        mDialog.show();

        NewMessageRequest newMessageRequest = new NewMessageRequest(Common.token, Integer.valueOf(auctionId), messageSubject, messageBody);

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postNewMessage(newMessageRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(ChatActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(ChatActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }


                return;
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(ChatActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
            }
        });
    }*/


}
