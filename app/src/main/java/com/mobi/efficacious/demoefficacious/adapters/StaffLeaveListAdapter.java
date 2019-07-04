package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.dialogbox.image_zoom_dialog;
import com.mobi.efficacious.demoefficacious.entity.LeaveDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffLeaveListAdapter extends RecyclerView.Adapter<StaffLeaveListAdapter.LeaveHolder> {

    private ArrayList<LeaveDetail> itemsArrayList;
    private Context mcontext;

    String LeaveApplication_id;
    public StaffLeaveListAdapter(ArrayList<LeaveDetail> dataList,Context context) {
        this.itemsArrayList = dataList;
        this.mcontext = context;

    }

    @Override
    public LeaveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_teacherleave, parent, false);
        return new LeaveHolder(view);
    }

    @Override
    public void onBindViewHolder(final LeaveHolder holder, final int position) {

        try {
            holder.clMlcount.setText("CL :" + itemsArrayList.get(position).getIntCL() + " ML :" + itemsArrayList.get(position).getIntML());
            LeaveApplication_id = String.valueOf(itemsArrayList.get(position).getIntLeaveApplocationId());
            holder.from_todate.setText("Date: " + itemsArrayList.get(position).getDtFromDate()+" - "+ itemsArrayList.get(position).getDtToDate());
//            holder.toDate.setText("Days: " + itemsArrayList.get(position).getIntTotalDays() + "(" + itemsArrayList.get(position).getVchLeaveType() + ")");
            holder.reasontv.setText("Reason: " + itemsArrayList.get(position).getVchReason());
            holder.nametv.setText(itemsArrayList.get(position).getName());
            String url = RetrofitInstance.Image_URL + itemsArrayList.get(position).getVchProfile();
            Glide.with(mcontext)
                    .load(url)
                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.fabprofil);

            if (itemsArrayList.get(position).getBitAdminApproval().contentEquals("Rejected")) {
                Glide.with(mcontext)
                        .load(R.drawable.rejected)
                        .fitCenter()// image url
                        .error(R.drawable.rejected)
                        .into(holder.fabapprove);

                holder.nametv.setTextColor(mcontext.getResources().getColor(R.color.darkred));
            } else {
                holder.nametv.setTextColor(mcontext.getResources().getColor(R.color.darkgreen));
                Glide.with(mcontext)
                        .load(R.drawable.approve)
                        .fitCenter()// image url
                        .error(R.drawable.approve)
                        .into(holder.fabapprove);

            }
            holder.fabprofil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mcontext, image_zoom_dialog.class);
                    intent.putExtra("path",itemsArrayList.get(position).getVchProfile());
                    mcontext.startActivity(intent);
                }
            });
        } catch (Exception ex) {

        }

    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    class LeaveHolder extends RecyclerView.ViewHolder {
        TextView nametv,reasontv,from_todate,clMlcount;
        CircleImageView fabapprove,fabprofil;
        LeaveHolder(View rowView) {
            super(rowView);
            nametv=(TextView)rowView.findViewById(R.id.nametv);
            reasontv=(TextView)rowView.findViewById(R.id.reasontv);
            from_todate=(TextView)rowView.findViewById(R.id.from_todate);
            clMlcount=(TextView)rowView.findViewById(R.id.clMlcount);
            fabapprove=(CircleImageView) rowView.findViewById(R.id.fabapprove);
            fabprofil=(CircleImageView)rowView.findViewById(R.id.fabprofil);
        }

    }

}