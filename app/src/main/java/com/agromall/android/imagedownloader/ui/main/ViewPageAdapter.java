package com.agromall.android.imagedownloader.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agromall.android.imagedownloader.ui.main.images.all.AllImagesFragment;
import com.agromall.android.imagedownloader.ui.main.images.downloaded.DownloadedImagesFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {

    ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: {
                return new AllImagesFragment();
            }
            case 1: {
                return new DownloadedImagesFragment();
            }
            default: {
                return new AllImagesFragment();
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
