package com.example.save_a_road_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

// Sigleton 객체
public class pictureHandler {

    private Context context = null;

    private  pictureHandler(){
    }


    // https://jeong-pro.tistory.com/86
    // 일반적인 Sigleton 클래스 사용 방법
    private static class LazyHolder{
        public static final pictureHandler INSTANCE = new pictureHandler();
    }
    public static pictureHandler getInstance() {
        return LazyHolder.INSTANCE;
    }


    public void setContext(Context context){
        this.context = context;
    }

    public void saveBitmapToJpeg(Bitmap bitmap, String name) {

        if(context == null) return;

        //내부저장소 파일 경로
        File storage = context.getFilesDir();

        //저장할 파일 이름
        String fileName = name + ".jpg";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
    }
}
