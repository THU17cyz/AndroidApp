package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.gyf.immersionbar.ImmersionBar;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends BaseActivity implements  DateFormatter.Formatter {

  private MessagesListAdapter messagesAdapter;

  @BindView(R.id.messagesList)
  MessagesList messagesList;

  @BindView(R.id.btn_return)
  ImageView btn_return;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
    ButterKnife.bind(this);

    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();

    //消息列表
    messagesAdapter = new MessagesListAdapter<>("0", null);
    messagesAdapter.setDateHeadersFormatter(this);
    messagesList.setAdapter(messagesAdapter);

    Intent intent = getIntent();
    String text = intent.getStringExtra("text");

    Message message = new Message("0", new User("1", "", null, true), text);
    messagesAdapter.addToStart(message, false);


    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Intent intent = new Intent(InfoActivity.this,MainActivity.class);
//        startActivity(intent);
        finish();
      }
    });

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
}
