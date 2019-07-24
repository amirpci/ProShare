package sidev17.siits.procks;

import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<android.support.v4.app.Fragment> lstFragment = new ArrayList<>();
    private final List<String> lstTitles = new ArrayList<>();

    public ViewPagerAdapter(android.support.v4.app.FragmentManager fm){
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lstTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public void addFragment(android.support.v4.app.Fragment fragment, String title){
        lstFragment.add(fragment);
        lstTitles.add(title);
    }
    public void addFragment(android.support.v4.app.Fragment fragment[], String title[]){
        if(fragment.length != title.length)
            throw new IllegalArgumentException("\"fragment.length\"  &  \"title.length\" HARUS SAMA!");
        for(int i= 0; i< fragment.length; i++)
            addFragment(fragment[i], title[i]);
    }
}
