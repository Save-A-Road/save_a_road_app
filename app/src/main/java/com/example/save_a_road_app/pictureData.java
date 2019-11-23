package com.example.save_a_road_app;

import android.graphics.Bitmap;

public class pictureData {

    private Bitmap bitmap;
    private String date;

    public pictureData(Bitmap bitmap, String date){
        this.bitmap = bitmap;
        this.date = date;
    }

    public Bitmap getBitmap(){ return this.bitmap; }

    public String getDate(){
        return this.date;
    }

}
