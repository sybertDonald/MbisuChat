package com.example.sy.infosys_ips.Chat;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sy.infosys_ips.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {


    Toolbar toolbar12;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        toolbar12 = findViewById(R.id.toolbar12);
        setSupportActionBar(toolbar12);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar12.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tabchat);
        viewPager = findViewById(R.id.viewpagechat);


        ViewpageAdapter viewpageAdapter =new ViewpageAdapter(getSupportFragmentManager());
        viewpageAdapter.Addfragment(new ChatFragment(),"Chat");
        viewpageAdapter.Addfragment(new UserFragment(),"User");
        viewPager.setAdapter(viewpageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }



    class  ViewpageAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragments;
        ArrayList<String> title;

        public ViewpageAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.title = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void Addfragment(Fragment fragment,String title2){
            fragments.add(fragment);
            title.add(title2);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }
}
