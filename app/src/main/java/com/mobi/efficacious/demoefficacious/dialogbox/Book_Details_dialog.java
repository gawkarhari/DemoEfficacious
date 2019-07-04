package com.mobi.efficacious.demoefficacious.dialogbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mobi.efficacious.demoefficacious.R;

public class Book_Details_dialog extends Activity {

    String AccessionNo,BookName,AuthorName,Edition,Language,Price;
    TextView AccessionNotv,BookNametv,AuthorNametv,Editiontv,Languagetv,Pricetv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_detail_dialog);
        AccessionNotv=(TextView)findViewById(R.id.accessionno);
        BookNametv=(TextView)findViewById(R.id.bookname);
        AuthorNametv=(TextView)findViewById(R.id.authorname);
        Editiontv=(TextView)findViewById(R.id.editionname);
        Languagetv=(TextView)findViewById(R.id.lang);
        Pricetv=(TextView)findViewById(R.id.bookPrice);
        try {
            Intent intent = getIntent();
            AccessionNo = intent.getStringExtra("AccessionNo");
            BookName = intent.getStringExtra("BookName");
            AuthorName = intent.getStringExtra("AuthorName");
            Edition = intent.getStringExtra("Edition");
            Language = intent.getStringExtra("Language");
            Price = intent.getStringExtra("Price");
            AccessionNotv.setText(AccessionNo);
            BookNametv.setText(BookName);
            AuthorNametv.setText(AuthorName);
            Editiontv.setText(Edition);
            Languagetv.setText(Language);
            Pricetv.setText(Price);
        }catch (Exception ex)
        {

        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}