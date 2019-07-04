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
import com.mobi.efficacious.demoefficacious.Tab.Event_Tab;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.Standard_Spinner;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.EventDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class Event_application_fragment extends Fragment {
    View myview;
    String UsertypeId, flag;
    String UserId, Year_id;
    TextView EventStartDate, RegistrationStartDate;
    TextView EventEndDate, RegistrationEndDate;
    EditText Eventname, EventFees;
    EditText description;
    Spinner Standard_spinner;
    Button submit;
    private Calendar calendar;
    private int year, month, day;
    String fromdate;
    String todate, std_selected, Schooli_id;
    ConnectionDetector cd;
    ArrayList<StandardDetail> Standard_list = new ArrayList<StandardDetail>();
    String StandardSelected_id, RegStartDate, RegEndDate, Event_StartDate, Event_EndDate, EventName, Event_Fees, Event_Description;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    RelativeLayout reg_frmdate,reg_todate,event_frmdate,event_todate;
    int Status=0;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_event_application, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        UsertypeId = settings.getString("TAG_USERTYPEID", "");
        UserId = settings.getString("TAG_USERID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        if(UsertypeId.contentEquals("6")||UsertypeId.contentEquals("7"))
        {
            Schooli_id ="";
        }else
        {
            Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        }
        flag = "1";
        EventStartDate = (TextView) myview.findViewById(R.id.event_date);
        EventEndDate = (TextView) myview.findViewById(R.id.event_dateto);
        RegistrationStartDate = (TextView) myview.findViewById(R.id.registration_date);
        RegistrationEndDate = (TextView) myview.findViewById(R.id.registration_date2);
        Eventname = (EditText) myview.findViewById(R.id.edteventname);
        EventFees = (EditText) myview.findViewById(R.id.edteventfee);
        description = (EditText) myview.findViewById(R.id.edtdescription);
        submit = (Button) myview.findViewById(R.id.btnSubmit_leave);
        Standard_spinner = (Spinner) myview.findViewById(R.id.std_spinner);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        myview.setFocusableInTouchMode(true);
        myview.requestFocus();
        myview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            Event_list_fragment event_list_fragment = new Event_list_fragment();
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, event_list_fragment).commitAllowingStateLoss();

                        } catch (Exception ex) {

                        }


                        return true;
                    }
                }
                return false;
            }
        });
        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            try
            {
                StudenStandardtAsync();
            }catch (Exception ex)
            {

            }

        }
        EventStartDate.setOnClickListener(new View.OnClickListener() {
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
                                    fromdate = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
                                    EventStartDate.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                }catch (Exception ex)
                                {

                                }
                               }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        EventEndDate.setOnClickListener(new View.OnClickListener() {
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
                                    todate = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
                                    EventEndDate.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                }catch (Exception ex)
                                {

                                }
                                }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        RegistrationStartDate.setOnClickListener(new View.OnClickListener() {
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
                                    fromdate = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
                                    RegistrationStartDate.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                }catch (Exception ex)
                                {

                                }
                               }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        RegistrationEndDate.setOnClickListener(new View.OnClickListener() {
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
                                    fromdate = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
                                    RegistrationEndDate.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));

                                }catch (Exception ex)
                                {

                                }
                                  }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        Standard_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Std_spinner = position;
                try
                {
                    StandardSelected_id=String.valueOf(Standard_list.get(position).getIntStandardId());
                    std_selected = String.valueOf(Standard_list.get(position).getVchStandardName());
                    if(UsertypeId.contentEquals("6")||UsertypeId.contentEquals("7"))
                    {
                        Schooli_id=String.valueOf(Standard_list.get(position).getIntschoolId());
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
                try
                {
                    RegStartDate = RegistrationStartDate.getText().toString();
                    RegEndDate = RegistrationEndDate.getText().toString();
                    Event_StartDate = EventStartDate.getText().toString();
                    Event_EndDate = EventEndDate.getText().toString();
                    Event_Fees = EventFees.getText().toString();
                    if (Event_Fees.contentEquals("")) {
                        Event_Fees = "0";
                    }
                    Event_Description = description.getText().toString();
                    EventName = Eventname.getText().toString();
                    if(UsertypeId.contentEquals("6")||UsertypeId.contentEquals("7"))
                    {
                        if (!std_selected.contentEquals("---Select--") && !RegStartDate.contentEquals("") && !RegEndDate.contentEquals("") && !Event_StartDate.contentEquals("") && !Event_EndDate.contentEquals("") && !EventName.contentEquals("") && !Event_Description.contentEquals("")) {
                            if(std_selected.contentEquals("AllStudent & AllTeacher")||std_selected.contentEquals("AllStudent")||std_selected.contentEquals("AllTeacher"))
                            {

                                SubmitASYNCBYPrincipal(std_selected, RegStartDate, RegEndDate, Event_StartDate, Event_EndDate, Event_Fees, Event_Description, EventName);
                            }
                            else {
                                if(std_selected.equalsIgnoreCase("Nursery"))
                                {
                                    std_selected="1";
                                }else if(std_selected.equalsIgnoreCase("Play Group"))
                                {
                                    std_selected="2";
                                }else if(std_selected.equalsIgnoreCase("KGI"))
                                {
                                    std_selected="3";
                                }else if(std_selected.equalsIgnoreCase("KGII"))
                                {
                                    std_selected="4";
                                }else if(std_selected.equalsIgnoreCase("I"))
                                {
                                    std_selected="5";
                                }else if(std_selected.equalsIgnoreCase("II"))
                                {
                                    std_selected="6";
                                }else if(std_selected.equalsIgnoreCase("III"))
                                {
                                    std_selected="7";
                                }else if(std_selected.equalsIgnoreCase("IV"))
                                {
                                    std_selected="8";
                                }else if(std_selected.equalsIgnoreCase("V"))
                                {
                                    std_selected="9";
                                }else if(std_selected.equalsIgnoreCase("VI"))
                                {
                                    std_selected="10";
                                }else if(std_selected.equalsIgnoreCase("VII"))
                                {
                                    std_selected="11";
                                }else if(std_selected.equalsIgnoreCase("VIII"))
                                {
                                    std_selected="12";
                                }else if(std_selected.equalsIgnoreCase("IX"))
                                {
                                    std_selected="13";
                                }else if(std_selected.equalsIgnoreCase("X"))
                                {
                                    std_selected="14";
                                }else if(std_selected.equalsIgnoreCase("XI"))
                                {
                                    std_selected="15";
                                }
                                else
                                {

                                }
                                SubmitASYNC(std_selected, RegStartDate, RegEndDate, Event_StartDate, Event_EndDate, Event_Fees, Event_Description, EventName);

                            }

                        } else {
                            if (std_selected.contentEquals("---Select--"))
                            {
                                setSpinnerError(Standard_spinner,"Select valid Standard ");
                            }
                            if(TextUtils.isEmpty(RegStartDate)) {
                                RegistrationStartDate.setError("Enter Valid Registration Start Date ");
                            }
                            if(TextUtils.isEmpty(RegEndDate)) {
                                RegistrationEndDate.setError("Enter Valid Registration End Date ");
                            }
                            if(TextUtils.isEmpty(Event_StartDate)) {
                                EventStartDate.setError("Enter Valid Event Start Date ");
                            }
                            if(TextUtils.isEmpty(Event_EndDate)) {
                                EventEndDate.setError("Enter Valid Event End Date ");
                            }
                            if(TextUtils.isEmpty(EventName)) {
                                Eventname.setError("Enter Valid Event Name ");
                            }
                            if(TextUtils.isEmpty(Event_Description)) {
                                description.setError("Enter Event Description ");
                            }

                        }

                    }else
                    {
                        if (!std_selected.contentEquals("--Select--") && !RegStartDate.contentEquals("") && !RegEndDate.contentEquals("") && !Event_StartDate.contentEquals("") && !Event_EndDate.contentEquals("") && !EventName.contentEquals("") && !Event_Description.contentEquals("")) {
                            if(std_selected.equalsIgnoreCase("Nursery"))
                            {
                                std_selected="1";
                            }else if(std_selected.equalsIgnoreCase("Play Group"))
                            {
                                std_selected="2";
                            }else if(std_selected.equalsIgnoreCase("KGI"))
                            {
                                std_selected="3";
                            }else if(std_selected.equalsIgnoreCase("KGII"))
                            {
                                std_selected="4";
                            }else if(std_selected.equalsIgnoreCase("I"))
                            {
                                std_selected="5";
                            }else if(std_selected.equalsIgnoreCase("II"))
                            {
                                std_selected="6";
                            }else if(std_selected.equalsIgnoreCase("III"))
                            {
                                std_selected="7";
                            }else if(std_selected.equalsIgnoreCase("IV"))
                            {
                                std_selected="8";
                            }else if(std_selected.equalsIgnoreCase("V"))
                            {
                                std_selected="9";
                            }else if(std_selected.equalsIgnoreCase("VI"))
                            {
                                std_selected="10";
                            }else if(std_selected.equalsIgnoreCase("VII"))
                            {
                                std_selected="11";
                            }else if(std_selected.equalsIgnoreCase("VIII"))
                            {
                                std_selected="12";
                            }else if(std_selected.equalsIgnoreCase("IX"))
                            {
                                std_selected="13";
                            }else if(std_selected.equalsIgnoreCase("X"))
                            {
                                std_selected="14";
                            }else if(std_selected.equalsIgnoreCase("XI"))
                            {
                                std_selected="15";
                            }
                            else
                            {

                            }
                                SubmitASYNC(std_selected, RegStartDate, RegEndDate, Event_StartDate, Event_EndDate, Event_Fees, Event_Description, EventName);
                        } else {
                            if (std_selected.contentEquals("--Select--"))
                            {
                                setSpinnerError(Standard_spinner,"Select valid Standard ");
                            }
                            if(TextUtils.isEmpty(RegStartDate)) {
                                RegistrationStartDate.setError("Enter Valid Registration Start Date ");
                            }
                            if(TextUtils.isEmpty(RegEndDate)) {
                                RegistrationEndDate.setError("Enter Valid Registration End Date ");
                            }
                            if(TextUtils.isEmpty(Event_StartDate)) {
                                EventStartDate.setError("Enter Valid Event Start Date ");
                            }
                            if(TextUtils.isEmpty(Event_EndDate)) {
                                EventEndDate.setError("Enter Valid Event End Date ");
                            }
                            if(TextUtils.isEmpty(EventName)) {
                                Eventname.setError("Enter Valid Event Name ");
                            }
                            if(TextUtils.isEmpty(Event_Description)) {
                                description.setError("Enter Event Description ");
                            }

                        }
                    }
                }catch (Exception ex)
                {
                    Toast.makeText(getActivity(),""+ex.toString(),Toast.LENGTH_LONG).show();
                }



            }
        });

        return myview;
    }
    public void  StudenStandardtAsync (){
        try {
            try {
                StandardDetail standardDetail;
                standardDetail = new StandardDetail(0, "--Select--", 0);
                Standard_list.addAll(Collections.singleton(standardDetail));
                standardDetail = new StandardDetail(0, "AllStudent & AllTeacher", 0);
                Standard_list.addAll(Collections.singleton(standardDetail));
                standardDetail = new StandardDetail(0, "AllStudent", 0);
                Standard_list.addAll(Collections.singleton(standardDetail));
                standardDetail = new StandardDetail(0, "AllTeacher", 0);
                Standard_list.addAll(Collections.singleton(standardDetail));
            }catch (Exception ex)
            {

            }
            String command;
            if(UsertypeId.contentEquals("6")||UsertypeId.contentEquals("7"))
            {
                command="selectStandardByPrincipal";
            }else
            {
                command="select";
            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<StandardDetailsPojo> call = service.getStandardDetails(command,Schooli_id,Year_id,"","","","");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Standard_list.addAll(body.getStandardDetails());
                        Standard_Spinner adapter = new Standard_Spinner(getActivity(), Standard_list);
                        Standard_spinner.setAdapter(adapter);
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
    public void SubmitASYNC(String std_selected, String regStartDate, String regEndDate, String event_startDate, String event_endDate, String event_fees, String event_description, String eventName) {
        try {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);

                EventDetail eventDetail=new EventDetail(std_selected,regStartDate,regEndDate,event_startDate,event_endDate,eventName,event_fees,event_description,Integer.parseInt(Schooli_id),Integer.parseInt(UserId),Integer.parseInt(Year_id),Integer.parseInt(UsertypeId));
                Observable<ResponseBody> call = service.InsertEvent("Insert",eventDetail);
                call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        dialog.show();
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        try {
                            if (body!=null)
                            {
                                Toast.makeText(getActivity(), "Event Created Successfully", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Exception in onNext!", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        dialog.dismiss();
                        //Toast.makeText(getActivity(), "Exception in onError !", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Event Created Successfully", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                        Event_Tab event_tab = new Event_Tab();
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, event_tab).commitAllowingStateLoss();

                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                            Toast.makeText(getActivity(), "Event Created Successfully", Toast.LENGTH_SHORT).show();
                            Event_Tab event_tab = new Event_Tab();
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, event_tab).commitAllowingStateLoss();
                    }
                });
        } catch (Exception ex) {

        }
    }
    public void SubmitASYNCBYPrincipal(String std_selected, String regStartDate, String regEndDate, String event_startDate, String event_endDate, String event_fees, String event_description, String eventName) {
        try {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            for(int i=1;i<3;i++)
            {
                Status=i;
                int intSchool_id=i;
            EventDetail eventDetail=new EventDetail(std_selected,regStartDate,regEndDate,event_startDate,event_endDate,eventName,event_fees,event_description,intSchool_id,Integer.parseInt(UserId),Integer.parseInt(Year_id),Integer.parseInt(UsertypeId));
            Observable<ResponseBody> call = service.InsertEvent("Insert",eventDetail);
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
                    if(Status==2)
                    {
                        Toast.makeText(getActivity(), "Event Created Successfully", Toast.LENGTH_SHORT).show();
                        Event_Tab event_tab = new Event_Tab();
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, event_tab).commitAllowingStateLoss();
                    }
                }
            });
            }
        } catch (Exception ex) {

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
