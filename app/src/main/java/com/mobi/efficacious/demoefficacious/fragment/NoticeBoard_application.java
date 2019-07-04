package com.mobi.efficacious.demoefficacious.fragment;

import android.app.DatePickerDialog;
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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.Division_spinner_adapter;
import com.mobi.efficacious.demoefficacious.adapters.Standard_Spinner;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.NoticeboardDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class NoticeBoard_application extends Fragment {
    View myview;
    Spinner Usertype, Standard;
    EditText IssueDate, EndDate, NoticeSubject, Notice;
    private Calendar calendar;
    private int year, month, day;
    String fromdate,enddate, Usertype_selected, Standard_selected, Year_id;
    Button saveBtn;
    ConnectionDetector cd;
    Division_spinner_adapter adapter;
    int insertflag;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Issue_Date, End_Date, Notice_Subject, Notice_Detail, role_id, userid, Schooli_id;
    RelativeLayout std_relativee;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    ArrayList<StandardDetail> Standard_list = new ArrayList<StandardDetail>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.notice_entry_layout, null);
        Usertype = (Spinner) myview.findViewById(R.id.spinner_usertype);
        Standard = (Spinner) myview.findViewById(R.id.spinner_standard);
        IssueDate = (EditText) myview.findViewById(R.id.notice_issueDate);
        EndDate = (EditText) myview.findViewById(R.id.notice_end_date);
        NoticeSubject = (EditText) myview.findViewById(R.id.subject_notice);
        Notice = (EditText) myview.findViewById(R.id.notice_et);
        saveBtn = (Button) myview.findViewById(R.id.btnSubmit);
        std_relativee = (RelativeLayout) myview.findViewById(R.id.std_relativee);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        //Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        try {
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                Schooli_id = "";
            } else {
                Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
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
                                                    Noticeboard noticeBoard_application = new Noticeboard();
                                                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, noticeBoard_application).commitAllowingStateLoss();

                                                } catch (Exception ex) {

                                                }


                                                return true;
                                            }
                                        }
                                        return false;
                                    }
        });
        dataList = new ArrayList<HashMap<Object, Object>>();
        userid = settings.getString("TAG_USERID", "");
        cd = new ConnectionDetector(getContext().getApplicationContext());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Issue_Date = IssueDate.getText().toString();
                    End_Date = EndDate.getText().toString();
                    Notice_Detail = Notice.getText().toString();
                    Notice_Subject = NoticeSubject.getText().toString();
                    if (!Issue_Date.contentEquals("") && !End_Date.contentEquals("") && !Notice_Subject.contentEquals("") && !Notice_Detail.contentEquals("") && !Usertype_selected.contentEquals("-- Select UserType --")) {
                        if (Usertype_selected.contentEquals("1")) {
                            if (!Standard_selected.contentEquals("-- Select Standard--")) {
                                if (!cd.isConnectingToInternet()) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                    alert.setMessage("No Internet Connection");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();

                                } else {
                                    SubmitASYNC();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Fill Proper Data", Toast.LENGTH_LONG).show();
                            }

                        } else if (Usertype_selected.contentEquals("0")) {
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                Standard_selected = "0";
                                if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                                    SubmitAllASYNCByPrincipal1 ();
                                } else {
                                    SubmitAllASYNC();
                                }
                            }
                        } else {
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                Standard_selected = "0";
                                if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                                    SubmitASYNC1 ();
                                } else {
                                    SubmitASYNC();
                                }

                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(Issue_Date)) {
                            IssueDate.setError("Enter Valid Issue Date");
                        }
                        if (TextUtils.isEmpty(End_Date)) {
                            EndDate.setError("Enter Valid End Date");
                        }
                        if (TextUtils.isEmpty(Notice_Detail)) {
                            Notice.setError("Enter Notice");
                        }
                        if (TextUtils.isEmpty(Notice_Subject)) {
                            NoticeSubject.setError("Enter Notice Subject");
                        }
                        if (Usertype_selected.contentEquals("-- Select UserType --")) {
                            setSpinnerError(Usertype, "Select Usertype");
                        }
                        //Toast.makeText(getActivity(),"Please Fill Proper Data", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }


            }
        });
        IssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //showDate1(year, month+1, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    NumberFormat f = new DecimalFormat("00");
                                    fromdate = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
