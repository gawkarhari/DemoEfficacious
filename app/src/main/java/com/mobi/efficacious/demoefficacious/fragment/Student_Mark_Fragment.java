package com.mobi.efficacious.demoefficacious.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.StandardAdapter;
import com.mobi.efficacious.demoefficacious.adapters.Student_mark_Adapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetailPojo;
import com.mobi.efficacious.demoefficacious.entity.MarkAttendence;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * Created by EFF-4 on 3/19/2018.
 */

public class Student_Mark_Fragment extends Fragment implements View.OnClickListener {

    View myview;
    Databasehelper mydb;
    Cursor cursor;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    int stdid;
    SharedPreferences settings;
    String role_id, std_div;
    String gender_selected = "", gender;
    String Standard_id, stdname, stddivision, academic_id, FCMToken;
    private static final String PREFRENCES_NAME = "myprefrences";
    String Schooli_id;
    Button button_dateSelection,btn_submit;
    TextView tv_dateSelection;
    String Selected_Date = "", Selected_Date1;
    String currentdate,status,intStandard_id;
    ConnectionDetector cd;
    private int mYear, mMonth, mDay;
    private CompositeDisposable mCompositeDisposable;
    private ProgressDialog progress;
    int intStudent_id,intDivision_id,TotalStudent=0,Absent_Count=0;
    boolean attendence_status_present,attendence_status_absent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_main_recyclerview, null);
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
                            if(role_id.contentEquals("5"))
                            {
                                Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                                Bundle args = new Bundle();
                                args.putString("pagename", "Std");
                                student_std_activity.setArguments(args);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commit();
                            }

                        } catch (Exception ex) {

                        }
                        return true;
                    }
                }
                return false;
            }
        });
        cd = new ConnectionDetector(getActivity());
        mrecyclerView = (RecyclerView) myview.findViewById(R.id.teacher_recyclerview);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        button_dateSelection = (Button) myview.findViewById(R.id.button_dateSelection);
        btn_submit=(Button)myview.findViewById(R.id.btn_submit);
        tv_dateSelection = (TextView) myview.findViewById(R.id.tv_dateSelection);
        currentdate = new SimpleDateFormat("mm-dd-yyyy", Locale.getDefault()).format(new Date());

        try {
            if (role_id.contentEquals("6") || role_id.contentEquals("7") ) {
                Schooli_id = StandardAdapter.intSchool_id;
            } else {
                Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
            }
            Standard_id = StudentAttendanceActivity.stdno;
            stdname = StudentAttendanceActivity.stname;
            stddivision = StudentAttendanceActivity.stddiv;
            stdid = Integer.parseInt(StudentAttendanceActivity.stdno);
            mydb = new Databasehelper(getActivity(), "Teacher_record", null, 1);
            gender_selected = "Female";
        } catch (Exception ex) {

        }
        button_dateSelection.setOnClickListener(this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                AttendanceMark();
            }
        });
        return myview;
    }

    @Override
    public void onClick(View v) {

        if (v == button_dateSelection) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            try {
                                NumberFormat f = new DecimalFormat("00");
                                Selected_Date = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
                                Selected_Date1 = (((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
                                Date date1 = null;
                                try {
                                    date1 = sdf.parse(((f.format(monthOfYear + 1)) + "-" + dayOfMonth + "-" + year));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv_dateSelection.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));
                                    if (!cd.isConnectingToInternet()) {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                        alert.setMessage("No InternetConnection");
                                        alert.setPositiveButton("OK", null);
                                        alert.show();

                                    } else {
                                        progress.show();
                                        LoginAsync();
                                    }

                            } catch (Exception ex) {

                            }

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();

        }

    }

    public void AttendanceMark() {
        int count=0;
        try
        {
            final ArrayList<AttendanceDetail> studentlist=((Student_mark_Adapter)madapter).getStudentList();
            for(int i=0;i<studentlist.size();i++) {
                count=i;
                AttendanceDetail singleteacher = studentlist.get(i);
                intStudent_id = singleteacher.getUserId();
                intStandard_id = singleteacher.getStandardId();
                intDivision_id = singleteacher.getIntDivision_Id();
                FCMToken = singleteacher.getFCMToken();
                attendence_status_present = singleteacher.isP_selected();
                attendence_status_absent = singleteacher.isSelected();
                if (attendence_status_present == true && attendence_status_absent == false) {
                    status = "Present";
                } else if (attendence_status_present == false && attendence_status_absent == true) {
                    status = "Absent";
                } else {
                    status = "";
                }
                int intSchoolId=Integer.parseInt(Schooli_id);
                int intAcademicId=Integer.parseInt(academic_id);
                if (!status.contentEquals("")) {
                    DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                    MarkAttendence attendanceDetail = new MarkAttendence(1,intSchoolId,String.valueOf(intStandard_id),intDivision_id,intAcademicId,Selected_Date,status,intStudent_id,FCMToken);
                    Observable<ResponseBody> call = service.MarkAttendence("Insert", attendanceDetail);

                    final int finalCount = count;
                    call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {
                            progress.show();
                        }

                        @Override
                        public void onNext(ResponseBody body) {
                        }

                        @Override
                        public void onError(Throwable t) {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onComplete() {
                            if(studentlist.size()==(finalCount+1))
                            {
                                progress.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("Attendance Mark Successfully");
                                alert.setPositiveButton("OK", null);
                                alert.show();
                                //Toast.makeText(getActivity(), "Attendance Mark Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }catch (Exception ex)
        {
            progress.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }

    public void LoginAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getAttendanceDetails("BackDate",Schooli_id,"1",Standard_id,stddivision,academic_id,Selected_Date,"","")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserver()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<AttendanceDetailPojo> getObserver(){
        return new DisposableObserver<AttendanceDetailPojo>() {
            @Override
            public void onNext(@NonNull AttendanceDetailPojo attendanceDetailPojo) {
                try {
                    generateStudentList((ArrayList<AttendanceDetail>) attendanceDetailPojo.getAttendanceDetail());
                } catch (Exception ex) {
                    progress.dismiss();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                progress.dismiss();
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                progress.dismiss();
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

    public void generateStudentList(ArrayList<AttendanceDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                try
                {
                    mrecyclerView.setHasFixedSize(true);
                    mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    madapter=new Student_mark_Adapter(taskListDataList,Selected_Date,academic_id,getContext(),Schooli_id,Selected_Date1);
                    mrecyclerView.setAdapter(madapter);

                }catch (Exception ex)
                {

                }
            } else {

                Toast toast = Toast.makeText(getActivity(),
                        "No Data Available",
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toastView.setBackgroundResource(R.drawable.no_data_available);
                toast.show();
                //   Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

        }
    }
}
