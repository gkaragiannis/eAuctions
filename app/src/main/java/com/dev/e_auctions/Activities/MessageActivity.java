package com.dev.e_auctions.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.MarkMessageAsReadRequest;
import com.dev.e_auctions.APIRequests.NewMessageRequest;
import com.dev.e_auctions.APIResponses.AuctionResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Message;
import com.dev.e_auctions.R;
import com.dev.e_auctions.Utilities.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class MessageActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView msg_sender, msg_receiver, msg_subject, msg_body;
    EditText new_msg;
    ImageButton btn_send;

    Message message;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, MailBoxActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        msg_sender = (TextView) findViewById(R.id.msg_sender);
        msg_receiver = (TextView) findViewById(R.id.msg_receiver);
        msg_subject = (TextView) findViewById(R.id.msg_subject_value);
        msg_body = (TextView) findViewById(R.id.msg_body);
        new_msg = (EditText) findViewById(R.id.chat_new_text);
        btn_send = (ImageButton) findViewById(R.id.chat_send_btn);

        if (getIntent() != null){
            message = (Message) getIntent().getParcelableExtra("message");
        }

        if (message!=null){
            getAuctionName(message.getAuctionId());
            msg_sender.setText(message.getSender().getUsername());
            msg_receiver.setText(message.getReceiver().getUsername());
            msg_subject.setText(message.getSubject().substring(message.getSubject().lastIndexOf(":") + 1));
            msg_body.setText(message.getMessage());
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(message.getAuctionId(), msg_subject.getText().toString(), new_msg.getText().toString());
                }
            });

            if (!message.isRead()) {
                markMessageRead(message.getMessageId());
            }
        }
        else{
            Toast.makeText(MessageActivity.this, "Unexpected Error Occurred", Toast.LENGTH_SHORT);
        }

    }

    /**
     *
     * @param auctionId
     */
    private void getAuctionName(Long auctionId) {
        final ProgressDialog mDialog = new ProgressDialog(MessageActivity.this);

        mDialog.setMessage("Please wait...");
        mDialog.show();

        Call<AuctionResponse> call = RestClient.getClient().create(RestApi.class).getAuctionsById(auctionId.toString());


        call.enqueue(new Callback<AuctionResponse>() {
            @Override
            public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                Log.d("auction ","the new auction request is "+ Utils.gson.toJson(response));
                if (response.isSuccessful() && response.body().getStatusCode().equals("SUCCESS") && response.body().getAuction()==null){
                    getSupportActionBar().setTitle(response.body().getAuction().getNameOfItem());
                    System.out.println(response.body().getAuction().getNameOfItem());
                    return;
                }
            }


            @Override
            public void onFailure(Call<AuctionResponse> call, Throwable t) {
                //do nothing
                return;
            }
        });

        mDialog.dismiss();
    }

    /**
     *
     * @param messageId
     */
    private void markMessageRead(int messageId) {
        MarkMessageAsReadRequest markMessageAsReadRequest = new MarkMessageAsReadRequest(Common.token, Integer.toString(messageId));

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postMarkMessageAsRead(markMessageAsReadRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    /**
     *
     * @param auctionId
     * @param messageSubject
     * @param messageBody
     */
    private void sendMessage(Long auctionId, String messageSubject, String messageBody) {
        final ProgressDialog mDialog = new ProgressDialog(MessageActivity.this);
        mDialog.setMessage("Your message is sending");
        mDialog.show();

        NewMessageRequest newMessageRequest = new NewMessageRequest(Common.token, auctionId.intValue(), messageSubject, messageBody);

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postNewMessage(newMessageRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(MessageActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(MessageActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MessageActivity.this, "Your message sent", Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(MessageActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
