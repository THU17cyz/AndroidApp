package com.example.androidapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.chatTest.GifSizeFilter;
import com.example.androidapp.chatTest.fixtures.MessagesFixtures;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.R;
import com.gyf.immersionbar.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatActivity
        extends BaseActivity
        implements  DateFormatter.Formatter,
                    DialogInterface.OnClickListener,
                    MessagesListAdapter.OnLoadMoreListener,
                    MessagesListAdapter.OnMessageClickListener
//                    MessageInput.InputListener,
//                    MessageInput.AttachmentsListener
{
  private static final int REQUEST_CODE_CHOOSE = 10;

  @BindView(R.id.messagesList)
  MessagesList messagesList;

  @BindView(R.id.input)
  MessageInput messageInput;

  @BindView(R.id.name)
  TextView name;

  @BindView(R.id.returnButton)
  Button btn_return;

  private MessagesListAdapter messagesAdapter;
  private Date lastLoadedDate;
  private ImageLoader imageLoader;

  private List<Uri> mUris;
  private List<String> mPaths;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);
    ButterKnife.bind(this);
    initView();

    messageInput.setInputListener(new MessageInput.InputListener() {
      @Override
      public boolean onSubmit(CharSequence input) {
        messagesAdapter.addToStart(
                new Message("0", new User("0", "ming", null, true), input.toString())
                , true);
        return true;
      }
    });

    messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
      @Override
      public void onAddAttachments() {
        Toast.makeText(getApplicationContext(), "attachment", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(ChatActivity.this)
                .setItems(R.array.view_types_dialog, ChatActivity.this)
                .show();
      }
    });
  }

  /**
   * 初始化视图
   */
  private void initView(){
    // 状态栏
    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();

    // 头像
    imageLoader = new ImageLoader() {
      @Override
      public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);
        Log.d("url", url);
      }
    };

    //消息列表
    messagesAdapter = new MessagesListAdapter<>("0", imageLoader);
    messagesAdapter.setDateHeadersFormatter(this);
    messagesList.setAdapter(messagesAdapter);
  }


  /**
   * 处理日期格式
   * @param date
   * @return
   */
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

  protected void loadMessages() {
    new Handler().postDelayed(new Runnable() { //imitation of internet connection
      @Override
      public void run() {
        ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
        lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
        messagesAdapter.addToEnd(messages, false);
      }
    }, 1000);
  }


  @Override
  public void onClick(DialogInterface dialog, int which) {
    switch (which) {
      case 0:
        // 从相册里选择图片
        Matisse.from(ChatActivity.this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(9)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                  Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                  Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);
        break;
      case 1:
//                messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true);
        break;
    }
  }


  /**
   * 获得已选择的照片
   * @param requestCode
   * @param resultCode
   * @param data
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // 发送已选择的图片
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
      mUris = Matisse.obtainResult(data);
      mPaths = Matisse.obtainPathResult(data);
      Log.d("Matisse", "mSelected: " + mUris);

      for (int i = 0; i < mUris.size(); i++) {
        Message message = new Message("0", new User("0", "ming", null, true), null);
        message.setImage(new Message.Image(mUris.get(i).toString()));
        Log.d("Matisse print", mUris.get(i).toString());
        messagesAdapter.addToStart(message, true);
      }
//content://media/external/images/media/27
//        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);

    }

  }

  /**
   * 加载更多信息
   * @param page
   * @param totalItemsCount
   */
  @Override
  public void onLoadMore(int page, int totalItemsCount) {
    Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
    if (totalItemsCount < 100) {
      loadMessages();
    }
  }

  /**
   * 消息点击事件
   * @param message
   */
  @Override
  public void onMessageClick(IMessage message) {
    Toast.makeText(getApplicationContext(), message.getText() + "clilcked", Toast.LENGTH_SHORT).show();
  }

//  /**
//   * 输入提交事件
//   * @param input
//   * @return
//   */
//  @Override
//  public boolean onSubmit(CharSequence input) {
//    messagesAdapter.addToStart(
//            new Message("0", new User("0", "ming", null, true), input.toString())
//            , true);
//    return true;
//  }
//
//  /**
//   * 加号点击事件
//   */
//  @Override
//  public void onAddAttachments() {
//    Toast.makeText(getApplicationContext(), "attachment", Toast.LENGTH_SHORT).show();
//    new AlertDialog.Builder(ChatActivity.this)
//            .setItems(R.array.view_types_dialog, ChatActivity.this)
//            .show();
//  }
}
