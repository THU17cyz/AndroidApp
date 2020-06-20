package com.example.androidapp.fragment.Conversation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapp.activity.ChatActivity;
import com.example.androidapp.chatTest.fixtures.DialogsFixtures;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.R;
import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.repository.chathistoryhasread.ChatHistoryHasRead;
import com.example.androidapp.request.conversation.GetAllMessagesRequest;
import com.example.androidapp.request.conversation.GetContactListRequest;
import com.example.androidapp.request.conversation.GetMessageDetailRequest;
import com.example.androidapp.request.conversation.GetMessageRequest;
import com.example.androidapp.request.conversation.GetNewMessagesRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.DateUtil1;
import com.example.androidapp.util.DateUtil3;
import com.example.androidapp.viewmodel.ChatHistoryHasReadViewModel;
import com.example.androidapp.viewmodel.ChatHistoryViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Watchable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLDisplay;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatFragment extends Fragment implements DateFormatter.Formatter{
    private DialogsList dialogsList;
    private List<ChatItem> mNameList;
    private DialogsListAdapter dialogsAdapter;
    private ImageLoader imageLoader;

    private int currentMessageId;
    private int messageId;
    private List<Integer> messageIdList;
    private ChatHistoryViewModel chatHistoryViewModel;
    private String userAccount;

    private ArrayList<Dialog> dialogs;

    private ArrayList<ChatHistory> tmpChatHistoryList;


    private int type;//0:老师 1：学生

    private Handler mHandler = new Handler(Looper.getMainLooper());
    boolean hasHandled = false;


    public ChatFragment(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        dialogsList = root.findViewById(R.id.dialogsList);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
               Picasso.get().load("DDD").placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);
                // Log.d("url",url);
            }
        };

        tmpChatHistoryList = new ArrayList<>();
        userAccount = BasicInfo.ACCOUNT;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        currentMessageId = sharedPreferences.getInt(BasicInfo.ACCOUNT,0);

        dialogsAdapter = new DialogsListAdapter<>(imageLoader);

        User user  =new User("0","ming","http://i.imgur.com/pv1tBmT.png",false);
        dialogs = new ArrayList<>();

        dialogsAdapter.setItems(dialogs);

        dialogsAdapter.setDatesFormatter(this);

        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
            @Override
            public void onDialogClick(IDialog dialog) {
                // todo
                User contact = (User)dialog.getUsers().get(0);
                String s = contact.getName();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user",userAccount);
                intent.putExtra("contact",contact.getName());
                intent.putExtra("contact_id",contact.getId());
                intent.putExtra("contact_type",contact.getType());
                startActivity(intent);
            }
        });

        dialogsList.setAdapter(dialogsAdapter);


        RefreshLayout refreshLayout = (RefreshLayout) root.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                newTest();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });

