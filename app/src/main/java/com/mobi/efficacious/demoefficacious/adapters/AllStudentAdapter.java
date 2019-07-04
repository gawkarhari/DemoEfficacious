package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.entity.StudentStandardwiseDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllStudentAdapter extends RecyclerView.Adapter<AllStudentAdapter.StudentListHolder> implements Filterable {

    private ArrayList<StudentStandardwiseDetail> dataList;
    private final Context mcontext;
    String pagename, role_id;
    private static final String PREFRENCES_NAME = "myprefrences";
    public ArrayList<StudentStandardwiseDetail> orig;
    SharedPreferences settings;
    public AllStudentAdapter(ArrayList<StudentStandardwiseDetail> dataList, Context context, String value) {
        this.dataList = dataList;
        this.mcontext = context;
        this.pagename = value;
    }

    @Override
    public StudentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.allstudent_row, parent, false);
        return new StudentListHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudentListHolder holder, final int position) {
        try {
            settings = mcontext.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            holder.name.setText(dataList.get(position).getName());
            String url= RetrofitInstance.Image_URL+dataList.get(position).getVchProfile();
            try
            {
                holder.student_image.setVisibility(View.VISIBLE);
                Glide.with(mcontext)
                        .load(url)
                        .fitCenter()// image url
                        .error(R.mipmap.profile)
                        .into(holder.student_image);
            }catch (Exception ex)
            {
                ex.getMessage();
            }
            holder.student_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int st = dataList.get(position).getStudentId();
                    if (pagename.contentEquals("Result")) {

                        if (role_id.contentEquals("3")) {
                            try {
//                                Result_Tab studentResultFragment = new Result_Tab();
//                                Bundle args = new Bundle();
//                                args.putString("Div_name", dataList.get(position).getDivision_name());
//                                args.putString("Std_name", dataList.get(position).getStandrad_name());
//                                args.putInt("Div_id", dataList.get(position).getDivision_id());
//                                args.putInt("Std_id", dataList.get(position).getStandard_id());
//                                args.putString("Stud_name", dataList.get(position).getName());
//                                args.putInt("stud_id12", dataList.get(position).getStudent_id());
//                                studentResultFragment.setArguments(args);
//                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentResultFragment).commitAllowingStateLoss();

                            } catch (Exception ex) {

                            }

                        } else {
                            try {
//                                StudentResultFragment studentResultFragment = new StudentResultFragment();
//                                Bundle args = new Bundle();
//                                args.putString("Div_name", dataList.get(position).getDivision_name());
//                                args.putString("Std_name", dataList.get(position).getStandrad_name());
//                                args.putInt("Div_id", dataList.get(position).getDivision_id());
//                                args.putInt("Std_id", dataList.get(position).getStandard_id());
//                                args.putString("Stud_name", dataList.get(position).getName());
//                                args.putInt("stud_id12", dataList.get(position).getStudent_id());
//                                studentResultFragment.setArguments(args);
//                                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentResultFragment).commitAllowingStateLoss();

                            } catch (Exception ex) {

                            }
                        }
                    } else {
                        try {
                            Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                            Bundle args = new Bundle();
                            args.putString("Stud_name", dataList.get(position).getName());
                            args.putInt("stud_id12", dataList.get(position).getStudentId());
                            args.putString("intSchool_id", String.valueOf(dataList.get(position).getIntSchoolId()));
                            args.putString("attendence", "student_attendence");
                            attendence_sliding_tab.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();

                        } catch (Exception ex) {

                        }

                    }

                }
            });
        } catch (Exception ex) {

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<StudentStandardwiseDetail> results = new ArrayList<StudentStandardwiseDetail>();
                if (orig == null)
                    orig = dataList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final StudentStandardwiseDetail g : orig) {
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
                    dataList = (ArrayList<StudentStandardwiseDetail>) results.values;
                    notifyDataSetChanged();
                } catch (Exception ex) {

                }

            }
        };
    }

    class StudentListHolder extends RecyclerView.ViewHolder {

        CircleImageView student_image;
        TextView id;
        TextView name;
        RelativeLayout linear;
        StudentListHolder(View itemView) {
            super(itemView);
            student_image = itemView.findViewById(R.id.student_image);
            name = (TextView) itemView.findViewById(R.id.name_allstudent);
            linear = (RelativeLayout) itemView.findViewById(R.id.linear_allstudent);

        }


    }

}