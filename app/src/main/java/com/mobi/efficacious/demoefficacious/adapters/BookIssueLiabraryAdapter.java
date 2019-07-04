package com.mobi.efficacious.demoefficacious.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.entity.LibraryDetail;

import java.util.ArrayList;

public class BookIssueLiabraryAdapter extends RecyclerView.Adapter<BookIssueLiabraryAdapter.ViewHolder>  implements Filterable {
    private ArrayList<LibraryDetail> data;
    public ArrayList<LibraryDetail> orig;
    Context mcontext;
    public BookIssueLiabraryAdapter(ArrayList<LibraryDetail> dataList, Context context) {
        data = dataList;
        mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_issue_adapter,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }



    public void onBindViewHolder(final ViewHolder holder, int position) {
        try
        {
            holder.name.setText(data.get(position).getVchBookDetailsBookName());
            holder.assigneddate.setText("Issue Date : "+data.get(position).getDtAssignedDate());
            holder.returneddate.setText("Return Date : "+data.get(position).getDtReturnDate());
        }catch (Exception ex)
        {

        }

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
        TextView name,assigneddate,returneddate;
        public ViewHolder(View itemView) {
            super(itemView);
             name=(TextView)itemView.findViewById(R.id.bookname_liabrary);
             assigneddate = (TextView)itemView.findViewById(R.id.assignedDate_liabrary);
             returneddate = (TextView)itemView.findViewById(R.id.returnedDate_liabrary);

        }
    }
}