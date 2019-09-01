package com.dev.e_auctions.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.R;
import com.dev.e_auctions.apirequests.SignInRequest;
import com.dev.e_auctions.apirequests.SignUpRequest;
import com.dev.e_auctions.apiresponses.AuthenticateUserResponse;
import com.dev.e_auctions.apiresponses.UsersResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dev.e_auctions.constants.Constant.AUCTION_APP;
import static com.dev.e_auctions.constants.Constant.SIGN_UP;
import static com.dev.e_auctions.constants.Constant.SUCCESS;

public class SignUpActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtConfirmPass, edtFirstName, edtLastName, edtTaxId,
            edtEmail, edtPhone, edtAddress, edtCity, edtCountry;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPass = (EditText) findViewById(R.id.edtcPassword);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtTaxId = (EditText) findViewById(R.id.edtTax);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                //Some code here for log in
                final SignUpRequest signUpRequest = new SignUpRequest(edtUsername.getText().toString(),
                        edtPassword.getText().toString(),
                        edtFirstName.getText().toString(),
                        edtLastName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtPhone.getText().toString(),
                        edtCountry.getText().toString(),
                        edtCity.getText().toString(),
                        edtAddress.getText().toString(),
                        edtTaxId.getText().toString()
                );

                Log.d(AUCTION_APP, SIGN_UP + "Prepare to call the WS");

                Call<UsersResponse> call = RestClient.getClient().create(RestApi.class).postSignUp(signUpRequest);

                call.enqueue(new Callback<UsersResponse>() {
                    @Override
                    public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                        mDialog.dismiss();

                        Log.d(AUCTION_APP, SIGN_UP + "WS called successfully");

                        if (!response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!response.body().getStatusCode().equals(SUCCESS)) {
                            Log.d(AUCTION_APP, SIGN_UP + "Couldn't sign up a new user");
                            Toast.makeText(SignUpActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                            return;

                        } else {
                            Log.d(AUCTION_APP, SIGN_UP + "Sign Up a new user completed successfully");
                            //Toast.makeText(SignUpActivity.this, "Successfully Sign Up", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                            Common.token = response.body().getToken();

                            final String[] toastMsg = new String[1];
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //Get the user details, so needs authentication with the provided username and password
                                    Log.d(AUCTION_APP, SIGN_UP + "Authenticate the user to log in his account");
                                    Log.d(AUCTION_APP, SIGN_UP + "Call the authentication WS process");
                                    AuthenticateUserResponse authenticateUserResponse = null;
                                    try {
                                        Log.d(AUCTION_APP, SIGN_UP + "Call the authentication WS process");
                                        authenticateUserResponse = RestClient.getClient()
                                                .create(RestApi.class)
                                                .postSignIn(new SignInRequest(edtUsername.getText().toString(), edtPassword.getText().toString()))
                                                .execute().body();

                                        //Successful authentication
                                        if (SUCCESS.equals(authenticateUserResponse.getStatusCode())) {
                                            Log.d(AUCTION_APP, SIGN_UP + "Authentication WS process completed successfully");
                                            Common.currentUser = authenticateUserResponse.getUser();
                                            toastMsg[0] = "Welcome " + Common.currentUser.getUsername() + " !";

                                        }
                                        //Failed to authenticate the user
                                        else {
                                            Log.d(AUCTION_APP, SIGN_UP + "Authentication WS process failed ");
                                            toastMsg[0] = "Failed to authenticate the user ! Insert as Guest";

                                        }

                                    } catch (IOException e) {
                                        Log.d(AUCTION_APP, SIGN_UP + "An error accured in the authentication process WS", e);
                                        toastMsg[0] = "Failed to authenticate the user ! Insert as Guest";
                                        e.printStackTrace();
                                    }
                                }
                            });
                            try {
                                thread.start();
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(SignUpActivity.this, toastMsg[0], Toast.LENGTH_SHORT).show();
                            Intent SignUpIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(SignUpIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UsersResponse> call, Throwable t) {
                        Log.d(AUCTION_APP, SIGN_UP + "Failed to call the WS ");
                        mDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }
}
