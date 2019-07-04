package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;

import java.util.ArrayList;


/**
 * Created by EFF on 2/22/2017.
 */

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {
    private ArrayList<AttendanceDetail> data;
    private final Context mcontext;
    public StudentAttendanceAdapter(Context context,ArrayList<AttendanceDetail> dataList) {
        data = dataList;
        mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.studentattendance_row,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }



    public void onBindViewHolder(final ViewHolder holder, int position) {
        try
        {
            if(data.get(position).getStatus().contentEquals("Absent"))
            {
                holder.date.setTextColor(mcontext.getResources().getColorStateList(R.color.darkred));
                holder.status.setTextColor(mcontext.getResources().getColorStateList(R.color.darkred));

            }else
            {
                holder.date.setTextColor(mcontext.getResources().getColorStateList(R.color.darkgreen));
                holder.status.setTextColor(mcontext.getResources().getColorStateList(R.color.darkgreen));
            }
            holder.date.setText(data.get(position).getDtdate());
            holder.status.setText(data.get(position).getStatus());
        }catch (Exception ex)
        {

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,status;
        public ViewHolder(View itemView) {
            super(itemView);
             date = (TextView) itemView.findViewById(R.id.date_studentattendance);
             status = (TextView) itemView.findViewById(R.id.status_studentattendance);
        }
    }
}
