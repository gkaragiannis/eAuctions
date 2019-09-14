package com.dev.e_auctions.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dev.e_auctions.APIRequests.NewAcutionRequest;
import com.dev.e_auctions.APIResponses.AllCategoriesResponse;
import com.dev.e_auctions.APIResponses.GeneralResponse;
import com.dev.e_auctions.APIResponses.NewAuctionResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.TimePickerDialog.*;

public class NewAuctionActivity extends AppCompatActivity {

    private Calendar mCalendar = Calendar.getInstance();
    private EditText edtTextName, edtTextDescription, edtTextCategory, edtTextStartingDate, edtTextEndDate;
    private ImageView newAuctionImage;
    private int day, month, year, hourOfDay, minute;
    private boolean[] checkItems;
    private String[] categories;
    private int[] categoryIds;
    private static final int PICK_IMAGE_REQUEST = 777;
    private Bitmap bitmap;
    private ClickNumberPickerView btnNewAuctionStartingPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_auction);

        edtTextName = (EditText) findViewById(R.id.newAuctionName);
        edtTextCategory = (EditText) findViewById(R.id.newAuctionCategory);
        edtTextCategory.setOnClickListener(CategoryClickListener);
        edtTextDescription = (EditText) findViewById(R.id.newAuctionDescription);

        newAuctionImage = (ImageView) findViewById(R.id.newAuctionImage);
        Button btnLoadImage = (Button) findViewById(R.id.btnLoadImage);
        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnNewAuctionStartingPrice = (ClickNumberPickerView) findViewById(R.id.btnNewAuctionStartingPrice);


        //Set Calendar elements
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        month = mCalendar.get(Calendar.MONTH);
        year = mCalendar.get(Calendar.YEAR);
        hourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
        minute = mCalendar.get(Calendar.MINUTE);

        edtTextStartingDate = (EditText) findViewById(R.id.newAuctionStartingDate);
        edtTextStartingDate.setOnClickListener(StartingDateClickListener);
        edtTextEndDate = (EditText) findViewById(R.id.newAuctionEndDate);
        edtTextEndDate.setOnClickListener(EndDateClickListener);

        Button btnSubmitAuction = (Button) findViewById(R.id.btnSubmitNewAuction);
        btnSubmitAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitAuction();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
