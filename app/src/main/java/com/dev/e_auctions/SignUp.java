package com.dev.e_auctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtConfirmPass, edtFirstName, edtLastName, edtTaxId,
            edtEmail, edtPhone, edtAddress, edtStreetNum, edtPostal, edtLocation, edtCountry;
    CheckBox chkGDPR;
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
        edtStreetNum = (EditText) findViewById(R.id.edtStreetNum);
        edtPostal = (EditText) findViewById(R.id.edtPostal);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        chkGDPR = (CheckBox) findViewById(R.id.chkGDPR);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                //Some code here for log in
                User newUser = new User(
                        edtUsername.getText().toString(),
                        edtFirstName.getText().toString(),
                        edtLastName.getText().toString(),
                        "Male",
                        "00/00/0000",
                        Integer.parseInt(edtTaxId.getText().toString()),
                        edtEmail.getText().toString(),
                        edtPhone.getText().toString(),
                        edtAddress.getText().toString(),
                        Integer.parseInt(edtStreetNum.getText().toString()),
                        Integer.parseInt(edtPostal.getText().toString()),
                        edtLocation.getText().toString(),
                        edtCountry.getText().toString()
                );

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://my-json-server.typicode.com/gkaragiannis/testREST/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RestApi client = retrofit.create(RestApi.class);

                Call<User> call = client.createNewUser(newUser);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if (!response.isSuccessful()){
                            Toast.makeText(SignUp.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mDialog.dismiss();
                        //Toast.makeText(SignUp.this, "Successfully Sign Up", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUp.this, Integer.toString(response.code()), Toast.LENGTH_LONG).show();

                        Intent SignInIntent = new Intent(SignUp.this, HomeActivity.class);
                        startActivity(SignInIntent);

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(SignUp.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }
}
