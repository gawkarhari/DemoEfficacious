package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.demoefficacious.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.dialogbox.Image_zoom_dialog_teacher;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetailPojo;
import com.mobi.efficacious.demoefficacious.entity.ProfileDetail;
import com.mobi.efficacious.demoefficacious.entity.ProfileDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;
import com.roomorama.caldroid.CaldroidFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by EFF-4 on 3/21/2018.
 */

public class Teacher_Calender_attendence extends Fragment {
    View myview;
    String school_id, teacherdesignation;
    TextView nametxtvw, designationtxtvw;
    Databasehelper mydb;
    Date holidayDay;
    Cursor merge_cursor;
    public static String image;
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    String a;
    CircleImageView profileimg;
    String teacher_id, teacher_name, designation;
    String status;
    ProgressBar progressBar;
    String role_id, academic_id;
    String user_id;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private ProgressDialog progress;
    public static String intMobileNo;
    private CompositeDisposable mCompositeDisposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.teacher_calender, null);
        nametxtvw = (TextView) myview.findViewById(R.id.textView);
        designationtxtvw = (TextView) myview.findViewById(R.id.textView2);
        profileimg = (CircleImageView) myview.findViewById(R.id.imageView13);
        try {
            teacher_name = Attendence_sliding_tab.teacher_name;
            designation = Attendence_sliding_tab.teacher_designation;
            nametxtvw.setText(teacher_name);
            designationtxtvw.setText(designation);
        } catch (Exception ex) {

        }
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        try {
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                school_id = Attendence_sliding_tab.intSchool_id;
            } else {
                school_id = settings.getString("TAG_SCHOOL_ID", "");
            }
            teacher_id = Attendence_sliding_tab.teacher_id;
        } catch (Exception ex) {

        }

        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);

        mCaldroidFragment.setArguments(args);
        try {
            progress.show();
            AdminAsync ();
            ProfileAsync();
        } catch (Exception ex) {

        }
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Image_zoom_dialog_teacher notifDialog = new Image_zoom_dialog_teacher(getContext());
                    notifDialog.show();
                } catch (Exception ex) {

                }
            }
        });
        myview.setFocusableInTouchMode(true);
        myview.requestFocus();
        myview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            if (role_id.contentEquals("3")) {
                                Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                                Bundle args = new Bundle();
                                args.putString("attendence", "teacher_self_attendence");
                                args.putString("designation", "Teacher");
                                attendence_sliding_tab.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();

                            } else {
                                StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity();
                                Bundle args = new Bundle();
                                args.putString("selected_layout", "Teacher_linearlayout");
                                studentAttendanceActivity.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commitAllowingStateLoss();

                            }
                        } catch (Exception ex) {

                        }

                        return true;
                    }
                }
                return false;
            }
        });
        return myview;
    }

    public void ProfileAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getProfiledetails("GetTeacherProfile", school_id, academic_id,teacher_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverProfileDetail()));
        } catch (Exception ex) {
            progress.dismiss();
        }
    }

    public DisposableObserver<ProfileDetailsPojo> getObserverProfileDetail() {
        return new DisposableObserver<ProfileDetailsPojo>() {

            @Override
            public void onNext(@NonNull ProfileDetailsPojo profileDetail) {
                try {
                    generateProfileDetail((ArrayList<ProfileDetail>) profileDetail.getProfileDetails());

                } catch (Exception ex) {
                    progress.dismiss();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onComplete() {
                progress.dismiss();
                try
                {
                    String url = RetrofitInstance.Image_URL + image + "";
                    Glide.with(getActivity())
                            .load(url) // image url
                            .error(R.mipmap.profile)
                            .into(profileimg);
                }catch (Exception ex)
                {

                }

            }
        };
    }
    public void generateProfileDetail(ArrayList<ProfileDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                image=taskListDataList.get(0).getVchImageURL();
                intMobileNo=taskListDataList.get(0).getIntMobileNo();
                if (intMobileNo.equals("") || intMobileNo.equals("null") || intMobileNo.equals("anyType{}")) {
                    intMobileNo = "-";
                }
            } else {
                try
                {
                    String url = RetrofitInstance.Image_URL + image + "";
                    Glide.with(getActivity())
                            .load(url)
                            .fitCenter()// image url
                            .error(R.mipmap.profile)
                            .into(profileimg);
                }catch (Exception ex)
                {

                }
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AdminAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getAttendancedetails("select", school_id, academic_id, teacher_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverAttendanceList()));
        } catch (Exception ex) {
            progress.dismiss();
        }
    }

    public DisposableObserver<AttendanceDetailPojo> getObserverAttendanceList() {
        return new DisposableObserver<AttendanceDetailPojo>() {

            @Override
            public void onNext(@NonNull AttendanceDetailPojo attendanceDetailPojo) {
                try {
                    generateAttendanceList((ArrayList<AttendanceDetail>) attendanceDetailPojo.getAttendanceDetail());
                } catch (Exception ex) {
                    progress.dismiss();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onComplete() {
                progress.dismiss();
                try {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cal_container, mCaldroidFragment).commitAllowingStateLoss();

                } catch (Exception ex) {

                }
            }
        };
    }

    public void generateAttendanceList(ArrayList<AttendanceDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                int day = 0;
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                for (int i = 0; i < taskListDataList.size(); i++) {
                    String inputString2 = taskListDataList.get(i).getDtdate();
                    String inputString1 = myFormat.format(date);
                    try {
                        Date date1 = null;
                        try {
                            date1 = myFormat.parse(inputString1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = myFormat.parse(inputString2);
                        //Calculating number of days from two dates
                        long diff = date2.getTime() - date1.getTime();
                        long datee = diff / (1000 * 60 * 60 * 24);
                        //Converting long type to int type
                        day = (int) datee;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, day);
                    holidayDay = cal.getTime();
                    ColorDrawable bgToday;
                    if (taskListDataList.get(i).getStatus().contentEquals("Present")) {
                        bgToday = new ColorDrawable(Color.GREEN);
                    } else {
                        bgToday = new ColorDrawable(Color.RED);
                    }

                    mCaldroidFragment.setBackgroundDrawableForDate(bgToday, holidayDay);
                }
            } else {
                Toast toast = Toast.makeText(getActivity(),
                        "No Data Available",
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toastView.setBackgroundResource(R.drawable.no_data_available);
                toast.show();
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

}
