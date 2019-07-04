package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.demoefficacious.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.StudentAttendanceAdapter;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetailPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class TeacherAttendanceDetail extends Fragment {
    View myview;
    Toolbar toolbar;
    ListView listView;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Standard_id, academic_id, school_id;
    Context mContext;
    String teach_id, role_id;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private CompositeDisposable mCompositeDisposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_teacher_attendance_detail_, null);
        mContext = getActivity();
        teach_id = Attendence_sliding_tab.teacher_id;
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Standard_id = settings.getString("TAG_STANDERDID", "");
        recyclerView = (RecyclerView) myview.findViewById(R.id.teacherattendancedetail_list);
        role_id = settings.getString("TAG_USERTYPEID", "");
        try {
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                school_id = Attendence_sliding_tab.intSchool_id;
            } else {
                school_id = settings.getString("TAG_SCHOOL_ID", "");
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
        try {
            LoginAsync();
        } catch (Exception ex) {

        }

        return myview;
    }

    public void LoginAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getAttendancedetails("select", school_id, academic_id, teach_id)
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

