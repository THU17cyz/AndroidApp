package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int type;
    ArrayList<Message> note1;
    ArrayList<Message> note2;
    ArrayList<Message> note3;
    ArrayList<Message> note4;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        // 状态栏
        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 消息列表
        messagesAdapter = new MessagesListAdapter<>("1", null);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(messagesAdapter);

        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        note1 = new ArrayList<>();
        note2 = new ArrayList<>();
        note3 = new ArrayList<>();
        note4 = new ArrayList<>();

        String type = text.substring(2, 4);
        if (type.equals("用户")) {
            this.type = 0;
            note1.addAll(BasicInfo.WELCOME_NOTIFICATIONS);
            messagesAdapter.addToEnd(note1, true);
            // messagesAdapter.updateAndMoveToStart(BasicInfo.WELCOME_NOTIFICATIONS);
        } else if (type.equals("关注")) {
            this.type = 1;
            note2.addAll(BasicInfo.FOLLOW_NOTIFICATIONS);
            messagesAdapter.addToEnd(note2, true);
            // messagesAdapter.updateAndMoveToStart(message);
        } else if (type.equals("意向")) {
            this.type = 2;
            note3.addAll(BasicInfo.INTENTION_NOTIFICATIONS);
            messagesAdapter.addToEnd(note3, true);
            // messagesAdapter.updateAndMoveToStart(message);
        } else {
            this.type = 3;
            note4.addAll(BasicInfo.PWD_CHANGE_NOTIFICATIONS);
            messagesAdapter.addToEnd(note4, true);
            //messagesAdapter.updateAndMoveToStart(message);
        }


        toolbar.setNavigationOnClickListener(v->this.finish());
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
            note1.clear();
            note1.addAll(BasicInfo.WELCOME_NOTIFICATIONS);
            messagesAdapter.addToEnd(note1, true);
            Message m = BasicInfo.WELCOME_NOTIFICATIONS.get(BasicInfo.WELCOME_NOTIFICATIONS.size() - 1);
            m.setRead();
            id = m.getId();
        } else if (type == 1) {
            note2.clear();
            note2.addAll(BasicInfo.FOLLOW_NOTIFICATIONS);
            messagesAdapter.addToEnd(note2, true);
            Message m = BasicInfo.FOLLOW_NOTIFICATIONS.get(BasicInfo.FOLLOW_NOTIFICATIONS.size() - 1);
            id = m.getId();
        } else if (type == 2) {
            note3.clear();
            note3.addAll(BasicInfo.INTENTION_NOTIFICATIONS);
            messagesAdapter.addToEnd(note3, true);
            Message m = BasicInfo.INTENTION_NOTIFICATIONS.get(BasicInfo.INTENTION_NOTIFICATIONS.size() - 1);
            id = m.getId();
        } else {
            note4.clear();
            note4.addAll(BasicInfo.PWD_CHANGE_NOTIFICATIONS);
            messagesAdapter.addToEnd(note4, true);
            messagesAdapter.addToEnd(BasicInfo.PWD_CHANGE_NOTIFICATIONS, true);
            Message m = BasicInfo.PWD_CHANGE_NOTIFICATIONS.get(BasicInfo.PWD_CHANGE_NOTIFICATIONS.size() - 1);
            id = m.getId();
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
