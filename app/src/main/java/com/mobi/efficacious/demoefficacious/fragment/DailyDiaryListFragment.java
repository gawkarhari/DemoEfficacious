package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.DailyDiary_Tab;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.DailyDiaryAdapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class DailyDiaryListFragment extends Fragment {
    View myview;
    RecyclerView DairyListview;
    DailyDiaryAdapter adapter;
    ConnectionDetector cd;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String userid, role_id, value, intStandard_id="", intDivision_id="", usertype, Academic_id, Schooli_id;
    SearchView searchView;
    private ProgressDialog progress;
    FloatingActionButton addButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_leavelist, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        DairyListview = (RecyclerView) myview.findViewById(R.id.leavelist_list);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        role_id = settings.getString("TAG_USERTYPEID", "");
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        searchView = (SearchView) myview.findViewById(R.id.search_view_teachername);
//        searchView = (SearchView) myview.findViewById(R.id.search_view_teacher_name);
        searchView.setVisibility(View.VISIBLE);
        addButton=(FloatingActionButton) myview.findViewById(R.id.addButton);
        addButton.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        if (role_id.contentEquals("3")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                try {
                    addButton.setVisibility(View.VISIBLE);
                    userid = settings.getString("TAG_USERID", "");
                    value = getArguments().getString("value");
                    usertype = "teacher";
                    DailyDiaryAsync ();
                } catch (Exception ex) {

                }

            }
        }
        if (role_id.contentEquals("5")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                try {
                    searchView.setVisibility(View.VISIBLE);
                    userid = settings.getString("TAG_USERID", "");
                    try {
                        value = getArguments().getString("value");
                    } catch (Exception ex) {
                        value = DailyDiary_Tab.value;
                    }
                    usertype = "Admin";
                    DailyDiaryAsync();
                } catch (Exception ex) {

                }

            }
        }
        if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                try {
                    searchView.setVisibility(View.VISIBLE);
                    userid = settings.getString("TAG_USERID", "");
                    try {
                        value = getArguments().getString("value");
                    } catch (Exception ex) {
                        value = DailyDiary_Tab.value;
                    }
                    usertype = "Admin";
                    DailyDiaryAsync ();
                } catch (Exception ex) {

                }

            }
        }
        if (role_id.contentEquals("2") || role_id.contentEquals("1")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                try {
                    usertype = "student";
                    userid = settings.getString("TAG_USERID", "");
                    intStandard_id = settings.getString("TAG_STANDERDID", "");
                    intDivision_id = settings.getString("TAG_DIVISIONID", "");
                    value = getArguments().getString("value");
                    DailyDiaryAsync ();
                } catch (Exception ex) {

                }

            }
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText!=null) {
                    adapter.getFilter().filter(newText);
                }else {
                    return true;
                }
                return true;
            }
        });
   /*     searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText!=null) {
                    Log.d("Handling NulPointer",""+newText);
                 }
                 else {
                    adapter.getFilter().filter(newText);
                    return true;
                }
                return false;
            }
        });*/
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyDiaryFragment dailyDiaryFragment = new DailyDiaryFragment();
                Bundle args = new Bundle();
                args.putString("value",value);
                dailyDiaryFragment.setArguments(args);
                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiaryFragment).commitAllowingStateLoss();
            }
        });
        return myview;
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search by Teacher Name ");
    }
    public void  DailyDiaryAsync (){
        try {
            String command;
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
             if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                 command="DailyDairyStudent";
             }
            else if (role_id.contentEquals("3")) {
                command="DailyDairyTeacher";
            } else if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                command="DailyDairyPrincipal";
            } else {
                command="DailyDairyAdmin";
            }
            Observable<StandardDetailsPojo> call = service.getStandardDetails(command,Schooli_id,Academic_id,intStandard_id,intDivision_id,userid,value);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        generateDiaryHomeworkList((ArrayList<StandardDetail>) body.getStandardDetails());
                    } catch (Exception ex) {
                        progress.dismiss();
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    progress.dismiss();
                }
            });
        } catch (Exception ex) {
            progress.dismiss();
        }
    }

    public void generateDiaryHomeworkList(ArrayList<StandardDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                adapter = new DailyDiaryAdapter(getActivity(),taskListDataList, value,usertype);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                DairyListview.setLayoutManager(layoutManager);
                DairyListview.setAdapter(adapter);
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
            progress.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }
}
