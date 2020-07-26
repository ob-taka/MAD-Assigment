package com.health.anytime;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class ChatHome extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter mTAA;
    private FirebaseAuth auth;

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
/*
option menu code below only works with toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.chat_button){
            startActivity(new Intent(ChatHome.this, ChatHome.class));
        }
        if(item.getItemId() == R.id.logout_button){
            auth.signOut();
        }

        return true;
    }
 */

}
