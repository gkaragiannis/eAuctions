package com.dev.e_auctions.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.dev.e_auctions.APIRequests.DeleteAuctionRequest;
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
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.TimePickerDialog.*;

public class NewAuctionActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
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
    private String[] filePath;
    private Uri selectedImage;

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

        Common.auctionId = null;
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

        getCategories();

    }

    //TODO: complete submitAuction
    private void submitAuction() throws Throwable {
        List<Category> itemCategories = new ArrayList<>();
        /*String auctionId = null;
        boolean imageUploaded;*/

        ProgressDialog mDialog = new ProgressDialog(NewAuctionActivity.this);
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

        Date startingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(edtTextStartingDate.getText().toString());
        Date endingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(edtTextEndDate.getText().toString());
        if (startingDate.getTime() > endingDate.getTime()){
            mDialog.dismiss();
            Toast.makeText(NewAuctionActivity.this, "Ending Date should be later than Starting Date", Toast.LENGTH_LONG).show();
            return;
        }

        //prepare new auction request
        for (int position = 0; position < categories.length; position++){
            if (checkItems[position] == true){
                itemCategories.add(new Category(categoryIds[position], categories[position]));
            }
        }

        DecimalFormat df = new DecimalFormat("#.00");
        NewAcutionRequest newAcutionRequest = new NewAcutionRequest(Common.token,
                edtTextName.getText().toString(),
                itemCategories,
                edtTextStartingDate.getText().toString(),
                edtTextEndDate.getText().toString(),
                edtTextDescription.getText().toString(),
                df.format((double) btnNewAuctionStartingPrice.getValue()));

        postNewAuction(newAcutionRequest);
//        new HttpPostNewAuctionTask().execute(newAcutionRequest);
//        System.out.println("from Common Id: " + Common.auctionId);


//        imageUploaded = postUploadImage(Common.auctionId, filePath);
        /*if (imageUploaded)
            System.out.println("Alleluia");
        mDialog.dismiss();*/
        mDialog.dismiss();
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

    private void postNewAuction(NewAcutionRequest newAcutionRequest) {

        Call<NewAuctionResponse> call = RestClient.getClient().create(RestApi.class).postNewAuction(newAcutionRequest);

        call.enqueue(new Callback<NewAuctionResponse>() {
            @Override
            public void onResponse(Call<NewAuctionResponse> call, Response<NewAuctionResponse> response) {
                boolean mitsos;
                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Common.auctionId = Integer.toString(response.body().getAuctionId());
                System.out.println("New Auction created successfully with Id: " + Common.auctionId);
                mitsos = postUploadImage();
            }

            @Override
            public void onFailure(Call<NewAuctionResponse> call, Throwable t) {
                Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private boolean postUploadImage() {
        File file = new File(filePath[0]);
        System.out.println("Starting image upload");
        RequestBody imagePart = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody tokenPart = RequestBody.create(MediaType.parse("text/plain"), Common.token);
        RequestBody auctionIdPart = RequestBody.create(MediaType.parse("text/plain"), Common.auctionId);
        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postUploadImage(imagePart, tokenPart, auctionIdPart);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                System.out.println("On response image upload");
                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    postDelete();
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_LONG).show();
                    postDelete();
                    return;
                }

                Toast.makeText(NewAuctionActivity.this, "New Auction created successfully with Id: " + Common.auctionId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                System.out.println("On failure image upload");
                Toast.makeText(NewAuctionActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                postDelete();
                return;
            }
        });
        return true;
    }

    private void postDelete() {
        final ProgressDialog mDialog = new ProgressDialog(NewAuctionActivity.this);
        mDialog.setMessage("Αuction is being deleted");
        mDialog.show();

        DeleteAuctionRequest deleteAuctionRequest = new DeleteAuctionRequest(Common.auctionId, Common.token);

        Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postDeleteAuction(deleteAuctionRequest);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mDialog.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(NewAuctionActivity.this, "Auction didn't created due to network error.", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(NewAuctionActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();*/
                return;

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

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
                    edtTextStartingDate.setText(start + " " + String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute));
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewAuctionActivity.this, timeSetListener, hourOfDay, minute, true);
            timePickerDialog.show();

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    edtTextStartingDate.setText(year + "-" + String.format("%02d",month) + "-" + String.format("%02d",dayOfMonth));
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAuctionActivity.this, dateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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
                    edtTextEndDate.setText(end + " " + String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute));
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewAuctionActivity.this, timeSetListener, hourOfDay, minute, true);
            timePickerDialog.show();

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    edtTextEndDate.setText(year + "-" + String.format("%02d",month) + "-" + String.format("%02d",dayOfMonth));
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAuctionActivity.this, dateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    };

    private void selectImage(){
        /*Intent intent = new Intent();
        intent.setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
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
        }*/
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            //display image
            selectedImage = data.getData();
            newAuctionImage.setImageURI(selectedImage);
            newAuctionImage.setVisibility(View.VISIBLE);

            //retrieve absolute path
            filePath = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            //Get the column index of MediaStore.Images.Media.DATA
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            //Gets the String value in the column
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            System.out.println(filePath[0]);
            /*Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            // Set the Image in ImageView after decoding the String
            imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));*/
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private class HttpPostNewAuctionTask extends AsyncTask<NewAcutionRequest, Void, NewAuctionResponse> {

        @Override
        protected NewAuctionResponse doInBackground(NewAcutionRequest... newAcutionRequests) {
            Call<NewAuctionResponse> call = RestClient.getClient().create(RestApi.class).postNewAuction(newAcutionRequests[0]);
            Response<NewAuctionResponse> response = null;
            try {
                response = call.execute();

                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return null;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return null;
                }
                else{
                    Common.auctionId = Integer.toString(response.body().getAuctionId());
                    System.out.println("from response Id: " + response.body().getAuctionId());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.body();
        }

        @Override
        protected void onPostExecute(NewAuctionResponse newAuctionResponse) {
            new HttpPostUploadImageTask().execute(newAuctionResponse);
        }
    }


    private class HttpPostUploadImageTask extends  AsyncTask<NewAuctionResponse, Void, GeneralResponse>{
        @Override
        protected GeneralResponse doInBackground(NewAuctionResponse... newAuctionResponses) {
            Response<GeneralResponse> response = null;
            File file = new File(filePath[0]);
            System.out.println("Starting image upload");
            RequestBody imagePart = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody tokenPart = RequestBody.create(MediaType.parse("text/plain"), Common.token);
            RequestBody auctionIdPart = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(newAuctionResponses[0].getAuctionId()));
            Call<GeneralResponse> call = RestClient.getClient().create(RestApi.class).postUploadImage(imagePart, tokenPart, auctionIdPart);

            try {
                response = call.execute();

                if (!response.isSuccessful()){
                    Toast.makeText(NewAuctionActivity.this, Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    System.out.println("Not successgul call " + Integer.toString(response.code()));
                    return null;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(NewAuctionActivity.this, response.body().getStatusMsg(), Toast.LENGTH_LONG).show();
                    System.out.println(response.body().getStatusMsg());
                    return null;
                }
                else {
                    Toast.makeText(NewAuctionActivity.this, "New Auction created successfully with Id: " + Common.auctionId, Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response == null)
                return null;
            else
                return response.body();
        }

        @Override
        protected void onPostExecute(GeneralResponse generalResponse) {
            if (generalResponse == null) {
//                new HttpPostDeleteAuctionTask.execute();
                System.out.println("Skata");
            }
            else if (generalResponse.getStatusCode().equals("SUCCESS")) {
                System.out.println("Aliluia");
            }
            else
                System.out.println("Skata");
        }
    }
}
