package com.mobi.efficacious.demoefficacious.MultiImages.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mobi.efficacious.demoefficacious.MultiImages.helpers.Constants;
import com.mobi.efficacious.demoefficacious.MultiImages.models.Image;
import com.mobi.efficacious.demoefficacious.R;
import com.mobi.efficacious.demoefficacious.fragment.DailyDiaryFragment;

import java.util.ArrayList;

public class Three_Images extends AppCompatActivity {
    public static ImageView img1, img2, img3, img4;
    String path;
    public static ArrayList<String> FilePath = new ArrayList<String>();
    public static ArrayList<String> FileName = new ArrayList<String>();
    String pathname;
    Button Open_Gallery, Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_images);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img4.setVisibility(View.GONE);
        Open_Gallery = (Button) findViewById(R.id.btn_select);
        Submit = (Button) findViewById(R.id.btnSubmit);
        Submit.setVisibility(View.GONE);
        Open_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Three_Images.this, AlbumSelectActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                } catch (Exception ex) {

                }

            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    if (FilePath.size() > 0) {
                        for(int i=0;i<FilePath.size();i++)
                        {
                            if(i==0)
                            {
                                Bitmap bm = BitmapFactory.decodeFile(FilePath.get(i),options);
                                DailyDiaryFragment.iv_attachment1.setVisibility(View.VISIBLE);
                                DailyDiaryFragment.iv_attachment1.setImageBitmap(bm);

                            }
                            if(i==1)
                            {
                                Bitmap bm = BitmapFactory.decodeFile(FilePath.get(i),options);
                                DailyDiaryFragment.iv_attachment2.setVisibility(View.VISIBLE);
                                DailyDiaryFragment.iv_attachment2.setImageBitmap(bm);

                            }
                            if(i==2)
                            {
                                Bitmap bm = BitmapFactory.decodeFile(FilePath.get(i),options);
                                DailyDiaryFragment.iv_attachment3.setVisibility(View.VISIBLE);
                                DailyDiaryFragment.iv_attachment3.setImageBitmap(bm);

                            }


                        }

                        finish();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Three_Images.this);
                        alert.setMessage("Please Select Images");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }


            }
        });
        try {
            Intent intent = new Intent(Three_Images.this, AlbumSelectActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        } catch (Exception ex) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            try {
                if (images.size() < 4) {
                    if (images.size() == 0) {
                        Submit.setVisibility(View.GONE);
                    } else {
                        Submit.setVisibility(View.VISIBLE);
                        for (int i = 0, l = images.size(); i < l; i++) {
                            stringBuffer.append(images.get(i).path + "\n");

                            if (i == 0) {
                                img1.setImageBitmap(BitmapFactory.decodeFile(images.get(i).path));

                            } else if (i == 1) {
                                img2.setImageBitmap(BitmapFactory.decodeFile(images.get(i).path));

                            } else if (i == 2) {
                                img3.setImageBitmap(BitmapFactory.decodeFile(images.get(i).path));
                            }
                            pathname = images.get(i).path;
                            FilePath.add(pathname);
                            FileName.add(images.get(i).name);
                        }
                    }

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Three_Images.this);
                    alert.setMessage("You Cannot Share more than 3 files");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }
            } catch (Exception ex) {

            }


        }
    }

}
