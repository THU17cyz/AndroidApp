package com.example.androidapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.androidapp.R;
import com.example.androidapp.chatTest.GifSizeFilter;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.request.conversation.SendMessageRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Uri2File;
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
import okhttp3.Callback;
import okhttp3.Response;


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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MessagesListAdapter messagesAdapter;
    private Date lastLoadedDate;
    private ImageLoader imageLoader;

    private List<Uri> mUris;
    private List<String> mPaths;

    private String user;
    private String contact;
    private String contactId;
    private String contactType;
    private String realName;

    private User thisUser;
    private User contactUser;

    private ArrayList<Message> msgs;


    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        //固定顶部导航栏
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 状态栏
        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        user = getIntent().getStringExtra("user");
        realName = getIntent().getStringExtra("real_name");
        contact = getIntent().getStringExtra("contact");
        contactId = getIntent().getStringExtra("contact_id");// 这里的id是用户id，用于传数据
        contactType = getIntent().getStringExtra("contact_type");

        System.out.println(user + contact + contactId + contactType);

        // String.valueOf(BasicInfo.ID)
        thisUser = new User("0",user,"http://diy.qqjay.com/u/files/2012/0510/d2e10cb3ac49dc63d013cb63ab6ca7cd.jpg",
                user,BasicInfo.TYPE, contactId);
        // contactId
        contactUser = new User("1",contact,"http://diy.qqjay.com/u/files/2012/0510/d2e10cb3ac49dc63d013cb63ab6ca7cd.jpg",
                contact,contactType, contactId);//这里面的id是用于显示



        // 头像
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.get().load(url).placeholder(R.drawable.ic_photoholder).into(imageView);

            }
        };

        //消息列表
        messagesAdapter = new MessagesListAdapter<>("0", imageLoader);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(messagesAdapter);

        toolbar.setNavigationOnClickListener(v->this.finish());

        msgs = new ArrayList<>();
        ArrayList<Message> tmp = BasicInfo.CHAT_HISTORY.get(contact); // 账号
        if (tmp != null) {
            for (Message m: tmp) {
                msgs.add(m);
            }
            messagesAdapter.addToEnd(msgs, true);
            messagesAdapter.notifyDataSetChanged();
        }
        Log.e("!!!!!!!!!!!!!!!", String.valueOf(msgs.size()));

        // 设置联系人用户名
        name.setText(realName);

        // 改成访问他人主页
        name.setOnClickListener(v -> {

        });

        messageInput.setInputListener(this);
        messageInput.setAttachmentsListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTimeCounterRunnable.run();
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mTimeCounterRunnable);
        super.onDestroy();
    }

    /**
     * 输入提交事件
     * @param input
     * @return
     */
    @Override
    public boolean onSubmit(CharSequence input) {
        new SendMessageRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error","发送失败");
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
                        // 发送成功后显示消息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messagesAdapter.addToStart(
                                        new Message("0", thisUser, input.toString())
                                        , true);
                            }
                        });

                    } else {
                        String info = jsonObject.getString("info");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },contactId, contactType,"T",input.toString(),null).send();
        return true;
    }

    // 轮询要干的事
    private synchronized void newTest() {
        Log.e("????????????????", String.valueOf(msgs.size()));
        int current = msgs.size();
        System.out.println(current);
        ArrayList<Message> tmp = BasicInfo.CHAT_HISTORY.get(contact); // 账号
        Log.e("????++++++", String.valueOf(msgs.size()));
        for (int i = current; i < tmp.size(); i++) {
            Message m = tmp.get(i);
            msgs.add(m);
            if (m.getUser().getId().equals("1")) messagesAdapter.addToStart(m, true);
        }
        Log.e("????????????????++++++", String.valueOf(msgs.size()));
        System.out.println(tmp.size());
        System.out.println(msgs.size());
        messagesAdapter.notifyDataSetChanged();
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

                String path = mPaths.get(i);
                String uri = mUris.get(i).toString();

                // 发送图片
                new SendMessageRequest(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error","发送图片失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"发送图片失败",Toast.LENGTH_SHORT).show();
                            }
                        });
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
                                // 发送成功之后显示图片
                                runOnUiThread(() -> {
                                    Message message = new Message("0", thisUser, null);
                                    message.setImage(new Message.Image(uri));
                                    Log.d("Matisse print", uri);
                                    messagesAdapter.addToStart(message, true);
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    String info = jsonObject.getString("info");
                                    @Override
                                    public void run() {
                                        runOnUiThread(() -> Toast.makeText(ChatActivity.this,info, Toast.LENGTH_LONG).show());
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },contactId, contactType,"P", null,Uri2File.convert(path)).send();
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

    }

    /**
     * 消息点击事件
     * @param message
     */
    @Override
    public void onMessageClick(IMessage message) {
        // Toast.makeText(getApplicationContext(), message.getText() + "clilcked", Toast.LENGTH_SHORT).show();
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


    // 轮询
    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("聊天界面轮询","+1");
            newTest();//getUnreadCount()执行的任务
            mHandler.postDelayed(this, 5 * 1000);
        }
    };

}
