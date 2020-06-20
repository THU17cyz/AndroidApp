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
import java.util.Collections;
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
                User contact = (User) dialog.getUsers().get(0);
                System.out.println(contact.getAccount() + contact.getId() + contact.getName());
                String s = contact.getName();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user", userAccount);
                intent.putExtra("contact", contact.getAccount());
                intent.putExtra("contact_id", contact.getId());
                intent.putExtra("contact_type", contact.getType());
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

        dialogs.clear();
        int i = 0;
        for (String account: BasicInfo.CHAT_HISTORY.keySet()) {
            ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(account);
            if (msgs.isEmpty()) continue;
            Message message = msgs.get(msgs.size() - 1);
            User user = message.getUser();
            System.out.println(user.getAccount() + user.getId() + user.getName());
            Dialog dialog = new Dialog(String.valueOf(i), account, "",
                    new ArrayList<>(Arrays.asList(user)),
                    message, message.isRead() ? 0 : 1);
            dialogs.add(dialog);
            i++;
        }
        Collections.sort(dialogs, (p1, p2) -> p2.getLastMessage().getCreatedAt().compareTo(p1.getLastMessage().getCreatedAt()));
        dialogsAdapter.notifyDataSetChanged();



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

