package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.demoefficacious.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.StandardAdapter;
import com.mobi.efficacious.demoefficacious.adapters.StudentAttendanceAdapter;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetailPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class StudentAttendanceDetail extends Fragment {
    View myview;
    RecyclerView recyclerView;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Standard_id;
    Context mContext;
    RecyclerView.Adapter adapter;
    String stud_id, academic_id;
    String school_id, stud_name, standard, role_id, name1;
    private CompositeDisposable mCompositeDisposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_studentattendancedetail, null);
        mContext = getContext();
        recyclerView = (RecyclerView) myview.findViewById(R.id.studentattendancedetail_list);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        try {
            if (role_id.contentEquals("6") || role_id.contentEquals("7") || role_id.contentEquals("3")) {
                school_id = Attendence_sliding_tab.intSchool_id;
            } else {
                school_id = settings.getString("TAG_SCHOOL_ID", "");
            }
        } catch (Exception ex) {

        }

        try {
            if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                stud_id = settings.getString("TAG_USERID", "");
                stud_name = settings.getString("TAG_NAME", "");
                standard = settings.getString("TAG_NAME2", "");
            } else {
                stud_id = Attendence_sliding_tab.stud_id;
                stud_name = Attendence_sliding_tab.stud_name;
                standard = StudentAttendanceActivity.stname;
            }
        } catch (Exception ex) {

        }

        myview.setFocusableInTouchMode(true);
        myview.requestFocus();
        myview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                                name1 = settings.getString("TAG_NAME", "");
                                stud_id = settings.getString("TAG_STUDENTID", "");
                                Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                                Bundle args = new Bundle();
                                args.putString("Stud_name", name1);
                                args.putString("stud_id12", stud_id);
                                args.putString("attendence", "student_self_attendence");
                                attendence_sliding_tab.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();

                            }else if(role_id.contentEquals("3"))
                            {
                                String Teacher_statndard=settings.getString("TAG_STANDERDID", "");
                                String Teacher_division=settings.getString("TAG_DIVISIONID", "");
                                StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity();
                                Bundle args = new Bundle();
                                args.putString("std_id",Teacher_statndard);
                                args.putString("std_name", Teacher_division);
                                args.putString("std_div", Teacher_division);
                                args.putString("selected_layout", "Stdwise_name");
                                studentAttendanceActivity.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commitAllowingStateLoss();
                            }
                            else  {
                                StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity();
                                Bundle args = new Bundle();
                                args.putString("std_id", StandardAdapter.Std_id_division);
                                args.putString("std_name", StandardAdapter.std_name_division);
                                args.putString("std_div", StudentAttendanceActivity.stddiv);
                                args.putString("selected_layout", "Stdwise_name");
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
        try {
            LoginAsync();
        } catch (Exception ex) {

        }


        return myview;
    }

    public void LoginAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getAttendancedetails("selectStudent", school_id, academic_id, stud_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverAttendanceList()));
        } catch (Exception ex) {

        }
    }

    public DisposableObserver<AttendanceDetailPojo> getObserverAttendanceList() {
        return new DisposableObserver<AttendanceDetailPojo>() {

            @Override
            public void onNext(@NonNull AttendanceDetailPojo attendanceDetailPojo) {
                try {
                    generateAttendanceList((ArrayList<AttendanceDetail>) attendanceDetailPojo.getAttendanceDetail());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

                try {


                } catch (Exception ex) {

                }
            }
        };
    }

    public void generateAttendanceList(ArrayList<AttendanceDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new StudentAttendanceAdapter(getActivity(),taskListDataList);
                recyclerView.setAdapter(adapter);
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

