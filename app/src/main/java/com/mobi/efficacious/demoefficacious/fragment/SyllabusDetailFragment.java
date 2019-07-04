package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.SubjectAdapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.SyllabusDetail;
import com.mobi.efficacious.demoefficacious.entity.SyllabusDetailPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class SyllabusDetailFragment extends Fragment {

    View myview;
    RecyclerView recyclerView;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Standard_id, academic_id;
    Context mContext;
    String subject_id = "", stand_id;
    String standard_id1, Schooli_id, role_id;
    ConnectionDetector cd;
    RecyclerView.Adapter madapter;
    private CompositeDisposable mCompositeDisposable;
    ArrayList<SyllabusDetail> SyllabusDetails = new ArrayList<SyllabusDetail>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_syllabusdetails, null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        mContext = getActivity();
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        Standard_id = settings.getString("TAG_STANDERDID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        try {
            standard_id1=getArguments().getString("stand_id");
            subject_id = getArguments().getString("sub_id");

        } catch (Exception ex) {

        }
        recyclerView = (RecyclerView) myview.findViewById(R.id.syllabusdetails__list);
        if (!cd.isConnectingToInternet()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No InternetConnection");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {
            try {

                LoginAsync ();
            } catch (Exception ex) {

            }

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
                                stand_id = settings.getString("TAG_STANDERDID", "");
                                StudentSyllabusFragment studentSyllabusFragment = new StudentSyllabusFragment();
                                Bundle args = new Bundle();
                                args.putString("std_id", stand_id);
                                studentSyllabusFragment.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentSyllabusFragment).commitAllowingStateLoss();
                            } else {
                                stand_id = standard_id1;
                                StudentSyllabusFragment studentSyllabusFragment = new StudentSyllabusFragment();
                                Bundle args = new Bundle();
                                args.putString("std_id", stand_id);
                                studentSyllabusFragment.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentSyllabusFragment).commitAllowingStateLoss();
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
    public void  LoginAsync (){
        try {
            String command;
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                command="FillGridByPrincipal";
            } else {
                command= "FillGrid";
            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getSyllabusDetails(command,Schooli_id,subject_id,standard_id1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserver()));
        } catch (Exception ex) {
        }
    }
    public DisposableObserver<SyllabusDetailPojo> getObserver(){
        return new DisposableObserver<SyllabusDetailPojo>() {

            @Override
            public void onNext(@NonNull SyllabusDetailPojo syllabusDetailPojo) {
                try {
                    generateSyllabusList((ArrayList<SyllabusDetail>) syllabusDetailPojo.getSyllabusDetail());
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
    public void generateSyllabusList(ArrayList<SyllabusDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                madapter = new SubjectAdapter(getActivity(),taskListDataList,"SyllabusDetail");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);
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
/*public void LoginAsync() {
    try {
        SyllabusDetail syllabusDetail;
        if (standard_id1.contentEquals("") || subject_id.contentEquals("")) {

        }
        else {
            if (standard_id1.contentEquals("4")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn1ps.pdf", "ahhn1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn101.pdf", "ahhn101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn102.pdf", "ahhn102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn103.pdf", "ahhn103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn104.pdf", "ahhn104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn105.pdf", "ahhn105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn106.pdf", "ahhn106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn107.pdf", "ahhn107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn108.pdf", "ahhn108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn109.pdf", "ahhn109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn110.pdf", "ahhn110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn111.pdf", "ahhn111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn112.pdf", "ahhn112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn113.pdf", "ahhn113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn114.pdf", "ahhn114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn115.pdf", "ahhn115", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn116.pdf", "ahhn116", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn117.pdf", "ahhn117", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn118.pdf", "ahhn118", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn119.pdf", "ahhn119", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn120.pdf", "ahhn120", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn121.pdf", "ahhn121", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn122.pdf", "ahhn122", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20hindi/ahhn123.pdf", "ahhn123", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen1ps.pdf", "aeen1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen101.pdf", "aeen101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen102.pdf", "aeen102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen103.pdf", "aeen103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen104.pdf", "aeen104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen105.pdf", "aeen105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen106.pdf", "aeen106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen107.pdf", "aeen107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen108.pdf", "aeen108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen109.pdf", "aeen109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1st%20class%20eng/aeen110.pdf", "aeen110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh1ps.pdf", "aemh1ps", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh1tn.pdf", "aemh1tn", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh101.pdf", "aemh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh102.pdf", "aemh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh103.pdf", "aemh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh104.pdf", "aemh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh105.pdf", "aemh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh106.pdf", "aemh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh107.pdf", "aemh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh108.pdf", "aemh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh109.pdf", "aemh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh110.pdf", "aemh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh111.pdf", "aemh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh112.pdf", "aemh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%201st/1class%20math/aemh113.pdf", "aemh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("5")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn1ps.pdf", "bhhn1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn101.pdf", "bhhn101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn102.pdf", "bhhn102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn103.pdf", "bhhn103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn104.pdf", "bhhn104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn105.pdf", "bhhn105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn106.pdf", "bhhn106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn107.pdf", "bhhn107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn108.pdf", "bhhn108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn109.pdf", "bhhn109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn110.pdf", "bhhn110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn111.pdf", "bhhn111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn112.pdf", "bhhn112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn113.pdf", "bhhn113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn114.pdf", "bhhn114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20hindi/bhhn115.pdf", "bhhn115", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd1ps.pdf", "berd1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd101.pdf", "berd101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd102.pdf", "berd102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd103.pdf", "berd103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd104.pdf", "berd104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd105.pdf", "berd105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd106.pdf", "berd106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd107.pdf", "berd107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd108.pdf", "berd108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd109.pdf", "berd109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd110.pdf", "berd110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd111.pdf", "berd111", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd112.pdf", "berd112", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd113.pdf", "berd113", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd114.pdf", "berd114", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20eng/berd115.pdf", "berd115", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh1ps.pdf", "bemh1ps", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh101.pdf", "bemh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh102.pdf", "bemh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh103.pdf", "bemh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh104.pdf", "bemh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh105.pdf", "bemh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh106.pdf", "bemh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh107.pdf", "bemh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh108.pdf", "bemh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh109.pdf", "bemh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh110.pdf", "bemh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh111.pdf", "bemh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh112.pdf", "bemh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh113.pdf", "bemh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh114.pdf", "bemh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%202nd/2%20math/bemh115.pdf", "bemh115", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("6")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/1cc2chhn.pdf", "1cc2chhn", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/1ccchhn.pdf", "1ccchhn", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/1pschhn.pdf", "1pschhn", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn101.pdf", "chhn101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn102.pdf", "chhn102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn103.pdf", "chhn103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn104.pdf", "chhn104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn105.pdf", "chhn105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn106.pdf", "chhn106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn107.pdf", "chhn107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn108.pdf", "chhn108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn109.pdf", "chhn109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn110.pdf", "chhn110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn111.pdf", "chhn111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn112.pdf", "chhn112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn113.pdf", "chhn113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20hindi/chhn114.pdf", "chhn114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen1ps.pdf", "ceen1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen101.pdf", "ceen101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen102.pdf", "ceen102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen103.pdf", "ceen103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen104.pdf", "ceen104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen105.pdf", "ceen105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen106.pdf", "ceen106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen107.pdf", "ceen107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen108.pdf", "ceen108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen109.pdf", "ceen109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20eng/ceen110.pdf", "ceen110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh101.pdf", "cemh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh102.pdf", "cemh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh103.pdf", "cemh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh104.pdf", "chhn104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh105.pdf", "cemh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh106.pdf", "cemh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh107.pdf", "cemh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh108.pdf", "cemh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh109.pdf", "cemh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh110.pdf", "cemh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh111.pdf", "cemh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh112.pdf", "cemh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh113.pdf", "cemh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cemh114.pdf", "cemh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/cs1cemh.pdf", "cs1cemh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%203rd/3%20math/ps1cemh.pdf", "ps1cemh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("7")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn1ps.pdf", "dhhn1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn101.pdf", "dhhn101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn102.pdf", "dhhn102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn103.pdf", "dhhn103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn104.pdf", "dhhn104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn105.pdf", "dhhn105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn106.pdf", "dhhn106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn107.pdf", "dhhn107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn108.pdf", "dhhn108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn109.pdf", "dhhn109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn110.pdf", "dhhn110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn111.pdf", "dhhn111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn112.pdf", "dhhn112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn113.pdf", "dhhn113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20hindi/dhhn114.pdf", "dhhn114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen1ps.pdf", "deen1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen101.pdf", "deen101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen102.pdf", "deen102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen103.pdf", "deen103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen104.pdf", "deen104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen105.pdf", "deen105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen106.pdf", "deen106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen107.pdf", "deen107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen108.pdf", "deen108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen109.pdf", "deen109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20eng/deen110.pdf", "deen110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh1ps.pdf", "demh1ps", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh101.pdf", "demh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh102.pdf", "demh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh103.pdf", "demh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh104.pdf", "demh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh105.pdf", "demh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh106.pdf", "demh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh107.pdf", "demh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh108.pdf", "demh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh109.pdf", "demh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh110.pdf", "demh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh111.pdf", "demh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh112.pdf", "demh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh113.pdf", "demh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%204th/4%20math/demh114.pdf", "demh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("8")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/cs1ehhn.pdf", "cs1ehhn", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn101.pdf", "ehhn101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn102.pdf", "ehhn102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn103.pdf", "ehhn103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn104.pdf", "ehhn104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn105.pdf", "ehhn105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn106.pdf", "ehhn106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn107.pdf", "ehhn107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn108.pdf", "ehhn108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn109.pdf", "ehhn109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn110.pdf", "ehhn110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn111.pdf", "ehhn111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn112.pdf", "ehhn112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn113.pdf", "ehhn113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn114.pdf", "ehhn114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn115.pdf", "ehhn115", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn116.pdf", "ehhn116", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn117.pdf", "ehhn117", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ehhn118.pdf", "ehhn118", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20himdi/ps1ehhn.pdf", "ps1ehhn", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/cs1eeen.pdf", "cs1eeen", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen101.pdf", "eeen101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen102.pdf", "eeen102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen102b.pdf", "eeen102b", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen103.pdf", "eeen103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen104.pdf", "eeen103b", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen104.pdf", "eeen104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen104b.pdf", "eeen104b", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen105.pdf", "eeen105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen106.pdf", "eeen106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen106b.pdf", "eeen106b", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen107.pdf", "eeen107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen108.pdf", "eeen108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen109.pdf", "eeen109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20eng/eeen110.pdf", "eeen110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/cs1eemh.pdf", "cs1eemh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/cs2eemh.pdf", "cs2eemh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh101.pdf", "eemh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh102.pdf", "eemh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh103.pdf", "eemh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh104.pdf", "eemh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh105.pdf", "eemh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh106.pdf", "eemh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh107.pdf", "eemh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh108.pdf", "eemh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh109.pdf", "eemh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh110.pdf", "eemh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh111.pdf", "eemh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh112.pdf", "eemh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh113.pdf", "eemh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh114.pdf", "eemh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/eemh114b.pdf", "eemh114b", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%205th/5%20math/ps1eemh.pdf", "ps1eemh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("9")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr1ps.pdf", "fhbr1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr101.pdf", "fhbr101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr102.pdf", "fhbr102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr103.pdf", "fhbr103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr104.pdf", "fhbr104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr105.pdf", "fhbr105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr106.pdf", "fhbr106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr107.pdf", "fhbr107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr108.pdf", "fhbr108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr109.pdf", "fhbr109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr110.pdf", "fhbr110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr111.pdf", "fhbr111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr112.pdf", "fhbr112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20hindi/fhbr113.pdf", "fhbr113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw1ps.pdf", "fepw1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw101.pdf", "fepw101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw102.pdf", "fepw102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw103.pdf", "fepw103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw104.pdf", "fepw104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw105.pdf", "fepw105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw106.pdf", "fepw106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw107.pdf", "fepw107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw108.pdf", "fepw108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw109.pdf", "fepw109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20eng/fepw110.pdf", "fepw110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else  if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/cs1femh.pdf", "cs1femh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh101.pdf", "femh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh102.pdf", "femh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh103.pdf", "femh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh104.pdf", "femh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh105.pdf", "femh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh106.pdf", "femh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh107.pdf", "femh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh108.pdf", "femh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh109.pdf", "femh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh110.pdf", "femh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh111.pdf", "femh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh112.pdf", "femh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh113.pdf", "femh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh114.pdf", "femh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh115.pdf", "femh115", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/femh116.pdf", "femh116", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%206th/6%20math/ps1femh.pdf", "ps1femh", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("10")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs1ps.pdf", "ghvs1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs101.pdf", "ghvs101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs102.pdf", "ghvs102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs103.pdf", "ghvs103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs104.pdf", "ghvs104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs105.pdf", "ghvs105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs106.pdf", "ghvs106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs107.pdf", "ghvs107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs108.pdf", "ghvs108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs109.pdf", "ghvs109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs110.pdf", "ghvs110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs111.pdf", "ghvs111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs112.pdf", "ghvs112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs113.pdf", "ghvs113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs114.pdf", "ghvs114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs115.pdf", "ghvs115", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs116.pdf", "ghvs116", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs117.pdf", "ghvs117", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs118.pdf", "ghvs118", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs119.pdf", "ghvs119", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs120.pdf", "ghvs120", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20hindi/ghvs121.pdf", "ghvs121", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc1ps.pdf", "gehc1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc101.pdf", "gehc101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc102.pdf", "gehc102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc103.pdf", "gehc103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc104.pdf", "gehc104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc105.pdf", "gehc105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc106.pdf", "gehc106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc107.pdf", "gehc107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc108.pdf", "gehc108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc109.pdf", "gehc109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20eng/gehc110.pdf", "gehc110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh1an.pdf", "gemh1an", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh1ps.pdf", "gemh1ps", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh101.pdf", "gemh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh102.pdf", "gemh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh103.pdf", "gemh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh104.pdf", "gemh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh105.pdf", "gemh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh106.pdf", "gemh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh107.pdf", "gemh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh108.pdf", "gemh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh109.pdf", "gemh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh110.pdf", "gemh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh111.pdf", "gemh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh112.pdf", "gemh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh113.pdf", "gemh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh114.pdf", "gemh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%207th/7%20math/gemh115.pdf", "gemh115", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("11")) {
                if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs1cs.pdf", "hhvs1cs", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs1ps.pdf", "hhvs1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs101.pdf", "hhvs101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs102.pdf", "hhvs102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs103.pdf", "hhvs103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs104.pdf", "hhvs104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs105.pdf", "hhvs105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs106.pdf", "hhvs106", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs107.pdf", "hhvs107", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs108.pdf", "hhvs108", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs109.pdf", "hhvs109", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs110.pdf", "hhvs110", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs111.pdf", "hhvs111", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs112.pdf", "hhvs112", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs113.pdf", "hhvs113", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs114.pdf", "hhvs114", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs115.pdf", "hhvs115", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs116.pdf", "hhvs116", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs117.pdf", "hhvs117", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs118.pdf", "hhvs118", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20hindi/hhvs119.pdf", "hhvs119", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }else   if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd101.pdf", "hehd101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd102.pdf", "hehd102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd103.pdf", "hehd103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd104.pdf", "hehd104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd105.pdf", "hehd105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd106.pdf", "hehd106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd107.pdf", "hehd107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd108.pdf", "hehd108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd109.pdf", "hehd109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehd110.pdf", "hehd110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehdcs1.pdf", "hehdcs1", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehdcs2.pdf", "hehdcs2", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20eng/hehdps1.pdf", "hehdps1", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }  else if (subject_id.contentEquals("Mathematics")) {
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh1cs.pdf", "hemh1cs", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh1ps.pdf", "hemh1ps", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh2cs.pdf", "hemh2cs", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh101.pdf", "hemh101", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh102.pdf", "hemh102", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh103.pdf", "hemh103", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh104.pdf", "hemh104", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh105.pdf", "hemh105", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh106.pdf", "hemh106", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh107.pdf", "hemh107", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh108.pdf", "hemh108", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh109.pdf", "hemh109", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh110.pdf", "hemh110", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh111.pdf", "hemh111", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh112.pdf", "hemh112", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh113.pdf", "hemh113", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh114.pdf", "hemh114", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh115.pdf", "hemh115", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%208th/8%20math/hemh116.pdf", "hemh116", "Mathematics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("12")) {
                if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe1ps.pdf", "iebe1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe101.pdf", "iebe101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe102.pdf", "iebe102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe103.pdf", "iebe103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe104.pdf", "iebe104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe105.pdf", "iebe105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe106.pdf", "iebe106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe107.pdf", "iebe107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe108.pdf", "iebe108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe109.pdf", "iebe109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe110.pdf", "iebe110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20eng/iebe111.pdf", "iebe111", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));


                } else if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20hindi/ihkr1ps.pdf", "ihkr1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20hindi/ihkr101.pdf", "ihkr101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20hindi/ihkr102.pdf", "ihkr102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20hindi/ihkr103.pdf", "ihkr103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20hindi/ihkr104.pdf", "ihkr104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20hindi/ihkr105.pdf", "ihkr105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Math")) {
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh1a1.pdf", "iemh1a1", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh1a2.pdf", "iemh1a2", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh1an.pdf", "iemh1an", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh1ps.pdf", "iemh1ps", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh101.pdf", "iemh101", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh102.pdf", "iemh102", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh103.pdf", "iemh103", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh104.pdf", "iemh104", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh105.pdf", "iemh105", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh106.pdf", "iemh106", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh107.pdf", "iemh107", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh108.pdf", "iemh108", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh109.pdf", "iemh109", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh110.pdf", "iemh110", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh111.pdf", "iemh111", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh112.pdf", "iemh112", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh113.pdf", "iemh113", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh114.pdf", "iemh114", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20math/iemh115.pdf", "iemh115", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Sanskrit")) {
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab1p1.pdf", "isab1p1", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab1p2.pdf", "isab1p2", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab1ps.pdf", "isab1ps", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab101.pdf", "isab101", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab102.pdf", "isab102", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab103.pdf", "isab103", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab104.pdf", "isab104", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab105.pdf", "isab105", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab106.pdf", "isab106", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab107.pdf", "isab107", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab108.pdf", "isab108", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab109.pdf", "isab109", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab110.pdf", "isab110", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab111.pdf", "isab111", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20sanskrit/isab112.pdf", "isab112", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Science")) {
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc1ps.pdf", "iesc1ps", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc101.pdf", "iesc101", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc102.pdf", "iesc102", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc103.pdf", "iesc103", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc104.pdf", "iesc104", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc105.pdf", "iesc105", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc106.pdf", "iesc106", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc107.pdf", "iesc107", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc108.pdf", "iesc108", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc109.pdf", "iesc109", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc110.pdf", "iesc110", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc111.pdf", "iesc111", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc112.pdf", "iesc112", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc113.pdf", "iesc113", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc114.pdf", "iesc114", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc115.pdf", "iesc115", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Science/iesc116.pdf", "iesc116", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Social science")) {
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss3ps.pdf", "ihss3ps", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss301.pdf", "ihss301", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss302.pdf", "ihss302", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss303.pdf", "ihss303", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss304.pdf", "ihss304", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss305.pdf", "ihss305", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss306.pdf", "ihss306", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss307.pdf", "ihss307", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%209th/9%20Social%20science/ihss308.pdf", "ihss308", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("13")) {
                if (subject_id.contentEquals("English")) {
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff1ps.pdf", "jeff1ps", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff101.pdf", "jeff101", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff102.pdf", "jeff102", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff103.pdf", "jeff103", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff104.pdf", "jeff104", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff105.pdf", "jeff105", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff106.pdf", "jeff106", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff107.pdf", "jeff107", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff108.pdf", "jeff108", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff109.pdf", "jeff109", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff110.pdf", "jeff110", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20eng/jeff111.pdf", "jeff111", "English");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Hindi")) {
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr1lp.pdf", "jhkr1lp", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr1ps.pdf", "jhkr1ps", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr101.pdf", "jhkr101", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr102.pdf", "jhkr102", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr103.pdf", "jhkr103", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr104.pdf", "jhkr104", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20hindi/jhkr105.pdf", "jhkr105", "Hindi");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Math")) {
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh1ps.pdf", "jemh1ps", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh101.pdf", "jemh101", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh102.pdf", "jemh102", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh103.pdf", "jemh103", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh104.pdf", "jemh104", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh105.pdf", "jemh105", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh106.pdf", "jemh106", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh107.pdf", "jemh107", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh108.pdf", "jemh108", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh109.pdf", "jemh109", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh110.pdf", "jemh110", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh111.pdf", "jemh111", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh112.pdf", "jemh112", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh113.pdf", "jemh113", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh114.pdf", "jemh114", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20math/jemh115.pdf", "jemh115", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Sanskrit")) {
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk1ps.pdf", "jhsk1ps", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk101.pdf", "jhsk101", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk102.pdf", "jhsk102", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk103.pdf", "jhsk103", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk104.pdf", "jhsk104", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk105.pdf", "jhsk105", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk106.pdf", "jhsk106", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk107.pdf", "jhsk107", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk108.pdf", "jhsk108", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk109.pdf", "jhsk109", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk110.pdf", "jhsk110", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk111.pdf", "jhsk111", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20sanskrit/jhsk112.pdf", "jhsk112", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Science")) {
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc1ps.pdf", "jesc1ps", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc101.pdf", "jesc101", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc102.pdf", "jesc102", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc103.pdf", "jesc103", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc104.pdf", "jesc104", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc105.pdf", "jesc105", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc106.pdf", "jesc106", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc107.pdf", "jesc107", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc108.pdf", "jesc108", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc109.pdf", "jesc109", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc110.pdf", "jesc110", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc111.pdf", "jesc111", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc112.pdf", "jesc112", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc113.pdf", "jesc113", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc114.pdf", "jesc114", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc115.pdf", "jesc115", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20science/jesc116.pdf", "jesc116", "Science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Social science")) {
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss3ps.pdf", "jhss3ps", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss301.pdf", "jhss301", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss302.pdf", "jhss302", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss303.pdf", "jhss303", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss304.pdf", "jhss304", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss305.pdf", "jhss305", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss306.pdf", "jhss306", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss307.pdf", "jhss307", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2010th/10%20social%20science/jhss308.pdf", "jhss308", "Social science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("14")) {
                if (subject_id.contentEquals("Accountancy 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac1ps.pdf", "khac1ps", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac101.pdf", "khac101", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac102.pdf", "khac102", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac103.pdf", "khac103", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac104.pdf", "khac104", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac105.pdf", "khac105", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac106.pdf", "khac106", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac107.pdf", "khac107", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%201/khac108.pdf", "khac108", "Accountancy 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Accountancy 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac2ps.pdf", "keac2ps", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac201.pdf", "keac201", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac202.pdf", "keac202", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac203.pdf", "keac203", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac204.pdf", "keac204", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac205.pdf", "keac205", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac206.pdf", "keac206", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20accountancy%202/keac207.pdf", "keac207", "Accountancy 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Bio")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo1ps.pdf", "kebo1ps", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo20.pdf", "kebo20", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo21.pdf", "kebo21", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo22.pdf", "kebo22", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo101.pdf", "kebo101", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo102.pdf", "kebo102", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo103.pdf", "kebo103", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo104.pdf", "kebo104", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo105.pdf", "kebo105", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo106.pdf", "kebo106", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo107.pdf", "kebo107", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo108.pdf", "kebo108", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo109.pdf", "kebo109", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo110.pdf", "kebo110", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo111.pdf", "kebo111", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo112.pdf", "kebo112", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo113.pdf", "kebo113", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo114.pdf", "kebo114", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo115.pdf", "kebo115", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo116.pdf", "kebo116", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo117.pdf", "kebo117", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo118.pdf", "kebo118", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20bio/kebo119.pdf", "kebo119", "Bio");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Business")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs1cs.pdf", "kebs1cs", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs1ps.pdf", "kebs1ps", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs101.pdf", "kebs101", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs102.pdf", "kebs102", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs103.pdf", "kebs103", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs104.pdf", "kebs104", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs105.pdf", "kebs105", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs106.pdf", "kebs106", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs107.pdf", "kebs107", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs108.pdf", "kebs108", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs109.pdf", "kebs109", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs110.pdf", "kebs110", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20business/kebs111.pdf", "kebs111", "Business");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Chemistry 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech1a1.pdf", "kech1a1", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech1an.pdf", "kech1an", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech1ps.pdf", "kech1ps", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech101.pdf", "kech101", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech102.pdf", "kech102", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech103.pdf", "kech103", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech104.pdf", "kech104", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech105.pdf", "kech105", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech106.pdf", "kech106", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%201/kech107.pdf", "kech107", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Chemistry 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech2an.pdf", "kech2an", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech2ps.pdf", "kech2ps", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech201.pdf", "kech201", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech202.pdf", "kech202", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech203.pdf", "kech203", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech204.pdf", "kech204", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech205.pdf", "kech205", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech206.pdf", "kech206", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20chemistry%202/kech207.pdf", "kech207", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Economics")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec1ps.pdf", "keec1ps", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec101.pdf", "keec101", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec102.pdf", "keec102", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec103.pdf", "keec103", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec104.pdf", "keec104", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec105.pdf", "keec105", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec106.pdf", "keec106", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec107.pdf", "keec107", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec108.pdf", "keec108", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec109.pdf", "keec109", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20economics/keec110.pdf", "keec110", "Economics");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Eng hornbill")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb1ps.pdf", "kehb1ps", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb101.pdf", "kehb101", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb102.pdf", "kehb102", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb103.pdf", "kehb103", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb104.pdf", "kehb104", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb105.pdf", "kehb105", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb106.pdf", "kehb106", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb107.pdf", "kehb107", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb108.pdf", "kehb108", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb111.pdf", "kehb111", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb112.pdf", "kehb112", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb113.pdf", "kehb113", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb114.pdf", "kehb114", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb115.pdf", "kehb115", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20eng%20hornbill/kehb116.pdf", "kehb116", "Eng hornbill");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Geography")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy2ps.pdf", "kegy2ps", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy201.pdf", "kegy201", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy202.pdf", "kegy202", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy203.pdf", "kegy203", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy204.pdf", "kegy204", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy205.pdf", "kegy205", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy206.pdf", "kegy206", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy207.pdf", "kegy207", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy208.pdf", "kegy208", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy209.pdf", "kegy209", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy210.pdf", "kegy210", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy211.pdf", "kegy211", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy212.pdf", "kegy212", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy213.pdf", "kegy213", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy214.pdf", "kegy214", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy215.pdf", "kegy215", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20geography/kegy216.pdf", "kegy216", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Hindi aroh 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar1ps.pdf", "khar1ps", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar101.pdf", "khar101", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar102.pdf", "khar102", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar103.pdf", "khar103", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar104.pdf", "khar104", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar105.pdf", "khar105", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar106.pdf", "khar106", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar107.pdf", "khar107", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar108.pdf", "khar108", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar109.pdf", "khar109", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar110.pdf", "khar110", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar111.pdf", "khar111", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar112.pdf", "khar112", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar113.pdf", "khar113", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar114.pdf", "khar114", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar115.pdf", "khar115", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar116.pdf", "khar116", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar117.pdf", "khar117", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar118.pdf", "khar118", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar119.pdf", "khar119", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20aroh%201/khar120.pdf", "khar120", "Hindi aroh 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Hindi vitan 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20vitan%202/khvt1ps.pdf", "khvt1ps", "Hindi vitan 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20vitan%202/khvt101.pdf", "khvt101", "Hindi vitan 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20vitan%202/khvt102.pdf", "khvt102", "Hindi vitan 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20vitan%202/khvt103.pdf", "khvt103", "Hindi vitan 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20hindi%20vitan%202/khvt104.pdf", "khvt104", "Hindi vitan 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("History")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20history/kehs1ps.pdf", "kehs1ps", "History");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20history/kehs101.pdf", "kehs101", "History");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20history/kehs102.pdf", "kehs102", "History");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20history/kehs103.pdf", "kehs103", "History");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20history/kehs104.pdf", "kehs104", "History");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Math")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh1a1.pdf", "kemh1a1", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh1a2.pdf", "kemh1a2", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh1an.pdf", "kemh1an", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh1ps.pdf", "kemh1ps", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh101.pdf", "kemh101", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh102.pdf", "kemh102", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh103.pdf", "kemh103", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh104.pdf", "kemh104", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh105.pdf", "kemh105", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh106.pdf", "kemh106", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh107.pdf", "kemh107", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh108.pdf", "kemh108", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh109.pdf", "kemh109", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh110.pdf", "kemh110", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh111.pdf", "kemh111", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh112.pdf", "kemh112", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh113.pdf", "kemh113", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh114.pdf", "kemh114", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh115.pdf", "kemh115", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20math/kemh116.pdf", "kemh116", "Math");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Physics 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph1a1.pdf", "keph1a1", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph1an.pdf", "keph1an", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph1ps.pdf", "keph1ps", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph101.pdf", "keph101", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph102.pdf", "keph102", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph103.pdf", "keph103", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph104.pdf", "keph104", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph105.pdf", "keph105", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph106.pdf", "keph106", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph107.pdf", "keph107", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%201/keph108.pdf", "keph108", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Physics 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph2an.pdf", "keph2an", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph2ps.pdf", "keph2ps", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph201.pdf", "keph201", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph202.pdf", "keph202", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph203.pdf", "keph203", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph204.pdf", "keph204", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph205.pdf", "keph205", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph206.pdf", "keph206", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20physics%202/keph207.pdf", "keph207", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Political science")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps1cs.pdf", "keps1cs", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps1ps.pdf", "keps1ps", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps2cs.pdf", "keps2cs", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps101.pdf", "keps101", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps102.pdf", "keps102", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps103.pdf", "keps103", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps104.pdf", "keps104", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps105.pdf", "keps105", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps106.pdf", "keps106", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps107.pdf", "keps107", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps108.pdf", "keps108", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps109.pdf", "keps109", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20political%20science/keps110.pdf", "keps110", "Political science");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Sanskrit")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk1mg.pdf", "khsk1mg", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk1pr.pdf", "khsk1pr", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk1ps.pdf", "khsk1ps", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk101.pdf", "khsk101", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk102.pdf", "khsk102", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk103.pdf", "khsk103", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk104.pdf", "khsk104", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk105.pdf", "khsk105", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk106.pdf", "khsk106", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk107.pdf", "khsk107", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk108.pdf", "khsk108", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk109.pdf", "khsk109", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk110.pdf", "khsk110", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk111.pdf", "khsk111", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20sanskrit/khsk112.pdf", "khsk112", "Sanskrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Sociology")) {
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20Sociology/kesy1ps.pdf", "kesy1ps", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20Sociology/kesy101.pdf", "kesy101", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20Sociology/kesy102.pdf", "kesy102", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20Sociology/kesy103.pdf", "kesy103", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20Sociology/kesy104.pdf", "kesy104", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2011th/11%20Sociology/kesy105.pdf", "kesy105", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }
            }
            if (standard_id1.contentEquals("15")) {
                if (subject_id.contentEquals("Math 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh1ps.pdf", "lemh1ps", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh101.pdf", "lemh101", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh102.pdf", "lemh102", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh103.pdf", "lemh103", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh104.pdf", "lemh104", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh105.pdf", "lemh105", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh106.pdf", "lemh106", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh107.pdf", "lemh107", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12%20math%201/lemh108.pdf", "lemh108", "Math 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Math 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh2an.pdf", "lemh2an", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh2ps.pdf", "lemh2ps", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh201.pdf", "lemh201", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh202.pdf", "lemh202", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh203.pdf", "lemh203", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh204.pdf", "lemh204", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh205.pdf", "lemh205", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh206.pdf", "lemh206", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20math%202/lemh207.pdf", "lemh207", "Math 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Accountace 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac2ps.pdf", "leac2ps", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac201.pdf", "leac201", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac202.pdf", "leac202", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac203.pdf", "leac203", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac204.pdf", "leac204", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac205.pdf", "leac205", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20accountance%202/leac206.pdf", "leac206", "Accountace 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Biology")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo1ps.pdf", "lebo1ps", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo101.pdf", "lebo101", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo102.pdf", "lebo102", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo103.pdf", "lebo103", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo104.pdf", "lebo104", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo105.pdf", "lebo105", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo106.pdf", "lebo106", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo107.pdf", "lebo107", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo108.pdf", "lebo108", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo109.pdf", "lebo109", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo110.pdf", "lebo110", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo111.pdf", "lebo111", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo112.pdf", "lebo112", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo113.pdf", "lebo113", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo114.pdf", "lebo114", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo115.pdf", "lebo115", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20biology/lebo116.pdf", "lebo116", "Biology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Business 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs1ps.pdf", "lebs1ps", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs101.pdf", "lebs101", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs102.pdf", "lebs102", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs103.pdf", "lebs103", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs104.pdf", "lebs104", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs105.pdf", "lebs105", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs106.pdf", "lebs106", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs107.pdf", "lebs107", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20bussieness%201/lebs108.pdf", "lebs108", "Business 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Business 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20business%202/lebs2ps.pdf", "lebs2ps", "Business 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20business%202/lebs201.pdf", "lebs201", "Business 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20business%202/lebs202.pdf", "lebs202", "Business 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20business%202/lebs203.pdf", "lebs203", "Business 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20business%202/lebs204.pdf", "lebs204", "Business 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20business%202/lebs205.pdf", "lebs205", "Business 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Chemistry 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech2ps.pdf", "lech2ps", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech201.pdf", "lech201", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech202.pdf", "lech202", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech203.pdf", "lech203", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech204.pdf", "lech204", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech205.pdf", "lech205", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech206.pdf", "lech206", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%201/lech207.pdf", "lech207", "Chemistry 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Chemistry 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech2ps.pdf", "lech2ps", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech201.pdf", "lech201", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech202.pdf", "lech202", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech203.pdf", "lech203", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech204.pdf", "lech204", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech205.pdf", "lech205", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech206.pdf", "lech206", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20chemistry%202/lech207.pdf", "lech207", "Chemistry 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Economics 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec2ps.pdf", "leec2ps", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec201.pdf", "leec201", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec202.pdf", "leec202", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec203.pdf", "leec203", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec204.pdf", "leec204", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec205.pdf", "leec205", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%201/leec206.pdf", "leec206", "Economics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Economics 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec1ps.pdf", "leec1ps", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec101.pdf", "leec101", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec102.pdf", "leec102", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec103.pdf", "leec103", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec104.pdf", "leec104", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec105.pdf", "leec105", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/leec106.pdf", "leec106", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20economics%202/Prelim.pdf", "Prelim", "Economics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("English 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl1ps.pdf", "lefl1ps", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl101.pdf", "lefl101", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl102.pdf", "lefl102", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl103.pdf", "lefl103", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl104.pdf", "lefl104", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl105.pdf", "lefl105", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl106.pdf", "lefl106", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl107.pdf", "lefl107", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl108.pdf", "lefl108", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl111.pdf", "lefl111", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl112.pdf", "lefl112", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl113.pdf", "lefl113", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl114.pdf", "lefl114", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl115.pdf", "lefl115", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%201/lefl116.pdf", "lefl116", "English 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("English 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt1ps.pdf", "levt1ps", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt101.pdf", "levt101", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt102.pdf", "levt102", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt103.pdf", "levt103", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt104.pdf", "levt104", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt105.pdf", "levt105", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt106.pdf", "levt106", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt107.pdf", "levt107", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20english%202/levt108.pdf", "levt108", "English 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Geography")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy1a1.pdf", "lhgy1a1", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy1ps.pdf", "lhgy1ps", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy101.pdf", "lhgy101", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy102.pdf", "lhgy102", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy103.pdf", "lhgy103", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy104.pdf", "lhgy104", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy105.pdf", "lhgy105", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy106.pdf", "lhgy106", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy107.pdf", "lhgy107", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy108.pdf", "lhgy108", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy109.pdf", "lhgy109", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20geography/lhgy110.pdf", "lhgy110", "Geography");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Hindi 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar1ps.pdf", "lhar1ps", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar101.pdf", "lhar101", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar102.pdf", "lhar102", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar103.pdf", "lhar103", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar104.pdf", "lhar104", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar105.pdf", "lhar105", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar106.pdf", "lhar106", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar107.pdf", "lhar107", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar108.pdf", "lhar108", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar109.pdf", "lhar109", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar110.pdf", "lhar110", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar111.pdf", "lhar111", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar112.pdf", "lhar112", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar113.pdf", "lhar113", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar114.pdf", "lhar114", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar115.pdf", "lhar115", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar116.pdf", "lhar116", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar117.pdf", "lhar117", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%201/lhar118.pdf", "lhar118", "Hindi 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Hindi 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%202/lhvt1ps.pdf", "lhvt1ps", "Hindi 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%202/lhvt101.pdf", "lhvt101", "Hindi 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%202/lhvt102.pdf", "lhvt102", "Hindi 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%202/lhvt103.pdf", "lhvt103", "Hindi 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20hindi%202/lhvt104.pdf", "lhvt104", "Hindi 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("History 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%201/lhhs1ps.pdf", "lhhs1ps", "History 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%201/lhhs101.pdf", "lhhs101", "History 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%201/lhhs102.pdf", "lhhs102", "History 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%201/lhhs103.pdf", "lhhs103", "History 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%201/lhhs104.pdf", "lhhs104", "History 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("History 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%202/lhhs2ps.pdf", "lhhs2ps", "History 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%202/lhhs201.pdf", "lhhs201", "History 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%202/lhhs202.pdf", "lhhs202", "History 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%202/lhhs203.pdf", "lhhs203", "History 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%202/lhhs204.pdf", "lhhs204", "History 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%202/lhhs205.pdf", "lhhs205", "History 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("History 3")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs3ps.pdf", "lhhs3ps", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs301.pdf", "lhhs301", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs302.pdf", "lhhs302", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs303.pdf", "lhhs303", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs304.pdf", "lhhs304", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs305.pdf", "lhhs305", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20history%203/lhhs306.pdf", "lhhs306", "History 3");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Home science 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe1ps.pdf", "lehe1ps", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe101.pdf", "lehe101", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe102.pdf", "lehe102", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe103.pdf", "lehe103", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe104.pdf", "lehe104", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe105.pdf", "lehe105", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe106.pdf", "lehe106", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe107.pdf", "lehe107", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe108.pdf", "lehe108", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe109.pdf", "lehe109", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%201/lehe110.pdf", "lehe110", "Home science 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Home science 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe2a1.pdf", "lehe2a1", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe2ps.pdf", "lehe2ps", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe201.pdf", "lehe201", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe202.pdf", "lehe202", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe203.pdf", "lehe203", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe204.pdf", "lehe204", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe205.pdf", "lehe205", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe206.pdf", "lehe206", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe207.pdf", "lehe207", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe208.pdf", "lehe208", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe209.pdf", "lehe209", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe210.pdf", "lehe210", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe211.pdf", "lehe211", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe212.pdf", "lehe212", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe213.pdf", "lehe213", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe214.pdf", "lehe214", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20home%20science%202/lehe215.pdf", "lehe215", "Home science 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Physics 1")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph1an.pdf", "leph1an", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph1ps.pdf", "leph1ps", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph101.pdf", "leph101", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph102.pdf", "leph102", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph103.pdf", "leph103", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph104.pdf", "leph104", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph105.pdf", "leph105", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph106.pdf", "leph106", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph107.pdf", "leph107", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%201/leph108.pdf", "leph108", "Physics 1");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Physics 2")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph2ps.pdf", "leph2ps", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph201.pdf", "leph201", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph202.pdf", "leph202", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph203.pdf", "leph203", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph204.pdf", "leph204", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph205.pdf", "leph205", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph206.pdf", "leph206", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20physics%202/leph207.pdf", "leph207", "Physics 2");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Political")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps1ps.pdf", "lhps1ps", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps101.pdf", "lhps101", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps102.pdf", "lhps102", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps103.pdf", "lhps103", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps104.pdf", "lhps104", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps105.pdf", "lhps105", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps106.pdf", "lhps106", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps107.pdf", "lhps107", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps108.pdf", "lhps108", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20political/lhps109.pdf", "lhps109", "Political");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Sankrit")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk1ps.pdf", "lhsk1ps", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk101.pdf", "lhsk101", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk102.pdf", "lhsk102", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk103.pdf", "lhsk103", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk104.pdf", "lhsk104", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk105.pdf", "lhsk105", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk106.pdf", "lhsk106", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk107.pdf", "lhsk107", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk108.pdf", "lhsk108", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk109.pdf", "lhsk109", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk110.pdf", "lhsk110", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk111.pdf", "lhsk111", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sankrit/lhsk112.pdf", "lhsk112", "Sankrit");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                } else if (subject_id.contentEquals("Sociology")) {
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy1ps.pdf", "lhsy1ps", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy101.pdf", "lhsy101", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy102.pdf", "lhsy102", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy103.pdf", "lhsy103", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy104.pdf", "lhsy104", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy105.pdf", "lhsy105", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy106.pdf", "lhsy106", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Class%2012th/12th%20sociology/lhsy107.pdf", "lhsy107", "Sociology");
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                }

            }
        }
        adaptermethod();
    } catch (Exception ex) {

    }
}*/

    public void adaptermethod()
    {
        try
        {
            if ((SyllabusDetails != null && !SyllabusDetails.isEmpty())) {
                madapter = new SubjectAdapter(getActivity(),SyllabusDetails,"SyllabusDetail");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);
            } else {
                madapter.notifyDataSetChanged();
                recyclerView.setAdapter(null);
                Toast toast = Toast.makeText(getActivity(),
                        "No Data Available",
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toastView.setBackgroundResource(R.drawable.no_data_available);
                toast.show();
            }
        }catch (Exception ex)
        {
            madapter.notifyDataSetChanged();
            recyclerView.setAdapter(null);
        }
    }
}
