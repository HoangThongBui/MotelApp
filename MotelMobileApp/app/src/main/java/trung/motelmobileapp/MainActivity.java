package trung.motelmobileapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import trung.motelmobileapp.Components.MainFragment;
import trung.motelmobileapp.Components.ProfileFragment;
import trung.motelmobileapp.Components.SearchFragment;
import trung.motelmobileapp.Components.TabAdapter;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.account));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.magnifier));
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new ProfileFragment(), "");
        MainFragment mainFragment = new MainFragment();
        Bundle cityInfo = new Bundle();
        cityInfo.putString("City", getIntent().getStringExtra("City"));
        mainFragment.setArguments(cityInfo);
        tabAdapter.addFragment(mainFragment, "");
        tabAdapter.addFragment(new SearchFragment(), "");
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
