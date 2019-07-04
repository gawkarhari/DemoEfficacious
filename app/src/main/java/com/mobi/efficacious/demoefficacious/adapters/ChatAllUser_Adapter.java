package com.mobi.efficacious.demoefficacious.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.IndividualChat;
import com.mobi.efficacious.demoefficacious.common.ConnectionDetector;
import com.mobi.efficacious.demoefficacious.entity.ChatDetail;
import com.mobi.efficacious.demoefficacious.fragment.Chat_StandardWise_Student_Fragment;

import java.util.ArrayList;

import static com.mobi.efficacious.demoefficacious.fragment.Student_Chat_Fragment.fulllayout;


public class ChatAllUser_Adapter extends RecyclerView.Adapter<ChatAllUser_Adapter.ListHolder> implements Filterable {

    ArrayList<ChatDetail> userList = new ArrayList<ChatDetail>();
    public ArrayList<ChatDetail> orig;
    Context context;
    String role_id, userid="";
    ConnectionDetector cd;
    public static String StandradId, DivisionId;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public ChatAllUser_Adapter(ArrayList<ChatDetail> UserList, Context context, String Role_id) {
        this.userList = UserList;
        this.context = context;
        this.role_id = Role_id;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_adapter, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        try {
            cd = new ConnectionDetector(context.getApplicationContext());
            settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            userid = settings.getString("TAG_USERID", "");
            String Name = (userList.get(position).getName());
            String alphabet = Name.substring(0, 1);
            if (userid.contentEquals(String.valueOf(userList.get(position).getUserId())) && role_id.contentEquals(String.valueOf(userList.get(position).getUserTypeId()))) {
              holder.cardView_layout.setVisibility(View.GONE);
            }else
            {
                holder.cardView_layout.setVisibility(View.VISIBLE);
                holder.UserProfile.setText(alphabet);
                holder.UserName.setText(userList.get(position).getName());
            }

        } catch (Exception ex) {

        }

        holder.UserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userList.get(position).getFCMToken().contentEquals("Group")) {

                    if (!cd.isConnectingToInternet()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("No InternetConnection");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    } else {
                        try {
                            Intent intent = new Intent(context, IndividualChat.class);
                            intent.putExtra("StandardId", String.valueOf(userList.get(position).getIntstandardId()));
                            intent.putExtra("DivisionId", String.valueOf(userList.get(position).getIntDivisionId()));
                            intent.putExtra("GroupName", userList.get(position).getName());
                            intent.putExtra("ReceiverFCMToken", userList.get(position).getFCMToken());
                            if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                                intent.putExtra("Teacher_ID", String.valueOf(userList.get(position).getIntTeacherId()));
                            }
                            context.startActivity(intent);
                        } catch (Exception ex) {

                        }

                    }
                } else if (userList.get(position).getFCMToken().contentEquals("StandardWiseName")) {
                    if (!cd.isConnectingToInternet()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("No InternetConnection");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    } else {

                        try {
                            StandradId = String.valueOf(userList.get(position).getIntstandardId());
                            DivisionId = String.valueOf(userList.get(position).getIntDivisionId());
                            fulllayout.setVisibility(View.GONE);
                            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.contain_main, new Chat_StandardWise_Student_Fragment());
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } catch (Exception ex) {

                        }

                    }
                } else {
                    if (userList.get(position).getName().contentEquals("No Data Available")) {
                        Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!cd.isConnectingToInternet()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setMessage("No InternetConnection");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        } else {
                            try {
                                Intent intent = new Intent(context, IndividualChat.class);
                                intent.putExtra("ReceiverName", userList.get(position).getName());
                                intent.putExtra("ReceiverId",String.valueOf( userList.get(position).getUserId()));
                                intent.putExtra("ReceiverFCMToken", userList.get(position).getFCMToken());
                                intent.putExtra("ReceiverUserTypeId", String.valueOf(userList.get(position).getUserTypeId()));
                                context.startActivity(intent);
                            } catch (Exception ex) {

                            }

                        }
                    }

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ChatDetail> results = new ArrayList<ChatDetail>();
                if (orig == null)
                    orig = userList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ChatDetail g : orig) {
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
                    userList = (ArrayList<ChatDetail>) results.values;
                    notifyDataSetChanged();
                } catch (Exception ex) {

                }

            }
        };
    }

    class ListHolder extends RecyclerView.ViewHolder {

        TextView UserName;
        TextView UserProfile;
        LinearLayout UserLayout;
CardView cardView_layout;
        ListHolder(View itemView) {
            super(itemView);
            UserName = (TextView) itemView.findViewById(R.id.text_view_username);
            UserProfile = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            UserLayout = (LinearLayout) itemView.findViewById(R.id.relativeLayout2);
            cardView_layout= (CardView) itemView.findViewById(R.id.cardView_layout);
        }


    }
}