//        btnSubmitAuction.setOnClickListener(SubmitAuctionClickListener);
        /*btnSubmitAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(NewAuctionActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                final Integer[] createdAuctionId = new Integer[1];
                String image = imageToString();
                final Auction newAuction = new Auction(edtTextName.getText().toString(),
                        (float) 0.01,
                        edtTextStartingDate.getText().toString(),
                        edtTextEndDate.getText().toString(),
                        Common.currentUser.getPassword(),
                        edtTextDescription.getText().toString(),
                        image);

                Call<Auction> requestAuction = RestClient.getClient().create(RestApi.class).postNewAuction(newAuction);
                requestAuction.enqueue(new Callback<Auction>() {
                    @Override
                    public void onResponse(Call<Auction> requestAuction, Response<Auction> response) {

                        if (!response.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(NewAuctionActivity.this, "Auction submitted successfully", Toast.LENGTH_LONG).show();
                        Intent NewAuctionIntent = new Intent(NewAuctionActivity.this, HomeActivity.class);
                        startActivity(NewAuctionIntent);
                        finish();
                        createdAuctionId[0] = response.body().getId();
                        return;
                    }

                    @Override
                    public void onFailure(Call<Auction> requestAuction, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

               if (createdAuctionId[0]!=null){
                   ArrayList<Category> createdAuctionCategories = new ArrayList<>();
                   for (int position=0; position<categories.length; position++){
                       if (checkItems[position]==true){
                           createdAuctionCategories.add(new Category(createdAuctionId[0], categories[position]));
                       }
                   }
                   Call<List<Category>> requestCategories = RestClient.getClient().create(RestApi.class).postCategories(createdAuctionCategories);
                   requestCategories.enqueue(new Callback<List<Category>>() {
                       @Override
                       public void onResponse(Call<List<Category>> requestCategories, Response<List<Category>> response) {

                           if (!response.isSuccessful()){
                               mDialog.dismiss();
                               Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                               return;
                           }
                           mDialog.dismiss();
                           return;
                       }

                       @Override
                       public void onFailure(Call<List<Category>> requestCategories, Throwable t) {
                           mDialog.dismiss();
                           Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                           return;
                       }
                   });
               }
                Toast.makeText(NewAuctionActivity.this, "Auction submitted successfully", Toast.LENGTH_LONG).show();
                Intent NewAuctionIntent = new Intent(NewAuctionActivity.this, HomeActivity.class);
                startActivity(NewAuctionIntent);
                finish();

            }
        });*/

        getCategories();

    }

    //TODO: complete submitAuction
    private void submitAuction() throws Throwable {
        final int[] auctionId = null;
        List<Category> itemCategories = new ArrayList<>();

        final ProgressDialog mDialog = new ProgressDialog(NewAuctionActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        //Validate data
        if (edtTextName.getText().toString().isEmpty() || edtTextCategory.getText().toString().isEmpty() ||
                edtTextStartingDate.getText().toString().isEmpty() || edtTextEndDate.getText().toString().isEmpty() ||
                edtTextDescription.getText().toString().isEmpty()){
            mDialog.dismiss();
            Toast.makeText(NewAuctionActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        Date startingDate = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(edtTextStartingDate.getText().toString());
        Date endingDate = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(edtTextEndDate.getText().toString());
        Date currentDate = new Date();
        if (endingDate.compareTo(startingDate) < 0){
            mDialog.dismiss();
            Toast.makeText(NewAuctionActivity.this, "Ending Date should be later than Starting Date", Toast.LENGTH_LONG).show();
            return;
        }
        else if (currentDate.compareTo(startingDate) > 0){
            mDialog.dismiss();
            Toast.makeText(NewAuctionActivity.this, "Starting Date should be later than Current Date", Toast.LENGTH_LONG).show();
            return;
        }

        //prepare new auction request
        for (int position = 0; position < categories.length; position++){
            if (checkItems[position] == true){
                itemCategories.add(new Category(categoryIds[position], categories[position]));
            }
        }

        DecimalFormat df = new DecimalFormat("#.00");
        double initialPrice = Math.round(btnNewAuctionStartingPrice.getValue()*100)/100;
        NewAcutionRequest newAcutionRequest = new NewAcutionRequest(Common.token,
                edtTextName.getText().toString(),
                itemCategories,
                edtTextStartingDate.getText().toString(),
                edtTextEndDate.getText().toString(),
                edtTextDescription.getText().toString(),
                initialPrice);

        Call<NewAuctionResponse> call = RestClient.getClient().create(RestApi.class).postNewAuction(newAcutionRequest);

        call.enqueue(new Callback<NewAuctionResponse>() {
            @Override
            public void onResponse(Call<NewAuctionResponse> call, Response<NewAuctionResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    auctionId[0] = response.body().getAuctionId();
                    System.out.println("New auction with id=" + auctionId[0]);
                }
            }

            @Override
            public void onFailure(Call<NewAuctionResponse> call, Throwable t) {
                Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }

    private void getCategories() {
        Call<AllCategoriesResponse> call = RestClient.getClient().create(RestApi.class).getCategories();

        call.enqueue(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call, Response<AllCategoriesResponse> response) {

                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                categories = new String[response.body().getCategories().size()];
                categoryIds = new int[response.body().getCategories().size()];
                checkItems = new boolean[response.body().getCategories().size()];
                int position = 0;
                for (Category category : response.body().getCategories()){
                    categories[position] = category.getCategoryName();
                    categoryIds[position] = category.getCategoryId();
                    checkItems[position] = false;
                    position++;
                }
            }

            @Override
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {
                Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    View.OnClickListener CategoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewAuctionActivity.this);
            alertDialog.setCancelable(false)
                    .setTitle(R.string.SelectCategory)
                    .setMultiChoiceItems(categories, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked)
                                checkItems[which] = true;
                            else
                                checkItems[which] = false;
                        }
                    })
                    //on OK set categories
                    .setPositiveButton(R.string.OkButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String edtTextCategoryValue = new String();
                            for (int position = 0; position < categories.length; position++){
                                if (checkItems[position] == true){
                                    if (edtTextCategoryValue.isEmpty())
                                        edtTextCategoryValue = categories[position];
                                    else
                                        edtTextCategoryValue = edtTextCategoryValue + "," + categories[position];
                                }
                            }
                            if (!edtTextCategoryValue.isEmpty()) {
                                edtTextCategory.setText(edtTextCategoryValue);
                                edtTextCategory.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                            else {
                                edtTextCategory.setText("Please choose at least one category");
                                edtTextCategory.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    })
                    //on CANCEL return
                    .setNegativeButton(R.string.CancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener StartingDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TimePickerDialog.OnTimeSetListener timeSetListener = new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String start = edtTextStartingDate.getText().toString();
                    edtTextStartingDate.setText(start + " " + hourOfDay + ":" + minute);
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewAuctionActivity.this, timeSetListener, hourOfDay, minute, true);
            timePickerDialog.show();

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    edtTextStartingDate.setText(year + "-" + month + "-" + dayOfMonth);
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAuctionActivity.this, dateSetListener, year, month, day);
            datePickerDialog.show();
        }
    };

    View.OnClickListener EndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TimePickerDialog.OnTimeSetListener timeSetListener = new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String end = edtTextEndDate.getText().toString();
                    edtTextEndDate.setText(end + " " + hourOfDay + ":" + minute);
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewAuctionActivity.this, timeSetListener, hourOfDay, minute, true);
            timePickerDialog.show();

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    edtTextEndDate.setText(year + "-" + month + "-" + dayOfMonth);
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAuctionActivity.this, dateSetListener, year, month, day);
            datePickerDialog.show();
        }
    };

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                newAuctionImage.setImageBitmap(bitmap);
                newAuctionImage.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

}
