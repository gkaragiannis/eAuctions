package com.dev.e_auctions.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.SignUpRequest;
import com.dev.e_auctions.APIResponses.UsersResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.dev.e_auctions.R;

public class SignUp extends AppCompatActivity {

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
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
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


                Call<UsersResponse> call = RestClient.getClient().create(RestApi.class).postSignUp(signUpRequest);

                call.enqueue(new Callback<UsersResponse>() {
                    @Override
                    public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                        mDialog.dismiss();

                        if (!response.isSuccessful()){
                            Toast.makeText(SignUp.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (!response.body().getStatusCode().equals("SUCCESS")){
                            Toast.makeText(SignUp.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Common.token = response.body().getToken();
                            Common.currentUser = response.body().getUser();
                            Toast.makeText(SignUp.this, "Welcome " + edtUsername.getText() + " !", Toast.LENGTH_SHORT).show();

                            Intent SignUpIntent = new Intent(SignUp.this, HomeActivity.class);
                            startActivity(SignUpIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UsersResponse> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(SignUp.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }
}
