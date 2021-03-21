package com.example.pet.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;

import com.example.pet.entity.MyMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


//未实验安卓版本区别化
public class FileDataStream {
    private Context context;
    ArrayList<MyMessage> myMessages=new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FileDataStream(Context context) {
        this.context = context;
    }

    public ArrayList<MyMessage> getMyMessages() {
        return myMessages;
    }

    public void setMyMessages(ArrayList<MyMessage> myMessages) {
        this.myMessages = myMessages;
    }

    public void save(){

        ObjectOutputStream outputStream=null;
        try{
            //写入文件
            outputStream=new ObjectOutputStream(context.openFileOutput("MyMessage.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(myMessages);
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<MyMessage> load(){
        try{
            ObjectInputStream inputStream=new ObjectInputStream(context.openFileInput("MyTime.txt"));
            myMessages=(ArrayList<MyMessage>) inputStream.readObject();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return myMessages;
    }

    /* 保存bitmap到本地
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap, String title) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = "/sdcard/MyPet/pic/";
        } else {
            savePath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/Mytime/pic/";
        }
        try {
            filePic = new File(savePath + title + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            //压缩文件
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }
}
