package com.mobi.efficacious.demoefficacious.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.DailyDiary_Tab;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.dialogbox.Diary_image_dialogBox;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DailyDiaryAdapter extends RecyclerView.Adapter<DailyDiaryAdapter.DiaryListHolder> implements Filterable {
    public ArrayList<StandardDetail> itemsArrayList;
    String Pagename;
    String Usertype,subjectName,divion_id,standard_id;
    Activity activity;
    ConnectionDetector cd;
    String messageStatus;
    String vchtype,intMy_id,int_Approval;
    private ProgressDialog progress;
    public ArrayList<StandardDetail> orig;
    public DailyDiaryAdapter(Activity activity,  ArrayList<StandardDetail> dailyDiaryHomeWorks,String pagename,String usertype) {
        this.activity = activity;
        this.itemsArrayList = dailyDiaryHomeWorks;
        this.Pagename=pagename;
        this.Usertype=usertype;
    }
    @Override
    public DiaryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dailydairy_adapter, parent, false);
        return new DiaryListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final DiaryListHolder holder, final int position) {
        cd = new ConnectionDetector(activity);
        progress = new ProgressDialog(activity);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("loading...");
        if (itemsArrayList.get(position).getVchFilePath2().contentEquals("0")) {
            holder.dowload_img2.setVisibility(View.GONE);
            holder.dwnloagIamge2.setVisibility(View.GONE);

        } else {
            holder.dwnloagIamge2.setVisibility(View.VISIBLE);
            holder.dowload_img2.setVisibility(View.VISIBLE);
            String url = RetrofitInstance.Image_URL + itemsArrayList.get(position).getVchFilePath2();
            Glide.with(activity)
                    .load(url)
                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.dowload_img2);
            //  Attachmntfile2.setText(itemsArrayList.get(position).getVchFilePath2());
        }
        if (itemsArrayList.get(position).getVchFilePath3().contentEquals("0")) {
            holder.dwnloagIamge3.setVisibility(View.GONE);
            holder.dowload_img3.setVisibility(View.GONE);
        } else {
            holder.dwnloagIamge3.setVisibility(View.VISIBLE);
            holder.dowload_img3.setVisibility(View.VISIBLE);
            String url = RetrofitInstance.Image_URL + itemsArrayList.get(position).getVchFilePath3();
            Glide.with(activity)
                    .load(url)
                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.dowload_img3);
            //Attachmntfile3.setText(itemsArrayList.get(position).getVchFilePath3());
        }


        if (itemsArrayList.get(position).getVchFilePath().contentEquals("0")) {
//            attachmntrelative.setVisibility(View.GONE);
            holder.dowload_img.setVisibility(View.GONE);
            holder.dwnloagIamge1.setVisibility(View.GONE);

        } else {
            holder.dwnloagIamge1.setVisibility(View.VISIBLE);
            holder.dowload_img.setVisibility(View.VISIBLE);
            String url = RetrofitInstance.Image_URL + itemsArrayList.get(position).getVchFilePath();
            Glide.with(activity)
                    .load(url)
                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.dowload_img);
            // Attachmntfile.setText(itemsArrayList.get(position).getVchFileName());
        }
        if (Usertype.contentEquals("student")) {
            holder.standard.setText("Teacher: " + itemsArrayList.get(position).getName());
            holder.division.setVisibility(View.GONE);
            holder.datetime.setText("Date: " + itemsArrayList.get(position).getDtDatetime());
            if (Pagename.contentEquals("HomeWork")) {
                holder.comment.setText("HomeWork: " + itemsArrayList.get(position).getVchComment());
            } else {
                holder.comment.setText("Remark: " + itemsArrayList.get(position).getVchComment());
            }
            holder.subject.setTextColor(ContextCompat.getColor(activity, R.color.black));
            holder.subject.setText("Subject: " + itemsArrayList.get(position).getVchSubjectName());

        } else if (Usertype.contentEquals("Admin")) {
            holder.namerelative.setVisibility(View.VISIBLE);
            holder.inserted_daterelative.setVisibility(View.VISIBLE);
            String url = RetrofitInstance.Image_URL + itemsArrayList.get(position).getVchProfile();
            Glide.with(activity)
                    .load(url)
                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.imageteacher);
            holder.inserted_date.setText("Inserted Date: " + itemsArrayList.get(position).getDtInsertedDatetime());
            holder.Teachername.setText("Name: " + itemsArrayList.get(position).getName());
            holder.standard.setText("Standard: " + itemsArrayList.get(position).getVchStandardName());
            holder.division.setText("Division: " + itemsArrayList.get(position).getVchDivisionName());
            holder.datetime.setText("Date: " + itemsArrayList.get(position).getDtDatetime());
            if (Pagename.contentEquals("HomeWork")) {
                holder.rejected_checkbx.setVisibility(View.VISIBLE);
                holder.approval_checkbx.setVisibility(View.VISIBLE);
                holder.Approvedtv.setVisibility(View.GONE);
                holder.rejectedtv.setVisibility(View.GONE);
                String Aprroval_status = String.valueOf(itemsArrayList.get(position).getIntApproval());
                if (Aprroval_status.contentEquals("1")) {
                    holder.editbutton.setVisibility(View.VISIBLE);
//                        holder. deletebutton.setVisibility(View.VISIBLE);
                    holder.Approvedtv.setVisibility(View.VISIBLE);
                    holder.approval_checkbx.setVisibility(View.GONE);
                    holder.rejected_checkbx.setVisibility(View.GONE);
                    holder.rejectedtv.setVisibility(View.GONE);
                } else if (Aprroval_status.contentEquals("2")) {
                    holder.editbutton.setVisibility(View.VISIBLE);
//                        holder. deletebutton.setVisibility(View.VISIBLE);
                    holder.Approvedtv.setVisibility(View.GONE);
                    holder.approval_checkbx.setVisibility(View.GONE);
                    holder.rejected_checkbx.setVisibility(View.GONE);
                    holder.rejectedtv.setVisibility(View.VISIBLE);
                } else {
                    holder.editbutton.setVisibility(View.GONE);
//                        holder. deletebutton.setVisibility(View.GONE);
                    holder.rejected_checkbx.setVisibility(View.VISIBLE);
                    holder.approval_checkbx.setVisibility(View.VISIBLE);
                    holder.rejectedtv.setVisibility(View.GONE);
                    holder.Approvedtv.setVisibility(View.GONE);
                    holder.approval_checkbx.setChecked(false);
                    holder.rejected_checkbx.setChecked(false);
                }
                holder.comment.setText("HomeWork: " + itemsArrayList.get(position).getVchComment());
            } else {
                holder.approval_checkbx.setVisibility(View.GONE);
                holder.comment.setText("Remark: " + itemsArrayList.get(position).getVchComment());
            }
            holder.subject.setTextColor(ContextCompat.getColor(activity, R.color.black));
            holder.subject.setText("Subject: " + itemsArrayList.get(position).getVchSubjectName());
        } else {
            holder.standard.setText("Standard: " + itemsArrayList.get(position).getVchStandardName());
            holder.division.setText("Division: " + itemsArrayList.get(position).getVchDivisionName());
            holder.datetime.setText("Date: " + itemsArrayList.get(position).getDtDatetime());
            if (Pagename.contentEquals("HomeWork")) {
                holder.comment.setText("HomeWork: " + itemsArrayList.get(position).getVchComment());
                String Aprroval_status = String.valueOf(itemsArrayList.get(position).getIntApproval());
                holder.Approvedtv.setVisibility(View.GONE);
                holder.approval_checkbx.setVisibility(View.GONE);
                holder.rejected_checkbx.setVisibility(View.GONE);
                holder.rejectedtv.setVisibility(View.GONE);

                if (Aprroval_status.contentEquals("1")) {
                    holder.Approvedtv.setVisibility(View.VISIBLE);
                    holder.approval_checkbx.setVisibility(View.GONE);
                    holder.rejected_checkbx.setVisibility(View.GONE);
                    holder.rejectedtv.setVisibility(View.GONE);
                } else if (Aprroval_status.contentEquals("2")) {
                    holder.Approvedtv.setVisibility(View.GONE);
                    holder.approval_checkbx.setVisibility(View.GONE);
                    holder.rejected_checkbx.setVisibility(View.GONE);
                    holder.rejectedtv.setVisibility(View.VISIBLE);

                } else {
                    holder.Approvedtv.setVisibility(View.GONE);
                    holder.approval_checkbx.setVisibility(View.GONE);
                    holder.rejected_checkbx.setVisibility(View.GONE);
                    holder.rejectedtv.setVisibility(View.GONE);
                }

            } else {
                holder.comment.setText("Remark: " + itemsArrayList.get(position).getVchComment());
            }
            holder.subject.setTextColor(ContextCompat.getColor(activity, R.color.black));
            holder.subject.setText("Subject: " + itemsArrayList.get(position).getVchSubjectName());
        }
        holder.dowload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        Intent intent = new Intent(activity, Diary_image_dialogBox.class);
                        intent.putExtra("path", itemsArrayList.get(position).getVchFilePath());
                        activity.startActivity(intent);
                    } catch (Exception ex) {

                    }

                }
            }
        });
        holder.dowload_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        Intent intent = new Intent(activity, Diary_image_dialogBox.class);
                        intent.putExtra("path", itemsArrayList.get(position).getVchFilePath2());
                        activity.startActivity(intent);
                    } catch (Exception ex) {

                    }

                }
            }
        });
        holder.dowload_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        Intent intent = new Intent(activity, Diary_image_dialogBox.class);
                        intent.putExtra("path", itemsArrayList.get(position).getVchFilePath3());
                        activity.startActivity(intent);
                    } catch (Exception ex) {

                    }

                }
            }
        });
        holder.approval_checkbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        subjectName=itemsArrayList.get(position).getVchSubjectName();
                        divion_id= String.valueOf(itemsArrayList.get(position).getIntDivisionId());
                        standard_id= String.valueOf(itemsArrayList.get(position).getIntStandardId());
                        intMy_id = String.valueOf(itemsArrayList.get(position).getIntMyId());
                        int_Approval = "1";

                    } catch (Exception ex) {

                    }

                    if (!cd.isConnectingToInternet()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        alert.setMessage("No InternetConnection");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    } else {
                        try {
                            vchtype=itemsArrayList.get(position).getVchType();
                            SumbitASYNC();
                        } catch (Exception ex) {

                        }

                    }

                } else {

                }
            }
        });
        holder.rejected_checkbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        subjectName=itemsArrayList.get(position).getVchSubjectName();
                        divion_id= String.valueOf(itemsArrayList.get(position).getIntDivisionId());
                        standard_id= String.valueOf(itemsArrayList.get(position).getIntStandardId());
                        intMy_id = String.valueOf(itemsArrayList.get(position).getIntMyId());
                        int_Approval = "2";
                        messageStatus="1";
                    } catch (Exception ex) {

                    }

                    if (!cd.isConnectingToInternet()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        alert.setMessage("No InternetConnection");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    } else {
                        try {
                            vchtype=itemsArrayList.get(position).getVchType();
                            SumbitASYNC();
                        } catch (Exception ex) {

                        }

                    }

                } else {

                }
            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String textToCopy = itemsArrayList.get(position).getVchComment();
                    if (textToCopy.length() != 0) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) // check sdk version
                        {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(textToCopy);
                            Toast.makeText(activity.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clipData = android.content.ClipData.newPlainText("Clip", textToCopy);
                            Toast.makeText(activity.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                            clipboard.setPrimaryClip(clipData);
                        }
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "Empty Selection", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {

                }

            }
        });

        holder.dwnloagIamge1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        DownloadManager downloadmanager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(RetrofitInstance.Image_URL + itemsArrayList.get(position).getVchFilePath());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(Pagename);
                        request.setDescription(itemsArrayList.get(position).getVchComment());
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, itemsArrayList.get(position).getVchFilePath());
                        downloadmanager.enqueue(request);
                    } catch (Exception ex) {

                    }

                }
            }
        });
        holder.dwnloagIamge2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        DownloadManager downloadmanager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(RetrofitInstance.Image_URL+ itemsArrayList.get(position).getVchFilePath2());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(Pagename);
                        request.setDescription(itemsArrayList.get(position).getVchComment());
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, itemsArrayList.get(position).getVchFilePath2());
                        downloadmanager.enqueue(request);
                    } catch (Exception ex) {

                    }

                }
            }
        });
        holder.dwnloagIamge3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    try {
                        DownloadManager downloadmanager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(RetrofitInstance.Image_URL+ itemsArrayList.get(position).getVchFilePath3());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(Pagename);
                        request.setDescription(itemsArrayList.get(position).getVchComment());
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, itemsArrayList.get(position).getVchFilePath3());
                        downloadmanager.enqueue(request);
                    } catch (Exception ex) {

                    }

                }
            }
        });

        holder.editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Aprroval_status = String.valueOf(itemsArrayList.get(position).getIntApproval());
                if (Aprroval_status.contentEquals("1")) {
                    holder.editbutton.setVisibility(View.GONE);
                    holder.Approvedtv.setVisibility(View.GONE);
                    holder.approval_checkbx.setVisibility(View.VISIBLE);
                    holder.rejected_checkbx.setVisibility(View.VISIBLE);
                    holder.rejectedtv.setVisibility(View.GONE);
                    messageStatus = "1";
                } else if (Aprroval_status.contentEquals("2")) {
                    holder.editbutton.setVisibility(View.GONE);
                    holder.Approvedtv.setVisibility(View.GONE);
                    holder.approval_checkbx.setVisibility(View.VISIBLE);
                    holder.rejected_checkbx.setVisibility(View.VISIBLE);
                    holder.rejectedtv.setVisibility(View.GONE);
                    messageStatus = "1";
                } else {

                }
            }
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<StandardDetail> results = new ArrayList<StandardDetail>();
                if (orig== null)
                    orig = itemsArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final StandardDetail g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                try {
                    itemsArrayList = (ArrayList<StandardDetail>) results.values;
                    notifyDataSetChanged();
                } catch (Exception ex) {

                }

            }
        };
    }
    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }


    class DiaryListHolder extends RecyclerView.ViewHolder {
        TextView standard ;
        TextView division;
        TextView datetime;
        TextView comment ;
        TextView subject;
        TextView Teachername ;
        LinearLayout attachmntrelative;
        TextView inserted_date;
        RelativeLayout namerelative;
        RelativeLayout inserted_daterelative;
        CircleImageView dowload_img ;
        CircleImageView dowload_img2;
        CircleImageView dowload_img3 ;;
        CheckBox approval_checkbx ;
        TextView Approvedtv;
        CircleImageView imageteacher;
        CheckBox rejected_checkbx ;
        TextView rejectedtv;
        ImageView dwnloagIamge1,dwnloagIamge2,dwnloagIamge3;
        ImageButton editbutton;
        DiaryListHolder(View rowView) {
            super(rowView);
            standard = (TextView)rowView.findViewById(R.id.fromdate_Leave);
            division=(TextView)rowView.findViewById(R.id.ToDate_Leave);
            datetime=(TextView)rowView.findViewById(R.id.Days_Leave);
            comment = (TextView)rowView.findViewById(R.id.Reason_Leave);
            subject = (TextView)rowView.findViewById(R.id.Approval_Leave);
            Teachername = (TextView)rowView.findViewById(R.id.Name_Leave);
            attachmntrelative=(LinearLayout)rowView.findViewById(R.id.attachmntrelative);
            inserted_date = (TextView)rowView.findViewById(R.id.Days_);
            namerelative=(RelativeLayout)rowView.findViewById(R.id.Name_relLeave);
            inserted_daterelative=(RelativeLayout)rowView.findViewById(R.id.inserted_daterelative);
            dowload_img = (CircleImageView)rowView.findViewById(R.id.downloadimg);
            dowload_img2 = (CircleImageView)rowView.findViewById(R.id.downloadimg2);
            dowload_img3 = (CircleImageView)rowView.findViewById(R.id.downloadimg3);
            approval_checkbx = (CheckBox)rowView.findViewById(R.id.approval_checkbx);
            Approvedtv=(TextView)rowView.findViewById(R.id.Approvedtv);
            imageteacher=(CircleImageView)rowView.findViewById(R.id.imageteacher);
            rejected_checkbx = (CheckBox)rowView.findViewById(R.id.reject_checkbx1);
            rejectedtv=(TextView)rowView.findViewById(R.id.rejecttv);
            inserted_daterelative.setVisibility(View.GONE);
            attachmntrelative.setVisibility(View.VISIBLE);
            approval_checkbx.setVisibility(View.GONE);
            dwnloagIamge1=(ImageView) rowView.findViewById(R.id.dwnloagIamge1);
            dwnloagIamge2=(ImageView) rowView.findViewById(R.id.dwnloagIamge2);
            dwnloagIamge3=(ImageView) rowView.findViewById(R.id.dwnloagIamge3);
            editbutton=(ImageButton) rowView.findViewById(R.id.editbutton);
        }

    }
    public void  SumbitASYNC () {
        try {
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            StandardDetail DailDAiary = new StandardDetail(messageStatus, Integer.parseInt(standard_id), Integer.parseInt(divion_id),subjectName, Integer.parseInt(int_Approval), Integer.parseInt(intMy_id));
            Observable<ResponseBody> call = service.UpdateDairyHomework("Update", DailDAiary);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    Toast.makeText(activity, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                        progress.dismiss();
                    if(Pagename.contentEquals("HomeWork")) {
                        if(int_Approval.contentEquals("1"))
                        {
                            Toast.makeText(activity, "HomeWork Approved Successfully", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(activity, "HomeWork Rejected Successfully", Toast.LENGTH_SHORT).show();
                        }
                        DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                        Bundle args = new Bundle();
                        args.putString("value", "HomeWork");
                        dailyDiary_tab.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commitAllowingStateLoss();

                    }
                    }
            });


        } catch (Exception ex) {
            progress.dismiss();
            Toast.makeText(activity, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
        }

    }
}