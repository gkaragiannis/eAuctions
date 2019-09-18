package com.dev.e_auctions.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.AuthenticateUserRequest;
import com.dev.e_auctions.APIResponses.AuthenticateUserResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        btnSignIn.setOnClickListener(SignInClickListener);
        /*btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                if (edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                    mDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Missing username and/or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthenticateUserRequest signInRequest = new AuthenticateUserRequest(edtUsername.getText().toString(), edtPassword.getText().toString());
                Call<AuthenticateUserResponse> call = RestClient.getClient().create(RestApi.class).postSignIn(signInRequest);

                call.enqueue(new Callback<AuthenticateUserResponse>() {
                    @Override
                    public void onResponse(Call<AuthenticateUserResponse> call, Response<AuthenticateUserResponse> response) {
                        //To disappear progressDialog
                        mDialog.dismiss();

                        if (!response.isSuccessful()){
                            Toast.makeText(SignInActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (!response.body().getStatusCode().equals("SUCCESS")){
                            Toast.makeText(SignInActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            System.out.println(response.body().toString());
                            Common.token = response.body().getToken();
                            Common.currentUser = response.body().getUser();
                            Toast.makeText(SignInActivity.this, "Welcome back " + edtUsername.getText() + " !", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<AuthenticateUserResponse> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

            }
        });*/
    }

    View.OnClickListener SignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
            mDialog.setMessage("Please wait...");
            mDialog.show();

            if (edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                mDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Missing username and/or password", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthenticateUserRequest signInRequest = new AuthenticateUserRequest(edtUsername.getText().toString(), edtPassword.getText().toString());
            Call<AuthenticateUserResponse> call = RestClient.getClient().create(RestApi.class).postSignIn(signInRequest);

            call.enqueue(new Callback<AuthenticateUserResponse>() {
                @Override
                public void onResponse(Call<AuthenticateUserResponse> call, Response<AuthenticateUserResponse> response) {
                    //To disappear progressDialog
                    mDialog.dismiss();

                    if (!response.isSuccessful()){
                        Toast.makeText(SignInActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!response.body().getStatusCode().equals("SUCCESS")){
                        Toast.makeText(SignInActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        System.out.println(response.body().toString());
                        Common.token = response.body().getToken();
                        Common.currentUser = response.body().getUser();
                        Toast.makeText(SignInActivity.this, "Welcome back " + edtUsername.getText() + " !", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<AuthenticateUserResponse> call, Throwable t) {
                    mDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                    return;
                }
            });

        }
    };
}
