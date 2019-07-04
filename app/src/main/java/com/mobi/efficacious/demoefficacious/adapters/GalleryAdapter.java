package com.mobi.efficacious.demoefficacious.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobi.efficacious.demoefficacious.Interface.DataService;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.activity.MainActivity;
import com.mobi.efficacious.demoefficacious.dialogbox.image_zoom_dialog;
import com.mobi.efficacious.demoefficacious.entity.SchoolDetail;
import com.mobi.efficacious.demoefficacious.fragment.Gallery_Folder;
import com.mobi.efficacious.demoefficacious.webApi.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<SchoolDetail> data;
    Context mcontext;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String school_id,role_id,ImageName;
    private ProgressDialog progress;
    public GalleryAdapter(ArrayList<SchoolDetail> dataList,Context context) {
        data = dataList;
        mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_adapter,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }



    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try
        {
            String url= RetrofitInstance.Image_URL+data.get(position).getName();
//            holder.image.setImageURI(url);
            Glide.with(mcontext)
                    .load(url)
//                    .fitCenter()// image url
                    .error(R.mipmap.profile)
                    .into(holder.image);
            holder.eventDescriptiontv.setText(data.get(position).getEventDescription());
           holder.image.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(mcontext,image_zoom_dialog.class);
                   intent.putExtra("path",data.get(position).getName());
                   mcontext.startActivity(intent);
               }
           });
            holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    settings = mcontext.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
                    school_id=settings.getString("TAG_SCHOOL_ID", "");
                    role_id = settings.getString("TAG_USERTYPEID", "");

                    if(role_id.contentEquals("5"))
                    {
                        try
                        {
                            DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            String imageId=data.get(position).getId().toString();
                                            ImageName=data.get(position).getName();
                                            DeleteAsync(imageId,ImageName);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                            builder.setMessage("Are you sure want to Delete?").setPositiveButton("Yes", dialogClickListenerr)
                                    .setNegativeButton("No", dialogClickListenerr).show();

                        }catch (Exception ex)
                        {

                        }
                    }else
                    {

                    }




                    return false;
                }
            });
        }catch (Exception ex)
        {

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventDescriptiontv;
        ImageView  image ;
        public ViewHolder(View itemView) {
            super(itemView);
            eventDescriptiontv=(TextView)itemView.findViewById(R.id.eventDescriptiontv);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void  DeleteAsync(String imageId, String imageName){
        try {
            progress = new ProgressDialog(mcontext);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("loading...");
            DataService service = RetrofitInstance.getRetrofitInstance().create(DataService.class);
            Observable<ResponseBody> call = service.delete(imageId,imageName,school_id);
            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    progress.show();
                }

                @Override
                public void onNext(ResponseBody body) {
                    try {

                    } catch (Exception ex) {
                        progress.dismiss();
                        Toast.makeText(mcontext, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    progress.dismiss();
                    Toast.makeText(mcontext, "Response taking time seems Network issue!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onComplete() {
                    progress.dismiss();
                    try {
                        Toast.makeText(mcontext, "Image Deleted Successfully", Toast.LENGTH_LONG).show();
                        Gallery_Folder gallery_folder = new Gallery_Folder();
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, gallery_folder).commitAllowingStateLoss();
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
            progress.dismiss();
        }
    }
}