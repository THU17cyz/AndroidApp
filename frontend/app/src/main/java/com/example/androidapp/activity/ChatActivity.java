package com.example.androidapp.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.chatTest.GifSizeFilter;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.R;
import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.request.conversation.SendMessageRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.Uri2File;
import com.example.androidapp.viewmodel.ChatHistoryViewModel;
import com.google.android.material.internal.NavigationMenu;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ChatActivity
        extends BaseActivity
        implements  DateFormatter.Formatter,
                    DialogInterface.OnClickListener,
                    MessagesListAdapter.OnLoadMoreListener,
                    MessagesListAdapter.OnMessageClickListener,
                    MessageInput.InputListener,
                    MessageInput.AttachmentsListener
{
  private static final int REQUEST_CODE_CHOOSE = 10;

  @BindView(R.id.messagesList)
  MessagesList messagesList;

  @BindView(R.id.input)
  MessageInput messageInput;

  @BindView(R.id.name)
  TextView name;

  @BindView(R.id.btn_return)
  ImageView btn_return;

  private MessagesListAdapter messagesAdapter;
  private Date lastLoadedDate;
  private ImageLoader imageLoader;

  private List<Uri> mUris;
  private List<String> mPaths;
  private ChatHistoryViewModel chatHistoryViewModel;

  private String user;
  private String contact;
  private String contactId;
  private String contactType;

  private User thisUser;
  private User contactUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);
    ButterKnife.bind(this);

    getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    user = getIntent().getStringExtra("user");
    contact = getIntent().getStringExtra("contact");
    contactId = getIntent().getStringExtra("contact_id");
    contactType = getIntent().getStringExtra("contact_type");

    // String.valueOf(BasicInfo.ID)
    thisUser = new User("0",user,"",user,BasicInfo.TYPE);
    // contactId
    contactUser = new User("1",contact,"",contact,contactType);

    // 聊天记录
    chatHistoryViewModel = ViewModelProviders.of(this).get(ChatHistoryViewModel.class);
    chatHistoryViewModel.getAllHistory().observe(this, new Observer<List<ChatHistory>>() {
      @Override
      public void onChanged(List<ChatHistory> chatHistories) {
      }
    });

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

    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    name.setText(contact);

    name.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 查找并显示历史聊天记录
        LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
        List<ChatHistory> historyList = list.getValue();
        if(historyList!=null){
          List<Message> messagesList = new ArrayList<>();
          for(int i=0;i<historyList.size();i++){
            ChatHistory history = historyList.get(i);
            if(history.getUser().equals(user)&& history.getContact().equals(contact)){
              Message message = new Message(String.valueOf(i),thisUser,history.getContent(),history.getTime());
              messagesList.add(message);
            }
          }
          messagesAdapter.addToEnd(messagesList, false);
          messagesAdapter.notifyDataSetChanged();
        }
      }
    });

    messageInput.setInputListener(this);
    messageInput.setAttachmentsListener(this);

//
//    RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
//    refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//      @Override
//      public void onRefresh(RefreshLayout refreshlayout) {
//
//        // 查找并显示历史聊天记录
//        LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
//        List<ChatHistory> historyList = list.getValue();
//        if(historyList!=null){
//          List<Message> messagesList = new ArrayList<>();
//          for(int i=0;i<historyList.size();i++){
//            ChatHistory history = historyList.get(i);
//            if(history.getUser().equals(user)&& history.getContact().equals(contact)){
//              Message message = new Message(String.valueOf(i),thisUser,history.getContent(),history.getTime());
//              messagesList.add(message);
//            }
//          }
//          messagesAdapter.addToEnd(messagesList, false);
//          messagesAdapter.notifyDataSetChanged();
//        }
//
//        refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//      }
//    });

