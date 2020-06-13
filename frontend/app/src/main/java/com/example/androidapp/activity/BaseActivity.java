package com.example.androidapp.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("BaseActivity",getClass().getSimpleName());
    ActivityCollector.addActivity(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ActivityCollector.removeActivity(this);
  }

  /**
   * 退出应用程序
   * @param context
   */
  public void appExit(Context context){
    try{
      ActivityCollector.finishAll();
      ActivityManager activityManager = (ActivityManager) context
              .getSystemService(Context.ACTIVITY_SERVICE);
      activityManager.killBackgroundProcesses(context.getPackageName());
      System.exit(0);
    }catch (Exception ignored){}
  }
}
