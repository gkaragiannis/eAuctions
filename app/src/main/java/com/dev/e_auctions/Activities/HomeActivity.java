package com.dev.e_auctions.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.dev.e_auctions.APIResponses.AllCategoriesResponse;
import com.dev.e_auctions.APIResponses.AuctionsResponse;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Auction;
import com.dev.e_auctions.Model.Category;
import com.dev.e_auctions.Model.MenuItem;
import com.dev.e_auctions.Adapter.RecyclerViewAdapter;
import com.dev.e_auctions.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {

    private List<String> suggestionList = new ArrayList<>();
    private List<String> getSuggestionList(){
        return suggestionList;
    }
    private void setSuggestionList() {
        suggestionList.clear();
        if (getMenuItemList() != null && getMenuItemList().size() > 0){
            for (MenuItem menuItem : getMenuItemList()){
                suggestionList.add(menuItem.getName());
            }
        }
    }
    private ArrayList<MenuItem> menuItemList = new ArrayList<>();
    public ArrayList<MenuItem> getMenuItemList() {
        return menuItemList;
    }
    private ArrayList<Auction> auctionList;
    public ArrayList<Auction> getAuctionList(){
        return auctionList;
    }
    private ArrayList<Category> categoryList;
    public ArrayList<Category> getCategoryList(){
        return categoryList;
    }

    private TextView txtUsername;
    private Toolbar toolbar;
    private MaterialSearchBar searchBar;
    private RecyclerView recyclerMenu;
    private RecyclerViewAdapter layoutAdapter = new RecyclerViewAdapter(menuItemList, HomeActivity.this);
    private RecyclerView.LayoutManager layoutManager;

    private SuggestionsAdapter sugestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.auctions);
        //setSupportActionBar(toolbar);

        //Create Search Bar
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
        /*LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        SuggestionsAdapter customSuggestionsAdapter = new SuggestionsAdapter(inflater);*/
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggestions = new ArrayList<String>();
                for (String suggestion : suggestionList){
                    if (suggestion.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        suggestions.add(suggestion);
                    }
                }
                searchBar.setLastSuggestions(suggestions);
                /*customSuggestionsAdapter.setSuggestions(suggestions);
                searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Restore suggest adapter, when searchBar is closed
                if (!enabled){
                    recyclerMenu.setAdapter(sugestionAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Show results of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });*/

        //Create Search FAB
        FloatingActionButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.enableSearch();
                searchBar.setVisibility(View.VISIBLE);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Create Navigation Menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Username on nav_header_home
        View headView = navigationView.getHeaderView(0);
        txtUsername = (TextView)headView.findViewById(R.id.txtUsername);
        if (Common.currentUser != null) {
            txtUsername.setText(Common.currentUser.getUsername());
            navigationView.getMenu().setGroupVisible(R.id.memberGroup, true);
        } else{
            txtUsername.setText("Guest");
            navigationView.getMenu().setGroupVisible(R.id.memberGroup, false);
        }

        //Create Recycler Menu
        recyclerMenu = (RecyclerView)findViewById(R.id.rvContentHome);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        //Request All Auctions
        new HttpRequestAuctionsTask().execute("All");
    }

    /*private void startSearch(CharSequence text) {
        //searchAdapter = new RecyclerViewAdapter<>()
    }*/

    @Override
    protected void onResume(){
        super.onResume();
        recyclerMenu.setAdapter(layoutAdapter);
        /*setSuggestionList();
        searchBar.setLastSuggestions(suggestionList);*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Common.currentUser = null;
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

    //Navigation Menu functionality
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(android.view.MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            searchBar.enableSearch();
            searchBar.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_auctions) {
            new HttpRequestAuctionsTask().execute("All");
            toolbar.setTitle(R.string.auctions);
        } else if (id == R.id.nav_category) {
            new HttpRequestCategoriesTask().execute();
            toolbar.setTitle(R.string.category);
        } else if (id == R.id.nav_new_auction) {
            startActivity(new Intent(HomeActivity.this, NewAuctionActivity.class));
        } else if (id == R.id.nav_my_auction) {
            new HttpRequestAuctionsTask().execute(new String[]{"BySellerId",Integer.toString(Common.currentUser.getId())});
            toolbar.setTitle(R.string.myAuction);
        } else if (id == R.id.nav_participate_auction) {
            new HttpRequestAuctionsTask().execute(new String[]{"ByBidderId",Integer.toString(Common.currentUser.getId())});
            toolbar.setTitle(R.string.participateAuction);
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

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.disableSearch();
        searchBar.setVisibility(View.GONE);
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                searchBar.disableSearch();
                searchBar.setVisibility(View.GONE);
                break;
        }
    }

    private class HttpRequestAuctionsTask extends AsyncTask <String, Void, ArrayList<Auction>>{

        final ProgressDialog mDialog = new ProgressDialog(HomeActivity.this);

        protected void onPreExecute(){
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }

        @Override
        protected ArrayList<Auction> doInBackground(String... strings) {

            ArrayList<Auction> resultList = new ArrayList<>();
            Call<AuctionsResponse> request = null;
            publishProgress();

            //prepare for call
            if (strings[0].equals("All")) {
                request = RestClient.getClient().create(RestApi.class).getOpenAuctions();
            }
            /*else if (strings[0].equals("ByCategory")) {
                request = RestClient.getClient().create(RestApi.class).getAuctionsByCategory(strings[1]);
            }
            else if (strings[0].equals("BySellerId")) {
                request = RestClient.getClient().create(RestApi.class).getAuctionsBySellerId(strings[1]);
            }
            else if (strings[0].equals("ByBidderId")) {
                request = RestClient.getClient().create(RestApi.class).getAuctionsByBidderId(strings[1]);
            }*/

            //try execute and get response
            try {
                Response<AuctionsResponse> response = request.execute();

                if (!response.isSuccessful()){
                    //floating message
                    Toast.makeText(HomeActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                    return null;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    Toast.makeText(HomeActivity.this, response.body().getStatusMsg(), Toast.LENGTH_SHORT).show();
                    return null;
                }
                else {

                    resultList.addAll(response.body().getAuctions());
                    /*Common.token = response.body().getToken();
                    Toast.makeText(SignIn.this, "Welcome back " + edtUsername.getText() + " !", Toast.LENGTH_SHORT).show();

                    Intent SignInIntent = new Intent(SignIn.this, HomeActivity.class);
                    startActivity(SignInIntent);
                    finish();*/
                }
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
                menuItemList = getAuctionListMenuItems();
                layoutAdapter.updateDataset(getMenuItemList());
            }
            else{
                Toast.makeText(HomeActivity.this, "Oops! No auctions found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class HttpRequestCategoriesTask extends AsyncTask <Void, Void, ArrayList<Category>>{

        final ProgressDialog mDialog = new ProgressDialog(HomeActivity.this);

        protected void onPreExecute(){
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }

        @Override
        protected ArrayList<Category> doInBackground(Void... voids) {
            ArrayList<Category> resultList = new ArrayList<>();

            publishProgress();
            Call<AllCategoriesResponse> request = RestClient.getClient().create(RestApi.class).getCategories();

            try {
                Response<AllCategoriesResponse> response = request.execute();
                resultList.addAll(response.body().getCategories());
            } catch (IOException e){
                e.printStackTrace();
            }

            HomeActivity.this.categoryList = resultList;
            return HomeActivity.this.getCategoryList();
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categoryList){

            mDialog.dismiss();
            if (categoryList != null && categoryList.size() > 0){ //&& auctionList.size() > 0 keep it or not
                menuItemList = getCategoryListMenuItems();
                layoutAdapter.updateDataset(getMenuItemList());
            }
            else{
                Toast.makeText(HomeActivity.this, "Oops! No auctions found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private ArrayList<MenuItem> getAuctionListMenuItems(){

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        if (getAuctionList() != null && getAuctionList().size() > 0){
            for (Auction auction : getAuctionList()){
                menuItems.add(new MenuItem(auction.getNameOfItem(), auction.getImage(), auction.getId()));
            }
        }

        return menuItems;
    }

    private ArrayList<MenuItem> getCategoryListMenuItems(){

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        if (getCategoryList() != null && getCategoryList().size() > 0){
            for (Category category: getCategoryList()){
                menuItems.add(new MenuItem(category.getCategoryName(), category.getCategoryImage(), category.getCategoryId()));
            }
        }
        return menuItems;
    }

}
