package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 3/19/2018.
 */

public class Student_mark_Adapter extends RecyclerView.Adapter<Student_mark_Adapter.ViewHolder> {
    ArrayList<AttendanceDetail> studentList = new ArrayList<AttendanceDetail>();
    private static final String PREFRENCES_NAME = "myprefrences";
    String Selected_Date, Selected_Date1, academic_id, status, FCMToken, Schooli_id;
    int intStudent_id, intStandard_id, intDivision_id;
    ConnectionDetector cd;
    Context context;
    View itemLayoutView;
    SharedPreferences settings;

    public Student_mark_Adapter(ArrayList<AttendanceDetail> studentList, String Selected_Date, String academic_id, Context context, String SchoolId, String SelectedDate) {
        this.studentList = studentList;
        this.Selected_Date = Selected_Date;
        this.academic_id = academic_id;
        this.context = context;
        this.Schooli_id = SchoolId;
        this.Selected_Date1 = SelectedDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpater_student_attendence_mark, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        Animation animFadein6 = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slidedown);
        itemLayoutView.startAnimation(animFadein6);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);

        try {
            cd = new ConnectionDetector(context);
            holder.studentname.setText(studentList.get(position).getName());
            if (studentList.get(position).getAttstatus().contentEquals("Present")) {
                studentList.get(pos).setP_selected(true);
            } else if (studentList.get(position).getAttstatus().contentEquals("Absent")) {
                studentList.get(pos).setSelected(true);
            } else {
                studentList.get(pos).setP_selected(false);
                studentList.get(pos).setSelected(false);
            }
            holder.radio_Absent.setChecked(studentList.get(position).isSelected());
            holder.radio_Absent.setTag(studentList.get(position));
            holder.radio_Present.setChecked(studentList.get(position).isP_selected());
            holder.radio_Present.setTag(studentList.get(position));
        } catch (Exception ex) {

        }


        holder.radio_Absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RadioButton cb=(RadioButton) v;
                    AttendanceDetail contact=(AttendanceDetail)cb.getTag();
                    contact.setSelected(cb.isChecked());
                    studentList.get(pos).setSelected(cb.isChecked());
                    studentList.get(pos).setP_selected(false);
                    holder.radio_Present.setChecked(studentList.get(pos).isP_selected());
                    holder.radio_Present.setTag(studentList.get(pos));
                    intStudent_id=studentList.get(pos).getUserId();
                    intStandard_id= Integer.parseInt(studentList.get(pos).getStandardId());
                    intDivision_id=studentList.get(pos).getIntDivision_Id();
                    FCMToken=studentList.get(pos).getFCMToken();
                    status="Absent";
                    studentList.get(pos).setAttstatus("Absent");
                } catch (Exception ex) {

                }

            }
        });

        holder.radio_Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RadioButton cb1=(RadioButton) v;
                    AttendanceDetail contact1=(AttendanceDetail)cb1.getTag();
                    contact1.setP_selected(cb1.isChecked());
                    studentList.get(pos).setP_selected(cb1.isChecked());
                    studentList.get(pos).setSelected(false);
                    holder.radio_Absent.setChecked(studentList.get(pos).isSelected());
                    holder.radio_Absent.setTag(studentList.get(pos));
                    intStudent_id=studentList.get(pos).getUserId();
                    intStandard_id= Integer.parseInt(studentList.get(pos).getStandardId());
                    intDivision_id=studentList.get(pos).getIntDivision_Id();
                    FCMToken=studentList.get(pos).getFCMToken();
                    status="Present";
                    studentList.get(pos).setAttstatus("Present");
                } catch (Exception ex) {

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentname;
        RadioButton radio_Absent;
        RadioButton radio_Present;

        public ViewHolder(View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.textView13);
            radio_Absent = (RadioButton) itemView.findViewById(R.id.radio_absent);
            radio_Present = (RadioButton) itemView.findViewById(R.id.radio_present);
        }
    }

    public ArrayList<AttendanceDetail> getStudentList() {
        return studentList;
    }
}