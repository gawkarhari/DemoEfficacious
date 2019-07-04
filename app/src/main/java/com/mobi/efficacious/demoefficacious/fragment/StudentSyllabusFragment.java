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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
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

public class StudentSyllabusFragment extends Fragment {
    View myview;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Standard_id, role_id;
    Context mContext;
    ArrayAdapter adapter;
    ListView listview;
    String stand_id;
    ConnectionDetector cd;
    RecyclerView recyclerView;
    RecyclerView.Adapter madapter;
    private CompositeDisposable mCompositeDisposable;
    ArrayList<SyllabusDetail> SyllabusDetails = new ArrayList<SyllabusDetail>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_leavelist, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        mContext = getActivity();
        try {
            stand_id = getArguments().getString("std_id");
        } catch (Exception ex) {
        }
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        Standard_id = settings.getString("TAG_STANDERDID", "");
        recyclerView = (RecyclerView) myview.findViewById(R.id.leavelist_list);


        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            try {
                LoginAsync();
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
                                Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                                Bundle args = new Bundle();
                                args.putString("pagename", "Syllabus");
                                student_std_activity.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commitAllowingStateLoss();
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

            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getSyllabusDetails("FillSubject","","",stand_id)
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
                madapter = new SubjectAdapter(getActivity(),taskListDataList,"SubjectName");
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
   /* public void LoginAsync() {
        try {
            SyllabusDetail syllabusDetail;
            int std=Integer.parseInt(stand_id);
            switch (std) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 5:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 6:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 7:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 8:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 9:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 10:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 11:
                    syllabusDetail= new SyllabusDetail("Hindi",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Mathematics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 12:
                    syllabusDetail= new SyllabusDetail("English",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Hindi", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Math",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Sanskrit",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Science", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Social science",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 13:
                    syllabusDetail= new SyllabusDetail("English",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Hindi", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Math",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Sanskrit",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Science", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Social science",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 14:
                    syllabusDetail= new SyllabusDetail("Accountancy 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Accountancy 2", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Bio",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Business",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Chemistry 1", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Chemistry 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Economics",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Eng hornbill", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Geography",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Hindi aroh 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Hindi vitan 2", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("History",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Math",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Physics 1", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Physics 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Political science",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Sanskrit", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Sociology",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
                case 15:
                    syllabusDetail= new SyllabusDetail("Math 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Math 2", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Accountace 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Biology",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Business 1", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Business 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Chemistry 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Chemistry 2", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Economics 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Economics 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English 1", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("English 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Geography",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Hindi 1", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Hindi 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("History 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("History 2", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("History 3",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Home science 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Home science 2", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Physics 1",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail= new SyllabusDetail("Physics 2",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Political", std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Sankrit",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    syllabusDetail = new SyllabusDetail("Sociology",std);
                    SyllabusDetails.addAll(Collections.singleton(syllabusDetail));
                    break;
            }
            adaptermethod();
        }
        catch (Exception ex)
        {
        }
    }*/
   /* public void adaptermethod()
    {
        try
        {
            if ((SyllabusDetails != null && !SyllabusDetails.isEmpty())) {
                madapter = new SubjectAdapter(getActivity(),SyllabusDetails,"SubjectName");
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
    }*/
}