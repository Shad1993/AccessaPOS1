package com.accessa.ibora.Admin.CompanyInfo.FormPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CompanyPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 3;

    private FirstSlideFragment firstSlideFragment;
    private SecondSlideFragment secondSlideFragment;
    private ThirdSlideFragment thirdSlideFragment;

    public CompanyPagerAdapter(FragmentManager fm) {
        super(fm);
        firstSlideFragment = new FirstSlideFragment();
        secondSlideFragment = new SecondSlideFragment();
        thirdSlideFragment = new ThirdSlideFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return firstSlideFragment;
        } else if (position == 1) {
            return secondSlideFragment;
        } else if (position == 2) {
            return thirdSlideFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    public String getFirstSlideData() {
        if (firstSlideFragment != null) {
            return firstSlideFragment.getCompanyName();
        }
        return "";
    }

    public String getSecondSlideData() {
        if (firstSlideFragment != null) {
            return firstSlideFragment.getShopName();
        }
        return "";
    }

    public String getThirdSlideData() {
        if (secondSlideFragment != null) {
            return secondSlideFragment.getVATNo();
        }
        return "";
    }

    public String getFourthSlideData() {
        if (secondSlideFragment != null) {
            return secondSlideFragment.getBRNNo();
        }
        return "";
    }

    public String getFifthSlideData() {
        if (thirdSlideFragment != null) {
            return thirdSlideFragment.getADR1();
        }
        return "";
    }

    public String getSixthSlideData() {
        if (thirdSlideFragment != null) {
            return thirdSlideFragment.getADR2();
        }
        return "";
    }

    public String getSeventhSlideData() {
        if (thirdSlideFragment != null) {
            return thirdSlideFragment.getADR3();
        }
        return "";
    }

    public String getEighthSlideData() {
        if (thirdSlideFragment != null) {
            return thirdSlideFragment.getTelNo();
        }
        return "";
    }

    public String getNinthSlideData() {
        if (thirdSlideFragment != null) {
            return thirdSlideFragment.getFaxNo();
        }
        return "";
    }

    public String getLogoPath() {
        if (firstSlideFragment != null) {
            return firstSlideFragment.getLogoPath();
        }
        return "";
    }
}
