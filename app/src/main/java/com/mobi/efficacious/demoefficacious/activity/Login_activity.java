package com.mobi.efficacious.demoefficacious.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.Division_spinner_adapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.common.SpinnerError;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.LoginDetail;
import com.mobi.efficacious.demoefficacious.entity.LoginDetailsPojo;
import com.mobi.efficacious.demoefficacious.entity.SchoolDetail;
import com.mobi.efficacious.demoefficacious.entity.SchoolDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Login_activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    Spinner spinner, school_spinner, sp_Academic_year;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String USERNAME, Selected_academic_id, Selected_School_id;
    String PASSWORD;
    public String User_id, Name;
    String Student_id;
    String Standard_id;
    String Division_id;
    String UserType_id, Name2;
    public String User_name;
    public String passWord;
    int roleid;
    String Session_usertype_id, session_username, session_password, session_fcmtoken, session_emailid;
    ConnectionDetector cd;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList1;
    private ArrayList<HashMap<Object, Object>> dataList2;
    Databasehelper mydb;
    Division_spinner_adapter adapter1;
    SpinnerError spinnerError;
    RadioGroup radioGroup;
    RadioButton rbAdmin, rbTeacher, rbStudent;
    int loginverificationstatus = 0;
    int loginstatus = 0;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        spinnerError = new SpinnerError(Login_activity.this);
        btnLogin = (Button) findViewById(R.id.btnSubmit_login);
        radioGroup = (RadioGroup) findViewById(R.id.rdgroup);
        rbAdmin = findViewById(R.id.rb_admin);
        rbTeacher = findViewById(R.id.rb_teacher);
        rbStudent = findViewById(R.id.rb_student);
//        school_spinner = (Spinner) findViewById(R.id.sp_SchoolName);
        btnLogin.setOnClickListener(this);
        cd = new ConnectionDetector(getApplicationContext());
//        sp_Academic_year = (Spinner) findViewById(R.id.sp_Academic_year);
        dataList1 = new ArrayList<HashMap<Object, Object>>();
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        edtUsername = (EditText) findViewById(R.id.edtUserName_login);
        edtPassword = (EditText) findViewById(R.id.edtPassword_login);
//        spinner = (Spinner) findViewById(R.id.spUserType_login);
//        spinner.setOnItemSelectedListener(this);
        Selected_academic_id = "2";
        Selected_School_id="1";
        session_emailid = settings.getString("TAG_USEREMAILID", "");
        session_fcmtoken = settings.getString("TAG_USERFIREBASETOKEN", "");
        Session_usertype_id = settings.getString("TAG_USERTYPEID", "");
        session_username = settings.getString("TAG_USERNAME", "");
        session_password = settings.getString("TAG_PASSWORD", "");
        mydb = new Databasehelper(getApplicationContext(), "Notifications", null, 1);
        mydb.query("Create table if not exists MessageCenter(ID INTEGER PRIMARY KEY AUTOINCREMENT,Message varchar,MessageDate varchar)");
        try {
            mydb.query("Create table if not exists EILGroupChat(ID INTEGER PRIMARY KEY AUTOINCREMENT,SenderMessage varchar,SenderName varchar,RecieverName varchar,ReciverMessage varchar,GroupName varchar,MessageDate varchar)");
            mydb.query("Create table if not exists EILChat(ID INTEGER PRIMARY KEY AUTOINCREMENT,SenderMessage varchar,ReciverMessage varchar,ReciverId varchar,MessageDate varchar,ReceiverUserTypeId varchar)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mydb.query("Create table if not exists NoticeBoard(ID INTEGER PRIMARY KEY AUTOINCREMENT,Subject varchar,Notice varchar,IssueDate varchar,LastDate varchar)");
        if (!Session_usertype_id.contentEquals("") && !session_username.contentEquals("") && !session_password.contentEquals("") && !session_emailid.contentEquals("")) {
            loginstatus = 1;
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Login_activity.this);
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                Intent HomeScreenIntent = new Intent(Login_activity.this, MainActivity.class);
                startActivity(HomeScreenIntent);
                finish();
            }
        }
        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Login_activity.this);
            alert.setMessage("No InternetConnection");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            if (loginstatus == 0) {
                try {
                    SchoolASYNC();
                } catch (Exception ex) {
                }
                try {
                    Academic_Year();
                } catch (Exception ex) {
                }
            }
        }
