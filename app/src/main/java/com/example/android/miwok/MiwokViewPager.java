package com.example.android.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ehayes on 2/8/2017.
 */

public class MiwokViewPager extends FragmentPagerAdapter {
    public MiwokViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
            if(position == 0){
                return new NumbersFragment();
            }else if(position == 1){
                return new FamilyFragment();
            }else if(position == 2){
                return new ColorsFragment();
            }else{
                return new PhrasesFragment();
            }
        }

    @Override
    public int getCount() {
        return 4;
    }
}
