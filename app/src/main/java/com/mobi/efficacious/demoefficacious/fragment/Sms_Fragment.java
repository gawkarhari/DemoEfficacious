package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.Division_Spinner;
import com.mobi.efficacious.demoefficacious.adapters.Standard_Spinner;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetail;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetailsPojo;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by EFF-4 on 3/27/2018.
 */

public class Sms_Fragment extends Fragment {

    RelativeLayout relativeLayout_notice, relativeLayout_notice1, relativeLayout_notice2, relativeLayout2, relativeLayout3;
    Button send_button;
    TextView textView;
    Spinner spinner_usertype, spinner_std, spinner_section;
    String[] UserType;
    ArrayList<String> sms_phone_no_array = new ArrayList<String>();
    private static final String PREFRENCES_NAME = "myprefrences";
    EditText sms_box, sms_box2, phone_no, sms_box3;
    TextView char_160, sms_count, char1_160, char2_160, sms1_count, sms2_count;
    int char_remain;
    String status = "", Schooli_id;
    SharedPreferences settings;
    String std_selected_name = "", std_selected_id = "", role_id;
    List<String> phone_no_list;
    String std_id, academic_id, UserType_Selected;
    ConnectionDetector cd;
    View myview;
    CardView sms_box3Cardview, phonenoCardview;
    private ProgressDialog progress;
    ArrayList<StandardDetail> Standard_list = new ArrayList<StandardDetail>();
    ArrayList<StandardDetail> Division_list = new ArrayList<StandardDetail>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.sms_layout, null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        relativeLayout2 = (RelativeLayout) myview.findViewById(R.id.relativelayout2);
        relativeLayout3 = (RelativeLayout) myview.findViewById(R.id.relativelayout3);
        relativeLayout_notice = (RelativeLayout) myview.findViewById(R.id.notice);
        relativeLayout_notice1 = (RelativeLayout) myview.findViewById(R.id.notice2);
        relativeLayout_notice2 = (RelativeLayout) myview.findViewById(R.id.notice3);
        sms_box = (EditText) myview.findViewById(R.id.editText22);
        sms_box2 = (EditText) myview.findViewById(R.id.editText2);
        sms_box3 = (EditText) myview.findViewById(R.id.editText288);
        sms1_count = (TextView) myview.findViewById(R.id.textView34);
        sms2_count = (TextView) myview.findViewById(R.id.textView311);
        char1_160 = (TextView) myview.findViewById(R.id.textView27);
        char2_160 = (TextView) myview.findViewById(R.id.textView291);
        phone_no = (EditText) myview.findViewById(R.id.phoneno_et);
        sms_count = (TextView) myview.findViewById(R.id.textView31);
        char_160 = (TextView) myview.findViewById(R.id.textView29);
        send_button = (Button) myview.findViewById(R.id.button5);
        spinner_usertype = (Spinner) myview.findViewById(R.id.spinner3);
        spinner_section = (Spinner) myview.findViewById(R.id.spinner5);
        spinner_std = (Spinner) myview.findViewById(R.id.spinner4);
        sms_box3Cardview = (CardView) myview.findViewById(R.id.cv_add3);
        phonenoCardview = (CardView) myview.findViewById(R.id.cv_add4);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        UserType = getResources().getStringArray(R.array.spinner);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, UserType);
        spinner_usertype.setAdapter(ad);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int usertype_selected = position;
                try {
                    switch (usertype_selected) {
                        case 0:
                            status = "Select";
                           /* relativeLayout2.setVisibility(View.GONE);
                            relativeLayout3.setVisibility(View.GONE);
                            relativeLayout_notice.setVisibility(View.GONE);
                            relativeLayout_notice1.setVisibility(View.VISIBLE);
                            relativeLayout_notice2.setVisibility(View.GONE);
                            sms_box3.setVisibility(View.GONE);
                            phone_no.setVisibility(View.GONE);
                            sms_box3Cardview.setVisibility(View.GONE);
                            phonenoCardview.setVisibility(View.GONE);*/
                            callInitialstate();

                            break;
                        case 1:
                            status = "standard";
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                Standard_name();
                            }
                            relativeLayout2.setVisibility(View.VISIBLE);
                            relativeLayout3.setVisibility(View.VISIBLE);
                            relativeLayout_notice.setVisibility(View.VISIBLE);
                            relativeLayout_notice1.setVisibility(View.GONE);
                            relativeLayout_notice2.setVisibility(View.GONE);
                            sms_box3.setVisibility(View.GONE);
                            phone_no.setVisibility(View.GONE);
                            sms_box3Cardview.setVisibility(View.GONE);
                            phonenoCardview.setVisibility(View.GONE);
                            break;
                        case 2:
                            relativeLayout2.setVisibility(View.GONE);
                            relativeLayout3.setVisibility(View.GONE);
                            relativeLayout_notice.setVisibility(View.GONE);
                            relativeLayout_notice1.setVisibility(View.VISIBLE);
                            relativeLayout_notice2.setVisibility(View.GONE);
                            sms_box3.setVisibility(View.GONE);
                            phone_no.setVisibility(View.GONE);
                            sms_box3Cardview.setVisibility(View.GONE);
                            phonenoCardview.setVisibility(View.GONE);
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                                    usertype_selected("AllTeacherByPrincipal");

                                } else {
                                    usertype_selected("AllTeacher");
                                }
                            }
                            break;
                        case 3:
                            relativeLayout2.setVisibility(View.GONE);
                            relativeLayout3.setVisibility(View.GONE);
                            relativeLayout_notice.setVisibility(View.GONE);
                            relativeLayout_notice1.setVisibility(View.VISIBLE);
                            relativeLayout_notice2.setVisibility(View.GONE);
                            sms_box3.setVisibility(View.GONE);
                            phone_no.setVisibility(View.GONE);
                            sms_box3Cardview.setVisibility(View.GONE);
                            phonenoCardview.setVisibility(View.GONE);
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                                    usertype_selected("AllStaffByPrincipal");
                                } else {
                                    usertype_selected("AllStaff");
                                }
                            }
                            break;
                        case 4:
                            relativeLayout2.setVisibility(View.GONE);
                            relativeLayout3.setVisibility(View.GONE);
                            relativeLayout_notice.setVisibility(View.GONE);
                            relativeLayout_notice1.setVisibility(View.VISIBLE);
                            relativeLayout_notice2.setVisibility(View.GONE);
                            sms_box3.setVisibility(View.GONE);
                            phone_no.setVisibility(View.GONE);
                            sms_box3Cardview.setVisibility(View.GONE);
                            phonenoCardview.setVisibility(View.GONE);
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                                    usertype_selected("AllSMSAdminByPrincipal");
                                } else {
                                    usertype_selected("AllSMSAdmin");
                                }
                            }
                            break;
                        case 5:
                            status = "Multiple";
                            relativeLayout2.setVisibility(View.GONE);
                            relativeLayout3.setVisibility(View.GONE);
                            relativeLayout_notice.setVisibility(View.GONE);
                            relativeLayout_notice1.setVisibility(View.GONE);
                            relativeLayout_notice2.setVisibility(View.VISIBLE);
                            sms_box3.setVisibility(View.VISIBLE);
                            phone_no.setVisibility(View.VISIBLE);
                            sms_box3Cardview.setVisibility(View.VISIBLE);
                            phonenoCardview.setVisibility(View.VISIBLE);
                            break;

                    }
                } catch (Exception ex) {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sms_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int msgcount;
                    int limit = 160;
                    int char_count = s.length();


                    if (char_count > 160) {
                        int charachtercount;
                        msgcount = (1 + (char_count / 160));
                        charachtercount = (char_count - 160);
                        if (charachtercount <= 160) {
                            char_remain = (limit - charachtercount);
                        } else {
                            char_remain = (charachtercount - limit);
                        }
                        char_160.setText(String.valueOf(char_remain));
                        sms_count.setText(String.valueOf(msgcount));
                    } else {
                        msgcount = 1;
                        sms_count.setText(String.valueOf(msgcount));
                        char_remain = (limit - char_count);
                        char_160.setText(String.valueOf(char_remain));
                    }
                } catch (Exception ex) {

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sms_box2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int msgcount;
                    int limit = 160;
                    int char_count = s.length();


                    if (char_count > 160) {
                        int charachtercount;
                        msgcount = (1 + (char_count / 160));
                        charachtercount = (char_count - 160);
                        if (charachtercount <= 160) {
                            char_remain = (limit - charachtercount);
                        } else {
                            char_remain = (charachtercount - limit);
                        }
                        char1_160.setText(String.valueOf(char_remain));
                        sms1_count.setText(String.valueOf(msgcount));
                    } else {
                        msgcount = 1;
                        sms1_count.setText(String.valueOf(msgcount));
                        char_remain = (limit - char_count);
                        char1_160.setText(String.valueOf(char_remain));
                    }
                } catch (Exception ex) {

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sms_box3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int msgcount;
                    int limit = 160;
                    int char_count = s.length();


                    if (char_count > 160) {
                        int charachtercount;
                        msgcount = (1 + (char_count / 160));
                        charachtercount = (char_count - 160);
                        if (charachtercount <= 160) {
                            char_remain = (limit - charachtercount);
                        } else {
                            char_remain = (charachtercount - limit);
                        }
                        char2_160.setText(String.valueOf(char_remain));
                        sms2_count.setText(String.valueOf(msgcount));
                    } else {
                        msgcount = 1;
                        sms2_count.setText(String.valueOf(msgcount));
                        char_remain = (limit - char_count);
                        char2_160.setText(String.valueOf(char_remain));
                    }
                } catch (Exception ex) {

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (status.contentEquals("Select")) {
                        setSpinnerError(spinner_usertype, "Select valid Usertype ");

                    } else if (status.contentEquals("standard")) {
                        String sms = sms_box2.getText().toString();
                        if (sms.contentEquals("")) {
                            if (TextUtils.isEmpty(sms)) {
                                sms_box2.setError("Enter SMS ");
                            }
                        } else {
                            sms = sms.replace("&", "and");
                            sms = sms.replace("%", "percent");
                            sms = sms.replace("/", "-");
                            sms = sms.replaceAll("[^-a-zA-Z0-9-,-.- ]", "");
                            sms = sms.replace("Upto", "Up to");
                            sms = sms.replace("upto", "up to");
                            sms = sms.replace("Election", "Elec-tion");
                            sms = sms.replace("election", "elec-tion");
                            sms = sms.replace("elections", "elec-tions");
                            sms = sms.replace("Elections", "Elec-tions");
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                try {
                                    sendsms sendsms = new sendsms(sms);
                                    sendsms.execute();
                                } catch (Exception ex) {

                                }


                            }
                            status = "";
                        }
                    } else if (status.contentEquals("Multiple")) {

                        String phoneno = phone_no.getText().toString();
                        phone_no_list = Arrays.asList(phoneno.split(","));
                        String sms = sms_box3.getText().toString();
                        if (phoneno.contentEquals("") || sms.contentEquals("")) {
                            if (TextUtils.isEmpty(sms)) {
                                sms_box3.setError("Enter SMS ");
                            }
                            if (TextUtils.isEmpty(phoneno)) {
                                phone_no.setError("Enter Phone No. ");
                            }
                        } else {
                            sms = sms.replace("&", "and");
                            sms = sms.replace("%", "percent");
                            sms = sms.replace("/", "-");
                            sms = sms.replaceAll("[^-a-zA-Z0-9-,-.- ]", "");

                            sms = sms.replace("Upto", "Up to");
                            sms = sms.replace("upto", "up to");
                            sms = sms.replace("Election", "Elec-tion");
                            sms = sms.replace("election", "elec-tion");
                            sms = sms.replace("elections", "elec-tions");
                            sms = sms.replace("Elections", "Elec-tions");
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                try {
                                    multiple sendsms = new multiple(sms);
                                    sendsms.execute();
//                                    callingInitialdstate();
//                                    Toast.makeText(getContext(),"Please select Usertype Again!!",Toast.LENGTH_LONG).show();
                                } catch (Exception ex) {

                                }


                            }
                            status = "";
                        }
                    } else {

                        String sms = sms_box.getText().toString();
                        if (sms.contentEquals("")) {
                            if (TextUtils.isEmpty(sms)) {
                                sms_box.setError("Enter SMS ");
                            }
                        } else {
                            sms = sms.replace("&", "and");
                            sms = sms.replace("%", "percent");
                            sms = sms.replace("/", "-");
                            sms = sms.replaceAll("[^-a-zA-Z0-9-,-.- ]", "");
                            sms = sms.replace("Upto", "Up to");
                            sms = sms.replace("upto", "up to");
                            sms = sms.replace("Election", "Elec-tion");
                            sms = sms.replace("election", "elec-tion");
                            sms = sms.replace("elections", "elec-tions");
                            sms = sms.replace("Elections", "Elec-tions");
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                try {
                                    sendsms sendsms = new sendsms(sms);
                                    sendsms.execute();
                                } catch (Exception ex) {

                                }


                            }

//                    sms_box.setText();
                        }
                    }
                } catch (Exception ex) {

                }


            }
        });
        spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    std_selected_name = String.valueOf(Standard_list.get(position).getVchStandardName());
                    spinner_section.setVisibility(View.VISIBLE);
                    if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                        Schooli_id = String.valueOf(Standard_list.get(position).getIntschoolId());
                    }
                    std_id = String.valueOf(Standard_list.get(position).getIntStandardId());

                    if (std_selected_name.contentEquals("All Student")) {
                        spinner_section.setVisibility(View.GONE);
                        if (!cd.isConnectingToInternet()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        } else {
                            AllStandard_sms();
                        }
                    } else {
                        if (!cd.isConnectingToInternet()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        } else {
                            Division_name(std_id);
                        }
                    }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String std_div_id = String.valueOf(Division_list.get(position).getIntDivisionId());
                    Standard_wise_sms(std_id, std_div_id);
                } catch (Exception ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return myview;
    }

    private void callInitialstate() {
        relativeLayout2.setVisibility(View.GONE);
        relativeLayout3.setVisibility(View.GONE);
        relativeLayout_notice.setVisibility(View.GONE);
        relativeLayout_notice1.setVisibility(View.VISIBLE);
        relativeLayout_notice2.setVisibility(View.GONE);
        sms_box3.setVisibility(View.GONE);
        phone_no.setVisibility(View.GONE);
        sms_box3Cardview.setVisibility(View.GONE);
        phonenoCardview.setVisibility(View.GONE);
    }

    private void callingInitialdstate() {
        relativeLayout2.setVisibility(View.VISIBLE);
        relativeLayout3.setVisibility(View.GONE);
        relativeLayout_notice.setVisibility(View.GONE);
        relativeLayout_notice1.setVisibility(View.VISIBLE);
        relativeLayout_notice2.setVisibility(View.GONE);
        sms_box3.setVisibility(View.GONE);
        phone_no.setVisibility(View.GONE);
        sms_box3Cardview.setVisibility(View.GONE);
        phonenoCardview.setVisibility(View.GONE);
    }

    public void Standard_name() {
        try {
            try {
                Standard_list.clear();
                StandardDetail standardDetail;
                standardDetail = new StandardDetail(0, "All Student", 0);
                Standard_list.addAll(Collections.singleton(standardDetail));
            } catch (Exception ex) {

            }
            String command;
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                command = "selectStandardByPrincipal";
            } else {
                command = "select";
            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<StandardDetailsPojo> call = service.getStandardDetails(command, Schooli_id, academic_id, "", "", "", "");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Standard_list.addAll(body.getStandardDetails());
                        Standard_Spinner adapter = new Standard_Spinner(getActivity(), Standard_list);
                        spinner_std.setAdapter(adapter);
                    } catch (Exception ex) {

                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception ex) {

        }
    }

    public void AllStandard_sms() {
        try {
            String command;
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                command = "AllMessageToStudentByPrincipal";
            } else {
                command = "AllMessageToStudent";
            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<DashboardDetailsPojo> call = service.getDashboardDetails(command, academic_id, Schooli_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DashboardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(DashboardDetailsPojo body) {
                    try {
                        generateSMSNo((ArrayList<DashboardDetail>) body.getDashboardDetails());
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

    public void generateSMSNo(ArrayList<DashboardDetail> taskListDataList) {
        try {
            sms_phone_no_array.clear();
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                for (int i = 0; i < taskListDataList.size(); i++) {
                    sms_phone_no_array.add(taskListDataList.get(i).getIntBusAlert1());
                }
                Toast.makeText(getActivity(), "Total Phone No Count:" + taskListDataList.size(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Total Phone No Count: 0", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            progress.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Standard_wise_sms(String std_id, String std_div_id) {
        try {
            String command = "AllMessageToDivision";
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<DashboardDetailsPojo> call = service.getDashboardDetails(command, academic_id, std_div_id, std_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DashboardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(DashboardDetailsPojo body) {
                    try {
                        generateSMSNo((ArrayList<DashboardDetail>) body.getDashboardDetails());
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

    public void generateSMS(ArrayList<DashboardDetail> taskListDataList) {
        try {
            sms_phone_no_array.clear();
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                for (int i = 0; i < taskListDataList.size(); i++) {
                    sms_phone_no_array.add(taskListDataList.get(i).getIntmobileno());
                }
                Toast.makeText(getActivity(), "Total Phone No Count:" + taskListDataList.size(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Total Phone No Count: 0", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            progress.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }

    public void usertype_selected(String allSMSAdmin) {
        try {
            String command = allSMSAdmin;
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<DashboardDetailsPojo> call = service.getDashboardDetails(command, academic_id, Schooli_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DashboardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(DashboardDetailsPojo body) {
                    try {
                        generateSMS((ArrayList<DashboardDetail>) body.getDashboardDetails());
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

    private class multiple extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String message = "";
        int SMS_SendCount = 0;
        int Total_Count = 0;

        public multiple(String sms) {
            message = sms;
        }

        protected Void doInBackground(Void... voids) {
            try {
                Total_Count = phone_no_list.size();
                HttpURLConnection uc = null;
                String requestUrl = "";
                for (int i = 0; i < phone_no_list.size(); i++) {
                    requestUrl = ("http://www.smsjust.com/sms/user/urlsms.php?username=GurukulGlobal&pass=C4$8eY-k&senderid=GRUKUL&to=" + URLEncoder.encode(phone_no_list.get(i), "UTF-8") + "&message=" + URLEncoder.encode(message, "UTF-8") + "&format=json&custom=1,2&flash=0&unicode=1");
                    //requestUrl = ("http://alerts.justnsms.com/api/web2sms.php?workingkey=A2cabcee227fa491ee050155a13485498&sender=CMSBKP&to=" + URLEncoder.encode(phone_no_list.get(i), "UTF-8") + "&message=" + URLEncoder.encode(message, "UTF-8") + "&format=json&custom=1,2&flash=0&unicode=1");
                    URL url = new URL(requestUrl);
                    uc = (HttpURLConnection) url.openConnection();
                    String responseMessage = uc.getResponseMessage();

                    if (responseMessage.contentEquals("OK")) {
                        SMS_SendCount = SMS_SendCount + 1;
                    }
                    uc.disconnect();
                }


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Sending SMS...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            try {
                phone_no.setText("");
                sms_box3.setText("");
                Toast.makeText(getActivity(), "SMS Send Successfully", Toast.LENGTH_SHORT).show();
//                callInitialstate();
//                send_button.setVisibility(View.GONE);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(SMS_SendCount + " SMS Sent Successfully Out of " + Total_Count + aVoid.toString());
                alert.setPositiveButton("OK", null);
                alert.show();
//                callInitialstate();

            } catch (Exception ex) {

            }


        }
    }

    private class sendsms extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String message = "";
        int SMS_SendCount = 0;
        int Total_Count = 0;

        public sendsms(String sms) {
            message = sms;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Total_Count = sms_phone_no_array.size();
                HttpURLConnection uc = null;
                String requestUrl = "";
                for (int i = 0; i < sms_phone_no_array.size(); i++) {
                    requestUrl = ("http://www.smsjust.com/sms/user/urlsms.php?username=GurukulGlobal&pass=C4$8eY-k&senderid=GRUKUL&to=" + URLEncoder.encode(sms_phone_no_array.get(i), "UTF-8") + "&message=" + URLEncoder.encode(message, "UTF-8") + "&format=json&custom=1,2&flash=0&unicode=1");
                    URL url = new URL(requestUrl);
                    uc = (HttpURLConnection) url.openConnection();
                    System.out.println(uc.getResponseMessage());
                    String response = uc.getResponseMessage();
                    if (response.contentEquals("OK")) {
                        SMS_SendCount = SMS_SendCount + 1;
                    }
                    uc.disconnect();
                }


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Sending SMS...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            try {
                sms_phone_no_array.clear();
                Toast.makeText(getActivity(), "SMS Send Successfully", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(SMS_SendCount + " SMS Sent Successfully Out of " + Total_Count);
                alert.setPositiveButton("OK", null);
                alert.show();
                callInitialstate();
            } catch (Exception ex) {

            }
        }
    }

    public void Division_name(String std_id) {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<StandardDetailsPojo> call = service.getStandardDetails("GetDivision", Schooli_id, "", std_id, "", "", "");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Division_list.clear();
                        Division_list.addAll(body.getStandardDetails());
                        Division_Spinner adapter = new Division_Spinner(getActivity(), Division_list);
                        spinner_section.setAdapter(adapter);
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

    private void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }
}
