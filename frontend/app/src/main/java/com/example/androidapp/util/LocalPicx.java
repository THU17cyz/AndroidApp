package com.example.androidapp.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.example.androidapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class LocalPicx {
    public static String NOTIFICATION_PASSWORD_CHANGE;
    public static String NOTIFICATION_WATCH;
    public static String NOTIFICATION_WELCOME;
    public static String NOTIFICATION_INTENTION;
    public static void loadAsset(Activity activity) {
        BitmapDrawable d = (BitmapDrawable) activity.getDrawable(R.drawable.notifications_password_change);
        Bitmap img = d.getBitmap();

        String fn = "NOTIFICATION_PASSWORD_CHANGE.png";
        String path = activity.getFilesDir() + File.separator + fn;
        try{
            OutputStream os = new FileOutputStream(path);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            NOTIFICATION_PASSWORD_CHANGE = "file://" + path;
        }catch(Exception e){
            Log.e("TAG", "", e);
        }

        d = (BitmapDrawable) activity.getDrawable(R.drawable.notifications_watch);
        img = d.getBitmap();

        fn = "NOTIFICATION_PASSWORD_CHANGE.png";
        path = activity.getFilesDir() + File.separator + fn;
        try{
            OutputStream os = new FileOutputStream(path);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            NOTIFICATION_WATCH = "file://" + path;
        }catch(Exception e){
            Log.e("TAG", "", e);
        }

        d = (BitmapDrawable) activity.getDrawable(R.drawable.notifications_welcome);
        img = d.getBitmap();

        fn = "NOTIFICATION_PASSWORD_CHANGE.png";
        path = activity.getFilesDir() + File.separator + fn;
        try{
            OutputStream os = new FileOutputStream(path);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            NOTIFICATION_WELCOME = "file://" + path;
        }catch(Exception e){
            Log.e("TAG", "", e);
        }

        d = (BitmapDrawable) activity.getDrawable(R.drawable.notifications_intention);
        img = d.getBitmap();

        fn = "NOTIFICATION_PASSWORD_CHANGE.png";
        path = activity.getFilesDir() + File.separator + fn;
        try{
            OutputStream os = new FileOutputStream(path);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            NOTIFICATION_INTENTION = "file://" + path;
        }catch(Exception e){
            Log.e("TAG", "", e);
        }

    }
}
