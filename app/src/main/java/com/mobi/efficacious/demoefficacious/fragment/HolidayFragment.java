package com.mobi.efficacious.demoefficacious.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.HolidayAdapter;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetail;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetailsPojo;
import com.mobi.efficacious.demoefficacious.entity.Holiday;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class HolidayFragment  extends Fragment {

    View myview;
    private CompositeDisposable mCompositeDisposable;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String academic_id,a,fesival,newDate;
    int dayscount;
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Holiday> holiday1 = new ArrayList<Holiday>();
    FrameLayout calenderview;
    Date holidayDay;
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    String date_selected;
    RecyclerView recyclerView;
    HolidayAdapter holidayAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_holiday, container, false);
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);
        calenderview = (FrameLayout) getActivity().findViewById(R.id.cal_container);
        mCaldroidFragment.setArguments(args);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        recyclerView = (RecyclerView) myview.findViewById(R.id.allholidaylist);
        recyclerView.setNestedScrollingEnabled(false);
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
        HolidayAsync();
        return myview;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
    public void HolidayAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getDashboardDetails("HolidaysAndVacation", academic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverHolidayList()));
        } catch (Exception ex) {
        }
    }
    public DisposableObserver<DashboardDetailsPojo> getObserverHolidayList(){
        return new DisposableObserver<DashboardDetailsPojo>() {

            @Override
            public void onNext(@NonNull DashboardDetailsPojo dashboardDetailsPojo) {
                try {
                    generateHolidayList((ArrayList<DashboardDetail>) dashboardDetailsPojo.getDashboardDetails());
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
                    holidayAdapter = new HolidayAdapter(getActivity(),taskListDataList);
                    recyclerView.setHasFixedSize(true);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(holidayAdapter);
                } catch (Exception ex) {

                }
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
            ColorDrawable bgToday = new ColorDrawable(Color.RED);
            mCaldroidFragment.setBackgroundDrawableForDate(bgToday, holidayDay);

        }
    }
}