//                            tv_dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
                                    Date date1 = null;
                                    try {
                                        date1 = sdf.parse(((f.format(monthOfYear + 1)) + "-" + dayOfMonth + "-" + year));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date date2 = null;
                                    try {
                                        date2 = sdf.parse(fromdate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    IssueDate.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                } catch (Exception ex) {

                                }

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //showDate1(year, month+1, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    NumberFormat f = new DecimalFormat("00");
                                    enddate = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
//                            tv_dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
                                    Date date1 = null;
                                    try {
                                        date1 = sdf.parse(((f.format(monthOfYear + 1)) + "-" + dayOfMonth + "-" + year));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date date2 = null;
                                    try {
                                        date2 = sdf.parse(enddate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    EndDate.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                } catch (Exception ex) {

                                }

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        Usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Usertype_selected = String.valueOf(dataList.get(i).get("UserType_id"));
                    Issue_Date = IssueDate.getText().toString();
                    End_Date = EndDate.getText().toString();
                    Notice_Detail = Notice.getText().toString();
                    Notice_Subject = NoticeSubject.getText().toString();
                    if (Usertype_selected.contentEquals("-- Select UserType --")) {
                        //Toast.makeText(getActivity(),"NO Data available for this Month", Toast.LENGTH_LONG).show();
                    } else {
                        if (!cd.isConnectingToInternet()) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        } else {
                            if (Usertype_selected.contentEquals("1")) {
                                std_relativee.setVisibility(View.VISIBLE);
                                StandardAsync();
                            } else {
                                std_relativee.setVisibility(View.GONE);

                            }

                        }
                    }
                } catch (Exception ex) {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Standard_selected = String.valueOf(Standard_list.get(i).getIntStandardId());
                    Schooli_id = String.valueOf(Standard_list.get(i).getIntschoolId());
                } catch (Exception ex) {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!cd.isConnectingToInternet()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {
            try {
                UserTypeAsync userTypeAsync = new UserTypeAsync();
                userTypeAsync.execute();
            } catch (Exception ex) {

            }


        }
        return myview;
    }

    private class UserTypeAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {

            dataList.clear();

            try {
                map = new HashMap<Object, Object>();
                map.put("UserType", "-- Select UserType --");
                map.put("UserType_id", "-- Select UserType --");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType", "All");
                map.put("UserType_id", "0");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType", "Student");
                map.put("UserType_id", "1");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType", "Teacher");
                map.put("UserType_id", "3");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType", "Staff");
                map.put("UserType_id", "4");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType", "Admin");
                map.put("UserType_id", "5");
                dataList.add(map);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                adapter = new Division_spinner_adapter(getActivity(), dataList, "NoticeUserType");
                Usertype.setAdapter(adapter);
            } catch (Exception ex) {

            }

            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }
    }

    public void StandardAsync() {
        try {
            try {
                StandardDetail standardDetail;
                standardDetail = new StandardDetail(0, "All", 0);
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
            Observable<StandardDetailsPojo> call = service.getStandardDetails(command, Schooli_id, Year_id, "", "", "", "");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Standard_list.addAll(body.getStandardDetails());
                        Standard_Spinner adapter = new Standard_Spinner(getActivity(), Standard_list);
                        Standard.setAdapter(adapter);
                    } catch (Exception ex) {

                        Toast.makeText(getActivity(), "Response taking time seems Network issue!"+ex, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!"+t, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception ex) {

        }
    }

    public void SubmitASYNC() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        try {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            NoticeboardDetail noticeboardDetail = new NoticeboardDetail(Integer.parseInt(Usertype_selected), Integer.parseInt(Standard_selected), 0, 0, fromdate, enddate, Notice_Subject, Notice_Detail, Integer.parseInt(userid), "", 1);
            Observable<ResponseBody> call = service.InsertNotice("insert", noticeboardDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    dialog.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Notice Created Successfully", Toast.LENGTH_SHORT).show();
                    Noticeboard noticeBoardTab = new Noticeboard();
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, noticeBoardTab).commitAllowingStateLoss();
                }
            });
        } catch (Exception ex) {

        }
    }

    public void SubmitAllASYNC() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        try {

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            for (int i = 1; i < 6; i++) {
                final String UserAll = String.valueOf(i);
                if (UserAll.contentEquals("2")) {

                } else {
                    DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                    NoticeboardDetail noticeboardDetail = new NoticeboardDetail(Integer.parseInt(UserAll), Integer.parseInt(Standard_selected), 0, 0, fromdate, enddate, Notice_Subject, Notice_Detail, Integer.parseInt(userid), "", Integer.parseInt(Schooli_id));
                    Observable<ResponseBody> call = service.InsertNotice("insert", noticeboardDetail);
                    call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {
                            dialog.show();
                        }

                        @Override
                        public void onNext(ResponseBody body) {
                            try {

                            } catch (Exception ex) {
                                dialog.dismiss();
//                                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            dialog.dismiss();
//                            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onComplete() {
                            if(UserAll.contentEquals("5"))
                            {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Notice Created Successfully", Toast.LENGTH_SHORT).show();
                                Noticeboard noticeBoardTab = new Noticeboard();
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, noticeBoardTab).commitAllowingStateLoss();

                            }
                        }
                    });
                }
            }
        }
        catch (Exception ex) {
        }
    }

    public void SubmitAllASYNCByPrincipal1 () {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        try {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            for (int i = 1; i <6 ; i++) {
                final String UserAll = String.valueOf(i);
                if (UserAll.contentEquals("2")) {

                } else {
                    DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                    NoticeboardDetail noticeboardDetail = new NoticeboardDetail(Integer.parseInt(UserAll), Integer.parseInt(Standard_selected), 0, 0, fromdate, enddate, Notice_Subject, Notice_Detail, Integer.parseInt(userid), "", 1);
                    Observable<ResponseBody> call = service.InsertNotice("insert", noticeboardDetail);
                    call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {
                            dialog.show();
                        }

                        @Override
                        public void onNext(ResponseBody body) {
                            try {

                            } catch (Exception ex) {
                                dialog.dismiss();
//                                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            dialog.dismiss();
//                            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onComplete() {
                                if(UserAll.contentEquals("5"))
                                {
                                    dialog.dismiss();
                                    SubmitAllASYNCByPrincipal2();
                                }
                        }
                    });
                }
            }
        }
        catch (Exception ex) {

        }
    }


    public void SubmitAllASYNCByPrincipal2 () {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        try {

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            for (int i = 1; i < 6; i++) {
                final String UserAll = String.valueOf(i);
                if (UserAll.contentEquals("2")) {

                } else {
                    DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                    NoticeboardDetail noticeboardDetail = new NoticeboardDetail(Integer.parseInt(UserAll), Integer.parseInt(Standard_selected), 0, 0, fromdate, enddate, Notice_Subject, Notice_Detail, Integer.parseInt(userid), "", 2);
                    Observable<ResponseBody> call = service.InsertNotice("insert", noticeboardDetail);
                    call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {
                            dialog.show();
                        }

                        @Override
                        public void onNext(ResponseBody body) {
                            try {

                            } catch (Exception ex) {
                                dialog.dismiss();
//                                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            dialog.dismiss();
//                            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onComplete() {
                            if(UserAll.contentEquals("5"))
                            {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Notice Created Successfully", Toast.LENGTH_SHORT).show();
                                Noticeboard noticeBoardTab = new Noticeboard();
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, noticeBoardTab).commitAllowingStateLoss();

                            }
                        }
                    });
                }
            }
        }
        catch (Exception ex) {

        }
    }

    public void SubmitASYNC1() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        try {

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            NoticeboardDetail noticeboardDetail = new NoticeboardDetail(Integer.parseInt(Usertype_selected), Integer.parseInt(Standard_selected), 0, 0, fromdate, enddate, Notice_Subject, Notice_Detail, Integer.parseInt(userid), "",1);
            Observable<ResponseBody> call = service.InsertNotice("insert", noticeboardDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    dialog.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    dialog.dismiss();
                    SubmitASYNC2();
                }
            });
        } catch (Exception ex) {

        }
    }
    public void SubmitASYNC2() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        try {

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            NoticeboardDetail noticeboardDetail = new NoticeboardDetail(Integer.parseInt(Usertype_selected), Integer.parseInt(Standard_selected), 0, 0, fromdate, enddate, Notice_Subject, Notice_Detail, Integer.parseInt(userid), "", 2);
            Observable<ResponseBody> call = service.InsertNotice("insert", noticeboardDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    dialog.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Notice Created Successfully", Toast.LENGTH_SHORT).show();
                    Noticeboard noticeBoardTab = new Noticeboard();
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, noticeBoardTab).commitAllowingStateLoss();
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

