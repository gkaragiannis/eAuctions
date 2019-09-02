package com.dev.e_auctions.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.SignInRequest;
import com.dev.e_auctions.APIResponses.UsersResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

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

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "Missing username and/or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                SignInRequest signInRequest = new SignInRequest(edtUsername.getText().toString(), edtPassword.getText().toString());
                Call<UsersResponse> call = RestClient.getClient().create(RestApi.class).postSignIn(signInRequest);

                call.enqueue(new Callback<UsersResponse>() {
                    @Override
                    public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                        //To disappear progressDialog
                        mDialog.dismiss();

                        //add extra condition if empty List
                        if (!response.isSuccessful()){
                            //floating message
                            Toast.makeText(SignIn.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (!response.body().getStatusCode().equals("SUCCESS")){
                            Toast.makeText(SignIn.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            System.out.println(response.body().toString());
                            Common.token = response.body().getToken();
                            Common.currentUser = response.body().getUser();
                            Toast.makeText(SignIn.this, "Welcome back " + edtUsername.getText() + " !", Toast.LENGTH_SHORT).show();

                            Intent SignInIntent = new Intent(SignIn.this, HomeActivity.class);
                            startActivity(SignInIntent);
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<UsersResponse> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

            }
        });
    }
}
