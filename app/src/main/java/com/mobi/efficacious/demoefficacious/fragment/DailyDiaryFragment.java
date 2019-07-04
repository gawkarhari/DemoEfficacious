package com.mobi.efficacious.demoefficacious.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.MultiImages.activities.Three_Images;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.adapters.Division_Spinner;
import com.mobi.efficacious.demoefficacious.adapters.Standard_Spinner;
import com.mobi.efficacious.demoefficacious.adapters.Subject_Spinner;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.entity.StandardDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class DailyDiaryFragment extends Fragment {
    View myview;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id,Academic_id,Schooli_id;
    public static CircleImageView iv_attachment1, iv_attachment2, iv_attachment3;
    Spinner Std_spinner,Div_spinner,Sub_spinner;
    Button calenderBtn,SubmitBtn;
    EditText remarktv;
    ImageView attachmentbtn;
    TextView tv_dateSelection;
    String currentdate,value,userid,TeacherName;
    String StandardName,std_selected,DivisionName,div_selected ,SubjectName,sub_selected;
    ConnectionDetector cd;
    ArrayList<StandardDetail> Standard_list = new ArrayList<StandardDetail>();
    ArrayList<StandardDetail> Division_list = new ArrayList<StandardDetail>();
    ArrayList<StandardDetail> Subject_list = new ArrayList<StandardDetail>();
    private int mYear, mMonth, mDay;
    String Selected_Date,dtDatetime;
    private ProgressDialog progressDoalog;
    String filepath="0",filepath2="0",filepath3="0",Remark;
    int FileSize=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_daily_diary, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        role_id = settings.getString("TAG_USERTYPEID", "");
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        iv_attachment1 = (CircleImageView) myview.findViewById(R.id.iv_attachment1);
        iv_attachment2 = (CircleImageView) myview.findViewById(R.id.iv_attachment2);
        iv_attachment3 = (CircleImageView) myview.findViewById(R.id.iv_attachment3);
        Std_spinner = (Spinner) myview.findViewById(R.id.std_spinner);
        Div_spinner = (Spinner) myview.findViewById(R.id.div_spinner);
        Sub_spinner = (Spinner) myview.findViewById(R.id.sub_spinner);
        calenderBtn = (Button) myview.findViewById(R.id.calenderbtn);
        tv_dateSelection = (TextView) myview.findViewById(R.id.datetextvw);
        SubmitBtn = (Button) myview.findViewById(R.id.btnSubmit);
        remarktv = (EditText) myview.findViewById(R.id.remarktv);
        attachmentbtn = (ImageView) myview.findViewById(R.id.attachmentbtn);
        iv_attachment1.setVisibility(View.GONE);
        iv_attachment2.setVisibility(View.GONE);
        iv_attachment3.setVisibility(View.GONE);
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setCancelable(false);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.setMessage("uploading...");
        currentdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_dateSelection.setText(currentdate);
        try {
            value =getArguments().getString("value");;
            if (value.contentEquals("HomeWork")) {
                remarktv.setHint("Write Home work here");
            } else {
                remarktv.setHint("Write Remark here");
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
                            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                            Bundle args = new Bundle();
                            args.putString("value",value);
                            dailyDiaryListFragment.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiaryListFragment).commitAllowingStateLoss();

                        } catch (Exception ex) {

                        }

                        return true;
                    }
                }
                return false;
            }
        });
        if (role_id.contentEquals("3")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                try {
                    userid = settings.getString("TAG_USERID", "");
                    TeacherName = settings.getString("TAG_NAME", "");
                    StudenStandardtAsync ();
                } catch (Exception ex) {

                }

            }
        }
        attachmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Three_Images.FilePath.clear();
                    Three_Images.FileName.clear();
                    iv_attachment1.setVisibility(View.GONE);
                    iv_attachment2.setVisibility(View.GONE);
                    iv_attachment3.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), Three_Images.class);
                    startActivity(intent);
                } catch (Exception ex) {

                }

            }
        });
        Std_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Std_spinner = position;
                try {
                    StandardName = String.valueOf(Standard_list.get(position).getVchStandardName());
                    std_selected = String.valueOf(Standard_list.get(position).getIntStandardId());
                    Schooli_id = String.valueOf(Standard_list.get(position).getIntschoolId());
                        if (!cd.isConnectingToInternet()) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        } else {
                            StudentDivisionAsync(std_selected);
                        }

                } catch (Exception ex) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Please Select Standard", Toast.LENGTH_SHORT).show();
            }
        });
        Div_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Div_spinner = position;
                try {
                    DivisionName = String.valueOf(Division_list.get(position).getVchDivisionName());
                    div_selected = String.valueOf(Division_list.get(position).getIntDivisionId());
                        if (!cd.isConnectingToInternet()) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        } else {
                           StudentSubjectAsync(div_selected);
                        }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Please Select Division", Toast.LENGTH_SHORT).show();
            }
        });
        Sub_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Sub_spinner = position;
                try {
                    SubjectName = String.valueOf(Subject_list.get(position).getVchSubjectName());
                    sub_selected = String.valueOf(Subject_list.get(position).getIntSubjectId());

                } catch (Exception ex) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Please Select Subject", Toast.LENGTH_SHORT).show();
            }
        });
        calenderBtn.setOnClickListener(new View.OnClickListener() {
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
                                try {
                                    NumberFormat f = new DecimalFormat("00");
                                    Selected_Date = ((f.format(monthOfYear + 1)) + "/" + (f.format(dayOfMonth)) + "/" + year);
                                    tv_dateSelection.setText(((f.format(dayOfMonth)) + "/" + (f.format(monthOfYear + 1)) + "/" + year));
                                } catch (Exception ex) {

                                }
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     Remark = remarktv.getText().toString();
                    if (!std_selected.contentEquals("0") && !div_selected.contentEquals("0") && !sub_selected.contentEquals("0")&& !Remark.contentEquals("")) {
                        dtDatetime = tv_dateSelection.getText().toString();
                        try {
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage("No InternetConnection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {
                                FileSize = Three_Images.FilePath.size();
                                if (FileSize != 0) {
                                    for (int i = 0; i < FileSize; i++) {
                                        UploadAsync(i);
                                    }
                                }else
                                {
                                    SumbitASYNC(Remark, dtDatetime, std_selected, div_selected, sub_selected);
                                }

                            }
                        } catch (Exception e) {

                        }

                    } else {
                        try
                        {
                            if (TextUtils.isEmpty(Remark)) {
                                remarktv.setError("Enter Remark");
                            }
                            if (std_selected.contentEquals("0")) {
                                setSpinnerError(Std_spinner, "Select valid Standard ");
                            }
                            if (div_selected.contentEquals("0")) {
                                setSpinnerError(Div_spinner, "Select valid Division ");
                            }
                            if (sub_selected.contentEquals("0")) {
                                setSpinnerError(Sub_spinner, "Select valid Subject ");
                            }
                        }catch (Exception ex)
                        {

                        }

                    }
                } catch (Exception ex) {

                }

            }
        });
        return myview;
    }
    public void StudenStandardtAsync (){
        try {
            try {
                Standard_list.clear();
                StandardDetail standardDetail;
                standardDetail = new StandardDetail(0, "--Select Standard--", 0);
                Standard_list.addAll(Collections.singleton(standardDetail));
            }catch (Exception ex)
            {

            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<StandardDetailsPojo> call = service.getStandardDetails("selectStandradByLectures",Schooli_id,Academic_id,"","",userid,"");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Standard_list.addAll(body.getStandardDetails());
                        Standard_Spinner adapter = new Standard_Spinner(getActivity(), Standard_list);
                        Std_spinner.setAdapter(adapter);
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
    public void StudentDivisionAsync(String std_selected){
        try {
            try {
                Division_list.clear();
                StandardDetail standardDetail;
                standardDetail = new StandardDetail(0, "--Select Division--");
                Division_list.addAll(Collections.singleton(standardDetail));
            }catch (Exception ex)
            {

            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<StandardDetailsPojo> call = service.getStandardDetails("selectDivisionByLectures",Schooli_id,Academic_id,std_selected,"",userid,"");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Division_list.addAll(body.getStandardDetails());
                        Division_Spinner adapter = new Division_Spinner(getActivity(), Division_list);
                        Div_spinner.setAdapter(adapter);
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
    public void StudentSubjectAsync(String div_selected){
        try {
            try {
                Subject_list.clear();
                StandardDetail standardDetail;
                standardDetail = new StandardDetail(Integer.parseInt(userid), 0,"--Select Subject--");
                Subject_list.addAll(Collections.singleton(standardDetail));
            }catch (Exception ex)
            {

            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<StandardDetailsPojo> call = service.getStandardDetails("SelectSubjectByLectures",Schooli_id,Academic_id,std_selected,div_selected,userid,"");
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StandardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(StandardDetailsPojo body) {
                    try {
                        Subject_list.addAll(body.getStandardDetails());
                        Subject_Spinner adapter = new Subject_Spinner(getActivity(), Subject_list);
                        Sub_spinner.setAdapter(adapter);
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
    public void UploadAsync(final int i) {
        try {
            try {
                String extension = "";
                String fileName = Three_Images.FileName.get(i);
                String filename = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS", Locale.getDefault()).format(new Date());
                extension = fileName.substring(fileName.lastIndexOf("."));
                extension = filename + extension;
                if (i == 0) {
                    filepath=extension;
                }
                if (i == 1) {
                    filepath2 = extension;
                }
                if (i == 2) {
                    filepath3 = extension;
                }
                DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
                String pathname = Three_Images.FilePath.get(i);
                String path = compressImage(pathname);
                File file = new File(path);
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("image/*"),
                                file
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                Observable<ResponseBody> call = service.upload(body, extension);
                call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        progressDoalog.show();
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        try {

                        } catch (Exception ex) {
                            progressDoalog.dismiss();
                            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        progressDoalog.dismiss();
                        Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        progressDoalog.dismiss();
                        if(i==(FileSize-1))
                        {
                            try {
                                Three_Images.FilePath.clear();
                                Three_Images.FileName.clear();
                                SumbitASYNC(Remark, dtDatetime, std_selected, div_selected, sub_selected);
                            } catch (Exception ex) {

                            }

                        }
                    }
                });

            } catch (Exception ex) {


            }

        } catch (Exception ex) {

        }
    }
    public void SumbitASYNC(String remark, String dtDatetime, String std_selected, String div_selected, String sub_selected) {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            StandardDetail DailDAiary = new StandardDetail(Integer.parseInt(Academic_id), Integer.parseInt(userid),value, Integer.parseInt(std_selected), Integer.parseInt(Schooli_id), Integer.parseInt(div_selected), Integer.parseInt(sub_selected),"", filepath,filepath2,filepath3,0,remark,dtDatetime,TeacherName,StandardName,DivisionName,SubjectName);
            Observable<ResponseBody> call = service.UpdateDairyHomework("Insert", DailDAiary);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progressDoalog.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                }

                @Override
                public void onError(Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    progressDoalog.dismiss();
                    try {
                        Toast.makeText(getActivity(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
                        DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                        Bundle args = new Bundle();
                        args.putString("value",value);
                        dailyDiaryListFragment.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiaryListFragment).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }
                }
            });


        } catch (Exception ex) {
            progressDoalog.dismiss();
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
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
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}