package com.example.save_a_road_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class listAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<pictureData> pDataList;

    public listAdapter(Context context, ArrayList<pictureData> data){
        this.mContext = context;
        this.pDataList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount(){
        return pDataList.size();
    }

    @Override
    public pictureData getItem(int position) {
        return pDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.activity_listview_element, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView textView = (TextView)view.findViewById(R.id.textView);

        imageView.setImageBitmap(pDataList.get(position).getBitmap());
        textView.setText(pDataList.get(position).getDate());

        return view;
    }
}
