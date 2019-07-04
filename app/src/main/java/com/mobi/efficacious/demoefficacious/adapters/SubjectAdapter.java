package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.SyllabusDetail;
import com.mobi.efficacious.demoefficacious.fragment.SyllabusDetailFragment;

import java.util.ArrayList;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectListHolder> {

    private ArrayList<SyllabusDetail> dataList;
    private Context mcontext;
    ConnectionDetector cd;
    String PageName,Url;
    public SubjectAdapter(Context context,ArrayList<SyllabusDetail> dataList,String Pagename) {
        this.dataList = dataList;
        this.mcontext = context;
        this.PageName=Pagename;
    }

    @Override
    public SubjectListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.subject_row, parent, false);
        return new SubjectListHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubjectListHolder holder, final int position) {
        try {

            if(PageName.contentEquals("SubjectName"))
            {
                holder.name.setText(dataList.get(position).getVchSubjectName());
            }else
            {
                holder.name.setText(dataList.get(position).getVchTopicName());
            }

        } catch (Exception ex) {

        }
        holder.syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(mcontext.getApplicationContext());
                if (!cd.isConnectingToInternet()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
                    alert.setMessage("No InternetConnection");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                } else {
                    if(PageName.contentEquals("SubjectName"))
                    {
                        try {

                            SyllabusDetailFragment syllabusDetailFragment = new SyllabusDetailFragment();
                            Bundle args = new Bundle();
                            args.putString("sub_id", String.valueOf(dataList.get(position).getIntSubjectId()));
                            args.putString("stand_id", String.valueOf(dataList.get(position).getIntSTDId()));
                            syllabusDetailFragment.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, syllabusDetailFragment).commitAllowingStateLoss();


                        } catch (Exception ex) {

                        }
                    }else if(PageName.contentEquals("SyllabusDetail"))
                    {
                        try
                        {
                            Url="http://eserveshiksha.co.in/Trafford/pdf/"+dataList.get(position).getSyllabusurl();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                            mcontext.startActivity(browserIntent);
                        }catch (Exception ex)
                        {

                        }

                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class SubjectListHolder extends RecyclerView.ViewHolder {

        TextView name;
        LinearLayout syllabus;
        SubjectListHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.subName_subject);
            syllabus = (LinearLayout) itemView.findViewById(R.id.linearsubjt);
        }


    }

}

