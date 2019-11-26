package com.example.save_a_road_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
    private int port;
    private Handler handler = null;
    // Picture 관련 변수들
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] buffer;
    int bytesRead;
    InputStream inputStream;
    OutputStream outputStream;
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
            InetSocketAddress socketAddress = new InetSocketAddress(addr, port);
            socket = new Socket();
            socket.connect(socketAddress, 7000);
        }catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
            Log.d("SocketLog", e.toString());
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
        if(!connect(addr, port)) {
            Log.d("SocketLog", "Connected Fail !!");
            Log.d("SocketLog", "address" + addr + " " + port);
            return;
        }
        if(socket == null) return;

        // buffer receiver & sender
        try{
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream() ;
        }catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
        }

        bConnected = true;

        // send socket connection message to Handler
        makeMessage(MessageType.SIMSOCK_CONNECTED, "");
        Log.d("SocketLog", "socket_thread start !!");


        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("hello");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }



        while(!Thread.interrupted()){

                    // buffer에 bytesRead만큼 쓰기
                    //outputStream.write(buffer, 0, bytesRead);
                    // byte 이미지를 bitmap으로 변환
                    //bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    // 현재 시간
                    now = System.currentTimeMillis();
                    mDate = new Date(now);
                    currentTime = simpleDateFormat.format(mDate);
                    // cache 데이터에 저장
                    picture_handler.saveBitmapToJpeg(bitmap, currentTime);
                    // UI 업데이트 (UI 업데이트 시 pictureData 객체들을 불러와서 보여주자)
                    makeMessage(MessageType.SIMSOCK_DATA, "");

            Log.d("SocketLog", Integer.toString(bytesRead));
            makeMessage(MessageType.SIMSOCK_DISCONNECTED, "");
            Log.d("SocketLog", "socket_thread terminated !!");

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
