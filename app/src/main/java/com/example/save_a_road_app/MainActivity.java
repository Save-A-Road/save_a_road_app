package com.example.save_a_road_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<pictureData> pDataList;
    ArrayList<String> pathList;
    ArrayList<String> nameList;
    pictureHandler picture_handler = pictureHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, loadingActivity.class);
        startActivity(intent);
        Context context = getApplicationContext();

        Update_pDataList();

        ListView listView = (ListView)findViewById(R.id.listView);
        final listAdapter myAdapter = new listAdapter(this,pDataList);
        listView.setAdapter(myAdapter);

        // 1. 리스트 클릭하면 pictureViewActivity로
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getDate(),
                        Toast.LENGTH_LONG).show();
            }
        });

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch(inputMessage.what){

                    // CONNECTED == 0 / DATA == 1 / DISCONNECTED == 3
                    case 1:
                        // do something with UI
                        // 2. update_pDataList 한 후 Adepter에 다시 넘겨서 ListView 업데이트 !!
                        break;

                    case 2 :
                        String msg = (String) inputMessage.obj;
                        Log.d("socket",  "Socket Data Receive !!");
                        // do something with UI
                        break;

                    case 3 :
                        // do something with UI
                        break;

                }
            }
        };

        clientSocket socket = new clientSocket("192.168.0.1", 8080, mHandler, context);
        socket.start();

    }

    public void Update_pDataList(){

        pDataList = new ArrayList<pictureData>();
        pathList = picture_handler.getPathList();
        nameList = picture_handler.getNameList();

        Bitmap bitmap = null;
        String date = "";

        for (String path : pathList){
            bitmap = BitmapFactory.decodeFile(path);
            date = nameList.remove(0);
            pDataList.add(new pictureData(bitmap, date));
        }

    }

}
