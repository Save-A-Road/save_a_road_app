package com.example.save_a_road_app;

public class pictureData {

    private String path;
    private String date;

    public pictureData(String path, String date){

        this.path = path;
        this.date = date;
    }

    public String getPath(){
        return this.path;
    }

    public String getDate(){
        return this.date;
    }

}
