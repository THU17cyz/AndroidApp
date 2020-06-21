package com.example.androidapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.UI.conversation.ConversationFragment;
import com.example.androidapp.UI.dashboard.DashboardFragment;
import com.example.androidapp.UI.follow.FollowFragment;
import com.example.androidapp.UI.home.HomeFragment;
import com.example.androidapp.UI.notification.NotificationFragment;
import com.example.androidapp.adapter.ViewPagerAdapter;
import com.example.androidapp.application.App;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.request.conversation.GetMessagePictureRequest;
import com.example.androidapp.request.conversation.GetNewMessagesRequest;
import com.example.androidapp.request.information.GetInformationDetailRequest;
import com.example.androidapp.request.information.GetInformationRequest;
import com.example.androidapp.request.user.GetInfoPictureRequest;
import com.example.androidapp.request.user.LogoutRequest;
import com.example.androidapp.request.user.UpdateInfoPictureRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.LocalPicx;
import com.example.androidapp.util.LoginCache;
import com.example.androidapp.util.MyImageLoader;
import com.example.androidapp.util.StringCutter;
import com.example.androidapp.util.Uri2File;
import com.example.androidapp.viewmodel.ChatHistoryViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.kingja.loadsir.core.LoadSir;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    final FragmentManager fm = getSupportFragmentManager();

    private static final int REQUEST_CODE_CHOOSE = 11;
    private long exitTime = 0;
    private boolean loaded = false;

    private Drawer drawer;

    private List<IProfile> drawerHead;

    public static Handler msgHandler;

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private NavController navController;
    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    BottomNavigationView navView;


    private ChatHistoryViewModel chatHistoryViewModel;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
//        loadService = LoadSir.getDefault().register(this, (com.kingja.loadsir.callback.Callback.OnReloadListener) v -> { });


        navView = findViewById(R.id.nav_view);

        BasicInfo.NAV_VIEW = navView;
        navView.removeBadge(R.id.navigation_conversations);
//        Window window = getWindow();
//        window.setStatusBarColor(Color.TRANSPARENT);
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_follow:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_conversations:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(4);
                    break;
            }
            return true;
        });


        viewPager = findViewById(R.id.nav_host_fragment);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);


//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupWithNavController(navView, navController);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        navView.getMenu().findItem(R.id.navigation_follow).setChecked(true);
                        break;
                    case 2:
                        navView.getMenu().findItem(R.id.navigation_conversations).setChecked(true);
                        break;
                    case 3:
                        navView.getMenu().findItem(R.id.navigation_notifications).setChecked(true);
                        break;
                    default:
                        navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        chatHistoryViewModel = new ViewModelProvider(this).get(ChatHistoryViewModel.class);
