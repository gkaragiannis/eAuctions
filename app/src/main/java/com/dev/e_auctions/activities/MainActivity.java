package com.dev.e_auctions.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.R;

public class MainActivity extends AppCompatActivity {

    Button btnSignUp, btnSignIn, btnGuest;
    TextView txtSlogan;
    private ConnectivityManager connectivityManager;
    private ImageView initialImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnGuest = (Button) findViewById(R.id.btnGuest);

        txtSlogan = (TextView) findViewById(R.id.txtSlogan);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });


//        initialImage = findViewById(R.id.initialImage);
//
//        Thread thread = new Thread(new Runnable() {
//            ImageApiResponse imageApiResponse;
//            @Override
//            public void run() {
//                Log.d(AUCTION_APP, "Get Image " );
//                try {
//
//                    Log.d(AUCTION_APP, "Get Image " );
//                    imageApiResponse = RestClient.getClient()
//                            .create(RestApi.class)
//                            .getImage( 1L)
//                            .execute().body();
//
//
//                    System.out.println(imageApiResponse.getData());
//
//
//                    System.out.println("To Image response "+imageApiResponse.toString());
//
//
//                    //Successful authentication
//                    if (SUCCESS.equals(imageApiResponse.getStatusCode())) {
//                        Log.d(AUCTION_APP, "Get Image " + "image case successfully ");
//                        System.out.println("To image byte array einai \n "+imageApiResponse.toString());
////                        byte[] imageByteArray = imageApiResponse.getData().get(0).getBytes();
//                        Bitmap image = byteArrayToBitmap(imageApiResponse.getData());
//                        Log.d(AUCTION_APP, "Get Image " + "ImageBecame bitmap ");
//                        initialImage.setImageBitmap(image);
////                        Common.currentUser = authenticateUserResponse.getUser();
////                        toastMsg[0] = "Welcome " + Common.currentUser.getUsername() + " !";
//
//                    }
//                    //Failed to authenticate the user
//                    else {
//                        Log.d(AUCTION_APP, "Get Image " + "failed to fetch the image ");
//
//                    }
//
//                } catch (IOException e) {
//                    Log.d(AUCTION_APP, "Get Image " + "failed to fetch the image ");
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_LONG).show();
    }
}
