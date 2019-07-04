package com.mobi.efficacious.demoefficacious.Tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.fragment.Friday_teacher_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Monday_teacher_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Saturday_teacher_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Thursday_teacher_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Tuesday_teacher_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Wednesday_teacher_Fragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by EFF-4 on 3/22/2018.
 */

public class TimetableActivity_teacher extends Fragment {
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public static String teacher_id, intSchool_id;
    String role_id, ID_;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calender, container, false);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        try {
            if (role_id.contentEquals("3")) {
                ID_ = settings.getString("TAG_USERID", "");
                teacher_id = ID_;
            } else {
                if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                    intSchool_id = getArguments().getString("intSchool_id");
                }
                teacher_id = getArguments().getString("teacher_id");
            }
        } catch (Exception ex) {

        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            if (role_id.contentEquals("3")) {
                                TimetableActivity_teacher timetableActivity_teacher = new TimetableActivity_teacher();
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, timetableActivity_teacher).commitAllowingStateLoss();
                            } else {
                                Timetable_sliding_tab timetable_sliding_tab = new Timetable_sliding_tab();
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, timetable_sliding_tab).commitAllowingStateLoss();
                            }
                        } catch (Exception ex) {

                        }

                        return true;
                    }
                }
                return false;
            }
        });
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity.getSupportActionBar() != null;
        // activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightorange)));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        // after you set the adapter you have to check if view is laid out, i did a custom method for it
        if (ViewCompat.isLaidOut(tabLayout)) {
            setViewPagerListener();
        } else {
            tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    setViewPagerListener();
                    tabLayout.removeOnLayoutChangeListener(this);
                }
            });
        }
    }

    private void setViewPagerListener() {
        tabLayout.setupWithViewPager(viewPager);
        // use class TabLayout.ViewPagerOnTabSelectedListener
        // note that it's a class not an interface as OnTabSelectedListener, so you can't implement it in your activity/fragment
        // methods are optional, so if you don't use them, you can not override them (e.g. onTabUnselected)
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        try {
            viewPagerAdapter.addFragment(new Monday_teacher_Fragment(), "MON");
            viewPagerAdapter.addFragment(new Tuesday_teacher_Fragment(), "TUE");
            viewPagerAdapter.addFragment(new Wednesday_teacher_Fragment(), "WED");
            viewPagerAdapter.addFragment(new Thursday_teacher_Fragment(), "THR");
            viewPagerAdapter.addFragment(new Friday_teacher_Fragment(), "FRI");
            viewPagerAdapter.addFragment(new Saturday_teacher_Fragment(), "SAT");
        } catch (Exception ex) {

        }

        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }
}