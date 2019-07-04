package com.mobi.efficacious.demoefficacious.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.NoticeBoardAdapterr;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;

import java.util.ArrayList;
import java.util.HashMap;


public class NoticeboardActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    Databasehelper mydb;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    HashMap<Object, Object> map;
    ArrayList<HashMap<Object, Object>> dataList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);


        mrecyclerView=(RecyclerView)findViewById(R.id.chat_recyclerview);
        mydb=new Databasehelper(NoticeboardActivity.this,"Notifications",null,1);

        dataList = new ArrayList<HashMap<Object, Object>>();
        try
        {
            MessageCenterAsync messageCenterAsync=new MessageCenterAsync();
            messageCenterAsync.execute();
        }catch (Exception ex)
        {

        }
        }

    private class MessageCenterAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(NoticeboardActivity.this);
        @Override
        protected Void doInBackground(Void... params) {

            try
            {
                Cursor  cursor =mydb.querydata("Select Subject,Notice,IssueDate,LastDate from NoticeBoard order by ID desc");
                int count=cursor.getCount();
                if(count==0)
                {
                    map = new HashMap<Object, Object>();
                    map.put("Subject","");
                    map.put("IssueDate","");
                    map.put("LastDate","");
                    map.put("Notice","No Data Available");
                    dataList.add(map);
                }


                cursor.moveToFirst();
                if (cursor != null) {

                    if (cursor.moveToFirst()) {
                        do {
                            map = new HashMap<Object, Object>();
                            map.put("Subject",cursor.getString(cursor.getColumnIndex("Subject")));
                            map.put("Notice",cursor.getString(cursor.getColumnIndex("Notice")));
                            map.put("IssueDate",cursor.getString(cursor.getColumnIndex("IssueDate")));
                            map.put("LastDate",cursor.getString(cursor.getColumnIndex("LastDate")));
                            dataList.add(map);
                        } while (cursor.moveToNext());

                    }

                }

            }
            catch (Exception e)
            {
                map = new HashMap<Object, Object>();
                map.put("Subject","");
                map.put("IssueDate","");
                map.put("LastDate","");
                map.put("Notice","No Data Available");
                dataList.add(map);

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try
            {
                mrecyclerView.setHasFixedSize(true);
                mrecyclerView.setLayoutManager(new LinearLayoutManager(NoticeboardActivity.this));
                madapter=new NoticeBoardAdapterr(dataList);
                mrecyclerView.setAdapter(madapter);
            }catch (Exception ex)
            {

            }

            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }
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