package com.example.androidapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.androidapp.R;
import com.example.androidapp.UI.conversation.ConversationFragment;
import com.example.androidapp.UI.dashboard.DashboardFragment;
import com.example.androidapp.UI.follow.FollowFragment;
import com.example.androidapp.UI.home.HomeFragment;
import com.example.androidapp.UI.notification.NotificationFragment;
import com.example.androidapp.application.App;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.request.user.LogoutRequest;
import com.example.androidapp.request.user.UpdateInfoPictureRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Uri2File;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

//    Fragment fragment1 = new HomeFragment();
//    Fragment fragment2 = null;// = new FollowFragment();
//    Fragment fragment3 = null;// = new ConversationFragment();
//    Fragment fragment4 = null;// = new NotificationFragment();
//    Fragment fragment5 = null;// = new DashboardFragment();
//    Fragment active = fragment1;

    final FragmentManager fm = getSupportFragmentManager();

    private static final int REQUEST_CODE_CHOOSE = 11;
    private long exitTime = 0;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
    private Drawer drawer;

    public static Handler msgHandler;

    private NavController navController;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_query, R.id.navigation_notifications, R.id.navigation_dashboard)
//                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment5, "5").hide(fragment5).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();


//        fm.beginTransaction().add(R.id.nav_host_fragment,fragment1, "1").commit();
//        navView.setOnNavigationItemSelectedListener(
//                item -> {
//                    switch (item.getItemId()) {
//                        case R.id.navigation_home: {
//                            fm.beginTransaction().hide(active).show(fragment1).commit();
//                            active = fragment1;
//                            return true;
//                        }
//                        case R.id.navigation_follow: {
//                            if (fragment2 == null) {
//                                fragment2 = new FollowFragment();
//                                fm.beginTransaction().hide(active).add(R.id.nav_host_fragment, fragment2, "2").commit();
//                            } else {
//                                fm.beginTransaction().hide(active).show(fragment2).commit();
//                            }
//                            active = fragment2;
//                            return true;
//                        }
//                        case R.id.navigation_conversations: {
//                            if (fragment3 == null) {
//                                fragment3 = new ConversationFragment();
//                                fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(active).commit();
//                            } else {
//                                fm.beginTransaction().hide(active).show(fragment3).commit();
//                            }
//                            active = fragment3;
//                            return true;
//                        }
//                        case R.id.navigation_notifications: {
//                            if (fragment4 == null) {
//                                fragment4 = new NotificationFragment();
//                                fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(active).commit();
//                            } else {
//                                fm.beginTransaction().hide(active).show(fragment4).commit();
//                            }
//                            active = fragment4;
//                            return true;
//                        }
//                        case R.id.navigation_dashboard: {
//                            if (fragment5 == null) {
//                                fragment5 = new DashboardFragment();
//                                fm.beginTransaction().add(R.id.nav_host_fragment, fragment5, "5").hide(active).commit();
//                            } else {
//                                fm.beginTransaction().hide(active).show(fragment5).commit();
//                            }
//                            active = fragment5;
//                            return true;
//                        }
//                    }
//                    return false;
//                });



        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .init();

        // setSupportActionBar(toolbar);
        // toolbar.inflateMenu(R.menu.top_menu_home);

        // 初始化侧边栏
        initDrawer();

//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.TEST, R.string.TEST
//        );
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();



        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);


        // 初始化websocket
        // WebSocket.initSocket();


