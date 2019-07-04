package com.mobi.efficacious.demoefficacious.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.LeaveDetail;
import com.mobi.efficacious.demoefficacious.entity.LeaveDetailPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ApplyLeaveFragment extends Fragment {
    View myview;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String UsertypeId,UserId,Schooli_id,StudId,Year_id,UserName;
    ConnectionDetector cd;
    RelativeLayout LeaveType_realative;
    Spinner spinner_Leavetype;
    EditText reason;
    Button submit,dateimgfrm,dateimgto;
    TextView monthtvFrm,yeartvFrm,monthtvto,yeartvto;
    private Calendar calendar;
    private int year, month,day;
    String FromDate="",ToDate="";
    private ProgressDialog progress;
    String MLCOunt,CLCount,Leave_Type="",vchReason;
    String TotalDaysCount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.fragment_applyleave,null);
        myview.setFocusableInTouchMode(true);
        myview.requestFocus();
        myview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            LeaveListFragment leaveListFragment = new LeaveListFragment();
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, leaveListFragment).commit();

                        } catch (Exception ex) {

                        }

                        return true;
                    }
                }
                return false;
            }
        });
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        cd = new ConnectionDetector(getActivity());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        UsertypeId = settings.getString("TAG_USERTYPEID", "");
        UserId = settings.getString("TAG_USERID", "");
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        StudId = settings.getString("TAG_STUDENTID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        UserName=settings.getString("TAG_NAME", "");
        LeaveType_realative = (RelativeLayout) myview.findViewById(R.id.Rel_main22);
        spinner_Leavetype = (Spinner)  myview.findViewById(R.id.sp_LeaveType);
        reason = (EditText)  myview.findViewById(R.id.edtreason_leave);
        submit = (Button)  myview.findViewById(R.id.btnSubmit_leave);
        dateimgfrm = (Button)  myview.findViewById(R.id.dateimgfrm);
        dateimgto = (Button)  myview.findViewById(R.id.dateimgto);

        monthtvFrm = (TextView) myview.findViewById(R.id.monthtv);
        yeartvFrm = (TextView)  myview.findViewById(R.id.yeartv);
        monthtvto = (TextView)  myview.findViewById(R.id.monthtvto);
        yeartvto = (TextView)  myview.findViewById(R.id.yeartvto);
        try
        {
            if(UsertypeId.contentEquals("3")||UsertypeId.contentEquals("4"))
            {
                LeaveType_realative.setVisibility(View.VISIBLE);
            }else
            {
                LeaveType_realative.setVisibility(View.GONE);
            }
            if(UsertypeId.contentEquals("3")||UsertypeId.contentEquals("4"))
            {
                LeaveCountASNYC();
            }
        }catch (Exception ex)
        {

        }
        dateimgfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    NumberFormat f = new DecimalFormat("00");
                                    FromDate=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
                                  String Date=String.valueOf(f.format(dayOfMonth));
                                    dateimgfrm.setText(Date);
                                    dateimgfrm.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.darkgreen));
                                   String Month=String.valueOf(f.format(monthOfYear +1));
                                    if(Month.contentEquals("01"))
                                    {
                                        monthtvFrm.setText("JAN");
                                    }else if(Month.contentEquals("02"))
                                    {
                                        monthtvFrm.setText("FEB");
                                    }else if(Month.contentEquals("03"))
                                    {
                                        monthtvFrm.setText("MAR");
                                    }else if(Month.contentEquals("04"))
                                    {
                                        monthtvFrm.setText("APR");
                                    }else if(Month.contentEquals("05"))
                                    {
                                        monthtvFrm.setText("MAY");
                                    }else if(Month.contentEquals("06"))
                                    {
                                        monthtvFrm.setText("JUNE");
                                    }else if(Month.contentEquals("07"))
                                    {
                                        monthtvFrm.setText("JULY");
                                    }else if(Month.contentEquals("08"))
                                    {
                                        monthtvFrm.setText("AUG");
                                    }else if(Month.contentEquals("09"))
                                    {
                                        monthtvFrm.setText("SEPT");
                                    }else if(Month.contentEquals("10"))
                                    {
                                        monthtvFrm.setText("OCT");
                                    }else if(Month.contentEquals("11"))
                                    {
                                        monthtvFrm.setText("NOV");
                                    }else if(Month.contentEquals("12"))
                                    {
                                        monthtvFrm.setText("DEC");
                                    }
                                    else
                                    {
                                        monthtvFrm.setText("Select");
                                    }
                                    yeartvFrm.setText(String.valueOf(year));
                                } catch (Exception ex) {

                                }
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        dateimgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try
                                {
                                    NumberFormat f = new DecimalFormat("00");
                                    ToDate=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
                                    String Date=String.valueOf(f.format(dayOfMonth));
                                    dateimgto.setText(Date);
                                    dateimgto.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.darkred));
                                    String Month=String.valueOf(f.format(monthOfYear +1));
                                    if(Month.contentEquals("01"))
                                    {
                                        monthtvto.setText("JAN");
                                    }else if(Month.contentEquals("02"))
                                    {
                                        monthtvto.setText("FEB");
                                    }else if(Month.contentEquals("03"))
                                    {
                                        monthtvto.setText("MAR");
                                    }else if(Month.contentEquals("04"))
                                    {
                                        monthtvto.setText("APR");
                                    }else if(Month.contentEquals("05"))
                                    {
                                        monthtvto.setText("MAY");
                                    }else if(Month.contentEquals("06"))
                                    {
                                        monthtvto.setText("JUNE");
                                    }else if(Month.contentEquals("07"))
                                    {
                                        monthtvto.setText("JULY");
                                    }else if(Month.contentEquals("08"))
                                    {
                                        monthtvto.setText("AUG");
                                    }else if(Month.contentEquals("09"))
                                    {
                                        monthtvto.setText("SEPT");
                                    }else if(Month.contentEquals("10"))
                                    {
                                        monthtvto.setText("OCT");
                                    }else if(Month.contentEquals("11"))
                                    {
                                        monthtvto.setText("NOV");
                                    }else if(Month.contentEquals("12"))
                                    {
                                        monthtvto.setText("DEC");
                                    }
                                    else
                                    {
                                        monthtvto.setText("Select");
                                    }
                                    yeartvto.setText(String.valueOf(year));
                                }catch (Exception ex)
                                {

                                }
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        List<String> categories = new ArrayList<String>();
        categories.add("--Select--");
        categories.add("CL");
        categories.add("ML");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);
        spinner_Leavetype.setAdapter(dataAdapter);
        spinner_Leavetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
                Leave_Type="";
                try
                {
                    if(item.equalsIgnoreCase("--Select--"))
                    {
                        Leave_Type="--Select--";
                    }
                    if(item.equalsIgnoreCase("CL"))
                    {
                        Leave_Type="CL";
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setMessage("CL : "+CLCount +" already used Out of 12 CL");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                    if(item.equalsIgnoreCase("ML"))
                    {
                        Leave_Type="ML";
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setMessage("ML : "+MLCOunt+" already used Out of 15 ML");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                }catch (Exception ex)
                {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK",null);
                    alert.show();

                }
                else {
                    SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                    if (FromDate.contentEquals("") || ToDate.contentEquals("") || reason.getText().toString().contentEquals("")) {
                        if (TextUtils.isEmpty(FromDate)) {
                            dateimgfrm.setError("Enter Valid From Date ");
                        }
                        if (TextUtils.isEmpty(ToDate)) {
                            dateimgto.setError("Enter Valid To Date ");
                        }
                        if (TextUtils.isEmpty(reason.getText().toString())) {
                            reason.setError("Enter Reason of Leave ");
                        }
                    } else {
                        String inputString1 = FromDate;
                        String inputString2 = ToDate;
                        try {
                            Date date1 = myFormat.parse(inputString1);
                            Date date2 = myFormat.parse(inputString2);
                            long diff = date2.getTime() - date1.getTime();
                            int dayscoun = Integer.parseInt(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                            if (dayscoun >= 0) {
                                TotalDaysCount = String.valueOf(dayscoun + 1);
                                Toast.makeText(getActivity(), String.valueOf("Days: " + TotalDaysCount), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast toast = Toast.makeText(getActivity(),
                                        "Please Select Proper Date",
                                        Toast.LENGTH_SHORT);
                                View toastView = toast.getView();
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toastView.setBackgroundResource(R.drawable.no_data_available);
                                toast.show();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(UsertypeId.contentEquals("3")||UsertypeId.contentEquals("4"))
                        {
                            if(Leave_Type.contentEquals("--Select--")||Leave_Type.contentEquals(""))
                            {
                                setSpinnerError(spinner_Leavetype,"Select valid Leave Type ");
                            }else
                            {
                                try
                                {
                                    AdminAsync();
                                }catch (Exception ex)
                                {

                                }

                            }
                        }else
                        {
                            try
                            {
                                AdminAsync ();
                            }catch (Exception ex)
                            {

                            }

                        }
                    }
                }

            }
        });
        return myview;
    }
    public void  LeaveCountASNYC (){
    try {
        String command="";
        if(UsertypeId.contentEquals("3"))
        {
            command="SelectLeaveCount";
        }
        else if(UsertypeId.contentEquals("4"))
        {
                command="SelectLeaveCountStaff";
        }
        DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
        Observable<LeaveDetailPojo> call = service.getLeaveDetailDetails(command,Year_id,UsertypeId,UserId,Schooli_id,UserId);
        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LeaveDetailPojo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                progress.show();
            }

            @Override
            public void onNext(LeaveDetailPojo body) {
                try {
                    generateLeaveList((ArrayList<LeaveDetail>) body.getLeaveDetail());
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

    public void generateLeaveList(ArrayList<LeaveDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                CLCount=taskListDataList.get(0).getIntCL().toString();
                MLCOunt=taskListDataList.get(0).getIntML().toString();

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
    public void  AdminAsync (){
        try {
            vchReason=reason.getText().toString();
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            LeaveDetail leaveDetail = new LeaveDetail(Integer.parseInt(UsertypeId),1, Integer.parseInt(UserId), Integer.parseInt(Schooli_id), 1, Integer.parseInt(Year_id),vchReason,FromDate,ToDate, Integer.parseInt(TotalDaysCount),Leave_Type,"0",UserName);
            Observable<ResponseBody> call = service.updateLeaveDetail("insert",leaveDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

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
                    try {
                            Toast.makeText(getActivity(), "Leave Applied Successfully", Toast.LENGTH_LONG).show();
                        LeaveListFragment leaveListFragment = new LeaveListFragment();
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, leaveListFragment).commit();

                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
            progress.dismiss();
        }
    }
    private void setSpinnerError(Spinner spinner, String error){
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