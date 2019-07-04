package com.mobi.efficacious.demoefficacious.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atom.mobilepaymentsdk.PayActivity;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.Fee_Payment_History;
import com.mobi.efficacious.demoefficacious.adapters.AddToFeeRecyclerview;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.AddToFee;
import com.mobi.efficacious.demoefficacious.entity.FeesDetail;
import com.mobi.efficacious.demoefficacious.entity.FeesDetailsPojo;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class Fees_PaymentFragment extends Fragment {
    View myview;
    Databasehelper mydb;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    private ProgressDialog progressBar;
    private CompositeDisposable mCompositeDisposable;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    Cursor recycleview_cursor;
    ArrayList<AddToFee> addToFee=new ArrayList<AddToFee>();
    public static TextView Amounttv;
    int amount=0;
    int count;
    Button DoPaymentbtn,PaymentHistory;
    String CurrDateTime;
    String message="";
    ConnectionDetector cd;
    String Receipt_Id="", intstudent_id,intStandard_id,intAcademic_id,Schooli_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fees_paymentfragment, null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            mrecyclerView = (RecyclerView) myview.findViewById(R.id.addtofeeerecylerview);
            Amounttv = (TextView) myview.findViewById(R.id.textViewamount);
            DoPaymentbtn = (Button) myview.findViewById(R.id.buttonpayment);
            PaymentHistory = (Button) myview.findViewById(R.id.button_view);
            settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            intstudent_id = settings.getString("TAG_STUDENTID", "");
            intStandard_id = settings.getString("TAG_STANDERDID", "");
            intAcademic_id = settings.getString("TAG_ACADEMIC_ID", "");
            Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
            mydb = new Databasehelper(getActivity(), "Fees_collection", null, 1);
            AddToFeeCartAsync addToFeeCartAsync = new AddToFeeCartAsync();
            addToFeeCartAsync.execute();
            CurrDateTime = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
            PaymentHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Fee_Payment_History.class);
                    startActivity(intent);
                }
            });
            DoPaymentbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(),"clicked on DoPaymentbtn",Toast.LENGTH_LONG).show();
                    RecipetAsync();
                }
            });
        }
        return myview;
    }

    private class AddToFeeCartAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                addToFee.clear();
                recycleview_cursor =mydb.querydata("Select intMonth_ID,intTutionID,intMonth,SumAmount from tblfeecollection1 ");
                count=recycleview_cursor.getCount();


                recycleview_cursor.moveToFirst();
                if (recycleview_cursor != null) {

                    if (recycleview_cursor.moveToFirst()) {
                        do {

                            String intTutionID=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intTutionID"));
                            String intMonth=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intMonth"));
                            String SumAmount=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("SumAmount"));
                            String intMonth_ID=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intMonth_ID"));
                            addToFee.add(new AddToFee(intTutionID,intMonth,SumAmount,intMonth_ID));
                            amount=amount+Integer.parseInt(SumAmount);
                        } while (recycleview_cursor.moveToNext());

                    }

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mrecyclerView.setHasFixedSize(true);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            madapter=new AddToFeeRecyclerview(addToFee,getActivity());
            mrecyclerView.setAdapter(madapter);
            if(count>0)
            {
                Amounttv.setText(String.valueOf(amount));
            }else {
                Amounttv.setText("0");
            }
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
            //  progressBar.setVisibility(View.VISIBLE);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (data != null)
            {
                message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");
                if(resKey!=null && resValue!=null)
                {
                    for(int i=0; i<resKey.length; i++)
                        System.out.println(" "+i+" resKey : "+resKey[i]+" resValue : "+resValue[i]);
                }
            }
        }
        Transactiondata();
    }
    public void Transactiondata()
    {
        String intTutionID="";
        String intMonthname="",intPaidAmt="",Month_Id="";
        if(message.contentEquals("Transaction Successful!"))
        {
            recycleview_cursor =mydb.querydata("Select intMonth_ID,intTutionID,intMonth,SumAmount from tblfeecollection1 ");
            count=recycleview_cursor.getCount();

            recycleview_cursor.moveToFirst();
            if (recycleview_cursor != null) {

                if (recycleview_cursor.moveToFirst()) {
                    do {
                        intTutionID=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intTutionID"));
                        intMonthname=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intMonth"));
                        intPaidAmt=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("SumAmount"));
                        Month_Id=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intMonth_ID"));
                        StudentPaidFeesAsync (intTutionID,intMonthname,intPaidAmt,Month_Id);
                    } while (recycleview_cursor.moveToNext());

                }

            }
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(message);
            alert.setPositiveButton("OK",null);
            alert.show();
//
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//        madapter.notifyDataSetChanged();
        }
        else
        {
                mydb.query("delete  from tblfeecollection1");
            Toast.makeText(getActivity(), ""+message, Toast.LENGTH_LONG).show();
        }
    }
    public void RecipetAsync()
    {

        try {

            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            mCompositeDisposable.add(service.getFeesDetails("GetAutoReceiptID","",intAcademic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(getObserverFeesAmount()));
        } catch (Exception ex) {
        }
    }
    public DisposableObserver<FeesDetailsPojo> getObserverFeesAmount(){
        return new DisposableObserver<FeesDetailsPojo>() {

            @Override
            public void onNext(@NonNull FeesDetailsPojo schoolDetailsPojo) {
                try {
                    generateFeesAmount((ArrayList<FeesDetail>) schoolDetailsPojo.getFeesDetail());
                } catch (Exception ex) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getActivity(), "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                PaymentMethod();
            }
        };
    }

    public void generateFeesAmount(ArrayList<FeesDetail> taskListDataList) {
        try {
            if ((taskListDataList != null && !taskListDataList.isEmpty())) {
                Receipt_Id =String.valueOf(taskListDataList.get(0).getReceiptId());
            }
        }catch (Exception ex)
        {
        }
    }

    public void StudentPaidFeesAsync(String intTutionID, String intMonthname, String intPaidAmt, String month_Id)
    {

        try {
            progressBar = new ProgressDialog(getActivity());
            progressBar.setCancelable(false);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setMessage("loading...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            FeesDetail feesDetail=new FeesDetail(Integer.parseInt(intAcademic_id), Integer.parseInt(Schooli_id), Integer.parseInt(intstudent_id), Integer.parseInt(intStandard_id), Integer.parseInt(intPaidAmt), Integer.parseInt(intTutionID), 3, String.valueOf(intMonthname), Receipt_Id, Integer.parseInt(month_Id));
            Observable<ResponseBody> call = service.FeesInsert("InsertFee",feesDetail);
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
                    Toast.makeText(getActivity(), " Successfully done ", Toast.LENGTH_SHORT).show();


                }
            });
        } catch (Exception ex) {

        }
    }

    public void PaymentMethod()
    {
        try {
            String FinalAmount = Amounttv.getText().toString();
            Intent newPayIntent = new Intent(getActivity(), PayActivity.class);
            newPayIntent.putExtra("merchantId", "85080");
            newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be 0
            newPayIntent.putExtra("loginid", "85080");
            newPayIntent.putExtra("password", "bd48acb6");
            newPayIntent.putExtra("prodid", "GGS");
            newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be ?INR?
            newPayIntent.putExtra("clientcode", "001");
            newPayIntent.putExtra("custacc", "0954010242813");
            newPayIntent.putExtra("amt",FinalAmount);//Should be 3 decimal number i.e 51.000
            newPayIntent.putExtra("txnid", "013");
            newPayIntent.putExtra("date",CurrDateTime);//Should be in same format
            newPayIntent.putExtra("discriminator", "All"); // NB or IMPS or All ONLY (value should be same as commented)
            newPayIntent.putExtra("signature_request", "509a0a8bbc279c8fbf");
            newPayIntent.putExtra("signature_response", "5a33ebd88c251a4fcc");

            newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param");

            startActivityForResult(newPayIntent, 1);
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

}
