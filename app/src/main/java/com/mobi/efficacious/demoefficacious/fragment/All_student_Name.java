package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.AllStudentAdapter;
import com.mobi.efficacious.demoefficacious.adapters.StandardAdapter;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.StudentStandardwiseDetail;
import com.mobi.efficacious.demoefficacious.entity.StudentStandardwiseDetailPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by EFF-4 on 3/17/2018.
 */

public class All_student_Name extends Fragment {
    View myview;
    AllStudentAdapter studadapter;
    RecyclerView recyclerView;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    Context mContext;
    SearchView searchView;
    Databasehelper mydb;
    Cursor cursor;
    String Standard_id, stdname, stddivision, academic_id;
    int a, b, c, d;
    String name, role_id;
    String date, school_id, pagename = "";
    private CompositeDisposable mCompositeDisposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_allstudent, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        myview.setFocusableInTouchMode(true);
        myview.requestFocus();
        myview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
//                            ||role_id.contentEquals("3")
                            if(role_id.contentEquals("5"))
                            {
                                Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                                Bundle args = new Bundle();
                                args.putString("pagename", "Std");
                                student_std_activity.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commit();

                            }else {

                            }

                        } catch (Exception ex) {

                        }

                        return true;
                    }
                }
                return false;
            }
        });

        searchView = (SearchView) myview.findViewById(R.id.search_view_student);
        recyclerView = (RecyclerView) myview.findViewById(R.id.allstudent_list);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mydb = new Databasehelper(getActivity(), "Teacher_record", null, 1);
        mydb.query("Create table if not exists Student_data(ID INTEGER PRIMARY KEY AUTOINCREMENT,Student_id INTEGER ,Division_id INTEGER ,Standard_id INTEGER ,Name varchar,student_attendence varchar,Currentdate Date,status boolean)");
        mContext = getContext();
        try {
            Standard_id = StudentAttendanceActivity.stdno;
            stdname = StudentAttendanceActivity.stname;
            stddivision = StudentAttendanceActivity.stddiv;
        } catch (Exception ex) {

        }


        try {
            if (role_id.contentEquals("6") || role_id.contentEquals("7") ) {
                school_id = StandardAdapter.intSchool_id;
            } else {
                school_id = settings.getString("TAG_SCHOOL_ID", "");
            }
            pagename = "Attendence";
        } catch (Exception ex) {

        }
        try {
            LoginAsync();
        } catch (Exception ex) {

        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studadapter.getFilter().filter(newText);
                return true;
            }
        });
        return myview;
    }
    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Student Name Here");
    }
    public void  LoginAsync (){
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getStudentStandardWiseDetails("select",school_id,academic_id,Standard_id,stddivision)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserver()));
        } catch (Exception ex) {
        }
    }
    public DisposableObserver<StudentStandardwiseDetailPojo> getObserver(){
        return new DisposableObserver<StudentStandardwiseDetailPojo>() {

            @Override
            public void onNext(@NonNull StudentStandardwiseDetailPojo studentStandardwiseDetailPojo) {
                try {
                    generateStudentList((ArrayList<StudentStandardwiseDetail>) studentStandardwiseDetailPojo.getStudentStandardwiseDetail());
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        };
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }


    public void generateStudentList(ArrayList<StudentStandardwiseDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                studadapter = new AllStudentAdapter(taskListDataList, getActivity(),pagename);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(studadapter);
                setupSearchView();
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
}
