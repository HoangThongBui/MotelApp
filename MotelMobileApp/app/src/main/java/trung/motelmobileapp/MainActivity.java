package trung.motelmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import trung.motelmobileapp.Components.MainFragment;
import trung.motelmobileapp.Components.ProfileFragment;
import trung.motelmobileapp.Components.SearchFragment;
import trung.motelmobileapp.Components.TabAdapter;
import trung.motelmobileapp.Components.UnauthorizedProfileFragment;
import trung.motelmobileapp.MyTools.Constant;


public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter tabAdapter;
    SharedPreferences mySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        mySession = getSharedPreferences(Constant.MY_SESSION, Context.MODE_PRIVATE);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.account));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.magnifier));
        tabAdapter = new TabAdapter(getSupportFragmentManager());

        //Profile tab
        tabAdapter.addFragment(new UnauthorizedProfileFragment(), "");

        //Main tab
        MainFragment mainFragment = new MainFragment();
        Bundle locationInfo = new Bundle();
        locationInfo.putDouble("Latitude", getIntent().getDoubleExtra("Latitude", 0));
        locationInfo.putDouble("Longitude", getIntent().getDoubleExtra("Longitude", 0));

        mainFragment.setArguments(locationInfo);
        tabAdapter.addFragment(mainFragment, "");

        //Search tab
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(locationInfo);
        tabAdapter.addFragment(searchFragment, "");

        //viewpager setup
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
