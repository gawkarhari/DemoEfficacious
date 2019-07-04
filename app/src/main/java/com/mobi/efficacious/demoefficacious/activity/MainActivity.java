package com.mobi.efficacious.demoefficacious.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.infideap.drawerbehavior.Advance3DDrawerLayout;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.AdminApproval_Tab;
import com.mobi.efficacious.demoefficacious.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.demoefficacious.Tab.Chating_Sliding_Tab;
import com.mobi.efficacious.demoefficacious.Tab.DailyDiary_Tab;
import com.mobi.efficacious.demoefficacious.Tab.Event_Tab;
import com.mobi.efficacious.demoefficacious.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.demoefficacious.Tab.TimetableActivity_student;
import com.mobi.efficacious.demoefficacious.Tab.TimetableActivity_teacher;
import com.mobi.efficacious.demoefficacious.Tab.Timetable_sliding_tab;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.fragment.Admin_Dashboard;
import com.mobi.efficacious.demoefficacious.fragment.All_Standard_Book;
import com.mobi.efficacious.demoefficacious.fragment.DailyDiaryListFragment;
import com.mobi.efficacious.demoefficacious.fragment.Event_list_fragment;
import com.mobi.efficacious.demoefficacious.fragment.Gallery_Folder;
import com.mobi.efficacious.demoefficacious.fragment.Group_Chat_Fragment_Admin;
import com.mobi.efficacious.demoefficacious.fragment.HolidayFragment;
import com.mobi.efficacious.demoefficacious.fragment.LeaveListFragment;
import com.mobi.efficacious.demoefficacious.fragment.MessageCenter;
import com.mobi.efficacious.demoefficacious.fragment.Noticeboard;
import com.mobi.efficacious.demoefficacious.fragment.Profile_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.Sms_Fragment;
import com.mobi.efficacious.demoefficacious.fragment.StandardWise_Book;
import com.mobi.efficacious.demoefficacious.fragment.StudentChangePassword;
import com.mobi.efficacious.demoefficacious.fragment.StudentExamFragment;
import com.mobi.efficacious.demoefficacious.fragment.StudentSyllabusFragment;
import com.mobi.efficacious.demoefficacious.fragment.Student_Std_Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int k, FabmenuStatus = 0;
    String title = "";
    ConnectionDetector cd;
    ImageView chating_imgbtn;
    public static CircleImageView profile_img;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id, name1, user_id, academic_id, school_id, stud_id, stand_id;
    String Teacher_statndard,Teacher_division;
    public static FragmentManager fragmentManager;
    private Advance3DDrawerLayout drawer;
    NavigationView navigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            user_id = settings.getString("TAG_USERID", "");
            academic_id = settings.getString("TAG_ACADEMIC_ID", "");
            school_id = settings.getString("TAG_SCHOOL_ID", "");
        } catch (Exception ex) {

        }
        try {
            chating_imgbtn = (ImageView) findViewById(R.id.chating_imgbtn);
            fragmentManager = getSupportFragmentManager();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            cd = new ConnectionDetector(getApplicationContext());
            profile_img = (CircleImageView) findViewById(R.id.profile_img);
            drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            FragmentManager mfragment = getSupportFragmentManager();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setItemIconTintList(null);
            drawer.setViewScale(Gravity.START, 0.96f);
            drawer.setRadius(Gravity.START, 20);
            drawer.setViewElevation(Gravity.START, 8);
            drawer.setViewRotation(Gravity.START, 15);
        } catch (Exception Ex) {

        }

        try {
            Menu menu = navigationView.getMenu();
            MenuItem result = menu.findItem(R.id.nav_Result);
            result.setVisible(false);
            MenuItem library = menu.findItem(R.id.nav_library);
            library.setVisible(false);
            if (role_id.contentEquals("1") || role_id.contentEquals("2")) {

                MenuItem nav_sendNotification = menu.findItem(R.id.nav_sendNotification);
                nav_sendNotification.setVisible(false);
                MenuItem message = menu.findItem(R.id.nav_message);
                message.setVisible(false);
                MenuItem target = menu.findItem(R.id.nav_dashboard);
                target.setVisible(false);
                MenuItem teacher = menu.findItem(R.id.nav_teacherAttendence);
                teacher.setVisible(false);
                MenuItem student = menu.findItem(R.id.nav_studentAttendence);
                student.setTitle("Self Attendance");
                MenuItem diary = menu.findItem(R.id.nav_Diary);
                diary.setVisible(false);
                MenuItem staff = menu.findItem(R.id.nav_staffAttendence);
                staff.setVisible(false);
                MenuItem chat = menu.findItem(R.id.nav_chat);
                chat.setVisible(false);
                if (!cd.isConnectingToInternet()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {
                    try {
                        fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                    getSupportActionBar().setTitle("Profile");
                    } catch (Exception ex) {

                    }
                }
            } else if (role_id.contentEquals("3")) {
                try
                {
                    MenuItem nav_sendNotification = menu.findItem(R.id.nav_sendNotification);
                    nav_sendNotification.setVisible(false);
                    Teacher_statndard=settings.getString("TAG_STANDERDID", "");
                    Teacher_division=settings.getString("TAG_DIVISIONID", "");

                    if(Teacher_statndard.contentEquals("0")||Teacher_division.contentEquals("0"))
                    {
                        MenuItem target;
                        target = menu.findItem(R.id.nav_studentAttendence);
                        target.setVisible(false);
                    }else
                    {
                        MenuItem target;
                        target = menu.findItem(R.id.nav_studentAttendence);
                        target.setVisible(true);
                    }
                    MenuItem staff = menu.findItem(R.id.nav_staffAttendence);
                    staff.setVisible(false);
                    MenuItem target = menu.findItem(R.id.nav_dashboard);
                    target.setVisible(false);
                    MenuItem message = menu.findItem(R.id.nav_message);
                    message.setVisible(false);
                    MenuItem student = menu.findItem(R.id.nav_teacherAttendence);
                    student.setTitle("Self Attendance");
                    MenuItem payment = menu.findItem(R.id.nav_payment);
                    payment.setVisible(false);
                    MenuItem chat = menu.findItem(R.id.nav_chat);
                    chat.setVisible(false);
                    if (!cd.isConnectingToInternet()) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    } else {
                        try {
                            fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                            getSupportActionBar().setTitle("Profile");
                        } catch (Exception ex) {
                        }
                    }
                }catch (Exception ex)
                {
                }
            }else if(role_id.contentEquals("4"))
            {
                MenuItem nav_sendNotification = menu.findItem(R.id.nav_sendNotification);
                nav_sendNotification.setVisible(false);
//                MenuItem staff = menu.findItem(R.id.nav_staffAttendence);
//                staff.setVisible(false);
                MenuItem target;
                target = menu.findItem(R.id.nav_studentAttendence);
                target.setTitle("Self Attendance");
                target = menu.findItem(R.id.nav_dashboard);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_teacherAttendence);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_syllabus);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_timetable);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_examination);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_library);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_Homework);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_Diary);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_Result);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_payment);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_Event);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_message);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_chat);
                target.setVisible(false);

                if (!cd.isConnectingToInternet()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {

                    try {
                        MenuItem payment;
                        payment = menu.findItem(R.id.nav_payment);
                        payment.setVisible(false);
                        fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                        getSupportActionBar().setTitle("Profile");
                    } catch (Exception ex) {

                    }
                }
            }
            else {
                MenuItem target;
                target = menu.findItem(R.id.nav_payment);
                target.setVisible(false);
                MenuItem staff = menu.findItem(R.id.nav_staffAttendence);
                staff.setVisible(true);
                if (!cd.isConnectingToInternet()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {
                    try {
                    fragmentManager.beginTransaction().replace(R.id.content_main, new Admin_Dashboard()).commitAllowingStateLoss();
                    getSupportActionBar().setTitle("Dashboard");
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
        }
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {
                            getSupportActionBar().setTitle("Profile");
                            fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                    }
                }catch (Exception ex)
                {
                }
            }
        });
         chating_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    try
                    {
                        getSupportActionBar().setTitle("Chat");
                        fragmentManager.beginTransaction().replace(R.id.content_main,new Chating_Sliding_Tab() ).commitAllowingStateLoss();
                    }catch (Exception ex)
                    {
                    }

                }
            }
        });
        if(role_id.contentEquals("1")||role_id.contentEquals("2")||role_id.contentEquals("3"))
        {
            chating_imgbtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        k++;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k = 0;
            }
        }, 1000);
        if (k == 1) {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure want to Exit?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ChangePassword) {
            try {
                getSupportActionBar().setTitle("Change Password");
                fragmentManager.beginTransaction().replace(R.id.content_main, new StudentChangePassword()).commitAllowingStateLoss();
            } catch (Exception ex) {
            }
            return true;
        }
        if (id == R.id.action_Logout) {
            try {
                SharedPreferences.Editor editor_delete = settings.edit();
                editor_delete.clear().commit();
                this.deleteDatabase("Notifications");
                Intent intent = new Intent(MainActivity.this, Login_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } catch (Exception ex) {
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager mfragment = getSupportFragmentManager();
        cd = new ConnectionDetector(getApplicationContext());
        drawer.closeDrawer(GravityCompat.START);
        try {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                //Admin Manager Principal menu option  Admin roleid=5,Principal roleid=6 ,Manager roleid=7
                if (role_id.equalsIgnoreCase("5")||role_id.equalsIgnoreCase("6")||role_id.equalsIgnoreCase("7")) {
                    if (id == R.id.nav_dashboard) {
                        try {
                            title = "Dashboard";
                            mfragment.beginTransaction().replace(R.id.content_main, new Admin_Dashboard()).commitAllowingStateLoss();
                        } catch (Exception ex) {
                        }
                    }
                    if (id == R.id.nav_sendNotification) {
                        try {
                            title = "Send Notification";
                            mfragment.beginTransaction().replace(R.id.content_main, new Group_Chat_Fragment_Admin()).commitAllowingStateLoss();
                        } catch (Exception ex) {
                        }
                    }
                    else if (id == R.id.nav_studentAttendence) {
                        try
                        {
                            title = "Attendance";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "Std");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }else if(id==R.id.nav_teacherAttendence)
                    {
                        try
                        {
                            title = "Attendance";
                            StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                            Bundle args = new Bundle();
                            args.putString("selected_layout","Teacher_linearlayout");
                            studentAttendanceActivity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_syllabus) {
                        try
                        {
                            title="Syllabus";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "Syllabus");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_timetable) {
                        try
                        {
                            title = "TimeTable";
                            mfragment.beginTransaction().replace(R.id.content_main, new Timetable_sliding_tab()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_examination) {
                        try {
                            title = "Examination";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "Exam");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        } catch (Exception ex) {
                        }
                    } else if (id == R.id.nav_payment) {
                        try
                        {
                            new AlertDialog.Builder(this)
                                    .setMessage("Coming Soon...")
                                    .setNegativeButton("ok", null)
                                    .show();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_library) {
                            try
                            {
                                title = "Library";
                                mfragment.beginTransaction().replace(R.id.content_main, new All_Standard_Book()).commitAllowingStateLoss();

                            }catch (Exception ex)
                            {
                            }
                    }else if (id == R.id.nav_Diary) {
                        try
                        {
                            title = "Daily Diary";
                            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                            Bundle args = new Bundle();
                            args.putString("value", "DailyDiary");
                            dailyDiaryListFragment.setArguments(args);
                            mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Homework)
                    {
                        try
                        {
                            title = "Home Work";
                            DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                            Bundle args = new Bundle();
                            args.putString("value","HomeWork");
                            dailyDiary_tab.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_calender) {
                        try
                        {
                            title = "Calender";
                            mfragment.beginTransaction().replace(R.id.content_main, new HolidayFragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_leave) {
                        try
                        {
                            title="Leave";
                            mfragment.beginTransaction().replace(R.id.content_main,new AdminApproval_Tab() ).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_Event) {
                        try
                        {
                            title="Event";
                            mfragment.beginTransaction().replace(R.id.content_main,new Event_list_fragment() ).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_noticeboard) {
                        try
                        {
                            title = "Noticeboard";
                            mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    } else if (id == R.id.nav_message) {
                        try {
                            title = "Messaging";
                            mfragment.beginTransaction().replace(R.id.content_main, new Sms_Fragment()).commitAllowingStateLoss();
                        } catch (Exception ex) {
                        }
                    }else if (id == R.id.nav_messageCenter) {
                    try
                    {
                        title = "Notification";
                        mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commitAllowingStateLoss();
                    }catch (Exception ex)
                    {
                    }
                } else if (id == R.id.nav_Gallery) {
                        try
                        {
                            title="Gallery";
                            mfragment.beginTransaction().replace(R.id.content_main,new Gallery_Folder() ).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_chat) {
                        try
                        {
                            title="Chat";
                            fragmentManager.beginTransaction().replace(R.id.content_main,new Chating_Sliding_Tab() ).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_profile) {
                        try
                        {
                            title="Profile";
                            fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_changePassword) {
                        try
                        {
                            title="Change Password";
                            fragmentManager.beginTransaction().replace(R.id.content_main, new StudentChangePassword()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Logout) {
                        try {
                            SharedPreferences.Editor editor_delete = settings.edit();
                            editor_delete.clear().commit();
                            this.deleteDatabase("Notifications");
                            Intent intent = new Intent(MainActivity.this, Login_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (Exception ex) {
                        }
                    }
                    else if (id == R.id.nav_staffAttendence) {
                        try
                        {
                            title = "Attendance";
                            StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                            Bundle args = new Bundle();
                            args.putString("selected_layout","Staff_layout");
                            studentAttendanceActivity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                }
                /// roleId=4 Staff Login
                else if (role_id.contentEquals("4")) {
                     if (id == R.id.nav_calender)
                    {
                        try
                        {
                            title = "Calender";
                            mfragment.beginTransaction().replace(R.id.content_main, new HolidayFragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_leave)
                    {
                        try
                        {
                            title = "Leave";
                            mfragment.beginTransaction().replace(R.id.content_main, new LeaveListFragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                     else if (id == R.id.nav_Gallery)
                     {
                         try
                         {
                             title="Gallery";
                             mfragment.beginTransaction().replace(R.id.content_main,new Gallery_Folder() ).commitAllowingStateLoss();
                         }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_studentAttendence)
                     {
                         try
                         {
                             title="Attendance";
                             Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                             Bundle args = new Bundle();
                             args.putString("attendence", "staff_attendence");
                             attendence_sliding_tab.setArguments(args);
                             getSupportFragmentManager().beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();

                         }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_noticeboard)
                     {
                         try
                         {
                             title = "Noticeboard";
                             mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commitAllowingStateLoss();
                         }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_messageCenter)
                     {
                         try
                         {
                             title = "Notification";
                             mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commitAllowingStateLoss();
                         }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_chat) {
                         try
                         {
                             /*title="Chat";
                             fragmentManager.beginTransaction().replace(R.id.content_main,new Chating_Sliding_Tab() ).commitAllowingStateLoss();
                     */    }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_profile) {
                         try
                         {
                             title="Profile";
                             fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                         }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_changePassword) {
                         try
                         {
                             title="Change Password";
                             fragmentManager.beginTransaction().replace(R.id.content_main, new StudentChangePassword()).commitAllowingStateLoss();
                         }catch (Exception ex)
                         {
                         }
                     }
                     else if (id == R.id.nav_Logout) {
                         try {
                             SharedPreferences.Editor editor_delete = settings.edit();
                             editor_delete.clear().commit();
                             this.deleteDatabase("Notifications");
                             Intent intent = new Intent(MainActivity.this, Login_activity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             startActivity(intent);
                             finish();
                         } catch (Exception ex) {
                         }
                     }
                }
                /// roleId=3 Teacher Login
                else if (role_id.contentEquals("3")) {
                    if (id == R.id.nav_syllabus)
                    {
                        try
                        {
                            title = "Syllabus";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "Syllabus");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_timetable)
                    {
                        try
                        {
                            title = "TimeTable";
                            mfragment.beginTransaction().replace(R.id.content_main, new TimetableActivity_teacher()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_examination)
                    {
                        try
                        {
                            title = "Examination";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "Exam");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_calender)
                    {
                        try
                        {
                            title = "Calender";
                            mfragment.beginTransaction().replace(R.id.content_main, new HolidayFragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_leave)
                    {
                        try
                        {
                            title = "Leave";
                            mfragment.beginTransaction().replace(R.id.content_main, new LeaveListFragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Event)
                    {
                        try
                        {
                            title = "Event";
                            mfragment.beginTransaction().replace(R.id.content_main, new Event_Tab()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Gallery)
                    {
                        try
                        {
                            title="Gallery";
                            mfragment.beginTransaction().replace(R.id.content_main,new Gallery_Folder() ).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Homework)
                    {
                        try
                        {
                            title = "Home Work";
                            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                            Bundle args = new Bundle();
                            args.putString("value","HomeWork");
                            dailyDiaryListFragment.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiaryListFragment).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Diary)
                    {
                        try
                        {
                            title = "Daily Diary";
                            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                            Bundle args = new Bundle();
                            args.putString("value", "DailyDiary");
                            dailyDiaryListFragment.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiaryListFragment).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
//                    else if (id == R.id.nav_Result)
//                    {
//                        try
//                        {
//                            title = "Result";
//                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
//                            Bundle args = new Bundle();
//                            args.putString("pagename", "Standarad_Result");
//                            student_std_activity.setArguments(args);
//                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
//
//                        }catch (Exception ex)
//                        {
//
//                        }
//
//                    }
                    else if (id == R.id.nav_studentAttendence)
                    {
                        try
                        {
                            // attendence class teacherwise
                            title = "Attendance";
                            Teacher_statndard=settings.getString("TAG_STANDERDID", "");
                            Teacher_division=settings.getString("TAG_DIVISIONID", "");
                            title = "Attendance";
                            StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity();
                            Bundle args = new Bundle();
                            args.putString("std_id",Teacher_statndard);
                            args.putString("std_name", Teacher_division);
                            args.putString("std_div", Teacher_division);
                            args.putString("selected_layout", "Stdwise_name");
                            studentAttendanceActivity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commitAllowingStateLoss();
                         // attendence timetablewise
//                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
//                            Bundle args = new Bundle();
//                            args.putString("pagename", "Std");
//                            student_std_activity.setArguments(args);
//                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }

                    }
                    else if (id == R.id.nav_teacherAttendence)
                    {
                        try
                        {
                            title = "Attendance";
                            Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                            Bundle args = new Bundle();
                            args.putString("attendence", "teacher_self_attendence");
                            args.putString("designation", "Teacher");
                            attendence_sliding_tab.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_noticeboard)
                    {
                        try
                        {
                            title = "Noticeboard";
                            mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_messageCenter)
                    {
                        try
                        {
                            title = "Notification";
                            mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_library) {
                        try
                        {
                            title = "Library";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "LibraryTeacher");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_chat) {
                        try
                        {
                           /* title="Chat";
                            fragmentManager.beginTransaction().replace(R.id.content_main,new Chating_Sliding_Tab() ).commitAllowingStateLoss();*/
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_profile) {
                        try
                        {
                            title="Profile";
                            fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_changePassword) {
                        try
                        {
                            title="Change Password";
                            fragmentManager.beginTransaction().replace(R.id.content_main, new StudentChangePassword()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }
                    }
                    else if (id == R.id.nav_Logout) {
                        try {
                            SharedPreferences.Editor editor_delete = settings.edit();
                            editor_delete.clear().commit();
                            this.deleteDatabase("Notifications");
                            Intent intent = new Intent(MainActivity.this, Login_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (Exception ex) {
                        }
                    }
                }
                //Student Parent Login
                else if(role_id.contentEquals("1"))
                {
                    if (id == R.id.nav_syllabus)
                    {
                        try
                        {
                            title="Syllabus";
                            stand_id= settings.getString("TAG_STANDERDID", "");
                            StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            subjectFragment.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, subjectFragment).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }

                    } else if (id == R.id.nav_timetable)
                    {
                        try
                        {
                            title = "TimeTable";
                            stand_id = settings.getString("TAG_STANDERDID", "");
                            TimetableActivity_student timetableActivity_student = new TimetableActivity_student();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            timetableActivity_student.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, timetableActivity_student).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {
                        }

                    } else if (id == R.id.nav_examination)
                    {
                        try
                        {
                            title = "Examination";
                            stand_id = settings.getString("TAG_STANDERDID", "");
                            StudentExamFragment studentExamActivity = new StudentExamFragment();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            studentExamActivity.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, studentExamActivity).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    } else if (id == R.id.nav_calender)
                    {
                        try
                        {
                            title = "Calender";
                            mfragment.beginTransaction().replace(R.id.content_main, new HolidayFragment()).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    } else if (id == R.id.nav_leave)
                    {
                        try
                        {
                            title="Leave";
                            mfragment.beginTransaction().replace(R.id.content_main,new LeaveListFragment() ).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }
                    }
                    else if (id == R.id.nav_Event)
                    {
                        try
                        {
                            title="Event";
                            mfragment.beginTransaction().replace(R.id.content_main,new Event_Tab() ).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }else if (id == R.id.nav_Gallery)
                    {
                        try
                        {
                            title="Gallery";
                            mfragment.beginTransaction().replace(R.id.content_main,new Gallery_Folder() ).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    } else if (id == R.id.nav_payment)
                    {
                        try
                        {
//                            new AlertDialog.Builder(this)
//                                    .setMessage("Coming Soon...")
//                                    .setNegativeButton("ok", null)
//                                    .show();
                          Intent intent=new Intent(this,Fees_Activity.class);
                          startActivity(intent);
                        }catch (Exception ex)
                        {

                        }

                    } else if (id == R.id.nav_Homework)
                    {
                        try
                        {
                            title = "Home Work";
                            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                            Bundle args = new Bundle();
                            args.putString("value","HomeWork");
                            dailyDiaryListFragment.setArguments(args);
                            mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commitAllowingStateLoss();
                            // mfragment.beginTransaction().replace(R.id.content_main, new StudentHomeworkFragment()).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }
//                    else if (id == R.id.nav_Result)
//                    {
//                        try
//                        {
//                            title = "Result";
//                            mfragment.beginTransaction().replace(R.id.content_main, new StudentResultFragment()).commitAllowingStateLoss();
//
//                        }catch (Exception ex)
//                        {
//
//                        }
//
//                    }
                    else if (id == R.id.nav_studentAttendence)
                    {
                        try
                        {
                            title = "Attendance";
                            stud_id = settings.getString("TAG_STUDENTID", "");
                            Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                            Bundle args = new Bundle();
                            args.putString("Stud_name", name1);
                            args.putString("stud_id12", stud_id);
                            args.putString("attendence", "student_self_attendence");
                            attendence_sliding_tab.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }else if(id==R.id.nav_teacherAttendence)
                    {
                        try
                        {
                            title = "Attendance";
                            StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                            Bundle args = new Bundle();
                            args.putString("selected_layout","Teacher_linearlayout");
                            studentAttendanceActivity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }  else if (id == R.id.nav_noticeboard)
                    {
                        try
                        {
                            title = "Noticeboard";
                            mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }
                    else if (id == R.id.nav_messageCenter)
                    {
                        try
                        {
                            title = "Notification";
                            mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }
                    else if (id == R.id.nav_library) {
                        try
                        {
                            title = "Library";
                            mfragment.beginTransaction().replace(R.id.content_main, new StandardWise_Book()).commitAllowingStateLoss();

                        }catch (Exception ex)
                        {

                        }

                    }
                    else if (id == R.id.nav_chat) {
                        try
                        {
                            /*title="Chat";
                            fragmentManager.beginTransaction().replace(R.id.content_main,new Chating_Sliding_Tab() ).commitAllowingStateLoss();
                       */ }catch (Exception ex)
                        {

                        }

                    }
                    else if (id == R.id.nav_profile) {
                        try
                        {
                            title="Profile";
                            fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {

                        }

                    }
                    else if (id == R.id.nav_changePassword) {
                        try
                        {
                            title="Change Password";
                            fragmentManager.beginTransaction().replace(R.id.content_main, new StudentChangePassword()).commitAllowingStateLoss();
                        }catch (Exception ex)
                        {

                        }

                    }
                    else if (id == R.id.nav_Logout) {
                        try {
                            SharedPreferences.Editor editor_delete = settings.edit();
                            editor_delete.clear().commit();
                            this.deleteDatabase("Notifications");
                            Intent intent = new Intent(MainActivity.this, Login_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } catch (Exception ex) {

                        }

                    }
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle(title);
            }
        } catch (Exception ex) {
        }
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}

