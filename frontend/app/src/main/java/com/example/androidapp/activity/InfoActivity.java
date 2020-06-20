package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.request.information.SetInformationStateRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.LocalPicx;
import com.gyf.immersionbar.ImmersionBar;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class InfoActivity extends BaseActivity implements  DateFormatter.Formatter {

  private MessagesListAdapter messagesAdapter;

  @BindView(R.id.messagesList)
  MessagesList messagesList;

//  @BindView(R.id.text)
//  TextView textContent;
//
//  @BindView(R.id.time)
//  TextView textTime;

  @BindView(R.id.btn_return)
  ImageView btn_return;

  int type;

    private Handler mHandler = new Handler(Looper.getMainLooper());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
    ButterKnife.bind(this);

    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();

    // 消息列表
    messagesAdapter = new MessagesListAdapter<>("1", null);
    messagesAdapter.setDateHeadersFormatter(this);
    messagesList.setAdapter(messagesAdapter);

    Intent intent = getIntent();
    String text = intent.getStringExtra("text");
//    String dateString = intent.getStringExtra("dateString");
//    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
//    textContent.setText(text);
//    textTime.setText(dateString);

//    Message message = null;
//    try {
//      message = new Message("0", new User("1", "", null, true), text,sdf.parse(dateString));
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
    String type = text.substring(2, 4);
    if (type.equals("用户")) {
      this.type = 0;
      messagesAdapter.addToEnd(BasicInfo.WELCOME_NOTIFICATIONS, true);
      // messagesAdapter.updateAndMoveToStart(BasicInfo.WELCOME_NOTIFICATIONS);
    } else if (type.equals("关注")) {
      this.type = 1;
      messagesAdapter.addToEnd(BasicInfo.FOLLOW_NOTIFICATIONS, true);
      // messagesAdapter.updateAndMoveToStart(message);
    } else if (type.equals("意向")) {
      this.type = 2;
      messagesAdapter.addToEnd(BasicInfo.INTENTION_NOTIFICATIONS, true);
      // messagesAdapter.updateAndMoveToStart(message);
    } else {
      this.type = 3;
      messagesAdapter.addToEnd(BasicInfo.PWD_CHANGE_NOTIFICATIONS, true);
      //messagesAdapter.updateAndMoveToStart(message);
    }


    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Intent intent = new Intent(InfoActivity.this,MainActivity.class);
//        startActivity(intent);
        finish();
      }
    });
      mTimeCounterRunnable.run();
  }

  @Override
  public String format(Date date) {
    if (DateFormatter.isToday(date)) {
      return "今天";
    } else if (DateFormatter.isYesterday(date)) {
      return "昨天";
    } else {
      return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
    }
  }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mTimeCounterRunnable);
    }

    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {//在此添加需轮寻的接口
            Log.e("消息列表轮询","+1");
            refreshData();
            // 每30秒刷新一次
            mHandler.postDelayed(this, 5 * 1000);
        }
    };

    private void refreshData() {
      messagesAdapter.clear();
      String id;
      if (type == 0) {
        messagesAdapter.addToEnd(BasicInfo.WELCOME_NOTIFICATIONS, true);
        id = BasicInfo.WELCOME_NOTIFICATIONS.get(BasicInfo.WELCOME_NOTIFICATIONS.size() - 1).getId();
      } else if (type == 1) {
        messagesAdapter.addToEnd(BasicInfo.FOLLOW_NOTIFICATIONS, true);
        id = BasicInfo.FOLLOW_NOTIFICATIONS.get(BasicInfo.FOLLOW_NOTIFICATIONS.size() - 1).getId();
      } else if (type == 2) {
        messagesAdapter.addToEnd(BasicInfo.INTENTION_NOTIFICATIONS, true);
        id = BasicInfo.INTENTION_NOTIFICATIONS.get(BasicInfo.INTENTION_NOTIFICATIONS.size() - 1).getId();
      } else {
        messagesAdapter.addToEnd(BasicInfo.PWD_CHANGE_NOTIFICATIONS, true);
        id = BasicInfo.PWD_CHANGE_NOTIFICATIONS.get(BasicInfo.PWD_CHANGE_NOTIFICATIONS.size() - 1).getId();
      }
        messagesAdapter.notifyDataSetChanged();
      new SetInformationStateRequest(new okhttp3.Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) {

        }
      }, id, "R").send();
    }
}