//
//    messagesAdapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
//      @Override
//      public void onLoadMore(int page, int totalItemsCount) {
//        // 查找并显示历史聊天记录
//        LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
//        List<ChatHistory> historyList = list.getValue();
//        if(historyList!=null){
//          List<Message> messagesList = new ArrayList<>();
//          for(int i=0;i<historyList.size();i++){
//            ChatHistory history = historyList.get(i);
//            if(history.getUser().equals(user)&& history.getContact().equals(contact)){
//              Message message = new Message(String.valueOf(i),thisUser,history.getContent(),history.getTime());
//              messagesList.add(message);
//            }
//          }
//          messagesAdapter.addToEnd(messagesList, false);
//          messagesAdapter.notifyDataSetChanged();
//        }
//      }
//    });
//

  }



  @Override
  protected void onStart() {
    super.onStart();
  }

  /**
   * 初始化视图
   */
  private void initView(){

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
        Message message = new Message("0", thisUser, null);
        message.setImage(new Message.Image(mUris.get(i).toString()));
        Log.d("Matisse print", mUris.get(i).toString());
        messagesAdapter.addToStart(message, true);
        String path = "file://" + mUris.get(i).toString();
        new SendMessageRequest(new okhttp3.Callback() {
          @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {

          }

          @Override
          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            String resStr = response.body().string();
            Log.e("response", resStr);
            try {
              // 解析json，然后进行自己的内部逻辑处理
              JSONObject jsonObject = new JSONObject(resStr);

              Boolean status = jsonObject.getBoolean("status");
              if (status) {
              } else {
              }
              String info = jsonObject.getString("info");
              runOnUiThread(() -> Toast.makeText(ChatActivity.this,info, Toast.LENGTH_LONG).show());
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        },contactUser.getId(), contactUser.getType(),"P", "",Uri2File.convert(path)).send();
      }
    }

  }

  /**
   * 加载更多信息
   * @param page
   * @param totalItemsCount
   */
  @Override
  public void onLoadMore(int page, int totalItemsCount) {
//    // 查找并显示历史聊天记录
//    LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
//    List<ChatHistory> historyList = list.getValue();
//    if(historyList!=null){
//      List<Message> messagesList = new ArrayList<>();
//      for(int i=0;i<historyList.size();i++){
//        ChatHistory history = historyList.get(i);
//        if(history.getUser().equals(user)&& history.getContact().equals(contact)){
//          Message message = new Message(String.valueOf(i),thisUser,history.getContent(),history.getTime());
//          messagesList.add(message);
//        }
//      }
//      messagesAdapter.addToEnd(messagesList, false);
//      messagesAdapter.notifyDataSetChanged();
//    }
  }

  /**
   * 消息点击事件
   * @param message
   */
  @Override
  public void onMessageClick(IMessage message) {
    Toast.makeText(getApplicationContext(), message.getText() + "clilcked", Toast.LENGTH_SHORT).show();
  }

  /**
   * 输入提交事件
   * @param input
   * @return
   */
  @Override
  public boolean onSubmit(CharSequence input) {
    messagesAdapter.addToStart(
            new Message("0", thisUser, input.toString())
            , true);
    chatHistoryViewModel.insert(new ChatHistory(new Date(), input.toString(),"T","S",user,contact));
//    chatHistoryViewModel.insert(new ChatHistory(new Date(), input.toString(),"T","S",user,contact));
//    chatHistoryViewModel.insert(new ChatHistory(new Date(), input.toString(),"T","R",user,contact));

    LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
    List<ChatHistory> historyList = list.getValue();
    if(historyList!=null) {
      List<Message> messagesList = new ArrayList<>();
      for (int i = 0; i < historyList.size(); i++) {
        ChatHistory history = historyList.get(i);
        if (history.getUser().equals(user) && history.getContact().equals(contact)) {
          Message message = null;
          if(history.getSend().equals("S")){
            message = new Message(String.valueOf(i), thisUser, history.getContent(), history.getTime());
          } else {
            message = new Message(String.valueOf(i), contactUser, history.getContent(), history.getTime());
          }
          messagesList.add(message);
        }
      }
      messagesAdapter.addToEnd(messagesList, true);
      messagesAdapter.notifyDataSetChanged();
    }


    new SendMessageRequest(new okhttp3.Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {

      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

        String resStr = response.body().string();
        Log.e("response", resStr);
        try {
          // 解析json，然后进行自己的内部逻辑处理
          JSONObject jsonObject = new JSONObject(resStr);

          Boolean status = jsonObject.getBoolean("status");
          if (status) {
          } else {
          }
          String info = jsonObject.getString("info");
          runOnUiThread(() -> Toast.makeText(ChatActivity.this,info, Toast.LENGTH_LONG).show());
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    },contactUser.getId(), contactUser.getType(),"T",input.toString(),null).send();
    return true;
  }

  /**
   * 加号点击事件
   */
  @Override
  public void onAddAttachments() {
    Toast.makeText(getApplicationContext(), "attachment", Toast.LENGTH_SHORT).show();
    new AlertDialog.Builder(ChatActivity.this)
            .setItems(R.array.view_types_dialog, ChatActivity.this)
            .show();
  }

}
