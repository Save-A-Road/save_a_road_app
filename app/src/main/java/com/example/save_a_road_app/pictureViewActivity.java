package com.example.save_a_road_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class pictureViewActivity extends AppCompatActivity {

    // 구현해야 할 기능
    // 1. 홈아이콘 누르면 MainActivity로
    // 2. 사진 설정
    pictureHandler picture_handler = pictureHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String fileName = intent.getStringExtra("NAME");
        Bitmap bitmap = picture_handler.getBitmap(fileName);

        assert bitmap!= null : "fail to load bitmap image";

        ImageView imageView = (ImageView)findViewById(R.id.imageView2);
        TextView textView = (TextView)findViewById(R.id.textView2);

        imageView.setImageBitmap(bitmap);
        textView.setText(fileName);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
