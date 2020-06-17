package com.example.androidapp.fragment.Conversation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapp.activity.ChatActivity;
import com.example.androidapp.chatTest.fixtures.DialogsFixtures;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.R;
import com.example.androidapp.repository.ChatHistory;
import com.example.androidapp.request.conversation.GetContactListRequest;
import com.example.androidapp.request.conversation.GetMessageDetailRequest;
import com.example.androidapp.request.conversation.GetMessageRequest;
import com.example.androidapp.request.conversation.GetNewMessagesRequest;
import com.example.androidapp.request.user.GetInfoRequest;
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
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
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
    private String userAccount;


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
                Picasso.with(getContext()).load(url).placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);
                Log.d("url",url);
            }
        };

        dialogsAdapter = new DialogsListAdapter<>(imageLoader);

        User user  =new User("0","ming","http://i.imgur.com/pv1tBmT.png",false);
        ArrayList<Dialog> dialogs = new ArrayList<>();
        Dialog dialog = new Dialog("0","联系人一","http://i.imgur.com/pv1tBmT.png",
                new ArrayList<User>(Arrays.asList(user)),
                new Message("0",user,"一句话"),4);
        dialogs.add(dialog);

        dialogsAdapter.setItems(dialogs);
        Log.d("dia",DialogsFixtures.getDialogs().get(0).getDialogPhoto());

        dialogsAdapter.setDatesFormatter(this);
        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
            @Override
            public void onDialogClick(IDialog dialog) {
                // todo
                Intent intent = new Intent(getActivity(), ChatActivity.class);
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


        chatHistoryViewModel = ViewModelProviders.of(this).get(ChatHistoryViewModel.class);
        chatHistoryViewModel.initData("","");

        return root;

    }

    private void getUserAccount(){
        new GetInfoRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        userAccount = jsonObject.getString("account");
                    }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        },"I",null,null);
    }

    private void refreshData(){
        // 得到用户名列表和id列表
        new GetContactListRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){

                        contactIdList = null;
                        contactList = null;
                        contactIdList = new ArrayList<>();
                        contactList = new ArrayList<>();

                        JSONArray jsonArray = (JSONArray) jsonObject.get("conversation_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if(type==0&&jsonObject2.getString("object_type").equals("T")
                            || type==1&&jsonObject2.getString("object_type").equals("S")){
                                contactIdList.add(jsonObject2.getInt("object_id"));
                                contactList.add(jsonObject2.getString("object_account"));
                            }
                    }
                    }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        });

        // 得到头像列表

        //获取最新消息id
        new GetMessageRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        messageId = jsonObject.getInt("message_id");
                    }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        });

        //
        if(messageId==-1){

        }else if(messageId==currentMessageId){

        }else if(messageId>currentMessageId){

            // 获取最新消息id列表
            new GetNewMessagesRequest((new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                    Log.e("response", resStr);
                    try {
                        // 解析json，然后进行自己的内部逻辑处理
                        JSONObject jsonObject = new JSONObject(resStr);

                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            messageIdList = null;
                            messageIdList = new ArrayList<>();

                            JSONArray jsonArray = (JSONArray) jsonObject.get("message_id_list");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                messageIdList.add(jsonArray.getInt(i));
                            }
                        }
                        else {
                            String info = jsonObject.getString("info");
                            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                        }

                    } catch (JSONException e) {

                    }
                }
            }),String.valueOf(messageId));

            for(int i=0;i<messageIdList.size();i++){
                new GetMessageDetailRequest(new okhttp3.Callback(){
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                        Log.e("response", resStr);
                        try {
                            // 解析json，然后进行自己的内部逻辑处理
                            JSONObject jsonObject = new JSONObject(resStr);

                            Boolean status = jsonObject.getBoolean("status");
                            if(status){

                                // 将新消息插入数据库
                                String time = jsonObject.getString("message_time");
                                String content = jsonObject.getString("message_content");
                                String messageWay = jsonObject.getString("message_way");
                                String objectAccount = jsonObject.getString("object_account");
                                String messageType = jsonObject.getString("message_type");
                                chatHistoryViewModel.insert(new ChatHistory(new Date(time),content,messageType,messageWay,userAccount,objectAccount));
                            }else{
                                String info = jsonObject.getString("info");
                                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }
                },String.valueOf(messageIdList.get(i)));
            }
        }

        // 构造对话列表
        dialogList = null;
        dialogList = new ArrayList<>();

        for(int i=0;i<contactList.size();i++){
            User user = new User("1",contactList.get(i),"todo",false);


            Message message = new Message("0",user,"message",new Date());
            Dialog dialog = new Dialog(String.valueOf(i),contactList.get(i),"todo",
                    new ArrayList<User>(Arrays.asList(user)),message, 1);
            dialogList.add(dialog);
        }

        dialogsAdapter.setItems(dialogList);

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
