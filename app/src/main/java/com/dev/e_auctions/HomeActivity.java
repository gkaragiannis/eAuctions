package com.dev.e_auctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.MenuItem;
import com.dev.e_auctions.ViewHolder.RecyclerViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView txtUsername;
    private ArrayList<MenuItem> menuItemList = new ArrayList<>();

    private RecyclerView recyclerMenu;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter layoutAdapter = new RecyclerViewAdapter(menuItemList, HomeActivity.this);
    private RecyclerView.LayoutManager layoutManager;


    private ArrayList<Auction> auctionList;

    public ArrayList<Auction> getAuctionList(){
        return auctionList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("toolbar.setTitle@line:28");
        //setSupportActionBar(toolbar);

        //Create Search FAB
        FloatingActionButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Create Navigation Menu
        //NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Set Username on nav_header_home
        View headView = navigationView.getHeaderView(0);
        txtUsername = (TextView)headView.findViewById(R.id.txtUsername);
        if (Common.currentUser != null) {
            txtUsername.setText(Common.currentUser.getUsername());
        }
        else{
            txtUsername.setText("Guest");
        }

        //Load auctions on home main view
        recyclerMenu = (RecyclerView)findViewById(R.id.rvContentHome);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        recyclerView = (RecyclerView) HomeActivity.this.findViewById(R.id.rvContentHome);

        new HttpRequestTask().execute("All");
    }

    @Override
    protected void onResume(){
        super.onResume();
        recyclerView.setAdapter(layoutAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(android.view.MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {

        } else if (id == R.id.nav_category) {
            new HttpRequestTask().execute("ByCategory");
        } else if (id == R.id.nav_new_auction) {
            Intent AuctionIntent = new Intent(HomeActivity.this, AuctionActivity.class);
            startActivity(AuctionIntent);
        } else if (id == R.id.nav_my_auction) {
            new HttpRequestTask().execute(new String[]{"BySellerId",Integer.toString(Common.currentUser.getId())});
        } else if (id == R.id.nav_participate_auction) {
            new HttpRequestTask().execute(new String[]{"ByBidderId",Integer.toString(Common.currentUser.getId())});
        } else if (id == R.id.nav_logout) {
            Intent signIn = new Intent(HomeActivity.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Common.currentUser = null;
            finish();
            startActivity(signIn);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    private class HttpRequestTask extends AsyncTask <String, Void, ArrayList<Auction>>{

        final ProgressDialog mDialog = new ProgressDialog(HomeActivity.this);

        protected void onPreExecute(){
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }

        @Override
        protected ArrayList<Auction> doInBackground(String... strings) {

            ArrayList<Auction> resultList = new ArrayList<>();
            Call<List<Auction>> request = null;
            publishProgress();
            if (strings[0].equals("All")) {
                request = RestClient.getClient().create(RestApi.class).getAllAuctions();
            }
            else if (strings[0].equals("ByCategory")) {
                request = RestClient.getClient().create(RestApi.class).getAuctionsByCategory("");
            }
            else if (strings[0].equals("BySellerId")) {
                request = RestClient.getClient().create(RestApi.class).getAuctionsBySellerId(strings[1]);
            }
            else if (strings[0].equals("ByBidderId")) {
                request = RestClient.getClient().create(RestApi.class).getAuctionsByBidderId(strings[1]);
            }
            try {
                Response<List<Auction>> response = request.execute();
                resultList.addAll(response.body());
            } catch (IOException e){
                e.printStackTrace();
            }

            HomeActivity.this.auctionList = resultList;
            return HomeActivity.this.getAuctionList();
        }

        @Override
        protected void onPostExecute(ArrayList<Auction> auctionList){

            mDialog.dismiss();
            if (auctionList != null && auctionList.size() > 0){ //&& auctionList.size() > 0 keep it or not
                menuItemList = getMenuItems(auctionList);
                layoutAdapter.updateDataset(menuItemList);
            }
            else{
                Toast.makeText(HomeActivity.this, "Oops! No auctions found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private ArrayList<MenuItem> getMenuItems(List<Auction> auctionsList){

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        if (auctionsList != null && auctionsList.size() > 0){
            for (Auction auction : auctionsList){
                menuItems.add(new MenuItem(auction.getName(), auction.getImage(), auction.getId()));
            }
        }

        return menuItems;
    }
}
