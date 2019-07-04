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

public class Student_Exam_Timetable extends RecyclerView.Adapter<Student_Exam_Timetable.ExamListHolder> {

      Context context;
      ArrayList<TimeTableDetail> itemsArrayList;

    public Student_Exam_Timetable(Context context, ArrayList<TimeTableDetail> itemsArrayList) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public ExamListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.student_exam_timetable, parent, false);
        return new ExamListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExamListHolder holder, final int position) {
        try
        {
            String ExamDate = itemsArrayList.get(position).getDtExaminationDate();
            String[] separated = ExamDate.split("/");
            String DAte=separated[0];
            String Month=separated[1];
            String Year=separated[2];
            holder.exam_date.setText(DAte);
            if(Month.contentEquals("01"))
            {
                holder.monthtv.setText("Jan");
            }else if(Month.contentEquals("02"))
            {
                holder.monthtv.setText("Feb");
            }else if(Month.contentEquals("03"))
            {
                holder.monthtv.setText("Mar");
            }else if(Month.contentEquals("04"))
            {
                holder.monthtv.setText("Apr");
            }else if(Month.contentEquals("05"))
            {
                holder.monthtv.setText("May");
            }else if(Month.contentEquals("06"))
            {
                holder.monthtv.setText("Jun");
            }else if(Month.contentEquals("07"))
            {
                holder.monthtv.setText("Jul");
            }else if(Month.contentEquals("08"))
            {
                holder.monthtv.setText("Aug");
            }else if(Month.contentEquals("09"))
            {
                holder.monthtv.setText("Sep");
            }else if(Month.contentEquals("10"))
            {
                holder.monthtv.setText("Oct");
            }else if(Month.contentEquals("11"))
            {
                holder.monthtv.setText("Nov");
            }else if(Month.contentEquals("12"))
            {
                holder.monthtv.setText("Dec");
            }
           else
            {
                holder.monthtv.setText(" ");
            }
            holder.yeartv.setText(Year);
            holder.exam_time.setText(itemsArrayList.get(position).getDtExaminationTime());
            holder.exam_subject.setText(itemsArrayList.get(position).getVchSubjectName());

        }catch (Exception ex)
        {

        }
    }
    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    class ExamListHolder extends RecyclerView.ViewHolder {

        TextView exam_date,exam_time,exam_subject,monthtv,yeartv;
        ExamListHolder(View itemView) {
            super(itemView);
             exam_date = (TextView) itemView.findViewById(R.id.datetv);
             exam_time = (TextView) itemView.findViewById(R.id.timetv);
             exam_subject = (TextView) itemView.findViewById(R.id.subjecttv);
            monthtv = (TextView) itemView.findViewById(R.id.monthtv);
            yeartv = (TextView) itemView.findViewById(R.id.yeartv);
        }

    }

}