//        CommonInterface.sendOkHttpGetRequest("/hello", new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, resStr, Toast.LENGTH_LONG).show());
//                Log.e("response", resStr);
//                try {
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//                } catch (JSONException e) {
//
//                }
//            }
//        });



        // 获取account，id，type供全局使用
        new GetInfoRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                Toast.makeText(getApplicationContext(),"获取全局信息失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        BasicInfo.ACCOUNT = jsonObject.getString("account");
                        if(jsonObject.has("student_id")){
                            BasicInfo.ID = jsonObject.getInt("student_id");
                            BasicInfo.TYPE = "S";
                        }else {
                            BasicInfo.ID = jsonObject.getInt("teacher_id");
                            BasicInfo.TYPE = "T";
                        }
                        Log.d("basic info",BasicInfo.ACCOUNT);
                    }else{
                        String info = jsonObject.getString("info");
                    }
                } catch (JSONException e) {

                }
            }
        },"I",null,null).send();

        // *** HTTP 测试 ***
//        if (Global.HTTP_TEST_MODE) {
//            Log.d("Test", "******************");
//            new ClearAllIntentionRequest(Http.callbackExample_0).send();
//        }

    }
    private void loadImageCache() {
//        String imageCacheDir = getExternalCacheDir().getPath()+"/image/";
//
//        Picasso picasso = new Picasso.Builder(this).downloader(new OkHttpDownloader(new File(imageCacheDir))).build();
///**  setIndicatorsEnabled(true);
//
// * 左上角会显示个三角形，不同的颜色代表加载的来源
//
// * 红色：代表从网络下载的图片
//
// * 黄色：代表从磁盘缓存加载的图片
//
// * 绿色：代表从内存中加载的图片
//
// */
//        picasso.setIndicatorsEnabled(true);
//        Picasso.setSingletonInstance(picasso);
    }


    public void openDrawer() {
        drawer.openDrawer();
    }


    /**
     * [method]初始化侧边栏
     */
    private void initDrawer(){
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg_login)
                .addProfiles(
                        new ProfileDrawerItem().withName("用户名").withEmail("个性签名").withIcon(getResources().getDrawable(R.drawable.ic_drawer_homepage_24dp))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withIcon(getResources().getDrawable(R.drawable.ic_drawer_homepage_24dp)).withName("我的主页");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withIcon(getResources().getDrawable(R.drawable.ic_drawer_chat_24dp)).withName("我的会话");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withIcon(getResources().getDrawable(R.drawable.ic_drawer_focus_24dp)).withName("我的关注");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withIcon(getResources().getDrawable(R.drawable.ic_drawer_info_24dp)).withName("我的通知");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withIcon(getResources().getDrawable(R.drawable.ic_drawer_settings_24dp)).withName("设置");
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withIcon(getResources().getDrawable(R.drawable.ic_drawer_quit_24dp)).withName("退出登录");

        drawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
//                .withToolbar(toolbar)
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
                                navController.navigate(R.id.navigation_dashboard);
                                drawer.closeDrawer();
                                break;
                            }
                            case 3:
                            {
                                navController.navigate(R.id.navigation_conversations);
                                drawer.closeDrawer();
                                break;
                            }
                            case 5:
                            {
                                navController.navigate(R.id.navigation_follow);
                                drawer.closeDrawer();
                                break;
                            }
                            case 7:
                            {
                                navController.navigate(R.id.navigation_notifications);
                                drawer.closeDrawer();
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

        if(keyCode==KeyEvent.KEYCODE_BACK){

            // 获取当前fragment
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getPrimaryNavigationFragment();

            // 主页双击返回退出程序
            if(current != null && current instanceof HomeFragment){
                if(System.currentTimeMillis()-exitTime>2000){
                    Toast.makeText(getApplicationContext(),"再按一次退出程序",
                            Toast.LENGTH_LONG).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    App.appExit(MainActivity.this);
                }
                return false;
            }
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
            DashboardFragment fragment = (DashboardFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment).getChildFragmentManager()
                    .getPrimaryNavigationFragment();
            fragment.getAvatar("file://" + path);
            System.out.println(path);
            new UpdateInfoPictureRequest(new okhttp3.Callback() {
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

                        } else {
                            String info = jsonObject.getString("info");
                        }
                    } catch (JSONException e) {
                    }
                }
            }, Uri2File.convert(path)).send();
        }
    }

}
