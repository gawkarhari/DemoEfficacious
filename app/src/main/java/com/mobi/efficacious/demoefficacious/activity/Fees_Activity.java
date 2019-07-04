package com.mobi.efficacious.demoefficacious.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.fragment.FeesTabFragment;


public class Fees_Activity extends AppCompatActivity {
    FrameLayout frameLayout,Fixedframelayout;
    Databasehelper mydb;
    ConnectionDetector cd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_activity);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(Fees_Activity.this);
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {

            mydb = new Databasehelper(Fees_Activity.this, "Fees_collection", null, 1);
            mydb.query("Create table if not exists tblfeecollection1(ID INTEGER PRIMARY KEY AUTOINCREMENT,intstudent_id varchar ,intStandard_id varchar ,intTutionID varchar ,intPaidAmt varchar ,vchPaidAmt varchar ,intLateAmt varchar ,intTotalAmt varchar ,intDiscount varchar , intAfterDiscount varchar ,paidDate varchar ,intSchool_id varchar ,intPayMode varchar ,vchChequeNo varchar ,dtCheque varchar ,vchBankName varchar ,intMonth_ID varchar ,intAcademic_id varchar ,intMonth varchar ,Description  varchar ,  FromMonth varchar ,ToMonth varchar ,SumAmount INTEGER ,Receipt_Id varchar ,intActive_flg varchar)");
            mydb.query("delete  from tblfeecollection1");
            FragmentManager mfragment = getSupportFragmentManager();
            mfragment.beginTransaction().replace(R.id.content_main, new FeesTabFragment()).commit();
        }
    }
}
