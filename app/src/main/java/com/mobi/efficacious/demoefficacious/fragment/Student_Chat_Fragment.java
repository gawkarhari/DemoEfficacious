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
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.ChatAllUser_Adapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.ChatDetail;
import com.mobi.efficacious.demoefficacious.entity.ChatDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Student_Chat_Fragment extends Fragment {
    View myview;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    RecyclerView recyclerView;
    ChatAllUser_Adapter adapter;
    String Academic_id, Schooli_id, academic_id, role_id, Standard_id="", Division_id="", userid="";
    SearchView searchView;
    public static RelativeLayout fulllayout;
    ConnectionDetector cd;
    private ProgressDialog progress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.chat_studentlist, null);
        cd = new ConnectionDetector(getActivity());
        recyclerView = (RecyclerView) myview.findViewById(R.id.chat_listview);
        searchView = (SearchView) myview.findViewById(R.id.search_view_member);
        fulllayout = (RelativeLayout) myview.findViewById(R.id.relativeLayout2);
        fulllayout.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        userid = settings.getString("TAG_USERID", "");
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        try {
            if (role_id.contentEquals("2") || role_id.contentEquals("1")) {
                Standard_id = settings.getString("TAG_STANDERDID", "");
                Division_id = settings.getString("TAG_DIVISIONID", "");
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        ChatStudentAsync ();
                    } catch (Exception ex) {

                    }


                }
            } else if (role_id.contentEquals("3") || role_id.contentEquals("5") || role_id.contentEquals("6") || role_id.contentEquals("7")) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        ChatStudenStandardtAsync ();
                    } catch (Exception ex) {

                    }


                }
            } else {

            }
        } catch (Exception ex) {

        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return myview;
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setSuggestionsAdapter(null);
        searchView.setQueryHint("Search Student Name Here");
    }


    public void ChatStudentAsync ()
    {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<ChatDetailsPojo> call = service.getChatUserDetails("selectStudent",Schooli_id,"",Standard_id,Division_id,Academic_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ChatDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(ChatDetailsPojo body) {
                    try {
                        generateUserList((ArrayList<ChatDetail>) body.getChatDetails());
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
        }
    }

    public void generateUserList(ArrayList<ChatDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                adapter = new ChatAllUser_Adapter(taskListDataList,getActivity(),role_id);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                setupSearchView();
            } else {

            }

        } catch (Exception ex) {
            progress.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }

     public void ChatStudenStandardtAsync ()
     {
         try {
             String command;
             if (role_id.contentEquals("5")) {
                 command="selectAdminStandrad";
             } else if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                 command="selectPrincipalStandrad";
             } else {
                 command="selectTeacherStandrad";
             }
             DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
             Observable<ChatDetailsPojo> call = service.getChatUserDetails(command,Schooli_id,userid,Standard_id,Division_id,Academic_id);
             call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ChatDetailsPojo>() {
                 @Override
                 public void onSubscribe(Disposable disposable) {
                     progress.show();
                 }

                 @Override
                 public void onNext(ChatDetailsPojo body) {
                     try {
                         generateUserList((ArrayList<ChatDetail>) body.getChatDetails());
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


}
