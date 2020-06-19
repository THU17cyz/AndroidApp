package com.example.androidapp.fragment.Conversation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.androidapp.request.conversation.GetContactListRequest;
import com.example.androidapp.request.conversation.GetMessageDetailRequest;
import com.example.androidapp.request.conversation.GetMessageRequest;
import com.example.androidapp.request.conversation.GetNewMessagesRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatFragment extends Fragment implements DateFormatter.Formatter{
    private DialogsList dialogsList;
    private List<ChatItem> mNameList;
    private DialogsListAdapter dialogsAdapter;
    private ImageLoader imageLoader;

    private List<Integer> contactIdList;
    private List<String> contactList;
    private List<Dialog> dialogList;
    private int currentMessageId;
    private int messageId;
    private List<Integer> messageIdList;
    private ChatHistoryViewModel chatHistoryViewModel;
    private ChatHistoryHasReadViewModel chatHistoryHasReadViewModel;
    private String userAccount;

    private ArrayList<Dialog> dialogs;

    private ArrayList<ChatHistory> tmpChatHistoryList;


    private int type;//0:老师 1：学生


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
                Picasso.with(getContext()).load("DDD").placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);
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
        Dialog dialog = new Dialog("0","联系人一","http://i.imgur.com/pv1tBmT.png",
                new ArrayList<User>(Arrays.asList(user)),
                new Message("0",user,"一句话"),4);
        dialogs.add(dialog);

        dialogsAdapter.setItems(dialogs);
        dialogs.add(dialog);
        dialogsAdapter.notifyDataSetChanged();
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
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        chatHistoryViewModel = ViewModelProviders.of(getActivity()).get(ChatHistoryViewModel.class);
        chatHistoryViewModel.getAllHistory().observe(getActivity(), new Observer<List<ChatHistory>>() {
            @Override
            public void onChanged(List<ChatHistory> chatHistories) {

            }
        });
        chatHistoryHasReadViewModel = ViewModelProviders.of(this).get(ChatHistoryHasReadViewModel.class);

        Button button = root.findViewById(R.id.btn_refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
                Log.d("d","");
            }
        });

        Button button1 = root.findViewById(R.id.btn_insert);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetContactListRequest(new okhttp3.Callback() {
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
                            if(status){

                                JSONArray jsonArray = (JSONArray) jsonObject.get("conversation_list");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                    String objectType = jsonObject2.getString("object_type");
                                    int objectId = jsonObject2.getInt("object_id");
                                    String objectAccount = jsonObject2.getString("object_account");
                                    String objectName = jsonObject2.getString("object_name");


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 获取与该联系人的最新消息
                                    List<ChatHistory> chatHistoryList = getChatHistory(userAccount,objectAccount);
                                    ChatHistory chatHistory = null;
                                    if(chatHistoryList!=null){
                                        chatHistory = chatHistoryList.get(0);
                                    }

                                    // 构造联系人列表
                                    User user1 = new User(String.valueOf(objectId),objectName,"",objectAccount,objectType);
                                    Message message = new Message("",user1,"",new Date(),false);
                                    Dialog dialog = new Dialog("1",objectAccount,"",
                                            new ArrayList<>(Arrays.asList(user1)),
                                            message,1);
                                    dialogs.add(dialog);
                                    dialogsAdapter.notifyDataSetChanged();
                                        }
                                    });

                                }
                            }else{
                                String info = jsonObject.getString("info");

                            }
                        } catch (JSONException e) {

                        }
                    }
                }).send();

            }
        });
