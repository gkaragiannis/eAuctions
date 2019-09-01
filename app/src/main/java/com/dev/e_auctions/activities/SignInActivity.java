package com.dev.e_auctions.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.R;
import com.dev.e_auctions.apirequests.SignInRequest;
import com.dev.e_auctions.apiresponses.AuthenticateUserResponse;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dev.e_auctions.constants.Constant.AUCTION_APP;
import static com.dev.e_auctions.constants.Constant.SIGN_IN;
import static com.dev.e_auctions.constants.Constant.SUCCESS;

public class SignInActivity extends AppCompatActivity {

    MaterialEditText edtUsername, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtUsername = /*(MaterialEditText)*/ findViewById(R.id.edtUsername);
        edtPassword = /*(MaterialEditText)*/ findViewById(R.id.edtPassword);

        btnSignIn = /*(Button)*/ findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                    mDialog.dismiss();
                    Log.d(AUCTION_APP, SIGN_IN + "Missing username and/or password");
                    Toast.makeText(SignInActivity.this, "Missing username and/or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(AUCTION_APP, SIGN_IN + "Create the request to call the WS");
                SignInRequest signInRequest = new SignInRequest(edtUsername.getText().toString(), edtPassword.getText().toString());
                Call<AuthenticateUserResponse> call = RestClient.getClient().create(RestApi.class).postSignIn(signInRequest);

                call.enqueue(new Callback<AuthenticateUserResponse>() {
                    @Override
                    public void onResponse(Call<AuthenticateUserResponse> call, Response<AuthenticateUserResponse> response) {
                        //To disappear progressDialog
                        mDialog.dismiss();
                        Log.d(AUCTION_APP, SIGN_IN + "WS call was successful");

                        //add extra condition if empty List
                        if (!response.isSuccessful()) {
                            //floating message
                            Toast.makeText(SignInActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!response.body().getStatusCode().equals(SUCCESS)) {
                            Log.d(AUCTION_APP, SIGN_IN + "Not success authentication ");
                            Toast.makeText(SignInActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Log.d(AUCTION_APP, "Authentication completed successfully");
                            Common.token = response.body().getToken();
                            Common.currentUser = response.body().getUser();
                            Toast.makeText(SignInActivity.this, "Welcome back " + edtUsername.getText() + " !", Toast.LENGTH_SHORT).show();

                            Intent SignInIntent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(SignInIntent);
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<AuthenticateUserResponse> call, Throwable t) {
                        Log.d(AUCTION_APP, SIGN_IN + "Failed to call the WS");
                        mDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

            }
        });
    }
}
