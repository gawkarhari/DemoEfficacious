package com.mobi.efficacious.demoefficacious.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.AdminApproval_Tab;
import com.mobi.efficacious.demoefficacious.fragment.DailyDiaryListFragment;
import com.mobi.efficacious.demoefficacious.fragment.Event_list_fragment;
import com.mobi.efficacious.demoefficacious.fragment.Gallery_Folder;
import com.mobi.efficacious.demoefficacious.fragment.LeaveListFragment;
import com.mobi.efficacious.demoefficacious.fragment.MessageCenter;

public class Notifiacton extends AppCompatActivity {
    String value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        FragmentManager mfragment = getSupportFragmentManager();
        try {
            Intent intent = getIntent();
            value = intent.getStringExtra("pagename");
            if (value.contentEquals("Gallery")) {
                Gallery_Folder gallery_folder = new Gallery_Folder();
                mfragment.beginTransaction().replace(R.id.content_main, gallery_folder).commitAllowingStateLoss();
            }
            if (value.contentEquals("Event")) {
                Event_list_fragment event_list_fragment = new Event_list_fragment();
                mfragment.beginTransaction().replace(R.id.content_main, event_list_fragment).commitAllowingStateLoss();
            }
            if (value.contentEquals("LeaveApply")) {
                AdminApproval_Tab adminApproval_tab = new AdminApproval_Tab();
                mfragment.beginTransaction().replace(R.id.content_main, adminApproval_tab).commitAllowingStateLoss();
            }
            if (value.contentEquals("Leave Approval")) {
                LeaveListFragment leaveListFragment = new LeaveListFragment();
                mfragment.beginTransaction().replace(R.id.content_main, leaveListFragment).commitAllowingStateLoss();
            }
            if (value.contentEquals("DailyDiary")) {
                DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                Bundle args = new Bundle();
                args.putString("value", "DailyDiary");
                dailyDiaryListFragment.setArguments(args);
                mfragment.beginTransaction().replace(R.id.content_main, dailyDiaryListFragment).commitAllowingStateLoss();
            }
            if (value.contentEquals("HomeWork")) {
                DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                Bundle args = new Bundle();
                args.putString("value", "HomeWork");
                dailyDiaryListFragment.setArguments(args);
                mfragment.beginTransaction().replace(R.id.content_main, dailyDiaryListFragment).commitAllowingStateLoss();
            }
            if (value.contentEquals("Gurukul Admin")) {
                MessageCenter msgcenter = new MessageCenter();
                Bundle args = new Bundle();
                args.putString("value", "Gurukul Admin");
                msgcenter.setArguments(args);
                mfragment.beginTransaction().replace(R.id.content_main, msgcenter).commitAllowingStateLoss();
            } else {
                MessageCenter msgcenter = new MessageCenter();
                Bundle args = new Bundle();
                args.putString("value", "");
                msgcenter.setArguments(args);
                mfragment.beginTransaction().replace(R.id.content_main, msgcenter).commitAllowingStateLoss();
            }
        } catch (Exception ex) {

        }

    }
}
