package com.dev.e_auctions.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.e_auctions.Adapter.ViewPagerAdapter;
import com.dev.e_auctions.Fragments.InboxFragment;
import com.dev.e_auctions.Fragments.OutboxFragment;
import com.dev.e_auctions.R;

/**
 *
 */
public class MailBoxActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        tabLayout = findViewById(R.id.msg_tab_layout);
        viewPager = findViewById(R.id.msg_view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new InboxFragment(), "Inbox");
        viewPagerAdapter.addFragment(new OutboxFragment(), "Outbox");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
