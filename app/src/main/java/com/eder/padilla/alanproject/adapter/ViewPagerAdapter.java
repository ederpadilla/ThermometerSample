package com.eder.padilla.alanproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eder.padilla.alanproject.ui.main.fragments.ConectionStateFragment;
import com.eder.padilla.alanproject.ui.main.fragments.GrapfhFragment;
import com.eder.padilla.alanproject.ui.main.fragments.TemperatureFragment;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0:
                return TemperatureFragment.newInstance();
            case 1:
                return GrapfhFragment.newInstance();
            case 2:
                return  ConectionStateFragment.newInstance();
            default: return TemperatureFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
