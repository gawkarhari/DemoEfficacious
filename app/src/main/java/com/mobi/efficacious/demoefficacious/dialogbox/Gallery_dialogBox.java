package com.mobi.efficacious.demoefficacious.dialogbox;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.MultiImages.activities.MainImages;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.Division_spinner_adapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.common.SpinnerError;
import com.mobi.efficacious.demoefficacious.entity.SchoolDetail;
import com.mobi.efficacious.demoefficacious.entity.SchoolDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Gallery_dialogBox extends Activity {
    EditText EventDescriptin_et;
    Button btnsave, btnCancel;
    public static String EventDescriptin;
    ConnectionDetector cd;
    Spinner sp_FolderNmae;
    String Selected_Folder_id="";
    private ProgressDialog progress;
    Division_spinner_adapter adapter1;
    HashMap<Object, Object> map;
    SpinnerError spinnerError;
    private ArrayList<HashMap<Object, Object>> dataList2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery_dialogbox);
        cd = new ConnectionDetector(getApplicationContext());
        EventDescriptin_et = (EditText) findViewById(R.id.editText288);
        btnsave = (Button) findViewById(R.id.btnsave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        sp_FolderNmae= (Spinner) findViewById(R.id.sp_FolderNmae);
        spinnerError = new SpinnerError(Gallery_dialogBox.this);
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                } catch (Exception ex) {

                }
            }
        });
        sp_FolderNmae.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Selected_Folder_id = String.valueOf(dataList2.get(position).get("Folder_id"));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EventDescriptin=EventDescriptin_et.getText().toString();
                    if (!EventDescriptin_et.getText().toString().contentEquals("")&&!Selected_Folder_id.contentEquals("")&&!Selected_Folder_id.contentEquals("Select")) {
                        ActivityCompat.requestPermissions(Gallery_dialogBox.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                        ActivityCompat.requestPermissions(Gallery_dialogBox.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                        if (!cd.isConnectingToInternet()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(Gallery_dialogBox.this);
                            alert.setMessage("No InternetConnection");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        } else {
                            Intent intent = new Intent(Gallery_dialogBox.this, MainImages.class);
                            intent.putExtra("EventDescriptin",EventDescriptin);
                            intent.putExtra("Folder_id",Selected_Folder_id);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        if (TextUtils.isEmpty(EventDescriptin_et.getText().toString())) {
                            EventDescriptin_et.setError("Enter Event Description");
                        }
                        if(Selected_Folder_id.contentEquals("")||Selected_Folder_id.contentEquals("Select"))
                        {
                            spinnerError.setSpinnerError(sp_FolderNmae, "Select valid Folder");
                        }

                    }
                } catch (Exception ex) {

                }

            }
        });
        try {
            progress = new ProgressDialog(Gallery_dialogBox.this);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("loading...");
            GalleryAsync ();
        } catch (Exception ex) {

        }
    }


    public void  GalleryAsync (){
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<SchoolDetailsPojo> call = service.getSchoolDetails("selectGAlleryFolder","");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SchoolDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(SchoolDetailsPojo body) {
                    try {
                        generateImageList((ArrayList<SchoolDetail>) body.getSchoolDetails());
                    } catch (Exception ex) {
                        progress.dismiss();
                        Toast.makeText(Gallery_dialogBox.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    Toast.makeText(Gallery_dialogBox.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

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

    public void generateImageList(ArrayList<SchoolDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                dataList2.clear();
                map = new HashMap<Object, Object>();
                map.put("Folder_id", "Select");
                map.put("EventName", "-- Select Folder --");
                dataList2.add(map);
                for (int i = 0; i < taskListDataList.size(); i++) {
                    map = new HashMap<Object, Object>();
                    map.put("Folder_id", String.valueOf(taskListDataList.get(i).getFolderid()));
                    map.put("EventName", String.valueOf(taskListDataList.get(i).getEventName()));
                    dataList2.add(map);
                }

                try {
                    adapter1 = new Division_spinner_adapter(Gallery_dialogBox.this, dataList2, "Gallery");
                    sp_FolderNmae.setAdapter(adapter1);
                } catch (Exception ex) {

                }

            } else {

            }

        } catch (Exception ex) {
            progress.dismiss();
            Toast.makeText(Gallery_dialogBox.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

