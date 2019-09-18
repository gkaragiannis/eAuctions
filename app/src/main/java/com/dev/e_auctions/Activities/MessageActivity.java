package com.dev.e_auctions.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.e_auctions.APIRequests.MarkMessageAsReadRequest;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView msg_sender, msg_subject, msg_body;
    EditText newMsg;
    ImageButton btn_send;

    String messageId;

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
        msg_subject = (TextView) findViewById(R.id.msg_subject);
        msg_body = (TextView) findViewById(R.id.msg_body);
        newMsg = (EditText) findViewById(R.id.chat_new_text);
        btn_send = (ImageButton) findViewById(R.id.chat_send_btn);

        if (getIntent() != null){
            messageId = getIntent().getStringExtra("subject");
        }
        if (!messageId.isEmpty()){

            getSupportActionBar().setTitle("Mitsos");
        }
    }

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


}
