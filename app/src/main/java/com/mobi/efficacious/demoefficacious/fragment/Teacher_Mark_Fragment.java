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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.Teacher_Mark_Adapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetailPojo;
import com.mobi.efficacious.demoefficacious.entity.MarkAttendence;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
 * Created by EFF-4 on 3/16/2018.
 */

public class Teacher_Mark_Fragment extends Fragment {
    View myview;
    Databasehelper mydb;
    Cursor cursor;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    Button button_dateSelection,btn_submit;
    TextView tv_dateSelection;
    String Selected_Date,academic_id,currentdate,Schooli_id,role_id,Selected_Date1,FCMToken,status;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private CompositeDisposable mCompositeDisposable;
    private ProgressDialog progress;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;;
    ConnectionDetector cd;
    int intTeacher_id,intSchool_id,TotalStudent=0,Absent_Count=0;
    boolean attendence_status_present,attendence_status_absent;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.activity_main_recyclerview,null);
        cd = new ConnectionDetector(getActivity());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id=settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        currentdate = new SimpleDateFormat("mm-dd-yyyy", Locale.getDefault()).format(new Date());
        mrecyclerView=(RecyclerView)myview.findViewById(R.id.teacher_recyclerview);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        btn_submit=(Button)myview.findViewById(R.id.btn_submit);
        button_dateSelection=(Button)myview.findViewById(R.id.button_dateSelection);
        tv_dateSelection=(TextView) myview.findViewById(R.id.tv_dateSelection);
        mydb=new Databasehelper(getActivity(),"Teacher_record",null,1);



        button_dateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try
                                {
                                    NumberFormat f = new DecimalFormat("00");
                                    Selected_Date=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
//                            tv_dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    Selected_Date1=(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                                    try {

                                        tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                                        if (!cd.isConnectingToInternet())
                                        {

                                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                            alert.setMessage("No InternetConnection");
                                            alert.setPositiveButton("OK",null);
                                            alert.show();

                                        }
                                        else {
                                            progress.show();
                                            LoginAsync ();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }catch (Exception ex)
                                {

                                }


                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                AttendanceMark();
            }
        });
        return myview;
    }
    public void AttendanceMark() {
        int count=0;
        try
        {
            final ArrayList<AttendanceDetail> teacherlist=((Teacher_Mark_Adapter)madapter).getTchrList();
            for(int i=0;i<teacherlist.size();i++) {
                count=i;
                AttendanceDetail singleteacher = teacherlist.get(i);
                intTeacher_id = singleteacher.getUserId();
                intSchool_id=singleteacher.getIntSchoolId();
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
                int intSchoolId=intSchool_id;
                int intAcademicId=Integer.parseInt(academic_id);
                if (!status.contentEquals("")) {
                    DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                    MarkAttendence attendanceDetail = new MarkAttendence(3,intSchoolId,intAcademicId,Selected_Date,status,intTeacher_id,FCMToken);
                    Observable<ResponseBody> call = service.MarkAttendence("InsertTeacher", attendanceDetail);

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
                            if(teacherlist.size()==(finalCount+1))
                            {
                                progress.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("Attendance Mark Successfully");
                                alert.setPositiveButton("OK", null);
                                alert.show();

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
            String command;
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            if(role_id.contentEquals("6")||role_id.contentEquals("7"))
            {

                command="BackDateTeacherBYPrincipal";
            }else
            {
                command= "BackDateTeacher";
            }
            mCompositeDisposable.add(service.getAttendanceDetails(command,Schooli_id,"3","","",academic_id,Selected_Date,"","")
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
                    madapter=new Teacher_Mark_Adapter(taskListDataList, Selected_Date, academic_id, getContext(),Selected_Date1);
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
