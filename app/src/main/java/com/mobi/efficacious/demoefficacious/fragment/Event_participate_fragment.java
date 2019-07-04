package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.EventListAdapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.EventDetail;
import com.mobi.efficacious.demoefficacious.entity.EventDetailPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Event_participate_fragment extends Fragment {
    View myview;
    RecyclerView recyclerView;
    ArrayAdapter adapter;
    ConnectionDetector cd;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String  Year_id,Schooli_id;
    String userid, role_id, value,vchstandard_id;
    private ProgressDialog progress;
    EventListAdapter madapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_leavelist, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        recyclerView = (RecyclerView) myview.findViewById(R.id.leavelist_list);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        role_id = settings.getString("TAG_USERTYPEID", "");
        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            try
            {
                vchstandard_id = settings.getString("TAG_STANDERDID", "");
                if(vchstandard_id.equalsIgnoreCase("1"))
                {
                    vchstandard_id="Nursery";
                }else if(vchstandard_id.equalsIgnoreCase("2"))
                {
                    vchstandard_id="Play Group";
                }else if(vchstandard_id.equalsIgnoreCase("3"))
                {
                    vchstandard_id="KGI";
                }else if(vchstandard_id.equalsIgnoreCase("4"))
                {
                    vchstandard_id="KGII";
                }else if(vchstandard_id.equalsIgnoreCase("5"))
                {
                    vchstandard_id="I";
                }else if(vchstandard_id.equalsIgnoreCase("6"))
                {
                    vchstandard_id="II";
                }else if(vchstandard_id.equalsIgnoreCase("7"))
                {
                    vchstandard_id="III";
                }else if(vchstandard_id.equalsIgnoreCase("8"))
                {
                    vchstandard_id="IV";
                }else if(vchstandard_id.equalsIgnoreCase("9"))
                {
                    vchstandard_id="V";
                }else if(vchstandard_id.equalsIgnoreCase("10"))
                {
                    vchstandard_id="VI";
                }else if(vchstandard_id.equalsIgnoreCase("11"))
                {
                    vchstandard_id="VII";
                }else if(vchstandard_id.equalsIgnoreCase("12"))
                {
                    vchstandard_id="VIII";
                }else if(vchstandard_id.equalsIgnoreCase("13"))
                {
                    vchstandard_id="IX";
                }else if(vchstandard_id.equalsIgnoreCase("14"))
                {
                    vchstandard_id="X";
                }else if(vchstandard_id.equalsIgnoreCase("15"))
                {
                    vchstandard_id="XI";
                }
                else
                {

                }
                userid = settings.getString("TAG_USERID", "");
                Year_id = settings.getString("TAG_ACADEMIC_ID", "");
                value = "Event";
                progress = new ProgressDialog(getActivity());
                progress.setCancelable(false);
                progress.setCanceledOnTouchOutside(false);
                progress.setMessage("loading...");
                progress.show();
                EventAsync();
            }catch (Exception ex)
            {

            }

        }
        return myview;
    }

    public void  EventAsync (){
        try {
            String command = null;
            if(role_id.contentEquals("1")||role_id.contentEquals("2"))
            {
                command="SelectEventParticipatedByStudent";
            }else if(role_id.contentEquals("3"))
            {
                command="SelectEventParticipatedByTeacher";

            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<EventDetailPojo> call = service.getEventDetails(command,vchstandard_id,Year_id,Schooli_id,userid);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<EventDetailPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(EventDetailPojo body) {
                    try {
                        generateEventDetail((ArrayList<EventDetail>) body.getEventDetail());
                    } catch (Exception ex) {
                        progress.dismiss();
                        //Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    //Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

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

    public void generateEventDetail(ArrayList<EventDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                madapter = new EventListAdapter(taskListDataList,getActivity(),"EventParticipatedList");

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(madapter);
            } else {

            }

        } catch (Exception ex) {
            progress.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }
}