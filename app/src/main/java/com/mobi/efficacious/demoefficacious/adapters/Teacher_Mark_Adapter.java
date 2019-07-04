package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.AttendanceDetail;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 3/16/2018.
 */

public class Teacher_Mark_Adapter extends RecyclerView.Adapter<Teacher_Mark_Adapter.ViewHolder> {
    public ArrayList<AttendanceDetail> tchrList = new ArrayList<AttendanceDetail>();
    Context context;
    public String Selected_Date,academic_id,Selected_Date1,status,FCMToken;
    int intTeacherid;
    ConnectionDetector cd;
    public Teacher_Mark_Adapter(ArrayList<AttendanceDetail> teachersList, String Selected_Date, String academic_id, Context context, String SelectedDate) {
        this.tchrList=teachersList;
        this.Selected_Date=Selected_Date;
        this.academic_id=academic_id;
        this.context=context;
        this.Selected_Date1=SelectedDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_teacher_attendence_mark,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final  int pos=position;
        try
        {
            cd = new ConnectionDetector(context);


            holder.tchrname.setText(tchrList.get(position).getName());
            if (tchrList.get(position).getAttstatus().contentEquals("Present")) {
                tchrList.get(pos).setP_selected(true);
            } else if (tchrList.get(position).getAttstatus().contentEquals("Absent")) {
                tchrList.get(pos).setSelected(true);
            } else {
                tchrList.get(pos).setP_selected(false);
                tchrList.get(pos).setSelected(false);
            }

            holder.radio_Absent.setChecked(tchrList.get(position).isSelected());
            holder.radio_Absent.setTag(tchrList.get(position));
            holder.radio_Present.setChecked(tchrList.get(position).isP_selected());
            holder.radio_Present.setTag(tchrList.get(position));
        }catch (Exception ex)
        {

        }


        holder.radio_Absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    RadioButton cb=(RadioButton) v;
                    AttendanceDetail contact=(AttendanceDetail)cb.getTag();
                    contact.setSelected(cb.isChecked());
                    tchrList.get(pos).setSelected(cb.isChecked());
                    tchrList.get(pos).setP_selected(false);
                    holder.radio_Present.setChecked(tchrList.get(pos).isP_selected());
                    holder.radio_Present.setTag(tchrList.get(pos));
                    intTeacherid=tchrList.get(pos).getUserId();
                    FCMToken=tchrList.get(pos).getFCMToken();
                    tchrList.get(pos).setAttstatus("Absent");
                    status="Absent";

                }catch (Exception ex)
                {

                }

            }
        });

        holder.radio_Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    RadioButton cb1=(RadioButton) v;
                    AttendanceDetail contact1=(AttendanceDetail) cb1.getTag();
                    contact1.setP_selected(cb1.isChecked());
                    tchrList.get(pos).setP_selected(cb1.isChecked());
                    tchrList.get(pos).setSelected(false);
                    holder.radio_Absent.setChecked(tchrList.get(pos).isSelected());
                    holder.radio_Absent.setTag(tchrList.get(pos));
                    intTeacherid=tchrList.get(pos).getUserId();
                    FCMToken=tchrList.get(pos).getFCMToken();
                    tchrList.get(pos).setAttstatus("Present");
                    status="Present";
                }catch (Exception ex)
                {

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return tchrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tchrname;
        TextView tchrdesignation;
        RadioButton radio_Absent;
        RadioButton radio_Present;
        public AttendanceDetail singleteacher;
        public ViewHolder(View itemView) {
            super(itemView);
            tchrname=(TextView)itemView.findViewById(R.id.teachername);
            radio_Absent=(RadioButton)itemView.findViewById(R.id.radio_absent);
            radio_Present=(RadioButton)itemView.findViewById(R.id.radio_present);
        }
    }
    public ArrayList<AttendanceDetail> getTchrList()
    {
        return tchrList;
    }

}