package com.dev.e_auctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//import com.rengwuxian.materialedittext.MaterialEditText;

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

                //To diasappear progressDialog
                mDialog.dismiss();

                //floating message
                Toast.makeText(SignUp.this, "Successfully Sign Up", Toast.LENGTH_SHORT).show();

                Intent SignInIntent = new Intent(SignUp.this, HomeActivity.class);
                startActivity(SignInIntent);
            }
        });
    }
}
