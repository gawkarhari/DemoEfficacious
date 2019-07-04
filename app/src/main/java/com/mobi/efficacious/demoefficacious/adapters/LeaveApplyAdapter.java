package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.entity.LeaveDetail;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaveApplyAdapter  extends RecyclerView.Adapter<LeaveApplyAdapter.ListHolder> {

    private ArrayList<LeaveDetail> dataList;
    private final Context mcontext;
    String ApprovalStatus;
    public LeaveApplyAdapter(ArrayList<LeaveDetail> dataList, Context context) {
        this.dataList = dataList;
        this.mcontext = context;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.leave_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        try {
            ApprovalStatus=dataList.get(position).getBitAdminApproval().trim();
            if(ApprovalStatus.contentEquals("0"))
            {
                Glide.with(mcontext)
                        .load(R.drawable.pending)
                        .fitCenter()// image url
                        .error(R.drawable.pending)
                        .into(holder.fabapprove);
            }else if(ApprovalStatus.contentEquals("1"))
            {
                Glide.with(mcontext)
                        .load(R.drawable.approve)
                        .fitCenter()// image url
                        .error(R.drawable.approve)
                        .into(holder.fabapprove);
            }else
            {
                Glide.with(mcontext)
                        .load(R.drawable.rejected)
                        .fitCenter()// image url
                        .error(R.drawable.rejected)
                        .into(holder.fabapprove);
            }
            holder.datetv.setText("Date: " + dataList.get(position).getDtFromDate()+" - "+ dataList.get(position).getDtToDate());
            holder.reasontv.setText("Reason: " + dataList.get(position).getVchReason());
            holder.clMlcount.setText("Days :" + dataList.get(position).getIntTotalDays());
        } catch (Exception ex) {

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {

        TextView datetv,reasontv,clMlcount;
        CircleImageView fabapprove;
        ListHolder(View rowView) {
            super(rowView);
            datetv=(TextView)rowView.findViewById(R.id.datetv);
            reasontv=(TextView)rowView.findViewById(R.id.reasontv);
            clMlcount=(TextView)rowView.findViewById(R.id.clMlcount);
            fabapprove=(CircleImageView) rowView.findViewById(R.id.fabapprove);
        }


    }

}
