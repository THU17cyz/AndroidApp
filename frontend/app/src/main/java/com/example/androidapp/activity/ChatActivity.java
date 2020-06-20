package com.example.androidapp.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.chatTest.GifSizeFilter;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.R;
import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.request.conversation.GetMessageRequest;
import com.example.androidapp.request.conversation.GetNewMessagesRequest;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
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


  private Handler mHandler = new Handler(Looper.getMainLooper());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);
    ButterKnife.bind(this);

    getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    user = getIntent().getStringExtra("user");
    contact = getIntent().getStringExtra("contact");
    contactId = getIntent().getStringExtra("contact_id");// 这里的id是用户id，用于传数据
    contactType = getIntent().getStringExtra("contact_type");

    // String.valueOf(BasicInfo.ID)
    thisUser = new User("0",user,"",user,BasicInfo.TYPE);
    // contactId
    contactUser = new User("1",contact,"",contact,contactType);//这里面的id是用于显示

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
        Picasso.get().load(url).placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);

      }
    };

    //消息列表
    messagesAdapter = new MessagesListAdapter<>("0", imageLoader);
    messagesAdapter.setDateHeadersFormatter(this);
    messagesList.setAdapter(messagesAdapter);

    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.e("返回","image在上面");
        mHandler.removeCallbacks(mTimeCounterRunnable);
        finish();
      }
    });

    name.setText(contact);

    name.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 显示历史聊天记录
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
        new SendMessageRequest(new Callback() {
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
                // 本地数据库是否应该插入
                // chatHistoryViewModel.insert(new ChatHistory(new Date(),"","P","S",user,contact,contactId,contactType));
              } else {
              }
              String info = jsonObject.getString("info");
              runOnUiThread(() -> Toast.makeText(ChatActivity.this,info, Toast.LENGTH_LONG).show());
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        },contactId, contactType,"P", "",Uri2File.convert(path)).send();
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

    new SendMessageRequest(new Callback() {
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
    },contactId, contactType,"T",input.toString(),null).send();
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


  // resume中无法读取数据库
  @Override
  protected void onResume() {
    super.onResume();
    // mTimeCounterRunnable.run();
  }


  private Runnable mTimeCounterRunnable = new Runnable() {
    @Override
    public void run() {//在此添加需轮寻的接口
      Log.e("聊天界面轮询","+1");
      newTest();//getUnreadCount()执行的任务
      mHandler.postDelayed(this, 10 * 1000);
    }
  };

  // 轮询要干的事
  private void newTest(){
    // 获取share的messageId
    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
    int currentMessageId = sharedPreferences.getInt(BasicInfo.ACCOUNT,0);
    Log.e("当前id",String.valueOf(currentMessageId));

    // 获取最新的messageId
    new GetMessageRequest(new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e("error","获取最新id失败");
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String resStr = response.body().string();
        Log.e("收到回复",resStr);
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(resStr);
          Boolean status = jsonObject.getBoolean("status");
          if(status){
            int messageId = jsonObject.getInt("message_id");// 最新id
            Log.e("最新id",String.valueOf(messageId));
            if(messageId==-1){
              // 无消息
              SharedPreferences sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putInt(BasicInfo.ACCOUNT,0);
              editor.commit();
            } else if(messageId==currentMessageId){
              // 无新操作
            } else if(messageId>currentMessageId){
              new GetNewMessagesRequest(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                  Log.e("error","获取最新消息失败");
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                  String resStr = response.body().string();
                  JSONObject jsonObject = null;
                  try {
                    jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                      JSONArray jsonArray = (JSONArray) jsonObject.get("message_info_list");
                      for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int messageId = jsonObject1.getInt("message_id");
                        String objectType = jsonObject1.getString("object_type");
                        String objectId = jsonObject1.getString("object_id");
                        String objectAccount = jsonObject1.getString("object_account");
                        String objectName = jsonObject1.getString("object_name");
                        String messageWay = jsonObject1.getString("message_way");
                        String messageType = jsonObject1.getString("message_type");
                        String messageContent = jsonObject1.getString("message_content");
                        String messageTime = jsonObject1.getString("message_time");

                        // SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
                        // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        // sdf.parse(messageTime)
                        chatHistoryViewModel.insert(new ChatHistory(new Date(),messageContent,messageType,messageWay,BasicInfo.ACCOUNT,objectAccount,objectId,objectType));
                        // 如果是对方的消息则显示
                        if(objectAccount.equals(contact)&&messageWay.equals("R")){
                          // sdf.parse(messageTime)
                          Message message = new Message("",contactUser,messageContent,new Date());
                          runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              messagesAdapter.addToStart(message,true);
                              messagesAdapter.notifyDataSetChanged();
                            }
                          });

                        }

                      }

                      // tmp: 得到所有新消息后，直接更新currentMessageId
                      SharedPreferences sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putInt(BasicInfo.ACCOUNT,messageId);
                      editor.commit();
                      Log.e("已更新","当前id："+String.valueOf(sharedPreferences.getInt(BasicInfo.ACCOUNT,-1)));
                    } else {
                    }
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }


                }
              },String.valueOf(currentMessageId)).send();
            }
          } else {
            String info = jsonObject.getString("info");
            Log.e("error",info);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }).send();

  }

}