/*        sp_Academic_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_academic_id = String.valueOf(dataList1.get(position).get("intAcademic_id"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        school_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_School_id = String.valueOf(dataList2.get(position).get("intSchool_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        List<String> categories = new ArrayList<String>();
        categories.add("Admin");
        categories.add("Teacher");
        categories.add("Staff");
        categories.add("Parent");
        categories.add("student");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Login_activity.this, R.layout.spinner_item, categories);
//        spinner.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        String item = parent.getSelectedItem().toString();
        if (item.equalsIgnoreCase("Parent") || item.equalsIgnoreCase("student")) {
            school_spinner.setEnabled(true);
            roleid = 1;
        } else if (item.equalsIgnoreCase("Teacher")) {
            school_spinner.setEnabled(true);
            roleid = 3;
        } else if (item.equalsIgnoreCase("Staff")) {
            school_spinner.setEnabled(true);
            roleid = 4;
        } else if (item.equalsIgnoreCase("Admin")) {
            school_spinner.setEnabled(true);
            roleid = 5;
        } else if (item.equalsIgnoreCase("Principal")) {
            school_spinner.setEnabled(false);
            roleid = 6;
        } else if (item.equalsIgnoreCase("Manager")) {
            school_spinner.setEnabled(false);
            roleid = 7;
        }


    }


    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit_login:
                if (rbStudent.isChecked())
                {
                    roleid = 1;
                }else if (rbTeacher.isChecked())
                {
                    roleid=3;
                }else if (rbAdmin.isChecked())
                {
                    roleid=5;
                }
                Animation animFadein2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                btnLogin.startAnimation(animFadein2);
                try {
                    USERNAME = edtUsername.getText().toString().trim();
                    PASSWORD = edtPassword.getText().toString().trim();

                    if (!cd.isConnectingToInternet()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(Login_activity.this);
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    } else {
                        if (USERNAME.contentEquals("") || PASSWORD.contentEquals("")) {
                            if (TextUtils.isEmpty(USERNAME)) {
                                edtUsername.setError("Enter Valid Username ");
                            }
                            if (TextUtils.isEmpty(PASSWORD)) {
                                edtPassword.setError("Enter Valid Password ");
                            }
                        } else {
                            if (roleid == 6 || roleid == 7) {
                                if (Selected_academic_id.contentEquals("Select")) {
                                    spinnerError.setSpinnerError(sp_Academic_year, "Select valid Academic Year ");

                                } else {

                                    LoginAsync();

                                }
                            } else {
                                /*if (Selected_academic_id.contentEquals("Select") || Selected_School_id.contentEquals("Select")) {
                                    *//*if (Selected_academic_id.contentEquals("Select")) {
                                        spinnerError.setSpinnerError(sp_Academic_year, "Select valid Academic Year ");
                                    }
                                    if (Selected_School_id.contentEquals("Select")) {
                                        spinnerError.setSpinnerError(school_spinner, "Select valid School Name ");
                                    }*//*
                                } else {

                                    LoginAsync();
                                }*/
                                LoginAsync();
                            }

                        }
                    }
                } catch (Exception ex) {

                }

                break;

        }
    }

    public void LoginAsync() {
        try {
            String command = "";
            if (roleid == 1) {
                command = "student";
            } else if (roleid == 3) {
                command = "teacher";
            } else if (roleid == 4) {
                command = "staff";
            } else if (roleid == 5) {
                command = "admin";
            } else if (roleid == 6) {
                command = "Principal";
            } else if (roleid == 7) {
                command = "Manager";
            }

//            if (Selected_School_id.contentEquals("Select")) {
//                Selected_School_id = "0";
//            }

            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getLoginDetails(command, String.valueOf(roleid), USERNAME, PASSWORD, Selected_academic_id, Selected_School_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverLogin()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<LoginDetailsPojo> getObserverLogin() {
        return new DisposableObserver<LoginDetailsPojo>() {

            @Override
            public void onNext(@NonNull LoginDetailsPojo loginDetailsPojo) {
                try {
                    generateLoginDetails((ArrayList<LoginDetail>) loginDetailsPojo.getLoginDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void generateLoginDetails(ArrayList<LoginDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                UserType_id = String.valueOf(taskListDataList.get(0).getIntUserTypeId());
                if (UserType_id.equalsIgnoreCase("7")) {
                    User_id = String.valueOf(taskListDataList.get(0).getIntManagerId());
                    Name = String.valueOf(taskListDataList.get(0).getName());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());
                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_NAME", Name).commit();
                    settings.edit().putString("TAG_NAME2", "Manager").commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", "0").commit();

                } else if (UserType_id.equalsIgnoreCase("6")) {
                    User_id = String.valueOf(taskListDataList.get(0).getIntPrincipalId());
                    Name = String.valueOf(taskListDataList.get(0).getName());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());
                    // academic_id = column.getProperty(7).toString();
                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_NAME", Name).commit();
                    settings.edit().putString("TAG_NAME2", "Principal").commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", "0").commit();

                } else if (UserType_id.equalsIgnoreCase("5")) {
                    User_id = String.valueOf(taskListDataList.get(0).getIntAdminId());
                    Name = String.valueOf(taskListDataList.get(0).getName());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());
                    // academic_id = column.getProperty(7).toString();
                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_NAME", Name).commit();
                    settings.edit().putString("TAG_NAME2", "Administrator").commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id.trim()).commit();

                } else if (UserType_id.equalsIgnoreCase("4")) {
                    User_id = String.valueOf(taskListDataList.get(0).getIntStaffId());
                    Name = String.valueOf(taskListDataList.get(0).getName());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());

                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_NAME", Name).commit();
                    settings.edit().putString("TAG_NAME2", "Staff").commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id.trim()).commit();
                } else if (UserType_id.equalsIgnoreCase("3")) {
                    User_id = String.valueOf(taskListDataList.get(0).getIntTeacherId());
                    Name = String.valueOf(taskListDataList.get(0).getName());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());
                    Standard_id = String.valueOf(taskListDataList.get(0).getIntstandardId());
                    Division_id = String.valueOf(taskListDataList.get(0).getIntDivisionId());
                    settings.edit().putString("TAG_STANDERDID", Standard_id).commit();
                    settings.edit().putString("TAG_DIVISIONID", Division_id).commit();
                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_NAME", Name).commit();
                    settings.edit().putString("TAG_NAME2", "Teacher").commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id.trim()).commit();
                } else if (UserType_id.equalsIgnoreCase("2")) {
                    User_id = String.valueOf(taskListDataList.get(0).getIntStudentId());
                    Student_id = String.valueOf(taskListDataList.get(0).getIntStudentId());
                    Standard_id = String.valueOf(taskListDataList.get(0).getIntstandardId());
                    Division_id = String.valueOf(taskListDataList.get(0).getIntDivisionId());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());

                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_STUDENTID", Student_id).commit();
                    settings.edit().putString("TAG_STANDERDID", Standard_id).commit();
                    settings.edit().putString("TAG_DIVISIONID", Division_id).commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id.trim()).commit();
                } else {
                    User_id = String.valueOf(taskListDataList.get(0).getIntStudentId());
                    Student_id = String.valueOf(taskListDataList.get(0).getIntStudentId());
                    Name2 = String.valueOf(taskListDataList.get(0).getStd());
                    Standard_id = String.valueOf(taskListDataList.get(0).getIntstandardId());
                    Division_id = String.valueOf(taskListDataList.get(0).getIntDivisionId());
                    Name = String.valueOf(taskListDataList.get(0).getName());
                    User_name = String.valueOf(taskListDataList.get(0).getVchUserName());
                    passWord = String.valueOf(taskListDataList.get(0).getVchPassword());
                    // academic_id = column.getProperty(12).toString();
                    settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                    settings.edit().putString("TAG_STUDENTID", Student_id).commit();
                    settings.edit().putString("TAG_USERID", User_id).commit();
                    settings.edit().putString("TAG_STANDERDID", Standard_id).commit();
                    settings.edit().putString("TAG_DIVISIONID", Division_id).commit();
                    settings.edit().putString("TAG_USERNAME", User_name).commit();
                    settings.edit().putString("TAG_PASSWORD", passWord).commit();
                    settings.edit().putString("TAG_NAME", Name).commit();
                    settings.edit().putString("TAG_NAME2", Name2).commit();
                    settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                    settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id.trim()).commit();
                }
