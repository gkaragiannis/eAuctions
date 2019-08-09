package com.dev.e_auctions;

import android.content.Context;
import android.content.Intent;
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

import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.MenuItem;
import com.dev.e_auctions.ViewHolder.RecyclerViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView txtUsername;

    private RecyclerView recyclerMenu;
    private RecyclerView.Adapter layoutAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("toolbar.setTitle@line:28");
        //setSupportActionBar(toolbar);

        //Search FAB
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

        getActions("All");// this maybe onResume
        /*for (MenuItem i : Common.menuItemList){
            System.out.println(i.getName());
        }*/
        layoutAdapter = new RecyclerViewAdapter(Common.menuItemList, recyclerMenu.getContext());
        recyclerMenu.setAdapter(layoutAdapter);

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
            getActions("ByCategory");
            layoutAdapter.notifyDataSetChanged();// = new RecyclerViewAdapter(Common.menuItemList, recyclerMenu.getContext());
            recyclerMenu.setAdapter(layoutAdapter);
        } else if (id == R.id.nav_new_auction) {
            Intent AuctionIntent = new Intent(HomeActivity.this, AuctionActivity.class);
            startActivity(AuctionIntent);
        } else if (id == R.id.nav_my_auction) {
            getActions("BySellerId");
            layoutAdapter.notifyDataSetChanged();// = new RecyclerViewAdapter(Common.menuItemList, recyclerMenu.getContext());
            recyclerMenu.setAdapter(layoutAdapter);
        } else if (id == R.id.nav_participate_auction) {
            getActions("ByBidderId");
            layoutAdapter.notifyDataSetChanged();// = new RecyclerViewAdapter(Common.menuItemList, recyclerMenu.getContext());
            recyclerMenu.setAdapter(layoutAdapter);
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

    private void getActions(@NonNull String Query) {

        //Load auctions on home main view
        /*recyclerMenu = (RecyclerView)findViewById(R.id.rvContentHome);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);*/

        Call<List<Auction>> request = null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/gkaragiannis/testREST/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestApi client = retrofit.create(RestApi.class);

        if (Query.equals("All")){
            request = client.getAllAuctions();
        }
        else if (Query.equals("ById")){
            request = client.getAuctionsById(5);
        }
        else if (Query.equals("ByCategory")){
            request = client.getAuctionsByCategory("");
        }
        else if (Query.equals("BySellerId")){
            request = client.getAuctionsBySellerId("1");
        }
        else if (Query.equals("ByBidderId")){
            request = client.getAuctionsByBidderId("1");
        }

        request.enqueue(new Callback<List<Auction>>() {
            @Override
            public void onResponse(Call<List<Auction>> request, Response<List<Auction>> response) {
                if (!response.isSuccessful()){
                    //floating message
                    Toast.makeText(HomeActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Auction> auctionsList = response.body();
                //ArrayList<MenuItem> menuItemList = new ArrayList<>();
                if (Common.menuItemList.size()!=0){
                    Common.menuItemList.clear();
                }

                for (Auction auction : auctionsList){
                    Common.menuItemList.add(new MenuItem(auction.getName(), auction.getImage(), auction.getId()));
                }

                /*layoutAdapter = new RecyclerViewAdapter(Common.menuItemList, getBaseContext());
                recyclerMenu.setAdapter(layoutAdapter);*/
                return;

            }

            @Override
            public void onFailure(Call<List<Auction>> request, Throwable t) {
                Toast.makeText(HomeActivity.this, "Unavailable services", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

}
