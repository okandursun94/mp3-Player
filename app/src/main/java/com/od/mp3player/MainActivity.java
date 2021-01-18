package com.od.mp3player;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.od.mp3player.Adapter.ViewPagerAdapter;
import com.od.mp3player.Fragment.AlbumFragment;
import com.od.mp3player.Fragment.SongsFragment;
import com.od.mp3player.Helper.FindMusic;
import com.od.mp3player.Model.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Song> songArrayList;
    public static boolean shuffleOn =false, repeatOn=false;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        permissions();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initViewPager(){
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.table_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new AlbumFragment(),"Albümler");
        viewPagerAdapter.addFragments(new SongsFragment(),"Şarkılar");
        viewPagerAdapter.addFragments(new AlbumFragment(),"Sanatçılar");

        if(tabLayout.getWidth() < MainActivity.this.getResources().getDisplayMetrics().widthPixels)
        {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            ViewGroup.LayoutParams mParams = tabLayout.getLayoutParams();
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            tabLayout.setLayoutParams(mParams);

        }
        else
        {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());
                tabTextView.setTextSize(16);
                tabTextView.setTextColor(Color.WHITE);

                if (tabLayout.getTabCount()/2 == i) {
                    tabTextView.setTextSize(23);
                    tabTextView.setTextColor(Color.WHITE);
                    tabLayout.selectTab(tabLayout.getTabAt(i));
                }

            }

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(23);
                        ((TextView) tabViewChild).setTextColor(Color.WHITE);
                        ((TextView) tabViewChild).setSingleLine(true);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(16);
                        ((TextView) tabViewChild).setTextColor(Color.WHITE);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
            });

    }


    private void permissions(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            songArrayList = FindMusic.getAllSongs(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                songArrayList = FindMusic.getAllSongs(this);
                initViewPager();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
    }



}