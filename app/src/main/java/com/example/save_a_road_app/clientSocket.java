package com.example.save_a_road_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Handler;

public class clientSocket extends Thread{

    // Socket 관련 변수들
    private Socket socket;
    private String addr = "";
    private int port = 8080;
    private Handler handler = null;
    // Picture 관련 변수들
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] buffer;
    int bytesRead;
    InputStream inputStream;
    Bitmap bitmap;
    private boolean bConnected = false;
    pictureHandler picture_handler = pictureHandler.getInstance();
    // 날짜 관련 변수들
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    long now;
    Date mDate;
    String currentTime;

    public enum MessageType {
        SIMSOCK_CONNECTED(0), SIMSOCK_DATA(1), SIMSOCK_DISCONNECTED(2);
        private int value;
        private MessageType(int value) {
            value = value;
        }
        public int getValue() {
            return value;
        }
    }

    // Constructor
    public clientSocket(String addr, int port, Handler handler, Context context){
        this.addr = addr;
        this.port = port;
        this.handler = handler;
        picture_handler.setContext(context);
    }

    private boolean connect(String addr, int port) {

        try{
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(addr), port);
            socket = new Socket();
            socket.connect(socketAddress, 7000);
        }catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void makeMessage(MessageType mType, Object obj) {
        Message msg = Message.obtain();
        msg.what = mType.ordinal();
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    synchronized public boolean isConnected(){
        return bConnected;
    }

    @Override
    public void run(){

        // connect
        if(!connect(addr, port)) return;
        if(socket == null) return;

        // buffer receiver & sender
        try{
            inputStream = socket.getInputStream();
        }catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
        }

        bConnected = true;

        // send socket connection message to Handler
        makeMessage(MessageType.SIMSOCK_CONNECTED, "");
        Log.d("Socket Log", "socket_thread start !!");

        while(!Thread.interrupted()){
            buffer = new byte[4096];
            try{
                if( (bytesRead = inputStream.read(buffer)) > 0){
                    // buffer에 bytesRead만큼 쓰기
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    // byte 이미지를 bitmap으로 변환
                    bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    // 현재 시간
                    now = System.currentTimeMillis();
                    mDate = new Date(now);
                    currentTime = simpleDateFormat.format(mDate);
                    // cache 데이터에 저장
                    picture_handler.saveBitmapToJpeg(bitmap, currentTime);
                    // UI 업데이트 (UI 업데이트 시 pictureData 객체들을 불러와서 보여주자)
                    makeMessage(MessageType.SIMSOCK_DATA, "");
                }
            }catch (IOException e){
                System.out.println(e);
                e.printStackTrace();
            }

            makeMessage(MessageType.SIMSOCK_DISCONNECTED, "");
            Log.d("Socket Log", "socket_thread terminated !!");

        }

        try {
            byteArrayOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bConnected = false;

    }

}