//                Intent intent=new Intent(Login_activity.this,Gmail_login.class);
                Intent intent = new Intent(Login_activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast toast = Toast.makeText(Login_activity.this,
                        "Username Or Password Incorrect",
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toastView.setBackgroundResource(R.drawable.no_data_available);
                toast.show();
            }
        } catch (Exception ex) {
            Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }

    public void SchoolASYNC() {
        try {

            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getSchoolDetails("Select", "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverSchoolList()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<SchoolDetailsPojo> getObserverSchoolList() {
        return new DisposableObserver<SchoolDetailsPojo>() {

            @Override
            public void onNext(@NonNull SchoolDetailsPojo schoolDetailsPojo) {
                try {
                    generateSchoolList((ArrayList<SchoolDetail>) schoolDetailsPojo.getSchoolDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void generateSchoolList(ArrayList<SchoolDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                dataList2.clear();
                map = new HashMap<Object, Object>();
                map.put("intSchool_id", "Select");
                map.put("School_name", "-- Select School --");
                dataList2.add(map);
                for (int i = 0; i < taskListDataList.size(); i++) {
                    map = new HashMap<Object, Object>();
                    map.put("intSchool_id", String.valueOf(taskListDataList.get(i).getIntSchoolId()));
                    map.put("School_name", String.valueOf(taskListDataList.get(i).getVchSchoolName()));
                    dataList2.add(map);
                }

                try {
                    adapter1 = new Division_spinner_adapter(Login_activity.this, dataList2, "SchoolName");
                    school_spinner.setAdapter(adapter1);
                } catch (Exception ex) {

                }

            } else {
                Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception ex) {
            Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }

    public void Academic_Year() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getSchoolDetails("Academic_year", "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverAcademicYrList()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<SchoolDetailsPojo> getObserverAcademicYrList() {
        return new DisposableObserver<SchoolDetailsPojo>() {

            @Override
            public void onNext(@NonNull SchoolDetailsPojo schoolDetailsPojo) {
                try {
                    generateAcademicList((ArrayList<SchoolDetail>) schoolDetailsPojo.getSchoolDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void generateAcademicList(ArrayList<SchoolDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                dataList1.clear();
                map = new HashMap<Object, Object>();
                map.put("intAcademic_id", "Select");
                map.put("AcademicYear", "--Select Academic Year--");
                dataList1.add(map);
                for (int i = 0; i < taskListDataList.size(); i++) {
                    map = new HashMap<Object, Object>();
                    map.put("intAcademic_id", String.valueOf(taskListDataList.get(i).getIntAcademicId()));
                    map.put("AcademicYear", String.valueOf(taskListDataList.get(i).getAcademicYear()));
                    dataList1.add(map);
                }
                try {
                    adapter1 = new Division_spinner_adapter(Login_activity.this, dataList1, "AcademicYear");
                    sp_Academic_year.setAdapter(adapter1);
                } catch (Exception ex) {

                }
            } else {
                Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(Login_activity.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }
}