//        chatHistoryViewModel = ViewModelProviders.of(getActivity()).get(ChatHistoryViewModel.class);
//        chatHistoryViewModel.getAllHistory().observe(getActivity(), new Observer<List<ChatHistory>>() {
//            @Override
//            public void onChanged(List<ChatHistory> chatHistories) {
//
//            }
//        });

        Button button = root.findViewById(R.id.btn_refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // refreshData();
                Log.d("d","");
            }
        });

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(type==0){
            newTest();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(type==0){
//          mTimeCounterRunnable.run();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mTimeCounterRunnable);
    }

    public void newTest(){
        // 初步构造联系人列表
//        LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
//        List<ChatHistory> chatList = list.getValue();
//        if(chatList==null){
//            Toast.makeText(getActivity(),"出错null",Toast.LENGTH_SHORT).show();
//            Log.e("错误","数据库获取为null");
//        }
//        else {
//
//            if(dialogs.size()!=0){
//                dialogs.clear();
//            }
//
//            List<String> accounts = new ArrayList<>();//对方列表
//            List<String> messages = new ArrayList<>();//对方最新消息列表
//            List<Date> dates = new ArrayList<>();//对方最新消息时间列表
//            List<String> ids = new ArrayList<>();//对方id列表
//            List<String> types = new ArrayList<>();//对方类型列表
//
//            for(int i=0;i<chatList.size();i++){
//                ChatHistory chat = chatList.get(i);
//                // 判断是否为当前用户的消息
//                if(chat.getUser().equals(BasicInfo.ACCOUNT)){
//
//                    if(accounts.contains(chat.getContact())){
//                        // 如果已经在列表中则更新最新消息
//                        int index = accounts.indexOf(chat.getContact());
//                        messages.set(index,chat.getContent());
//                        dates.set(index,chat.getTime());
//                    } else {
//                        // 如果不在则加入
//                        accounts.add(chat.getContact());
//                        messages.add(chat.getContent());
//                        dates.add(chat.getTime());
//                        ids.add(chat.getContactId());
//                        types.add(chat.getContactType());
//                    }
//                }
//            }
//            for(int i=0;i<accounts.size();i++){
//                User user = new User(ids.get(i),accounts.get(i),"",accounts.get(i),types.get(i));
//                Message message = new Message("", user,messages.get(i),dates.get(i));
//                Dialog dialog = new Dialog(String.valueOf(i),accounts.get(i),"",
//                        new ArrayList<>(Arrays.asList(user)),
//                        message,0);
//                dialogs.add(dialog);
//                dialogsAdapter.notifyDataSetChanged();
//            }
//        }

        dialogs.clear();
        int i = 0;
        for (String account: BasicInfo.CHAT_HISTORY.keySet()) {
            System.out.println("account" + account);
            ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(account);
            Message message = msgs.get(msgs.size() - 1);
            User user = message.getUser();
            Dialog dialog = new Dialog(String.valueOf(i), account, "",
                    new ArrayList<>(Arrays.asList(user)),
                    message, message.isRead() ? 0 : 1);
            dialogs.add(dialog);
            i++;
        }
        dialogsAdapter.notifyDataSetChanged();

//        // 先获取本地聊天id
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
//        currentMessageId = sharedPreferences.getInt(BasicInfo.ACCOUNT,0);
//        Log.e("当前id:",String.valueOf(currentMessageId));
//
//        // 网络请求聊天记录
//        new GetMessageRequest(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error","获取最新id失败");
//                hasHandled = true;
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("回复",resStr);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(resStr);
//                    Boolean status = jsonObject.getBoolean("status");
//                    if(status){
//                        messageId = jsonObject.getInt("message_id");
//                        Log.e("收到最新id：",String.valueOf(messageId));
//
//                        if(messageId==-1){
//                            hasHandled = true;
//                            Log.e("hasHandled置true","，");
//                            // 无消息
//                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putInt(BasicInfo.ACCOUNT,0);
//                            editor.commit();
//                        }
//                        else if(messageId==currentMessageId){
//                            hasHandled =true;
//                            Log.e("hasHandled置true","，");
//                            // 无新操作
//                        } else if(messageId>currentMessageId){
//                            new GetNewMessagesRequest(new Callback() {
//                                @Override
//                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                    Log.e("error","获取最新消息失败");
//                                    hasHandled = true;
//                                    Log.e("hasHandled置true","，");
//                                }
//
//                                @RequiresApi(api = Build.VERSION_CODES.O)
//                                @Override
//                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                    String resStr = response.body().string();
//                                    JSONObject jsonObject = null;
//                                    try {
//                                        jsonObject = new JSONObject(resStr);
//                                        Boolean status = jsonObject.getBoolean("status");
//                                        if (status) {
//                                            JSONArray jsonArray = (JSONArray) jsonObject.get("message_info_list");
//
//                                            for (int i = 0; i < jsonArray.length(); i++){
//                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                                int messageId = jsonObject1.getInt("message_id");
//                                                String objectType = jsonObject1.getString("object_type");
//                                                String objectId = jsonObject1.getString("object_id");
//                                                String objectAccount = jsonObject1.getString("object_account");
//                                                String objectName = jsonObject1.getString("object_name");
//                                                String messageWay = jsonObject1.getString("message_way");
//                                                String messageType = jsonObject1.getString("message_type");
//                                                String messageContent = jsonObject1.getString("message_content");
//                                                String messageTime = jsonObject1.getString("message_time");
//
//                                                // SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
//                                                // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//
//                                                Log.e("插入",new Date().toString()+" "+messageContent+" "+ messageType+" "+messageWay+" "+BasicInfo.ACCOUNT+" "+objectAccount+" "+ objectId+ " "+objectType);
//
//                                                getActivity().runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        chatHistoryViewModel.insert(new ChatHistory(new Date(),messageContent,messageType,messageWay,BasicInfo.ACCOUNT,objectAccount,objectId,objectType));
//                                                    }
//                                                });
//
//                                                boolean hasMatched = false;
//                                                for (int idx=0;idx<dialogs.size();idx++){
//                                                    if(dialogs.get(idx).getUsers().get(0).getAccount().equals(objectAccount)){
//                                                        // todo 异步
//                                                        // 如果有重名则更新
//                                                        dialogs.get(idx).getLastMessage().setText(messageContent);
//                                                        dialogs.get(idx).getLastMessage().setCreatedAt(DateUtil3.parse(messageTime));
//                                                        // 不知道要不要uiThread
//                                                        getActivity().runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                dialogsAdapter.notifyDataSetChanged();
//                                                            }
//                                                        });
//
//                                                        hasMatched = true;
//                                                        break;
//                                                    }
//                                                }
//                                                if(hasMatched==false){
//                                                    // 没匹配到则加项
//                                                    User user = new User(objectId,objectAccount,"",objectAccount,objectType);
//                                                    Message message = new Message("",user,messageContent,DateUtil3.parse(messageTime));
//                                                    Dialog dialog = new Dialog(String.valueOf(i),objectAccount,"",
//                                                            new ArrayList<>(Arrays.asList(user)),
//                                                            message,0);
//                                                    dialogs.add(dialog);
//                                                    getActivity().runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            dialogsAdapter.notifyDataSetChanged();
//                                                        }
//                                                    });
//
//                                                }
//                                            }
//
//                                            // tmp: 得到所有新消息后，直接更新currentMessageId
//                                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                                            editor.putInt(BasicInfo.ACCOUNT,messageId);
//                                            editor.commit();
//                                            hasHandled = true;
//                                            Log.e("hasHandled置true","，");
//                                            Log.e("更新后id：",String.valueOf(messageId));
//
//
//                                        } else {
//                                            hasHandled = true;
//                                            Log.e("hasHandled置true","，");
//                                        }
//
//
//
//                                    } catch (JSONException | ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            },String.valueOf(currentMessageId)).send();
//                        }
//                    }
//                    else {
//                        hasHandled = true;
//                        Log.e("hasHandled置true","，");
//                        String info = jsonObject.getString("info");
//                        Log.e("error",info);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).send();

    }


    @Override
    public String format(Date date) {

//        if (DateFormatter.isToday(date)) {
//            return DateFormatter.format(date, DateFormatter.Template.TIME);
//            //return "今天";
//        } else if (DateFormatter.isYesterday(date)) {
//            return "昨天";
//        } else if (DateFormatter.isCurrentYear(date)) {
//            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
//        } else {
//            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
//        }
        try {
            String s =  DateUtil3.formatDate(date);
            return s;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "wrong";
    }

    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {//在此添加需轮寻的接口
            Log.e("联系人列表轮询","+1");
            if(hasHandled){
                hasHandled = false;
                Log.e("hasHandles置false","..");
                newTest();//getUnreadCount()执行的任务
            }

            mHandler.postDelayed(this, 20 * 1000);
        }
    };
}

