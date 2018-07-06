package trung.motelmobileapp.Components;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TabAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> alFragments = new ArrayList<>();
    ArrayList<String> alTitles = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        alFragments.add(fragment);
        alTitles.add(title);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void replaceFragmentAtPosition (Fragment fragment, int pos) {
        alFragments.set(pos, fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return alFragments.get(position);
    }

    @Override
    public int getCount() {
        return alFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return alTitles.get(position);
    }
}
