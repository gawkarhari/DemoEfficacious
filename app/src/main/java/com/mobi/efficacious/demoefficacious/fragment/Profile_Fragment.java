package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.MultiImages.activities.Single_image;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.LoginDetail;
import com.mobi.efficacious.demoefficacious.entity.ProfileDetail;
import com.mobi.efficacious.demoefficacious.entity.ProfileDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class Profile_Fragment extends Fragment {
    View myview;
    Databasehelper mydb;
    ConnectionDetector cd;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    CircleImageView ProfileImage,image_edit;
    TextView name,StdName,IdNo,MobileNotv,fathertv,Motherstv,fatherNametv,MotherNametv,addresstv;
    String Schooli_id,role_id,academic_id,UserId;
    private ProgressDialog progress;
    String refreshedToken;

    String command,User_id,Teacher_statndard,Teacher_division;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_profile, null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        UserId = settings.getString("TAG_USERID", "");
        if(role_id.contentEquals("3"))
        {
            Teacher_statndard=settings.getString("TAG_STANDERDID", "");
            Teacher_division=settings.getString("TAG_DIVISIONID", "");
        }
        name=(TextView)myview.findViewById(R.id.name);
        StdName=(TextView)myview.findViewById(R.id.StdName);
        IdNo=(TextView)myview.findViewById(R.id.IdNo);
        MobileNotv=(TextView)myview.findViewById(R.id.MobileNotv);
        fathertv=(TextView)myview.findViewById(R.id.father);
        Motherstv=(TextView)myview.findViewById(R.id.Mothers);
        fatherNametv=(TextView)myview.findViewById(R.id.fatherName);
        MotherNametv=(TextView)myview.findViewById(R.id.MotherName);
        addresstv=(TextView)myview.findViewById(R.id.addresstv);
        ProfileImage=(CircleImageView)myview.findViewById(R.id.imageView1);
        image_edit=(CircleImageView)myview.findViewById(R.id.imageViewedit);
        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Single_image.class);
                startActivity(intent);
            }
        });
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        progress.show();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ProfileAsync();
        return myview;
    }
    public void  ProfileAsync (){
        try {
            String command;
            if (role_id.contentEquals("3")) {
                if(Teacher_statndard.contentEquals("0")||Teacher_division.contentEquals("0"))
                {
                    command = "GetTeacherProfile";
                }else
                {
                    command = "GetTeacherProfilewithStandard";
                }
            } else if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                command = "GetStudentProfile";
            } else if (role_id.contentEquals("4")) {
                command = "GetStaffProfile";
            } else if (role_id.contentEquals("6")) {
                command = "GetPrincipalProfile";
            } else if (role_id.contentEquals("7")) {
                command = "GetManagerProfile";
            } else {
                command = "GetAdminProfile";

            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<ProfileDetailsPojo> call = service.getProfiledetails(command,Schooli_id, academic_id, UserId);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ProfileDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(ProfileDetailsPojo body) {
                    try {
                        generateProfileList((ArrayList<ProfileDetail>) body.getProfileDetails());
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
                    sendTokenToServer(refreshedToken);
                }
            });
        } catch (Exception ex) {
            progress.dismiss();
        }
    }

    public void generateProfileList(ArrayList<ProfileDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                name.setText(taskListDataList.get(0).getVchFirstName().toUpperCase().toString()+" "+taskListDataList.get(0).getLastName().toUpperCase().toString());
                MobileNotv.setText(taskListDataList.get(0).getIntMobileNo().toString());
                addresstv.setText(taskListDataList.get(0).getVchAddress().toString());
                if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                   StdName.setText(taskListDataList.get(0).getVchStandardname().toString());
                  //  IdNo.setText(taskListDataList.get(0).getIntRollNO().toString());
                    fatherNametv.setText(taskListDataList.get(0).getVchFatherNAme().toString());
                    MotherNametv.setText(taskListDataList.get(0).getVchMotherNAme().toString());
                    fathertv.setVisibility(View.VISIBLE);
                    IdNo.setVisibility(View.GONE);
                    StdName.setVisibility(View.VISIBLE);
                    Motherstv.setVisibility(View.VISIBLE);
                    fatherNametv.setVisibility(View.VISIBLE);
                    MotherNametv.setVisibility(View.VISIBLE);
                }else
                {
                    if(role_id.contentEquals("3"))
                    {
                        if(Teacher_statndard.contentEquals("0")||Teacher_division.contentEquals("0"))
                        {
                            StdName.setVisibility(View.GONE);
                        }else
                        {
                            StdName.setText(taskListDataList.get(0).getVchStandardname().toString());
                            StdName.setVisibility(View.VISIBLE);

                        }
                    }else
                    {
                        StdName.setVisibility(View.GONE);
                    }
                    MotherNametv.setVisibility(View.GONE);
                    fatherNametv.setVisibility(View.GONE);
                    Motherstv.setVisibility(View.GONE);
                    fathertv.setVisibility(View.GONE);
                    IdNo.setVisibility(View.GONE);

                }
                String url = RetrofitInstance.Image_URL + taskListDataList.get(0).getVchImageURL().toString();
                Glide.with(getActivity())
                        .load(url) // image url
                        .error(R.mipmap.profile)
                        .into(ProfileImage);
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
    public void sendTokenToServer(final String strToken) {
        String  email = settings.getString("TAG_USEREMAILID", "");
        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
        {
            User_id= settings.getString("TAG_STUDENTID", "");
        }else {
            User_id = settings.getString("TAG_USERID", "");
        }
        switch (Integer.parseInt(role_id))
        {
            case 1:command="FcmTokenStudent";
                break;
            case  2:command="FcmTokenStudent";
                break;
            case 3:command="FcmTokenTeacher";
                break;
            case 4:command="FcmTokenStaff";
                break;
            case 5:command="FcmTokenAdmin";
                break;
            case 6:command="FcmTokenPrincipal";
                break;
            case 7:command="FcmTokenManager";
                break;

        }

        settings.edit().putString("TAG_USERFIREBASETOKEN",strToken).apply();
        try
        {
            FCMTOKENASYNC(command,strToken,email);
        }catch (Exception ex)
        {

        }

    }

    public void FCMTOKENASYNC(String command,String Firebasetoken,String EMail) {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            LoginDetail loginDetail=new LoginDetail(Firebasetoken,EMail, Integer.parseInt(User_id), Integer.parseInt(Schooli_id), Integer.parseInt(academic_id));
            Observable<ResponseBody> call = service.FCMTokenUpdate(command,loginDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {


                    }
                }

                @Override
                public void onError(Throwable t) {
                }

                @Override
                public void onComplete() {
                }
            });
        } catch (Exception ex) {

        }
    }
}
