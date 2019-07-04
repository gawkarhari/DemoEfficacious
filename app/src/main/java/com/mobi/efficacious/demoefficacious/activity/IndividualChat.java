package com.mobi.efficacious.demoefficacious.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.FCMServices.FirebaseChatMainApp;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.adapters.MessageCenterAdapter;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.ChatDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class IndividualChat extends AppCompatActivity {
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public static int status = 0;
    Databasehelper mydb;
    public static RecyclerView mrecyclerView;
    public static RecyclerView.Adapter madapter;
    String ReceiverName, ReceiverId, ReceiverFCMToken, SenderName, ReceiverUserTypeId, role_id;
    ImageButton btnSend;
    EditText sendmessage;
    String message = "";
    String messagesend, Academic_id, Schooli_id, SenderId, SenderFCMToken, currentdate;
    String Standardid = "", Division_id = "", GroupName = "", Teacher_id = "", UserSelectedForNotification;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    Cursor cursor;
    ConnectionDetector cd;
    ProgressDialog progress;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_individual);
        cd = new ConnectionDetector(getApplicationContext());
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
        SenderName = settings.getString("TAG_NAME", "");
        SenderId = settings.getString("TAG_USERID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        SenderFCMToken = settings.getString("TAG_USERFIREBASETOKEN", "");
        Intent intent = getIntent();

        try {
            ReceiverFCMToken = intent.getStringExtra("ReceiverFCMToken");
            if (ReceiverFCMToken.contentEquals("Group")) {
                Standardid = intent.getStringExtra("StandardId");
                Division_id = intent.getStringExtra("DivisionId");
                GroupName = intent.getStringExtra("GroupName");
                if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                    Teacher_id = intent.getStringExtra("Teacher_ID");
                }
//                getSupportActionBar().setTitle(GroupName);
                getSupportActionBar().setTitle("Message Center");
            } else if (ReceiverFCMToken.contentEquals("Notification")) {
                UserSelectedForNotification = intent.getStringExtra("UserSelectedForNotification");
            } else {
                ReceiverName = intent.getStringExtra("ReceiverName");
                ReceiverId = intent.getStringExtra("ReceiverId");
                ReceiverUserTypeId = intent.getStringExtra("ReceiverUserTypeId");
//                getSupportActionBar().setTitle(ReceiverName);
                getSupportActionBar().setTitle(ReceiverName);
            }
        } catch (Exception ex) {
        }

        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        NumberFormat f = new DecimalFormat("00");
        currentdate = ((f.format(mDay)) + "/" + (f.format(mMonth + 1)) + "/" + mYear + "  " + date);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        sendmessage = (EditText) findViewById(R.id.editWriteMessage);
        if (role_id.contentEquals("1") || role_id.contentEquals("2") || role_id.contentEquals("3") || role_id.contentEquals("4")) {
            btnSend.setVisibility(View.GONE);
            sendmessage.setVisibility(View.GONE);
        } else {
            btnSend.setVisibility(View.VISIBLE);
            sendmessage.setVisibility(View.VISIBLE);
        }

        mydb = new Databasehelper(getApplicationContext(), "Notifications", null, 1);
        dataList = new ArrayList<HashMap<Object, Object>>();
        mrecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerview);
        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {
            try {
                if (ReceiverFCMToken.contentEquals("Group")) {
                    IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                    individualChatAsync.execute();
                } else {
                    IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                    individualChatAsync.execute();
                }
            } catch (Exception ex) {

            }
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    message = sendmessage.getText().toString();
                    messagesend = message;
                    if (message.contentEquals(" ") || message.contentEquals("")) {
                        Toast.makeText(IndividualChat.this, "Please Enter Message", Toast.LENGTH_SHORT).show();

                    } else {
                        if (ReceiverFCMToken.contentEquals("Group")) {
                            String recievermessage = "";
                            mydb.query("Insert into EILGroupChat(SenderMessage,ReciverMessage,SenderName,RecieverName,GroupName,MessageDate)values('" + message + "','" + recievermessage + "','" + SenderName + "','" + recievermessage + "','" + GroupName + "','" + currentdate + "')");
                            sendmessage.setText("");
                            if (!cd.isConnectingToInternet()) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {

                                IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                                individualChatAsync.execute();
                            }
                            if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                                if (!cd.isConnectingToInternet()) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                    alert.setMessage("No Internet Connection");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();

                                } else {

                                    GroupChatsendToTeacherAsync();
                                }
                            }
                            if (role_id.contentEquals("3") || role_id.contentEquals("1") || role_id.contentEquals("2")) {
                                if (!cd.isConnectingToInternet()) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                    alert.setMessage("No Internet Connection");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();

                                } else {

                                    GroupChatsendAsync();
                                }
                            }


                        } else if (ReceiverFCMToken.contentEquals("Notification")) {
                            String recievermessage = "";
                            mydb.query("Insert into EILGroupChat(SenderMessage,ReciverMessage,SenderName,RecieverName,GroupName,MessageDate)values('" + message + "','" + recievermessage + "','" + SenderName + "','" + recievermessage + "','" + GroupName + "','" + currentdate + "')");
                            sendmessage.setText("");
                            if (!cd.isConnectingToInternet()) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {

//                                IndividualChatAsync individualChatAsync = new IndividualChatAsync();
//                                individualChatAsync.execute();
                                NotificationMethod(UserSelectedForNotification, messagesend);
                            }
                        } else {
                            String recievermessage = "";
                            mydb.query("Insert into EILChat(SenderMessage,ReciverId,ReciverMessage,MessageDate,ReceiverUserTypeId)values('" + message + "','" + ReceiverId + "','" + recievermessage + "','" + currentdate + "','" + ReceiverUserTypeId + "')");
                            sendmessage.setText("");
                            if (!cd.isConnectingToInternet()) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();

                            } else {

                                IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                                individualChatAsync.execute();
                                IndividualChatsendAsync();
                            }
                        }

                    }
                } catch (Exception ex) {

                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }

    public void NotificationMethod(String command, String msg) {
        try {

            progress = new ProgressDialog(this);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("loading...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<ResponseBody> call = service.Notificationadmin(command, messagesend);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        progress.dismiss();
                        Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    progress.dismiss();
//                    IndividualChatAsync individualChatAsync = new IndividualChatAsync();
//                                individualChatAsync.execute();
                    Toast.makeText(IndividualChat.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } catch (Exception ex) {

        }
    }

    private class IndividualChatAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(IndividualChat.this);

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            try {

                if (ReceiverFCMToken.contentEquals("Group")) {
                    cursor = mydb.querydata("Select SenderMessage,ReciverMessage,MessageDate,RecieverName from EILGroupChat where GroupName='" + GroupName + "' order by ID desc");
                    int count = cursor.getCount();
                } else {
                    cursor = mydb.querydata("Select SenderMessage,ReciverMessage,MessageDate from EILChat where ReciverId='" + ReceiverId + "' and ReceiverUserTypeId='" + ReceiverUserTypeId + "' order by ID desc");
                    int count = cursor.getCount();
                }


                cursor.moveToFirst();
                if (cursor != null) {

                    if (cursor.moveToFirst()) {
                        do {
                            map = new HashMap<Object, Object>();
                            if (ReceiverFCMToken.contentEquals("Group")) {
                                map.put("MessageDate", cursor.getString(cursor.getColumnIndex("MessageDate")));
                                if (!cursor.getString(cursor.getColumnIndex("SenderMessage")).contentEquals("")) {
                                    map.put("SenderMessage", SenderName + "\n" + cursor.getString(cursor.getColumnIndex("SenderMessage")));
                                } else {
                                    map.put("SenderMessage", cursor.getString(cursor.getColumnIndex("SenderMessage")));
                                }
                                if (!cursor.getString(cursor.getColumnIndex("ReciverMessage")).contentEquals("")) {
                                    map.put("Message", cursor.getString(cursor.getColumnIndex("RecieverName")) + "\n" + cursor.getString(cursor.getColumnIndex("ReciverMessage")));
                                } else {
                                    map.put("Message", cursor.getString(cursor.getColumnIndex("ReciverMessage")));
                                }

                            } else {
                                map.put("MessageDate", cursor.getString(cursor.getColumnIndex("MessageDate")));
                                map.put("SenderMessage", cursor.getString(cursor.getColumnIndex("SenderMessage")));
                                map.put("Message", cursor.getString(cursor.getColumnIndex("ReciverMessage")));
                            }

                            dataList.add(map);
                        } while (cursor.moveToNext());

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                mrecyclerView.setHasFixedSize(true);
                mrecyclerView.setLayoutManager(new LinearLayoutManager(IndividualChat.this));
                madapter = new MessageCenterAdapter(dataList, "IndividualChat");
                mrecyclerView.setAdapter(madapter);
            } catch (Exception ex) {

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

    public void IndividualChatsendAsync() {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            ChatDetail chatDetail = new ChatDetail(SenderName, ReceiverName, messagesend.trim(), SenderFCMToken.trim(), Integer.parseInt(SenderId), ReceiverFCMToken, Integer.parseInt(role_id));
            Observable<ResponseBody> call = service.SendChatMessage("ChatMessage", chatDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {

                        Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {

                    Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception ex) {

        }
    }

    public void GroupChatsendAsync() {
        try {
            String command;
            if (role_id.contentEquals("3")) {
                command = "ChatGroupMessageForTeacher";

            } else {
                command = "ChatGroupMessageForStudent";
            }
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            ChatDetail chatDetail = new ChatDetail(Integer.parseInt(Schooli_id), Integer.parseInt(Academic_id), GroupName, SenderName, messagesend, 0, Integer.parseInt(SenderId), Integer.parseInt(Standardid), Integer.parseInt(Division_id));
            Observable<ResponseBody> call = service.SendChatMessage(command, chatDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {

                        Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {

                    Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception ex) {

        }
    }

    public void GroupChatsendToTeacherAsync() {
        try {
            String command;

            command = "ChatGroupMessageFromStudentToTeacher";

            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            ChatDetail chatDetail = new ChatDetail(Integer.parseInt(Schooli_id), Integer.parseInt(Academic_id), GroupName, SenderName, messagesend, Integer.parseInt(Teacher_id), Integer.parseInt(SenderId), Integer.parseInt(Standardid), Integer.parseInt(Division_id));
            Observable<ResponseBody> call = service.SendChatMessage(command, chatDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {

                        Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {

                    Toast.makeText(IndividualChat.this, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception ex) {

        }
    }

    @Override
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
