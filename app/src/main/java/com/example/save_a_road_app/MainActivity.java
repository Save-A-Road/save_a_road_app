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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<pictureData> pDataList;
    ArrayList<String> pathList;
    ArrayList<String> nameList;
    pictureHandler picture_handler = pictureHandler.getInstance();
    listAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, loadingActivity.class);
        startActivity(intent);
        Context context = getApplicationContext();
        File storage = context.getFilesDir();
        Log.w("SocketLog", "location");
        picture_handler.setContext(context);

        Update_pDataList();

        ListView listView = (ListView)findViewById(R.id.listView);
        myAdapter = new listAdapter(this,pDataList);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getDate(),
                        Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(getApplicationContext(), pictureViewActivity.class);
                intent2.putExtra("NAME", myAdapter.getItem(position).getDate());
                startActivity(intent2);

            }
        });

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch(inputMessage.what){

                    // CONNECTED == 0 / DATA == 1 / DISCONNECTED == 3
                    case 0:
                        // do something with UI
                        Update_pDataList();
                        myAdapter.notifyDataSetChanged();
                        break;

                    case 1:
                        String msg = (String) inputMessage.obj;
                        Log.d("socket",  "Socket Data Receive !!");
                        // do something with UI
                        break;

                    case 2:
                        // do something with UI
                        break;

                }
            }
        };

        clientSocket socket = new clientSocket("172.20.10.4", 37442, mHandler, context);
        socket.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Update_pDataList();
        myAdapter.notifyDataSetChanged();
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