//        chatHistoryViewModel = ViewModelProviders.of(this).get(ChatHistoryViewModel.class);
        chatHistoryViewModel.getAllHistory().observe(this, chatHistories -> {
            if (!BasicInfo.LOADED) {
                BasicInfo.LOADED = true;
                getDatabase();
                mTimeCounterRunnable.run();
            }

        });

        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();



        // 初始化侧边栏
        initDrawer();

        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };


        LocalPicx.loadAsset(this);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        boolean hasLogin = sharedPreferences.getBoolean("hasLogin",false);
        if(!hasLogin){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasLogin",true);
            editor.putString("type",BasicInfo.TYPE);
            editor.putString("account",BasicInfo.ACCOUNT);
            editor.putString("password",BasicInfo.PASSWORD);
            editor.commit();
        }

    }

    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {//在此添加需轮寻的接口
            Log.e("消息列表轮询","+1");
            refreshData();
            // 每30秒刷新一次
            mHandler.postDelayed(this, 2 * 1000);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        initDrawer();
//        loadService.showSuccess();
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mTimeCounterRunnable);
        super.onDestroy();
    }

    private void getDatabase() {
        Log.e("两边？？","fhsjafhkhfsaj");
        LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
        List<ChatHistory> chatList = list.getValue();
        if (chatList == null) {
            Log.e("错误","数据库获取为null");
        } else {
//            List<String> accounts = new ArrayList<>();//对方列表
//            List<String> messages = new ArrayList<>();//对方最新消息列表
//            List<Date> dates = new ArrayList<>();//对方最新消息时间列表
//            List<String> ids = new ArrayList<>();//对方id列表
//            List<String> types = new ArrayList<>();//对方类型列表

            for(int i = 0; i < chatList.size(); i++){
                ChatHistory chat = chatList.get(i);

                // 判断是否为当前用户的消息
                if(chat.getUser().equals(BasicInfo.ACCOUNT)){
                    String id = chat.getContactId();
                    String account = chat.getContact();
                    String type = chat.getType();
                    String otherUserType = chat.getContactType();
                    String msg = chat.getContent();
                    String send = chat.getSend();
                    Date date = chat.getTime();
                    String realName = chat.getRealName();
//                    User user = new User(id, account,"", account, type);
                    com.example.androidapp.chatTest.model.Message message;// = new com.example.androidapp.chatTest.model.Message("", user, msg, date);

                    if(send.equals("S")){
                        User user = new User("0", realName,
                                new GetInfoPictureRequest(otherUserType, id, id).getWholeUrl(),
                                account, otherUserType, id);
                        message = new com.example.androidapp.chatTest.model.Message("", user, msg, date, true);
                    } else {
                        User user = new User("1", realName,
                                new GetInfoPictureRequest(otherUserType, id, id).getWholeUrl(),
                                account, otherUserType, id);
                        message = new com.example.androidapp.chatTest.model.Message("", user, msg, date, true);
                    }
                    if (type.equals("P")) {
                        // message.setText("图片");
                        message.setImage(new com.example.androidapp.chatTest.model.Message.Image(
                                (new GetMessagePictureRequest(msg).getWholeUrl())));
                        System.out.println(new GetMessagePictureRequest(msg).getWholeUrl());

                    }

                    ArrayList<com.example.androidapp.chatTest.model.Message> msgs = BasicInfo.CHAT_HISTORY.get(account);
                    if (msgs == null) {
                        msgs = new ArrayList<>();
                        BasicInfo.CHAT_HISTORY.put(account, msgs);
                    }
//                    System.out.println(account);
//                    System.out.println(BasicInfo.CHAT_HISTORY.get(account));
                    msgs.add(message);
                }
            }
        }
    }

    private void refreshData() {
        User user = new User("0","","null",false);
        // 获取消息id列表
        new GetInformationRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("information_id_list");
                    ArrayList<Integer> informationIdList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        informationIdList.add(jsonArray.getInt(i));
                    }
                    int idx = -1;
                    int tmp;
                    int len;
                    len = BasicInfo.WELCOME_NOTIFICATIONS.size();
                    if (len > 0) {
                        tmp = Integer.valueOf(BasicInfo.WELCOME_NOTIFICATIONS.get(len-1).getId());
                        if (idx < tmp) idx = tmp;
                    }
                    len = BasicInfo.FOLLOW_NOTIFICATIONS.size();
                    if (len > 0) {
                        tmp = Integer.valueOf(BasicInfo.FOLLOW_NOTIFICATIONS.get(len-1).getId());
                        if (idx < tmp) idx = tmp;
                    }
                    len = BasicInfo.INTENTION_NOTIFICATIONS.size();
                    if (len > 0) {
                        tmp = Integer.valueOf(BasicInfo.INTENTION_NOTIFICATIONS.get(len-1).getId());
                        if (idx < tmp) idx = tmp;
                    }
                    len = BasicInfo.PWD_CHANGE_NOTIFICATIONS.size();
                    if (len > 0) {
                        tmp = Integer.valueOf(BasicInfo.PWD_CHANGE_NOTIFICATIONS.get(len-1).getId());
                        if (idx < tmp) idx = tmp;
                    }
                    //在获取id列表的基础上获取每条消息
                    for(int i = 0; i < informationIdList.size(); i++){
                        String id = informationIdList.get(i).toString();
                        if (Integer.valueOf(id) <= idx) continue;
                        new GetInformationDetailRequest(new okhttp3.Callback() {
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String resStr = response.body().string();

                                Log.e("response", resStr);
                                try {
                                    // 解析json，然后进行自己的内部逻辑处理
                                    JSONObject jsonObject = new JSONObject(resStr);

                                    Boolean status = jsonObject.getBoolean("status");
                                    if (status) {
                                        String time = jsonObject.getString("information_time");
                                        String state = jsonObject.getString("information_state");
                                        String content = (String) jsonObject.get("information_content");
                                        com.example.androidapp.chatTest.model.Message message;
                                        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
                                        if(state.equals("N")){
                                            message = new com.example.androidapp.chatTest.model.Message(id, user,content,sdf.parse(time),false);
                                        } else {
                                            message = new com.example.androidapp.chatTest.model.Message(id, user,content,sdf.parse(time),true);
                                        }
                                        // message.setDateString(time);
                                        Log.e("消息内容",sdf.parse(time).toString()+" "+state);
                                        String type = content.substring(2, 4);
                                        if (type.equals("用户")) {
                                            BasicInfo.WELCOME_NOTIFICATIONS.add(message);
                                        } else if (type.equals("关注")) {
                                            BasicInfo.FOLLOW_NOTIFICATIONS.add(message);
                                        } else if (type.equals("意向")) {
                                            BasicInfo.INTENTION_NOTIFICATIONS.add(message);
                                        } else {
                                            BasicInfo.PWD_CHANGE_NOTIFICATIONS.add(message);
                                        }


                                    } else {
                                        String info = jsonObject.getString("info");
                                    }
                                } catch (JSONException | ParseException e) {

                                }
                            }
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            }
                        }, String.valueOf(informationIdList.get(i))).send();
                    }

                } catch (JSONException e) {

                }
            }
        }).send();

        // 先获取本地聊天id
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        int currentMessageId = sharedPreferences.getInt(BasicInfo.ACCOUNT,0);
        Log.e("当前id:",String.valueOf(currentMessageId));

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
                    Log.e("response", resStr);
                    if (status) {
                        JSONArray jsonArray = (JSONArray) jsonObject.get("message_info_list");

                        int messageId = -1;
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            messageId = jsonObject1.getInt("message_id");
                            String objectType = jsonObject1.getString("object_type");
                            String objectId = jsonObject1.getString("object_id");
                            String objectAccount = jsonObject1.getString("object_account");
                            String objectName = jsonObject1.getString("object_name");
                            String messageWay = jsonObject1.getString("message_way");
                            String messageType = jsonObject1.getString("message_type");
                            String messageContent = jsonObject1.getString("message_content");
                            String messageTime = jsonObject1.getString("message_time");


                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date = simpleDateFormat.parse(messageTime);
//                            chatHistoryViewModel.insert(new ChatHistory(
//                                    date, messageContent, messageType,
//                                    messageWay, BasicInfo.ACCOUNT, objectAccount, objectId, objectType));
                            com.example.androidapp.chatTest.model.Message message;
                            if(messageWay.equals("S")){
                                User user = new User("0", objectName,
                                        new GetInfoPictureRequest(objectType, objectId, objectId).getWholeUrl(),
//                                        "http://diy.qqjay.com/u/files/2012/0510/d2e10cb3ac49dc63d013cb63ab6ca7cd.jpg",
                                        objectAccount, objectType, objectId);
                                message = new com.example.androidapp.chatTest.model.Message(String.valueOf(messageId), user, messageContent, date, false);
                            } else {
                                User user = new User("1", objectName,
                                        new GetInfoPictureRequest(objectType, objectId, objectId).getWholeUrl(),
//                                        "http://diy.qqjay.com/u/files/2012/0510/d2e10cb3ac49dc63d013cb63ab6ca7cd.jpg",
                                        objectAccount, objectType, objectId);
                                message = new com.example.androidapp.chatTest.model.Message(String.valueOf(messageId), user, messageContent, date, false);
                            }
                            if(messageType.equals("T")){
                                chatHistoryViewModel.insert(new ChatHistory(date, messageContent,
                                        messageType, messageWay, BasicInfo.ACCOUNT, objectAccount, objectId, objectType, objectName));

                            } else {
                                chatHistoryViewModel.insert(new ChatHistory(date, String.valueOf(messageId), messageType,
                                        messageWay, BasicInfo.ACCOUNT, objectAccount, objectId, objectType, objectName));
                                message.setImage(new com.example.androidapp.chatTest.model.Message.Image(
                                        (new GetMessagePictureRequest(String.valueOf(messageId)).getWholeUrl())));
                                message.setText(String.valueOf(messageId));
                            }

                            if (!messageWay.equals("S") || currentMessageId == 0) {
                                ArrayList<com.example.androidapp.chatTest.model.Message> msgs = BasicInfo.CHAT_HISTORY.get(objectAccount);
                                if (msgs == null) {
                                    msgs = new ArrayList<>();
                                    BasicInfo.CHAT_HISTORY.put(objectAccount, msgs);
                                }
                                msgs.add(message);
                                BasicInfo.addToBadgeChat(1);
                            }



                        }


                        if (messageId != -1) {
                            // tmp: 得到所有新消息后，直接更新currentMessageId
                            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(BasicInfo.ACCOUNT, messageId);
                            editor.commit();
                            Log.e("更新后id：",String.valueOf(messageId));
                        }



                    } else {
                        Log.e("hasHandled置true","，");
                    }



                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, String.valueOf(currentMessageId)).send();
    }




    public void openDrawer() {
        drawer.openDrawer();
    }


    /**
     * [method]初始化侧边栏
     */
    private void initDrawer(){
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(R.drawable.ic_avatarholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }

        });
        drawer = null;

        String url;
        if (BasicInfo.TYPE.equals("S"))
            url = new GetInfoPictureRequest("S", null, String.valueOf(BasicInfo.ID)).getWholeUrl();
        else
            url = new GetInfoPictureRequest("T", String.valueOf(BasicInfo.ID), null).getWholeUrl();

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg_login)
                .addProfiles(new ProfileDrawerItem().withName(BasicInfo.mName).withEmail(BasicInfo.mSignature).withIcon(url))
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();

        drawerHead = headerResult.getProfiles();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1)
                .withIcon(getDrawable(R.drawable.ic_drawer_homepage_24dp)).withName("我的主页");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2)
                .withIcon(getDrawable(R.drawable.ic_drawer_chat_24dp)).withName("我的会话");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3)
                .withIcon(getDrawable(R.drawable.ic_drawer_focus_24dp)).withName("我的关注");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4)
                .withIcon(getDrawable(R.drawable.ic_drawer_info_24dp)).withName("我的通知");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5)
                .withIcon(getDrawable(R.drawable.ic_drawer_settings_24dp)).withName("修改密码");
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6)
                .withIcon(getDrawable(R.drawable.ic_drawer_quit_24dp)).withName("退出登录");

        drawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem(),
                        item4,
                        new DividerDrawerItem(),
                        item5,
                        new DividerDrawerItem(),
                        item6
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        System.out.println("printed" + position);
                        // do something with the clicked item :D
                        switch (position){
                            case 1:
                            {
                                drawer.closeDrawer();
                                viewPager.setCurrentItem(4);
                                navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                                break;
                            }
                            case 3:
                            {
                                drawer.closeDrawer();
                                viewPager.setCurrentItem(2);
                                navView.getMenu().findItem(R.id.navigation_conversations).setChecked(true);
                                break;
                            }
                            case 5:
                            { ;
                                drawer.closeDrawer();
                                viewPager.setCurrentItem(1);
                                navView.getMenu().findItem(R.id.navigation_follow).setChecked(true);
                                break;
                            }
                            case 7:
                            {
                                drawer.closeDrawer();
                                viewPager.setCurrentItem(3);
                                navView.getMenu().findItem(R.id.navigation_notifications).setChecked(true);
                                break;
                            }
                            case 9:{
                                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case 11:{
                                logout();
                                break;
                            }
                            default: break;
                        }

                        return true;
                    }
                })
                .build();
        drawer.getDrawerLayout().setStatusBarBackgroundColor(Color.TRANSPARENT);
        System.out.println(getWindow().getDecorView().getSystemUiVisibility() + ">>>>>>>>>>>>>>>>>>>>>>>>>");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void logout() {
        new LogoutRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
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
                        // 登出时清除share中的信息
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasLogin",false);
                        editor.commit();

                        BasicInfo.reset();
                        MainActivity.this.finish();
//                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                        startActivity(intent);
                    } else {
                        String info = jsonObject.getString("info");
                    }
                } catch (JSONException e) {
                }
            }
        }).send();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 获取当前fragment
            Fragment current = mViewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());

            // 主页双击返回退出程序
            if(current != null && current instanceof HomeFragment){
                if(System.currentTimeMillis() - exitTime > 2000){
                    exitTime = System.currentTimeMillis();
                } else {
                    App.appExit(MainActivity.this);
                }
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " " + resultCode);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> mSelected = Matisse.obtainPathResult(data);
            String path = mSelected.get(0);
