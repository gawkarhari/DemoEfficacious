package com.mobi.efficacious.demoefficacious.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.database.Databasehelper;
import com.mobi.efficacious.demoefficacious.entity.AddToFee;
import com.mobi.efficacious.demoefficacious.fragment.Fees_PaymentFragment;
import com.mobi.efficacious.demoefficacious.fragment.Monthly_Fees_Fragment;

import java.util.ArrayList;

public class AddToFeeRecyclerview extends RecyclerView.Adapter<AddToFeeRecyclerview.ViewHolder> {
    ArrayList<AddToFee> addToFees = new ArrayList<AddToFee>();
    Databasehelper mydb;
    Activity activity;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    Cursor recycleview_cursor;

    public AddToFeeRecyclerview(ArrayList<AddToFee> addToFees, Activity activity) {
        this.addToFees = addToFees;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_fee_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        mydb = new Databasehelper(parent.getContext(), "Fees_collection", null, 1);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;

        String IntTutionID=addToFees.get(pos).getIntTutionID();
        if(IntTutionID.contentEquals("3"))
        {
            holder.TutionId.setText("Monthly");
        }else if(IntTutionID.contentEquals("2"))
        {
            holder.TutionId.setText("Session");
        }else
        {
            holder.TutionId.setText("Admission");
        }
        holder.Month_name.setText(addToFees.get(position).getIntMonth());
        holder.Amount.setText(addToFees.get(position).getSumAmount());
        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Month_name=addToFees.get(pos).getIntMonth();
                String Month_id=addToFees.get(pos).getIntMonth_ID();
                String TutionId=addToFees.get(pos).getIntTutionID();
                if(TutionId.contentEquals("3"))
                {
                    mydb.query("Delete from tblfeecollection1 where intMonth_ID='"+Month_id+"' and intMonth='"+Month_name+"' and intTutionID='"+TutionId+"' ");
                }else if(TutionId.contentEquals("2"))
                {
                    mydb.query("Delete from tblfeecollection1 where intTutionID='"+TutionId+"' ");
                }else
                {
                    mydb.query("Delete from tblfeecollection1 where intTutionID='"+TutionId+"' ");
                }
                if(TutionId.contentEquals("3"))
                {
                    switch (Integer.parseInt(Month_id))
                    {
                        case 12: Monthly_Fees_Fragment.May_cb.setChecked(false);
                            break;
                        case 11:Monthly_Fees_Fragment.April_cb.setChecked(false);
                            break;
                        case 10:Monthly_Fees_Fragment.March_cb.setChecked(false);
                            break;
                        case 9:Monthly_Fees_Fragment.February_cb.setChecked(false);
                            break;
                        case 8:Monthly_Fees_Fragment.January_cb.setChecked(false);
                            break;
                        case 7:Monthly_Fees_Fragment.December_cb.setChecked(false);
                            break;
                        case 6:Monthly_Fees_Fragment.November_cb.setChecked(false);
                            break;
                        case 5:Monthly_Fees_Fragment.October_cb.setChecked(false);
                            break;
                        case 4:Monthly_Fees_Fragment.September_cb.setChecked(false);
                            break;
                        case 3:Monthly_Fees_Fragment.August_cb.setChecked(false);
                            break;
                        case 2:Monthly_Fees_Fragment.July_cb.setChecked(false);
                            break;
                        case 1:Monthly_Fees_Fragment.June_cb.setChecked(false);
                            break;

                    }
                }

                AddToFeeCartAsync addToFeeCartAsync=new AddToFeeCartAsync();
                addToFeeCartAsync.execute();

            }
        });

    }

    @Override
    public int getItemCount() {
        return addToFees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Month_name;
        TextView Amount;
        TextView TutionId;
        Button deletebutton;
        public AddToFee singlestaff;

        public ViewHolder(View itemView) {
            super(itemView);
            Month_name = (TextView) itemView.findViewById(R.id.Month_name);
            Amount = (TextView) itemView.findViewById(R.id.Amount);
            TutionId=(TextView)itemView.findViewById(R.id.tvMonthly);
            deletebutton = (Button) itemView.findViewById(R.id.delete_button);
        }
    }

    private class AddToFeeCartAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(activity);
        int amount=0;
        protected Void doInBackground(Void... params) {
            try
            {

                addToFees.clear();
                recycleview_cursor =mydb.querydata("Select intMonth_ID,intTutionID,intMonth,SumAmount from tblfeecollection1 ");
                int count=recycleview_cursor.getCount();


                recycleview_cursor.moveToFirst();
                if (recycleview_cursor != null) {

                    if (recycleview_cursor.moveToFirst()) {
                        do {


                            String intTutionID=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intTutionID"));
                            String intMonth=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intMonth"));
                            String SumAmount=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("SumAmount"));
                            String intMonth_ID=recycleview_cursor.getString(recycleview_cursor.getColumnIndex("intMonth_ID"));
                            amount=amount+Integer.parseInt(SumAmount);
                            addToFees.add(new AddToFee(intTutionID,intMonth,SumAmount,intMonth_ID));

                        } while (recycleview_cursor.moveToNext());

                    }

                }

            }
            catch (Exception e)
            {
                addToFees.add(new AddToFee("","No Data Available","",""));
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Fees_PaymentFragment.Amounttv.setText(String.valueOf(amount));
            notifyDataSetChanged();
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }
    }
}
