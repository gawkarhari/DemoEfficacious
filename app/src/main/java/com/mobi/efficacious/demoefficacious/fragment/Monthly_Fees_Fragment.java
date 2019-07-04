package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.FeesDetail;
import com.mobi.efficacious.demoefficacious.entity.FeesDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Monthly_Fees_Fragment extends Fragment implements View.OnClickListener {
    View myview;
    private CompositeDisposable mCompositeDisposable;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Student_id, Academic_id, MonthlyAmount, Standard_id, intTutionID, Month_id_paid;

    TextView Monthlyamounttv;
    public static CheckBox April_cb, May_cb, June_cb, July_cb, August_cb, September_cb, October_cb, November_cb, December_cb, January_cb, February_cb, March_cb;
    Button AddToFeeBtn;
    ArrayList<String> Month_id = new ArrayList<String>();
    ArrayList<String> Month_name = new ArrayList<String>();
    ArrayList<String> Month_Idpaid = new ArrayList<String>();
    Databasehelper mydb;
    int paidfeeStatusApril_cb, paidfeeStatusMay_cb, paidfeeStatusJune_cb, paidfeeStatusJuly_cb, paidfeeStatusAugust_cb, paidfeeStatusSeptember_cb, paidfeeStatusOctober_cb, paidfeeStatusNovember_cb, paidfeeStatusDecember_cb, paidfeeStatusJanuary_cb, paidfeeStatusFebruary_cb, paidfeeStatusMarch_cb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.monthly_fees_fragment, null);

        Monthlyamounttv = (TextView) myview.findViewById(R.id.monthlyamountv);
        AddToFeeBtn = (Button) myview.findViewById(R.id.buttonAddToFee);
        mydb = new Databasehelper(getActivity(), "Fees_collection", null, 1);
        April_cb = (CheckBox) myview.findViewById(R.id.checkBoxapr);
        May_cb = (CheckBox) myview.findViewById(R.id.checkBoxmay);
        June_cb = (CheckBox) myview.findViewById(R.id.checkBoxjune);
        July_cb = (CheckBox) myview.findViewById(R.id.checkBoxjuly);
        August_cb = (CheckBox) myview.findViewById(R.id.checkBoxaugust);
        September_cb = (CheckBox) myview.findViewById(R.id.checkBoxseptember);
        October_cb = (CheckBox) myview.findViewById(R.id.checkBoxoctober);
        November_cb = (CheckBox) myview.findViewById(R.id.checkBoxnovember);
        December_cb = (CheckBox) myview.findViewById(R.id.checkBoxdecember);
        January_cb = (CheckBox) myview.findViewById(R.id.checkBoxjanuary);
        February_cb = (CheckBox) myview.findViewById(R.id.checkBoxfebruary);
        March_cb = (CheckBox) myview.findViewById(R.id.checkBoxmarch);

        April_cb.setOnClickListener(this);
        May_cb.setOnClickListener(this);
        June_cb.setOnClickListener(this);
        July_cb.setOnClickListener(this);
        August_cb.setOnClickListener(this);
        September_cb.setOnClickListener(this);
        October_cb.setOnClickListener(this);
        November_cb.setOnClickListener(this);
        December_cb.setOnClickListener(this);
        January_cb.setOnClickListener(this);
        February_cb.setOnClickListener(this);
        March_cb.setOnClickListener(this);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Student_id = settings.getString("TAG_STUDENTID", "");
        Standard_id = settings.getString("TAG_STANDERDID", "");
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        intTutionID = "3";
        MonthlyAmount();
        AddToFeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                Month_id.clear();
                Month_name.clear();

                if (April_cb.isChecked()) {
                    if (paidfeeStatusApril_cb != 11) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("11");
                            Month_name.add("Apr");

                    }
                    if (May_cb.isChecked()) {
                        if (paidfeeStatusMay_cb != 12) {

                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("12");
                            Month_name.add("May");

                        }
                    }
                    if (June_cb.isChecked()) {
                        if (paidfeeStatusJune_cb != 1) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("1");
                            Month_name.add("Jun");
                        }
                    }
                    if (July_cb.isChecked()) {
                        if (paidfeeStatusJuly_cb != 2) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("2");
                            Month_name.add("Jul");
                        }
                    }
                    if (August_cb.isChecked()) {
                        if (paidfeeStatusAugust_cb != 3) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("3");
                            Month_name.add("Aug");
                        }
                    }
                    if (September_cb.isChecked()) {
                        if (paidfeeStatusSeptember_cb != 4) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("4");
                            Month_name.add("Sep");
                        }
                    }
                    if (October_cb.isChecked()) {
                        if (paidfeeStatusOctober_cb != 5) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("5");
                            Month_name.add("Oct");
                        }
                    }
                    if (November_cb.isChecked()) {
                        if (paidfeeStatusNovember_cb != 6) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("6");
                            Month_name.add("Nov");
                        }
                    }
                    if (December_cb.isChecked()) {
                        if (paidfeeStatusDecember_cb != 7) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("7");
                            Month_name.add("Dec");
                        }
                    }
                    if (January_cb.isChecked()) {
                        if (paidfeeStatusJanuary_cb != 8) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("8");
                            Month_name.add("Jan");
                        }
                    }
                    if (February_cb.isChecked()) {
                        if (paidfeeStatusFebruary_cb != 9) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("9");
                            Month_name.add("Feb");
                        }
                    }
                    if (March_cb.isChecked()) {
                        if (paidfeeStatusMarch_cb != 10) {
                            i = i + Integer.parseInt(MonthlyAmount);
                            Month_id.add("10");
                            Month_name.add("Mar");
                        }
                    }
