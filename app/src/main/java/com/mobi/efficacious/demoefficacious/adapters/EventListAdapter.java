package com.mobi.efficacious.demoefficacious.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.Tab.Event_Tab;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.entity.EventDetail;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ListHolder> {

    Context context;
    ArrayList<EventDetail> itemsArrayList;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id, vchStandardId, userid, Year_id, intDivision_id, Schooli_id,Pagename;
    public EventListAdapter(ArrayList<EventDetail> itemsArrayList, Context context,String pagename) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.Pagename=pagename;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.event_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        try {
            settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            holder.registration_date.setText(itemsArrayList.get(position).getDtRegistrartionStartDate() + " - " + itemsArrayList.get(position).getDtRegistrationEndDate());
            holder.event_date.setText(itemsArrayList.get(position).getDtEventStartDate() + " - " + itemsArrayList.get(position).getDtEventEndDate());
            holder.nametv.setText(itemsArrayList.get(position).getVchEventName().toUpperCase());
            holder.feestv.setText(itemsArrayList.get(position).getVchEventFees());
            holder.description.setText(itemsArrayList.get(position).getVchEventDescription());
            if(Pagename.contentEquals("EventParticipatedList"))
            {
                holder.Participation_checkBox.setVisibility(View.GONE);
            }
            if(role_id.contentEquals("5")||role_id.contentEquals("6")||role_id.contentEquals("7"))
            {
                holder.Participation_checkBox.setVisibility(View.GONE);
            }
            holder.Participation_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
                        String EventId = String.valueOf(itemsArrayList.get(position).getIntEventId());
                        role_id = settings.getString("TAG_USERTYPEID", "");
                        vchStandardId = settings.getString("TAG_STANDERDID", "");
//                        if(intstandard_id.contentEquals(""))
//                        {
//                            intstandard_id="0";
//                        }
                        if(vchStandardId.equalsIgnoreCase("1"))
                        {
                            vchStandardId="Nursery";
                        }else if(vchStandardId.equalsIgnoreCase("2"))
                        {
                            vchStandardId="Play Group";
                        }else if(vchStandardId.equalsIgnoreCase("3"))
                        {
                            vchStandardId="KGI";
                        }else if(vchStandardId.equalsIgnoreCase("4"))
                        {
                            vchStandardId="KGII";
                        }else if(vchStandardId.equalsIgnoreCase("5"))
                        {
                            vchStandardId="I";
                        }else if(vchStandardId.equalsIgnoreCase("6"))
                        {
                            vchStandardId="II";
                        }else if(vchStandardId.equalsIgnoreCase("7"))
                        {
                            vchStandardId="III";
                        }else if(vchStandardId.equalsIgnoreCase("8"))
                        {
                            vchStandardId="IV";
                        }else if(vchStandardId.equalsIgnoreCase("9"))
                        {
                            vchStandardId="V";
                        }else if(vchStandardId.equalsIgnoreCase("10"))
                        {
                            vchStandardId="VI";
                        }else if(vchStandardId.equalsIgnoreCase("11"))
                        {
                            vchStandardId="VII";
                        }else if(vchStandardId.equalsIgnoreCase("12"))
                        {
                            vchStandardId="VIII";
                        }else if(vchStandardId.equalsIgnoreCase("13"))
                        {
                            vchStandardId="IX";
                        }else if(vchStandardId.equalsIgnoreCase("14"))
                        {
                            vchStandardId="X";
                        }else if(vchStandardId.equalsIgnoreCase("15"))
                        {
                            vchStandardId="XI";
                        }
                        else
                        {
                            vchStandardId="0";
                        }
                        userid = settings.getString("TAG_USERID", "");
                        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
                        intDivision_id = settings.getString("TAG_DIVISIONID", "");
                        if(intDivision_id.contentEquals(""))
                        {
                            intDivision_id="0";
                        }
                        if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                        {
                            Schooli_id="1";
                        }else
                        {
                            Schooli_id = settings.getString("TAG_SCHOOL_ID", "");
                        }

                        EventAsync(EventId, role_id, vchStandardId, userid, Year_id, intDivision_id);
                    } catch (Exception ex) {

                    }

                }
            });
        } catch (Exception ex) {

        }

    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {

        TextView nametv, registration_date, event_date, description, feestv;
        CheckBox Participation_checkBox;

        ListHolder(View itemView) {
            super(itemView);
            nametv = (TextView) itemView.findViewById(R.id.nametv);
            registration_date = (TextView) itemView.findViewById(R.id.registration_date);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            description = (TextView) itemView.findViewById(R.id.description);
            feestv = (TextView) itemView.findViewById(R.id.feestv);
            Participation_checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

    }


    public void EventAsync(String eventId, String role_id, String intstandard_id, String userid, String year_id, String intDivision_id) {
        try {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            EventDetail eventDetail=new EventDetail(vchStandardId,Integer.parseInt(intDivision_id),Integer.parseInt(userid),Integer.parseInt(role_id),Integer.parseInt(year_id),Integer.parseInt(Schooli_id),Integer.parseInt(eventId));
            Observable<ResponseBody> call = service.InsertEvent("InsertParticipation",eventDetail);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    dialog.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        dialog.dismiss();
                        Toast.makeText(context, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    dialog.dismiss();
                    Toast.makeText(context, "Participation Done", Toast.LENGTH_SHORT).show();
                    Event_Tab event_tab = new Event_Tab();
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, event_tab).commitAllowingStateLoss();
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

}
