package com.dev.e_auctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        edtUsername = (MaterialEditText) findViewById(R.id.edtUsername);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                //Some code here for log in
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://my-json-server.typicode.com/gkaragiannis/testREST/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RestApi client = retrofit.create(RestApi.class);

                Call<List<User>> call = client.getUserByUsername(edtUsername.getText().toString());

                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        //To disappear progressDialog
                        mDialog.dismiss();

                        if (!response.isSuccessful()){
                            //floating message
                            Toast.makeText(SignIn.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<User> responseUser = response.body();
                        int i=0;
                        for (User currentUser : responseUser){
                            Toast.makeText(SignIn.this, Integer.toString(i) , Toast.LENGTH_SHORT).show();
                            i++;
                        }

                        //floating message
                        // Toast.makeText(SignIn.this, "Successfully Log In", Toast.LENGTH_SHORT).show();

                        Intent SignInIntent = new Intent(SignIn.this, HomeActivity.class);
                        startActivity(SignInIntent);

                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        //To diasappear progressDialog
                        mDialog.dismiss();

                        //floating message
                        Toast.makeText(SignIn.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