//                Toast.makeText(getActivity(),"total :"+i+ "Month"+Month_id.size(),Toast.LENGTH_SHORT).show();
                    if (Month_id.size() > 0) {
                    inserfeeCart();
                    } else {
                        Toast.makeText(getActivity(), "Please Select Month", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        MonthlyAmountPaid();
        return myview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBoxapr:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='11' and intMonth='Apr'");
                break;
            case R.id.checkBoxmay:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='12' and intMonth='May'");
                break;
            case R.id.checkBoxjune:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='1' and intMonth='Jun'");
                break;
            case R.id.checkBoxjuly:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='2' and intMonth='Jul'");
                break;
            case R.id.checkBoxaugust:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='3' and intMonth='Aug'");
                break;
            case R.id.checkBoxseptember:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='4' and intMonth='Sep'");
                break;
            case R.id.checkBoxoctober:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='5' and intMonth='Oct'");
                break;
            case R.id.checkBoxnovember:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='6' and intMonth='Nov'");
                break;
            case R.id.checkBoxdecember:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='7' and intMonth='Dec'");
                break;
            case R.id.checkBoxjanuary:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='8' and intMonth='Jan'");

                break;
            case R.id.checkBoxfebruary:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='9' and intMonth='Feb'");
                break;
            case R.id.checkBoxmarch:
                mydb.query("Delete from tblfeecollection1 where intMonth_ID='10' and intMonth='Mar'");
                break;
        }
        FragmentManager mfragment = getActivity().getSupportFragmentManager();
        mfragment.beginTransaction().replace(R.id.content_main2, new Fees_PaymentFragment()).commit();
    }

    public void MonthlyAmount() {

        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<FeesDetailsPojo> call = service.getFeesDetails("Monthly", Student_id, Academic_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<FeesDetailsPojo>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(FeesDetailsPojo body) {
                    try {
                        generateFeesAmount((ArrayList<FeesDetail>) body.getFeesDetail());
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
            ex.getMessage();
        }

    }

    public void generateFeesAmount(ArrayList<FeesDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {


                MonthlyAmount = String.valueOf(taskListDataList.get(0).getIntAmount());

                Monthlyamounttv.setText(MonthlyAmount);
            }
        } catch (Exception ex) {
        }
    }

    public void MonthlyAmountPaid() {
        try {

            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getFeesDetails("Paid Month", Student_id, Academic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverPAidFees()));
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public DisposableObserver<FeesDetailsPojo> getObserverPAidFees() {
        return new DisposableObserver<FeesDetailsPojo>() {

            @Override
            public void onNext(@NonNull FeesDetailsPojo schoolDetailsPojo) {
                try {
                    generatePaidFeesList((ArrayList<FeesDetail>) schoolDetailsPojo.getFeesDetail());
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

    public void generatePaidFeesList(ArrayList<FeesDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                for (int i = 0; i < taskListDataList.size(); i++) {
                    Month_id_paid = String.valueOf(taskListDataList.get(i).getIntMonth());
                    switch (Integer.parseInt(Month_id_paid)) {
                        case 12:
                            May_cb.setChecked(true);
                            May_cb.setEnabled(false);
                            May_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusMay_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 11:
                            April_cb.setChecked(true);
                            April_cb.setEnabled(false);
                            April_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusApril_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 10:
                            March_cb.setChecked(true);
                            March_cb.setEnabled(false);
                            March_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusMarch_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 9:
                            February_cb.setChecked(true);
                            February_cb.setEnabled(false);
                            February_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusFebruary_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 8:
                            January_cb.setChecked(true);
                            January_cb.setEnabled(false);
                            January_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusJanuary_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 7:
                            December_cb.setChecked(true);
                            December_cb.setEnabled(false);
                            December_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusDecember_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 6:
                            November_cb.setChecked(true);
                            November_cb.setEnabled(false);
                            November_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusNovember_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 5:
                            October_cb.setChecked(true);
                            October_cb.setEnabled(false);
                            October_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusOctober_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 4:
                            September_cb.setChecked(true);
                            September_cb.setEnabled(false);
                            September_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusSeptember_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 3:
                            August_cb.setChecked(true);
                            August_cb.setEnabled(false);
                            August_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusAugust_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 2:
                            July_cb.setChecked(true);
                            July_cb.setEnabled(false);
                            July_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusJuly_cb = Integer.parseInt(Month_id_paid);
                            break;
                        case 1:
                            June_cb.setChecked(true);
                            June_cb.setEnabled(false);
                            June_cb.setTextColor(getResources().getColor(R.color.darkred));
                            paidfeeStatusJune_cb = Integer.parseInt(Month_id_paid);
                            break;

                    }
                }
            }
        } catch (Exception ex) {
        }
    }


    public void inserfeeCart() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Processing...");
        dialog.show();

        for (int i = 0; i < Month_id.size(); i++) {
            Cursor cursor = mydb.querydata("select intMonth from tblfeecollection1 where intMonth_ID='" + Month_id.get(i) + "' and intTutionID='" + intTutionID + "' ");
            if (cursor.getCount() == 0) {
                mydb.query("insert into tblfeecollection1 (intstudent_id,intStandard_id,intTutionID,intMonth_ID,intAcademic_id,intMonth,SumAmount) values('" + Student_id + "','" + Standard_id + "','" + intTutionID + "','" + Month_id.get(i) + "','" + Academic_id + "','" + Month_name.get(i) + "','" + MonthlyAmount + "')");

            }
        }
        FragmentManager mfragment = getActivity().getSupportFragmentManager();
        mfragment.beginTransaction().replace(R.id.content_main2, new Fees_PaymentFragment()).commit();
        dialog.dismiss();
    }


}
