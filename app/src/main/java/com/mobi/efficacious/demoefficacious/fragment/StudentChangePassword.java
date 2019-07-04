package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.LoginDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class StudentChangePassword extends Fragment implements View.OnClickListener {
    View myview;
    Button btnupdate;
    Button btnCancel;
    EditText edtUsername;
    EditText edtPassword;
    Toolbar toolbar;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String USERNAME;
    String PASSWORD;
    String User_id;
    public String User_name, role_id;
    public String Academic_id, Schooli_id;
    ConnectionDetector cd;
    private ProgressDialog progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_studentchangepassword, null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        User_id = settings.getString("TAG_USERID", "");
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        USERNAME = settings.getString("TAG_USERNAME", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        btnupdate = (Button) myview.findViewById(R.id.btnUpdate_studentchangepassword);
        btnupdate.setOnClickListener(this);
        btnCancel = (Button) myview.findViewById(R.id.btnCancel_studentchangepassword);
        btnCancel.setOnClickListener(this);
        edtUsername = (EditText) myview.findViewById(R.id.edtUserName_studentchangepassword);
        edtPassword = (EditText) myview.findViewById(R.id.edtPassword_studentchangepassword);
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("processing...");
        try {
            edtUsername.setText(USERNAME);
            edtUsername.clearFocus();
            edtUsername.setFocusable(false);
            edtUsername.setEnabled(false);
        } catch (Exception ex) {

        }


        return myview;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate_studentchangepassword:
                Animation animFadein2 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fadein);
                btnupdate.startAnimation(animFadein2);
                try {
                    USERNAME = edtUsername.getText().toString().trim();
                    PASSWORD = edtPassword.getText().toString().trim();
                } catch (Exception ex) {

                }


                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        if (PASSWORD.contentEquals("")) {
                            if (TextUtils.isEmpty(PASSWORD)) {
                                edtPassword.setError("Enter Valid Password");
                            }
                            // Toast.makeText(StudentChangePasswordActivity.this, "Please Insert Data Properly", Toast.LENGTH_SHORT).show();
                        } else {

                            if (isValidPassword(PASSWORD)) {
                                try {
                                    LoginAsync();
                                } catch (Exception ex) {

                                }


                            } else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setTitle("Password Alert");
                                alert.setMessage("Require Combination Of Characters, Numbers and Special Characters for Password");
                                alert.setPositiveButton("OK", null);
                                alert.show();
                                // Toast.makeText(StudentChangePasswordActivity.this, "Please Use atleast One lower case Character,Number,Special Charater for Password", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {

                    }


                }
                break;
            case R.id.btnCancel_studentchangepassword:
                try {

                    edtPassword.setText("");
                    edtPassword.setHint("Enter Password");
                } catch (Exception ex) {

                }
                Animation animFadein = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fadein);
                btnCancel.startAnimation(animFadein);
                break;
        }
    }

    public void LoginAsync () {
        String command;
        if (role_id.contentEquals("1") | role_id.contentEquals("2")) {
            command="updateStudent";
        } else if (role_id.contentEquals("3")) {
            command="updateTeacher";
        } else if (role_id.contentEquals("4")) {
            command="updateStaff";
        } else if (role_id.contentEquals("5")) {
            command="updateAdmin";
        } else if (role_id.contentEquals("6")) {
            command="updatePrincipal";
        } else {
            command="updateManager";
        }
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            LoginDetail loginDetail=new LoginDetail(Integer.parseInt(User_id), Integer.parseInt(Schooli_id), Integer.parseInt(role_id),USERNAME,PASSWORD, Integer.parseInt(Academic_id));
            Observable<ResponseBody> call = service.FCMTokenUpdate(command,loginDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progressBar.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        progressBar.dismiss();
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    progressBar.dismiss();
                    Toast.makeText(getActivity(), "Password Change Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {

        }
    }


    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{3,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();


    }
}
