package com.dev.e_auctions.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dev.e_auctions.APIResponses.GetMessagesResponse;
import com.dev.e_auctions.Adapter.ViewPagerAdapter;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Fragments.InboxFragment;
import com.dev.e_auctions.Fragments.OutboxFragment;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class MailBoxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        TabLayout tabLayout = findViewById(R.id.msg_tab_layout);
        ViewPager viewPager = findViewById(R.id.msg_view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new InboxFragment(), "Inbox");
        viewPagerAdapter.addFragment(new OutboxFragment(), "Outbox");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
}
