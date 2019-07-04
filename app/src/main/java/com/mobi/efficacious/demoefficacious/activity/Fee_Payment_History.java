package com.mobi.efficacious.demoefficacious.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;

public class Fee_Payment_History extends AppCompatActivity {
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    ConnectionDetector cd;
    String Student_id,Standard_id,Academic_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_payment_history);
        settings =getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Student_id = settings.getString("TAG_STUDENTID", "");
        Standard_id = settings.getString("TAG_STANDERDID", "");
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        mrecyclerView=(RecyclerView)findViewById(R.id.feehistory_recyclerview);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(Fee_Payment_History.this);
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            PaymentHistoryAsync ();
        }
    }
    public void PaymentHistoryAsync ()
    {
        OPERATION_NAME = "StudentTutionFeeCollection/PaymentHistory";
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
