package com.health.anytime;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class ChatHome extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter mTAA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        //Setting of the toolbar as well as the title
        mToolbar = findViewById(R.id.main_chat_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat");

        //The following below allows the user to switch between "Chat" and "Doctors"
        mViewPager = findViewById(R.id.chat_ViewPager);
        mTAA = new TabsAccessorAdapter(getSupportFragmentManager(),TabsAccessorAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mTAA);

        mTabLayout = findViewById(R.id.chat_tab);
        mTabLayout.setupWithViewPager(mViewPager);

    }
}