//            DashboardFragment fragment = (DashboardFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.nav_host_fragment).getChildFragmentManager()
//                    .getPrimaryNavigationFragment();

            DashboardFragment fragment = ((DashboardFragment) mViewPagerAdapter.getRegisteredFragment(4));
            fragment.getAvatar("file://" + path);
            System.out.println(path);
            new UpdateInfoPictureRequest(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
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
                            System.out.println(BasicInfo.PATH);
                            MyImageLoader.invalidate();
                            runOnUiThread(()-> {
                                initDrawer();
                                HomeFragment fragment1 = ((HomeFragment) mViewPagerAdapter.getRegisteredFragment(0));
                                fragment1.getAvatar();
                                FollowFragment fragment2 = ((FollowFragment) mViewPagerAdapter.getRegisteredFragment(1));
                                fragment2.getAvatar();
                                ConversationFragment fragment3 = ((ConversationFragment) mViewPagerAdapter.getRegisteredFragment(2));
                                fragment3.getAvatar();
                                NotificationFragment fragment4 = ((NotificationFragment) mViewPagerAdapter.getRegisteredFragment(3));
                                fragment4.getAvatar();
                                DashboardFragment fragment5 = ((DashboardFragment) mViewPagerAdapter.getRegisteredFragment(4));
                                fragment5.getAvatar(null);
                            });
//                            ((DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_dashboard)).getAvatar(null);
                            String info = jsonObject.getString("info");
                        }
                    } catch (JSONException e) {
                    }
                }
            }, Uri2File.convert(path)).send();
        }
    }



}