//


        test();

        // refreshData();
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // refreshData();
    }


    private void refreshData(){
        //获取最新消息id
        new GetMessageRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();

                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        messageId = jsonObject.getInt("message_id");
                        if(messageId==-1){
                            //没有信息 则列表为空
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(BasicInfo.ACCOUNT,0);
                            editor.commit();
                        } else if (messageId==currentMessageId) {
                            // 没有新信息 直接构造联系人列表
                            new GetContactListRequest(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.e("error", e.toString());
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String resStr = response.body().string();
                                    try {
                                        // 解析json，然后进行自己的内部逻辑处理
                                        JSONObject jsonObject = new JSONObject(resStr);
                                        Boolean status = jsonObject.getBoolean("status");
                                        if(status){

                                            JSONArray jsonArray = (JSONArray) jsonObject.get("conversation_list");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                                String objectType = jsonObject2.getString("object_type");
                                                int objectId = jsonObject2.getInt("object_id");
                                                String objectAccount = jsonObject2.getString("object_account");
                                                String objectName = jsonObject2.getString("object_name");
                                                List<ChatHistory> chatHistoryList = chatHistoryViewModel.getAllHistory().getValue();
                                                ChatHistory chatHistory = null;
                                                if(chatHistoryList!=null&&chatHistoryList.size()>0){
                                                    chatHistory = chatHistoryList.get(0);
                                                }
                                                User user1;
                                                Message message;
                                                Dialog dialog;

                                                if(chatHistory==null){
                                                    user1 = new User(String.valueOf(objectId),objectName,"",objectAccount,objectType);
                                                    message = new Message("",user1,"",new Date(),false);
                                                    dialog = new Dialog("1",objectAccount,"",
                                                            new ArrayList<>(Arrays.asList(user1)),
                                                            message,1);
                                                } else {
                                                    user1 = new User(String.valueOf(objectId),objectName,"",objectAccount,objectType);
                                                    message = new Message("",user1,chatHistory.getContent(),chatHistory.getTime(),false);
                                                    dialog = new Dialog("1",objectAccount,"",
                                                            new ArrayList<>(Arrays.asList(user1)),
                                                            message,1);
                                                }

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialogs.add(dialog);
                                                        dialogsAdapter.notifyDataSetChanged();
                                                    }
                                                });

                                            }
                                        }else{
                                            String info = jsonObject.getString("info");
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            }).send();

                        } else if(messageId>currentMessageId){
                            // 获取最新消息
                            new GetNewMessagesRequest((new Callback() {
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
                                            messageIdList = new ArrayList<>();

                                            JSONArray jsonArray = (JSONArray) jsonObject.get("message_id_list");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                messageIdList.add(jsonArray.getInt(i));
                                            }

                                            final int[] index = {0};
                                            while(index[0] !=messageIdList.size()){
                                                new GetMessageDetailRequest(new Callback(){
                                                    @Override
                                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                        String resStr = response.body().string();

                                                        Log.e("response", resStr);
                                                        try {
                                                            // 解析json，然后进行自己的内部逻辑处理
                                                            JSONObject jsonObject = new JSONObject(resStr);

                                                            Boolean status = jsonObject.getBoolean("status");
                                                            if(status){

                                                                String time = jsonObject.getString("message_time");
                                                                String content = jsonObject.getString("message_content");
                                                                String messageWay = jsonObject.getString("message_way");
                                                                String objectAccount = jsonObject.getString("object_account");
                                                                int objectId = jsonObject.getInt("object_id");
                                                                String messageType = jsonObject.getString("message_type");

                                                                // 将新消息插入数据库
                                                                SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
                                                                chatHistoryViewModel.insert(new ChatHistory(sdf.parse(time),content,messageType,messageWay,userAccount,objectAccount));
                                                                tmpChatHistoryList.add(new ChatHistory(sdf.parse(time),content,messageType,messageWay,userAccount,objectAccount));
                                                                index[0]++;

                                                            }else{
                                                                String info = jsonObject.getString("info");

                                                            }
                                                        } catch (JSONException | ParseException e) {

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                        Log.e("error", e.toString());
                                                    }
                                                },String.valueOf(messageIdList.get(index[0]))).send();
                                            }

                                            // 获取联系人列表
                                            new GetContactListRequest(new Callback() {
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
                                                        if(status){

                                                            JSONArray jsonArray = (JSONArray) jsonObject.get("conversation_list");

                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                if(i==jsonArray.length()-1){
                                                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putInt(BasicInfo.ACCOUNT,messageId);
                                                                    editor.commit();
                                                                    currentMessageId = messageId;
                                                                }

                                                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                                                String objectType = jsonObject2.getString("object_type");
                                                                int objectId = jsonObject2.getInt("object_id");
                                                                String objectAccount = jsonObject2.getString("object_account");
                                                                String objectName = jsonObject2.getString("object_name");


                                                                // 获取与该联系人的最新消息
                                                                List<ChatHistory> histories = getChatHistory(userAccount,objectAccount);
                                                                ChatHistory chatHistory = null;
                                                                if(histories!=null && histories.size()>0){
                                                                    chatHistory = histories.get(0);
                                                                }
                                                                User user1;
                                                                Message message;
                                                                Dialog dialog;
                                                                if(chatHistory==null){
                                                                    // 构造联系人列表
                                                                    user1 = new User(String.valueOf(objectId),objectName,null,objectAccount,objectType);
                                                                    message = new Message("",user1,"",new Date(),false);
                                                                    dialog = new Dialog("1",objectAccount,"",
                                                                            new ArrayList<>(Arrays.asList(user1)),
                                                                            message,0);
                                                                } else {
                                                                    // 构造联系人列表
                                                                    user1 = new User(String.valueOf(objectId),objectName,null,objectAccount,objectType);
                                                                    message = new Message("",user1,chatHistory.getContent(),chatHistory.getTime(),false);
                                                                    // int unreadCount = chatHistoryHasRead1.getHasRead()?0:1;
                                                                    dialog = new Dialog("1",objectAccount,"",
                                                                            new ArrayList<>(Arrays.asList(user1)),
                                                                            message,0);
                                                                }


                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        dialogs.add(dialog);
                                                                        dialogsAdapter.notifyDataSetChanged();
                                                                    }
                                                                });

                                                            }
                                                        }else{
                                                            String info = jsonObject.getString("info");
                                                        }
                                                    } catch (JSONException e) {

                                                    }
                                                }
                                            }).send();


                                        }
                                        else {
                                            String info = jsonObject.getString("info");

                                        }

                                    } catch (JSONException e) {

                                    }
                                }
                            }),String.valueOf(currentMessageId)).send();
                        }

                    }else{
                        String info = jsonObject.getString("info");

                    }
                } catch (JSONException e) {

                }
            }
        }).send();
    }

    private void test(){
        new GetContactListRequest(new okhttp3.Callback() {
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
                    if(status){

                        JSONArray jsonArray = (JSONArray) jsonObject.get("conversation_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            String objectType = jsonObject2.getString("object_type");
                            int objectId = jsonObject2.getInt("object_id");
                            String objectAccount = jsonObject2.getString("object_account");
                            String objectName = jsonObject2.getString("object_name");


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 获取与该联系人的最新消息
//                                    List<ChatHistory> chatHistoryList = chatHistoryViewModel.getUserContactHistory(userAccount,objectAccount).getValue();
//                                    ChatHistory chatHistory = null;
//                                    if(chatHistoryList!=null){
//                                        chatHistory = chatHistoryList.get(0);
//                                    }

                                    // 获取未读信息
                                    List<ChatHistoryHasRead> chatHistoryHasReadList = chatHistoryHasReadViewModel.getUserContactHistory(userAccount,objectAccount).getValue();
                                    ChatHistoryHasRead chatHistoryHasRead = null;
                                    if(chatHistoryHasReadList!=null){
                                        chatHistoryHasRead = chatHistoryHasReadList.get(0);
                                    }

                                    // 构造联系人列表

//                                    User user1 = new User(String.valueOf(objectId),objectName,null,objectAccount,objectType);
//                                    Message message = new Message("",user1,chatHistory.getContent(),chatHistory.getTime(),chatHistoryHasRead1.getHasRead());
//                                    int unreadCount = chatHistoryHasRead.getHasRead()?0:1;
//                                    Dialog dialog = new Dialog("1",objectAccount,"",
//                                            new ArrayList<>(Arrays.asList(user1)),
//                                            message,unreadCount);
                                    User user1 = new User(String.valueOf(objectId),objectName,"",objectAccount,objectType);
                                    Message message = new Message("",user1,"",new Date(),false);
                                    Dialog dialog = new Dialog("1",objectAccount,"",
                                            new ArrayList<>(Arrays.asList(user1)),
                                            message,1);
                                    dialogs.add(dialog);
                                    dialogsAdapter.notifyDataSetChanged();
                                }
                            });

                        }
                    }else{
                        String info = jsonObject.getString("info");

                    }
                } catch (JSONException e) {

                }
            }
        }).send();
    }

    public void getAllMessage(){

    }


    // 获取两人之间的聊天记录
    public List<ChatHistory> getChatHistory(String user,String contact){

        List<ChatHistory> result = new ArrayList<>();
        LiveData<List<ChatHistory>> list = chatHistoryViewModel.getAllHistory();
        List<ChatHistory> historyList = list.getValue();
        if(historyList!=null) {
            for (int i = 0; i < historyList.size(); i++) {
                ChatHistory history = historyList.get(i);
                if (history.getUser().equals(user) && history.getContact().equals(contact)) {
                    result.add(history);
                }
            }
        }
        return result;
    }

    @Override
    public String format(Date date) {

        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
            //return "今天";
        } else if (DateFormatter.isYesterday(date)) {
            return "昨天";
        } else if (DateFormatter.isCurrentYear(date)) {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }
}
