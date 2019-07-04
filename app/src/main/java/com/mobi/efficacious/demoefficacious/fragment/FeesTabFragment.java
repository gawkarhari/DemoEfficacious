package com.mobi.efficacious.demoefficacious.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

public class FeesTabFragment extends Fragment {
    View myview;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ConnectionDetector cd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.fees_tabfragment,null);
        toolbar = (Toolbar) myview.findViewById(R.id.studattendancetoolbar);
        toolbar.setTitle("Fees");
        viewPager = (ViewPager) myview.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return myview;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            adapter.addFrag(new Monthly_Fees_Fragment(), "Monthly Fee");
            //adapter.addFrag(new Late_Fees_Fragment(), "Late Fee");
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}


