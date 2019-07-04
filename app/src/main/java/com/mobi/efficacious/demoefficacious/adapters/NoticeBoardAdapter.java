package com.mobi.efficacious.demoefficacious.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.entity.DashboardDetail;

import java.util.ArrayList;


public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder> {
    private ArrayList<DashboardDetail> data;
    public NoticeBoardAdapter(ArrayList<DashboardDetail> dataList) {
        data = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.noticeboard_row,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        try
        {
            holder.Lastdate.setTextColor(Color.RED);
            holder.Issuedate.setText(data.get(position).getIssueDate().toString());
            holder.Lastdate.setText(data.get(position).getEndDate().toString());
            holder.Notice.setText("Notice:"+data.get(position).getNotice().toString());
            holder.Subject.setText("Subject:"+data.get(position).getSubject().toString());
        }catch (Exception ex)
        {

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Issuedate,Lastdate,Notice,Subject;
        public ViewHolder(View itemView) {
            super(itemView);
            Issuedate=(TextView)itemView.findViewById(R.id.fromdate_noticeboard);
            Lastdate=(TextView)itemView.findViewById(R.id.todate_noticeboard);
            Notice=(TextView)itemView.findViewById(R.id.message_noticeboard);
            Subject=(TextView)itemView.findViewById(R.id.title_noticeboard);
        }
    }
}
