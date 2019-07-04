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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.demoefficacious.Tab.TimetableActivity_teacher;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.entity.TeacherDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by EFF-4 on 9/3/2017.
 */

public class AllTeacherAdapter  extends RecyclerView.Adapter<AllTeacherAdapter.TeacherListHolder> implements Filterable {

    private final Context mcontext;
    ArrayList<TeacherDetail> menus = new ArrayList<TeacherDetail>();
    public ArrayList<TeacherDetail> categories;
    public ArrayList<TeacherDetail> orig;
    String url = "";
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String page_selected, role_id;
    public AllTeacherAdapter(Context context, ArrayList<TeacherDetail> Menus, String page) {
        this.mcontext = context;
        this.menus = Menus;
        this.page_selected = page;
    }

    @Override
    public TeacherListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.allteacher_name, parent, false);
        return new TeacherListHolder(view);
    }

    @Override
    public void onBindViewHolder(final TeacherListHolder holder, final int position) {
        try {
            settings = mcontext.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            holder.id.setText(menus.get(position).getDesignation());
            holder.name.setText(menus.get(position).getName());

            holder.teacher_image.setVisibility(View.VISIBLE);
            String url = RetrofitInstance.Image_URL + menus.get(position).getVchProfile();
            Glide.with(mcontext)
                    .load(url)
                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.teacher_image);

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_selected.contentEquals("attendence")) {
                    try {
                        Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                        Bundle args = new Bundle();
                        args.putString("teachername", menus.get(position).getName());
                        args.putString("designation", menus.get(position).getDesignation());
                        args.putString("teacher_id", String.valueOf(menus.get(position).getTeacherId()));
                        if (role_id.contentEquals("6") || role_id.contentEquals("7") || role_id.contentEquals("3")) {
                            args.putString("intSchool_id", String.valueOf(menus.get(position).getIntSchoolId()));
                        }
                        args.putString("attendence", "teacher_attendence");
                        attendence_sliding_tab.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commitAllowingStateLoss();

                    } catch (Exception ex) {

                    }

                } else {
                    try {
                        TimetableActivity_teacher timetableActivity_teacher = new TimetableActivity_teacher();
                        Bundle args = new Bundle();
                        if (role_id.contentEquals("6") || role_id.contentEquals("7")) {
                            args.putString("intSchool_id", String.valueOf(menus.get(position).getIntSchoolId()));
                        }
                        args.putString("teacher_id", String.valueOf(menus.get(position).getTeacherId()));
                        timetableActivity_teacher.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, timetableActivity_teacher).commitAllowingStateLoss();

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
        return menus.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<TeacherDetail> results = new ArrayList<TeacherDetail>();
                if (orig == null)
                    orig = menus;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final TeacherDetail g : orig) {
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
                    menus = (ArrayList<TeacherDetail>) results.values;
                    notifyDataSetChanged();
                } catch (Exception ex) {

                }

            }
        };
    }

    class TeacherListHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        CircleImageView teacher_image;
        LinearLayout linear;
        TeacherListHolder(View itemView) {
            super(itemView);
            teacher_image = (CircleImageView)itemView.findViewById(R.id.teacher_image);
            id = (TextView) itemView.findViewById(R.id.Dept_allteacher1);
            name = (TextView) itemView.findViewById(R.id.name_allteacher1);
            linear = (LinearLayout) itemView.findViewById(R.id.Linear_allteacher1);

        }


    }

}