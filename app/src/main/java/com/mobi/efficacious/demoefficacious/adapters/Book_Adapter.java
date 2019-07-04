package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.dialogbox.Book_Details_dialog;
import com.mobi.efficacious.demoefficacious.entity.LibraryDetail;

import java.util.ArrayList;

public class Book_Adapter extends RecyclerView.Adapter<Book_Adapter.ViewHolder>  implements Filterable {
    private ArrayList<LibraryDetail> data;
    public ArrayList<LibraryDetail> orig;
    Context mcontext;
    public Book_Adapter(ArrayList<LibraryDetail> dataList, Context context) {
        data = dataList;
        mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_detail_library,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }



    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try
        {
            holder.book_name.setText(data.get(position).getVchBookDetailsBookName());
        }catch (Exception ex)
        {

        }
        holder.relativebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent(mcontext,Book_Details_dialog.class);
                    intent.putExtra("AccessionNo",data.get(position).getVchAccessionNo());
                    intent.putExtra("BookName",data.get(position).getVchBookDetailsBookName());
                    intent.putExtra("AuthorName",data.get(position).getIntBookAuthorId());
                    intent.putExtra("Edition",data.get(position).getIntBookEditionId());
                    intent.putExtra("Language",data.get(position).getIntBookLanguageId());
                    intent.putExtra("Price",String.valueOf(data.get(position).getIntBookPrice()));
                   mcontext.startActivity(intent);
                } catch (Exception ex) {

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<LibraryDetail> results = new ArrayList<LibraryDetail>();
                if (orig == null)
                    orig = data;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final LibraryDetail g : orig) {
                            if (g.getVchBookDetailsBookName().toLowerCase()
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
                    data = (ArrayList<LibraryDetail>) results.values;
                    notifyDataSetChanged();
                } catch (Exception ex) {

                }

            }
        };
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name;
        RelativeLayout relativebook;
        public ViewHolder(View itemView) {
            super(itemView);
            book_name=(TextView)itemView.findViewById(R.id.Book_nametv);
            relativebook=(RelativeLayout)itemView.findViewById(R.id.relativebook);
        }
    }
}