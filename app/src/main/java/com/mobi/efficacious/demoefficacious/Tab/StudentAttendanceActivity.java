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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.fragment.All_Staff_Name;
import com.mobi.efficacious.demoefficacious.fragment.All_student_Name;
import com.mobi.efficacious.demoefficacious.fragment.All_teacher_Name;
import com.mobi.efficacious.demoefficacious.fragment.Staff_Mark_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Student_Mark_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Teacher_Mark_Fragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by EFF on 2/22/2017.
 */

public class StudentAttendanceActivity extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id, button_attendence;
    public static String stdno, stname, stddiv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calender, container, false);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity.getSupportActionBar() != null;
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
            try {
                button_attendence = getArguments().getString("selected_layout");
            } catch (Exception ex) {

            }
            if (button_attendence.contentEquals("Staff_layout")) {
                viewPagerAdapter.addFragment(new All_Staff_Name(), "View");
                viewPagerAdapter.addFragment(new Staff_Mark_Fragment(), "Mark");
            } else if (button_attendence.contentEquals("Stdwise_name")) {
                stname = getArguments().getString("std_name");
                stdno = getArguments().getString("std_id");
                stddiv = getArguments().getString("std_div");
                viewPagerAdapter.addFragment(new All_student_Name(), "View");
                viewPagerAdapter.addFragment(new Student_Mark_Fragment(), "Mark");

            } else {
                Timetable_sliding_tab.page = null;
                viewPagerAdapter.addFragment(new All_teacher_Name(), "View");
                viewPagerAdapter.addFragment(new Teacher_Mark_Fragment(), "Mark");
            }
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