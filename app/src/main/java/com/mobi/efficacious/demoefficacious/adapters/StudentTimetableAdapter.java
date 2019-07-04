package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.entity.TimeTableDetail;

import java.util.ArrayList;

public class StudentTimetableAdapter  extends RecyclerView.Adapter<StudentTimetableAdapter.TimeTableListHolder> {

    Context context;
    ArrayList<TimeTableDetail> itemsArrayList;
    String PageNmae;
    public StudentTimetableAdapter(Context context, ArrayList<TimeTableDetail> itemsArrayList,String Pagenmae) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.PageNmae=Pagenmae;
    }

    @Override
    public TimeTableListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.studenttimetable_row, parent, false);
        return new TimeTableListHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimeTableListHolder holder, final int position) {
        try
        {
            holder.time.setText(itemsArrayList.get(position).getTime());
            holder.subject.setText(itemsArrayList.get(position).getVchSubjectName());
            if(PageNmae.contentEquals("Teacher"))
            {
                holder.teacher.setText(itemsArrayList.get(position).getVchStandardName());
            }else
            {
                holder.teacher.setText(itemsArrayList.get(position).getTName());
            }

        }catch (Exception ex)
        {

        }
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    class TimeTableListHolder extends RecyclerView.ViewHolder {

        TextView time,subject,teacher;
        TimeTableListHolder(View itemView) {
            super(itemView);
             time=(TextView)itemView.findViewById(R.id.PeriodTime_studenttimetable);
             subject = (TextView)itemView.findViewById(R.id.Subject_studenttimetable);
             teacher = (TextView)itemView.findViewById(R.id.Teacher_studenttimetable);
        }


    }

}


