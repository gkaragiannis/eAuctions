package com.dev.e_auctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                //
                Call<List<User>> request = RestClient.getClient().create(RestApi.class).
                        getUserByUsername(edtUsername.getText().toString(), edtPassword.getText().toString());

                request.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> request, Response<List<User>> response) {
                        //To disappear progressDialog
                        mDialog.dismiss();

                        //add extra condition if empty List
                        if (!response.isSuccessful()){
                            //floating message
                            Toast.makeText(SignIn.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (response.body().size() != 1 ){
                            Toast.makeText(SignIn.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (response.body().size() == 1 ){

                            Common.currentUser = response.body().get(0);
                            Toast.makeText(SignIn.this, "Welcome back " + Common.currentUser.getUsername(), Toast.LENGTH_SHORT).show();

                            Intent SignInIntent = new Intent(SignIn.this, HomeActivity.class);
                            startActivity(SignInIntent);
                            finish();
                        }
                        else {
                            Toast.makeText(SignIn.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<User>> request, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

            }
        });
    }
}
