package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.Login_activity;
import com.mobi.efficacious.demoefficacious.adapters.NoticeBoardAdapter;
import com.mobi.efficacious.demoefficacious.adapters.StudentListAdapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetail;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetailsPojo;
import com.mobi.efficacious.demoefficacious.entity.Holiday;
import com.mobi.efficacious.demoefficacious.entity.LoginDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class Admin_Dashboard extends Fragment {
    View myview;
    TextView TextView_student;
    TextView TextView_staff;
    TextView TextView_teacher;
    TextView TextView_pendingfee;
    TextView TextView_receivedfee;
    TextView TextView_totalfee, txt_studentcnt, txt_teacher_cnt, txt_staffcount;
    LinearLayout layout_primary;
    String Totalfee, studentctn, teacherctn, staffctn;
    int dayscount;
    String Receivedfee;
    String Pendingfee, Schooli_id;
    RecyclerView studentlist;
    StudentListAdapter studadapter;
    String newDate, fesival;
    ArrayList<Holiday> holiday1 = new ArrayList<Holiday>();
    Context mContext;
    RecyclerView noticeboard;
    RecyclerView.Adapter madapter;
    String Admin_id, academic_id;
    FrameLayout calenderview;
    Date holidayDay;
    ArrayList<String> dates = new ArrayList<String>();
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    String a, role_id;
    String status, refreshedToken;
    ConnectionDetector cd;
    String date_selected;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String command, User_id;
    private CompositeDisposable mCompositeDisposable;
    PieChart pieChart;
    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_dashboard, null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        mContext = getActivity();
        noticeboard = (RecyclerView) myview.findViewById(R.id.dashnoticeboard_list);
        /*TextView_student = (TextView) myview.findViewById(R.id.tv_Student);
        TextView_staff = (TextView) myview.findViewById(R.id.tv_Staff);
        TextView_teacher = (TextView) myview.findViewById(R.id.tv_Teacher);
        TextView_pendingfee = (TextView) myview.findViewById(R.id.tv_pendingfee);
        TextView_receivedfee = (TextView) myview.findViewById(R.id.tv_receivedfee);
        TextView_totalfee = (TextView) myview.findViewById(R.id.tv_totalfee);
        noticeboard = (RecyclerView) myview.findViewById(R.id.dashnoticeboard_list);
        noticeboard.setNestedScrollingEnabled(false);
        studentlist = (RecyclerView) myview.findViewById(R.id.dashstud_list);*/
        studentlist.setNestedScrollingEnabled(false);
        studentlist = (RecyclerView) myview.findViewById(R.id.dashstud_list);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        Admin_id = settings.getString("TAG_USERID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);
        calenderview = (FrameLayout) myview.findViewById(R.id.cal_container);
        mCaldroidFragment.setArguments(args);
        bottomNavigationView = myview.findViewById(R.id.bottom_navigation);
        if (!cd.isConnectingToInternet()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {

            try {
                initialization();
                HolidayAsync();
                studentAsync();
                teacherAsync();
                staffAsync();
                StudentListAsync();
                NoticeboardAsync();

                piechart();
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent i;
                        switch (menuItem.getItemId()) {
                            case R.id.attendance:
                                Toast.makeText(getContext(),"Attendance",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.study:
                                Toast.makeText(getContext(),"Study",Toast.LENGTH_LONG).show();
//                        Toast.makeText(this,"click2",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.event:
                                Toast.makeText(getContext(),"Event",Toast.LENGTH_LONG).show();
//                        Toast.makeText(this,"click3",Toast.LENGTH_LONG);
                                break;

                            case R.id.chat:
                                Toast.makeText(getContext(),"Chat",Toast.LENGTH_LONG).show();
//                        i=new Intent(this,Study.class);
//                        startActivity(i);
                                break;
                            case R.id.Alert:
                                Toast.makeText(getContext(),"Alert",Toast.LENGTH_LONG).show();
//                        Toast.makeText(this,"click5",Toast.LENGTH_LONG);
                                break;

                        }
                        return true;
                    }
                });
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
                sendTokenToServer(refreshedToken);
            } catch (Exception ex) {

            }

        }
        mCaldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.get(Calendar.YEAR);

                    int day1 = date.getDate();
                    int month1 = ((date.getMonth()) + 1);
                    NumberFormat f = new DecimalFormat("00");
                    date_selected = ((f.format(day1)) + "/" + (f.format(month1)) + "/" + String.valueOf(calendar.get(Calendar.YEAR)));
//                 date_selected= String.valueOf(day1)+("/")+ String.valueOf(month1)+("/")+ String.valueOf(calendar.get(Calendar.YEAR));

                    boolean status = dates.contains(date_selected);
                    if (status == true) {
                        // festivalnmae(date_selected);
                        try {
                            String date_selected1 = date_selected;
                            int sizee = holiday1.size();
                            for (int i = 0; i < holiday1.size(); i++) {
                                String holidaydate = holiday1.get(i).getFromDate();

                                if (date_selected1.contentEquals(holidaydate)) {
                                    String holidayname = holiday1.get(i).getHoliday_name();
                                    Toast.makeText(getActivity(), holidayname, Toast.LENGTH_SHORT).show();
                                    break;
                                }


                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    } else {
                        Toast.makeText(getActivity(), " " + date_selected, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {

                }

            }
        });
        return myview;
    }

    private void initialization() {
        pieChart = myview.findViewById(R.id.piechart);
        txt_studentcnt = myview.findViewById(R.id.studentcnt_txt);
        txt_teacher_cnt = myview.findViewById(R.id.teachercnt_txt);
        txt_staffcount = myview.findViewById(R.id.staffcnt_txt);
    }

    private void piechart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(0,0,0,0);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(android.R.color.white);
        pieChart.setTransparentCircleRadius(90f);
        pieChart.setLeft(1);
//        button_signin=findViewById(R.id.button_signin);

        ArrayList<PieEntry> values = new ArrayList<>();

       /* values.add(new PieEntry(Float.valueOf(studentctn),"Student"));
        values.add(new PieEntry(Float.valueOf(teacherctn),"Teacher"));
        values.add(new PieEntry(Float.valueOf(staffctn),"Staff"));*/

        Float s = Float.parseFloat(studentctn);
        Float t = Float.parseFloat(teacherctn);
        Float staff = Float.parseFloat(staffctn);

        values.add(new PieEntry(s, "Student"));
        values.add(new PieEntry(t, "Teacher"));
        values.add(new PieEntry(staff, "Staff"));

        values.add(new PieEntry(35f,"Student"));
        values.add(new PieEntry(35f,"Teacher"));
        values.add(new PieEntry(35f ,"Staff"));
        Description description = new Description();


        pieChart.animateY(1000, Easing.EasingOption.EaseInCubic);

        PieDataSet pieDataSet = new PieDataSet(values, "Countries");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(pieDataSet);
        pieDataSet.setValueTextSize(3f);
        pieDataSet.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);
    }

    public void studentAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getDashboardDetails("StudentCountByPrincipal", academic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverStudentCount()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<DashboardDetailsPojo> getObserverStudentCount() {
        return new DisposableObserver<DashboardDetailsPojo>() {

            @Override
            public void onNext(@NonNull DashboardDetailsPojo dashboardDetailsPojo) {
                try {
                    generateStudentCount((ArrayList<DashboardDetail>) dashboardDetailsPojo.getDashboardDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void generateStudentCount(ArrayList<DashboardDetail> taskListDataList) {

        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                try {
                    studentctn = taskListDataList.get(0).getPresent() + "/" + taskListDataList.get(0).getCount();
                    txt_studentcnt.setText(studentctn);
                    TextView_totalfee.setText("Student: " + taskListDataList.get(0).getPresent() + "/" + taskListDataList.get(0).getCount());
                } catch (Exception ex) {
                    TextView_totalfee.setText("Student: 0/0");
                }
                try {
                    studentctn = taskListDataList.get(1).getPresent() + "/" + taskListDataList.get(1).getCount();
                    txt_studentcnt.setText(studentctn);
                    TextView_student.setText("Student: " + taskListDataList.get(1).getPresent() + "/" + taskListDataList.get(1).getCount());
                } catch (Exception ex) {
                    TextView_student.setText("Student: 0/0");
                }
            } else {
                TextView_totalfee.setText("Student: 0/0");
                TextView_student.setText("Student: 0/0");
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }

    public void staffAsync() {

        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getDashboardDetails("StaffCountByPrincipal", academic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverStaffCount()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<DashboardDetailsPojo> getObserverStaffCount() {
        return new DisposableObserver<DashboardDetailsPojo>() {

            @Override
            public void onNext(@NonNull DashboardDetailsPojo dashboardDetailsPojo) {
                try {
                    generateStaffCount((ArrayList<DashboardDetail>) dashboardDetailsPojo.getDashboardDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void generateStaffCount(ArrayList<DashboardDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                try {
                    staffctn = taskListDataList.get(0).getPresent() + "/" + taskListDataList.get(0).getCount();
                    txt_staffcount.setText(staffctn);
                    TextView_pendingfee.setText("Staff: " + taskListDataList.get(0).getPresent() + "/" + taskListDataList.get(0).getCount());
                } catch (Exception ex) {
                    TextView_pendingfee.setText("Staff: 0/0 ");
                }
                try {
                    staffctn = taskListDataList.get(1).getPresent() + "/" + taskListDataList.get(1).getCount();
                    txt_staffcount.setText(staffctn);
                    TextView_staff.setText("Staff: " + taskListDataList.get(1).getPresent() + "/" + taskListDataList.get(1).getCount());
                } catch (Exception ex) {
                    TextView_staff.setText("Staff: 0/0 ");
                }
            } else {
                TextView_pendingfee.setText("Staff: 0/0 ");
                TextView_staff.setText("Staff: 0/0 ");
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }


    public void teacherAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getDashboardDetails("TeacherCountBYPrincipal", academic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverTeacherCount()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<DashboardDetailsPojo> getObserverTeacherCount() {
        return new DisposableObserver<DashboardDetailsPojo>() {

            @Override
            public void onNext(@NonNull DashboardDetailsPojo dashboardDetailsPojo) {
                try {
                    generateTeacherCount((ArrayList<DashboardDetail>) dashboardDetailsPojo.getDashboardDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void generateTeacherCount(ArrayList<DashboardDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                try {
                    teacherctn = taskListDataList.get(0).getPresent() + "/" + taskListDataList.get(0).getCount();
                    txt_teacher_cnt.setText(teacherctn);
                    TextView_receivedfee.setText("Teacher: " + taskListDataList.get(0).getPresent() + "/" + taskListDataList.get(0).getCount());
                } catch (Exception ex) {
                    TextView_receivedfee.setText("Teacher: 0/0");
                }
                try {
                    teacherctn = taskListDataList.get(1).getPresent() + "/" + taskListDataList.get(1).getCount();
                    txt_teacher_cnt.setText(teacherctn);
                    TextView_teacher.setText("Teacher: " + taskListDataList.get(1).getPresent() + "/" + taskListDataList.get(1).getCount());
                } catch (Exception ex) {
                    TextView_teacher.setText("Teacher: 0/0");
                }
            } else {
                TextView_receivedfee.setText("Teacher: 0/0");
                TextView_teacher.setText("Teacher: 0/0");
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }


    public void StudentListAsync() {

        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getDashboardDetails("genderwiseStudentBYPrincipal", academic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserver()));
        } catch (Exception ex) {
        }
    }

    public DisposableObserver<DashboardDetailsPojo> getObserver() {
        return new DisposableObserver<DashboardDetailsPojo>() {

            @Override
            public void onNext(@NonNull DashboardDetailsPojo dashboardDetailsPojo) {
                try {
                    generateStudentList((ArrayList<DashboardDetail>) dashboardDetailsPojo.getDashboardDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

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


    public void generateStudentList(ArrayList<DashboardDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                studadapter = new StudentListAdapter(taskListDataList, getActivity());

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                studentlist.setLayoutManager(layoutManager);

                studentlist.setAdapter(studadapter);
            } else {

                //   Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }


    public void NoticeboardAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                mCompositeDisposable.add(service.getDashboardDetails("NoticeBoardPrincipal", academic_id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(getObserverNotice()));
            } else {
                mCompositeDisposable.add(service.getDashboardDetails("NoticeBoard", academic_id, Schooli_id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(getObserverNotice()));
            }

        } catch (Exception ex) {
        }
    }

    public DisposableObserver<DashboardDetailsPojo> getObserverNotice() {
        return new DisposableObserver<DashboardDetailsPojo>() {

            @Override
            public void onNext(@NonNull DashboardDetailsPojo dashboardDetailsPojo) {
                try {
                    generateNoticeboardList((ArrayList<DashboardDetail>) dashboardDetailsPojo.getDashboardDetails());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        };
    }

    public void generateNoticeboardList(ArrayList<DashboardDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                noticeboard.setHasFixedSize(true);
                noticeboard.setLayoutManager(new LinearLayoutManager(getActivity()));
                madapter = new NoticeBoardAdapter(taskListDataList);
                noticeboard.setAdapter(madapter);
            } else {

                //  Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }
    }

    public void HolidayAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<DashboardDetailsPojo> call = service.getDashboardDetails("HolidaysAndVacation", academic_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DashboardDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(DashboardDetailsPojo body) {
                    try {
                        generateHolidayList((ArrayList<DashboardDetail>) body.getDashboardDetails());
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


    public void generateHolidayList(ArrayList<DashboardDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                holiday1.clear();
                for (int j = 0; j < taskListDataList.size(); j++) {
                    Holiday hol = new Holiday();
                    a = taskListDataList.get(j).getDtFromDate();
                    dayscount = taskListDataList.get(j).getIntNoOfDay();
                    fesival = taskListDataList.get(j).getHoliday();
                    hol.setFromDate(a);
                    hol.setHoliday_name(fesival);
                    for (int i = 0; i < dayscount - 1; i++) {
                        Holiday vac1 = new Holiday();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        try {
                            //Setting the date to the given date
                            if (i == 0) {
                                c.setTime(sdf.parse(a));
                            } else {
                                c.setTime(sdf.parse(newDate));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //Number of Days to add
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        //Date after adding the days to the given date
                        newDate = sdf.format(c.getTime());
                        vac1.setFromDate(newDate);
                        vac1.setHoliday_name(fesival);
                        holiday1.add(vac1);
                        dates.add(newDate);

                    }
                    dates.add(a);
                    holiday1.add(hol);
                }
                holiday_list();
                try {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cal_container, mCaldroidFragment).commit();
                    status = "";
                } catch (Exception ex) {

                }
            } else {

            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }

    public void holiday_list() {
        int day = 0;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        for (int i = 0; i < dates.size(); i++) {
            String inputString2 = dates.get(i);
            String inputString1 = myFormat.format(date);

            try {
                //Converting String format to date format
                Date date1 = null;
                try {
                    date1 = myFormat.parse(inputString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = myFormat.parse(inputString2);
                //Calculating number of days from two dates
                long diff = date2.getTime() - date1.getTime();
                long datee = diff / (1000 * 60 * 60 * 24);
                //Converting long type to int type
                day = (int) datee;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, day);
            holidayDay = cal.getTime();
            ColorDrawable bgToday = new ColorDrawable(Color.LTGRAY);
            mCaldroidFragment.setBackgroundDrawableForDate(bgToday, holidayDay);

        }
    }

    public void sendTokenToServer(final String strToken) {
        String email = settings.getString("TAG_USEREMAILID", "");
        if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
            User_id = settings.getString("TAG_STUDENTID", "");
        } else {
            User_id = settings.getString("TAG_USERID", "");
        }
        switch (Integer.parseInt(role_id)) {
            case 1:
                command = "FcmTokenStudent";
                break;
            case 2:
                command = "FcmTokenStudent";
                break;
            case 3:
                command = "FcmTokenTeacher";
                break;
            case 4:
                command = "FcmTokenStaff";
                break;
            case 5:
                command = "FcmTokenAdmin";
                break;
            case 6:
                command = "FcmTokenPrincipal";
                break;
            case 7:
                command = "FcmTokenManager";
                break;

        }

        settings.edit().putString("TAG_USERFIREBASETOKEN", strToken).apply();
        try {
            FCMTOKENASYNC(command, strToken, email);
        } catch (Exception ex) {

        }

    }

    public void FCMTOKENASYNC(String command, String Firebasetoken, String EMail) {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            LoginDetail loginDetail = new LoginDetail(Firebasetoken, EMail, Integer.parseInt(User_id), Integer.parseInt(Schooli_id), Integer.parseInt(academic_id));
            Observable<ResponseBody> call = service.FCMTokenUpdate(command, loginDetail);
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