package com.example.save_a_road_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

    // 구현해야 할 기능
    // 1. 리스트 클릭하면 pictureViewActivity로
    // 2. 캐시 데이터에서 이미지들을 불러오는 코드 필요

    ArrayList<pictureData> pDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, loadingActivity.class);
        startActivity(intent);
        Context context = getApplicationContext();


        // 2


        ListView listView = (ListView)findViewById(R.id.listView);
        final listAdapter myAdapter = new listAdapter(this,pDataList);
        listView.setAdapter(myAdapter);

        // 1
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getPath(),
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

}
