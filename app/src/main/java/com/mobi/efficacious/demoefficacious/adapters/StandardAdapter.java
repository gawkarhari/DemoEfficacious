package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.TimetableActivity_student;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.dialogbox.Standard_division_dialog;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;
import com.mobi.efficacious.demoefficacious.fragment.StandardWise_Book;
import com.mobi.efficacious.demoefficacious.fragment.StudentExamFragment;
import com.mobi.efficacious.demoefficacious.fragment.StudentSyllabusFragment;

import java.util.ArrayList;

public class StandardAdapter extends RecyclerView.Adapter<StandardAdapter.StandardListHolder> {

    private ArrayList<StandardDetail> dataList;
    private final Context mcontext;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id;
    public static String Std_id_division, std_name_division, page_name, std_id, intSchool_id;
    private String pagename;
    public StandardAdapter(ArrayList<StandardDetail> dataList, Context context, String value) {
        this.dataList = dataList;
        this.mcontext = context;
        this.pagename = value;
    }

    @Override
    public StandardListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.standard_row, parent, false);
        return new StandardListHolder(view);
    }

    @Override
    public void onBindViewHolder(final StandardListHolder holder, final int position) {
        try {
            settings = mcontext.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            try {
                holder.id.setText(String.valueOf(dataList.get(position).getIntStandardId()));
                holder.name.setText(dataList.get(position).getVchStandardName());
            } catch (Exception ex) {

            }
        } catch (Exception ex) {

        }
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (pagename.equalsIgnoreCase("Exam")) {

                    try {
                        StudentExamFragment studentExamActivity = new StudentExamFragment();
                        Bundle args = new Bundle();
                        args.putString("std_id", String.valueOf(dataList.get(position).getIntStandardId()));
                        if (role_id.contentEquals("6") || role_id.contentEquals("7") || role_id.contentEquals("3")) {
                            args.putString("intSchool_id", String.valueOf(dataList.get(position).getIntschoolId()));
                        }
                        studentExamActivity.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentExamActivity).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }
                } else if (pagename.equalsIgnoreCase("Syllabus")) {
                    try {
                        StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                        Bundle args = new Bundle();
                        args.putString("std_id", String.valueOf(dataList.get(position).getIntStandardId()));
                        subjectFragment.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, subjectFragment).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }
                } else if (pagename.equalsIgnoreCase("Std")) {
                    try {
                        page_name = "Standarad_division";
                        Std_id_division = String.valueOf(dataList.get(position).getIntStandardId());
                        std_name_division = dataList.get(position).getVchStandardName();
                        if (role_id.contentEquals("6") || role_id.contentEquals("7") || role_id.contentEquals("3")) {
                            intSchool_id = String.valueOf(dataList.get(position).getIntschoolId());
                        }
                        Intent intent = new Intent(mcontext, Standard_division_dialog.class);
                        intent.putExtra("std_id", String.valueOf(dataList.get(position).getIntStandardId()));
                        intent.putExtra("std_name", dataList.get(position).getVchStandardName());
                        intent.putExtra("selected_layout", "Stdwise_name");
                        mcontext.startActivity(intent);
                    } catch (Exception ex) {

                    }


                } else if (pagename.equalsIgnoreCase("timetable")) {
                    try {
                        TimetableActivity_student timetableActivity_student = new TimetableActivity_student();
                        Bundle args = new Bundle();
                        args.putString("std_id", String.valueOf(dataList.get(position).getIntStandardId()));
                        if (role_id.contentEquals("6") || role_id.contentEquals("7") || role_id.contentEquals("3")) {
                            intSchool_id = String.valueOf(dataList.get(position).getIntschoolId());
                        }
                        timetableActivity_student.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, timetableActivity_student).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }

                } else if (pagename.equalsIgnoreCase("LibraryTeacher")) {
                    try {
                        std_id = String.valueOf(dataList.get(position).getIntStandardId());
                        StandardWise_Book standardWise_book = new StandardWise_Book();
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, standardWise_book).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }

                } else if (pagename.equalsIgnoreCase("Standarad_Result")) {
                    try {
                        page_name = "Standarad_Result";
                        String stn1 = dataList.get(position).getVchStandardName();
                        Std_id_division = String.valueOf(dataList.get(position).getIntStandardId());
                        std_name_division = dataList.get(position).getVchStandardName();
                        Intent intent = new Intent(mcontext, Standard_division_dialog.class);
                        intent.putExtra("std_id", String.valueOf(dataList.get(position).getIntStandardId()));
                        intent.putExtra("std_name", dataList.get(position).getVchStandardName());
                        intent.putExtra("selected_layout", "Stdwise_name");
                        mcontext.startActivity(intent);
                    } catch (Exception ex) {

                    }

                }
                else {
                    try {
                        StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                        Bundle args = new Bundle();
                        args.putString("std_id", String.valueOf(dataList.get(position).getIntStandardId()));
                        subjectFragment.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, subjectFragment).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class StandardListHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        RelativeLayout linear;
        StandardListHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.id_standard);
            name = (TextView) itemView.findViewById(R.id.name_standard);
            linear = (RelativeLayout) itemView.findViewById(R.id.Linear);
        }


    }

}