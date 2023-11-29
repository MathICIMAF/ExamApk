package com.alkilerprueba.amg.examapk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    public static SharedPreferences preference;
    public static SharedPreferences.Editor edit;
    public static List<Exercise> exercises;

    public static void launch(Activity activity, List<Exercise> exercises) {
        Intent intent = getLaunchIntent(activity);
        activity.startActivityForResult(intent, 1);
        TestActivity.exercises = exercises;
    }

    public static Intent getLaunchIntent(Context context ) {
        Intent intent = new Intent(context, TestActivity.class);
        return intent;
    }

    SimpleFragmentPagerAdapter pageAdapter;
    public ProgressBar progress;
    public TextView textProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        progress = (ProgressBar)findViewById(R.id.progress);
        textProgress = (TextView)findViewById(R.id.textProgress);
        for (int i = 0; i < exercises.size(); i++)
            fragmentList.add(SimpleFragment.newInstance(i));

        pageAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragmentList);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            exercises.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

class SimpleFragmentPagerAdapter extends
        FragmentPagerAdapter {
    // A List to hold our fragments
    private List<Fragment> fragments;
    // A constructor to receive a fragment manager and a List
    public SimpleFragmentPagerAdapter(FragmentManager fm,
                                      List<Fragment> fragments) {
// Call the super class' version
// of this constructor
        super(fm);
        this.fragments = fragments;
    }
    // Just two methods to override
// to get the current position of
// the adapter and the size of the List
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }
    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
