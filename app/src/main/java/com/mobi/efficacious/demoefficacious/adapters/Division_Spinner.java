package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.entity.StandardDetail;

import java.util.ArrayList;

public class Division_Spinner extends BaseAdapter {
    private final Context context;
    ArrayList<StandardDetail> menus = new ArrayList<StandardDetail>();
    ImageHolder holder = null;
    public Division_Spinner(Context context, ArrayList<StandardDetail> Menus) {
        super();
        this.context = context;
        this.menus = Menus;
    }
    static class ImageHolder
    {
        TextView textview1;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        try {
            if(row == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.spinner_item, parent, false);
                holder = new ImageHolder();
                row.setTag(holder);
            }
            else
            {
                holder = (ImageHolder)row.getTag();
            }
            holder.textview1=row.findViewById(R.id.textview1);
            holder.textview1.setText(menus.get(position).getVchDivisionName());
        }catch (Exception ex)
        {

        }

        return row;
    }



}