package com.example.androidapp.fragment.Conversation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.activity.ChatActivity;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.request.information.SetInformationStateRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.DateUtil3;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ChatFragment extends Fragment implements DateFormatter.Formatter {
    private DialogsList dialogsList;
    private List<ChatItem> mNameList;
    private DialogsListAdapter dialogsAdapter;
    private ImageLoader imageLoader;

    private int currentMessageId;
    private int messageId;
    private List<Integer> messageIdList;
    private String userAccount;

    private ArrayList<Dialog> dialogs;

    private ArrayList<ChatHistory> tmpChatHistoryList;

    // 全标已读
    @BindView(R.id.btn_all_read)
    TextView btnAllRead;

    private Unbinder unbinder;


    private int type; //0:老师 1：学生

    private Handler mHandler = new Handler(Looper.getMainLooper());
    boolean hasHandled = false;

    public ChatFragment() { }

    public ChatFragment(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        unbinder = ButterKnife.bind(this, root);

        dialogsList = root.findViewById(R.id.dialogsList);
        imageLoader = (imageView, url, payload) -> {
           Picasso.get().load(url).placeholder(R.drawable.ic_avatarholder).into(imageView);
            // Log.d("url",url);
        };

        tmpChatHistoryList = new ArrayList<>();
        userAccount = BasicInfo.ACCOUNT;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        currentMessageId = sharedPreferences.getInt(BasicInfo.ACCOUNT,0);

        dialogsAdapter = new DialogsListAdapter<>(imageLoader);

        dialogs = new ArrayList<>();

        dialogsAdapter.setItems(dialogs);

        dialogsAdapter.setDatesFormatter(this);

        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
            @Override
            public void onDialogClick(IDialog dialog) {
                // todo
                User contact = (User) dialog.getUsers().get(0);
                ((Dialog) dialog).getLastMessage().setRead();
                // System.out.println(BasicInfo.BADGE_CHAT.getNumber());
                BasicInfo.subFromBadgeChat(dialog.getUnreadCount());

                ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(contact.getAccount());
                for (Message m: msgs) {
                    m.setRead();
                }
                System.out.println(contact.getAccount() + contact.getId() + contact.getName());
                String s = contact.getName();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user", userAccount);
                intent.putExtra("contact", contact.getAccount());
                intent.putExtra("real_name", contact.getName());
                intent.putExtra("contact_id", contact.getUserId());
                intent.putExtra("contact_type", contact.getType());
                startActivity(intent);
            }
        });

        dialogsList.setAdapter(dialogsAdapter);


//        RefreshLayout refreshLayout = (RefreshLayout) root.findViewById(R.id.refreshLayout);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                newTest();
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//            }
//        });

        btnAllRead.setOnClickListener(v -> {
            for(int i = 0; i < dialogs.size(); i++){
                Dialog dialog = dialogs.get(i);
                BasicInfo.subFromBadgeChat(dialog.getUnreadCount());
                dialog.setUnreadCount(0);
                User contact = dialog.getUsers().get(0);
                ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(contact.getAccount());
                for (Message m: msgs) {
                    m.setRead();
                }
                dialogsAdapter.notifyDataSetChanged();
            }
        });

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if(type==0){
//            newTest();
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
//        if(type==0){
//          mTimeCounterRunnable.run();
//        }
        mTimeCounterRunnable.run();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mTimeCounterRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void newTest(){
        // 初步构造联系人列表

        dialogs.clear();
        int i = 0;
        for (String account: BasicInfo.CHAT_HISTORY.keySet()) {
            ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(account);
            if (msgs.isEmpty()) continue;

            int size = msgs.size() - 1;
            int count = 0;
            Message message = msgs.get(size);
            while (size >= 0) {
                Message tmp = msgs.get(size);
                if (tmp.isRead() && tmp.getUser().getId().equals("1")) break;
                else if (!tmp.isRead()) count++;
                size--;
            }
            User user = message.getUser();
            if (message.getImageUrl() != null) message.setText("[图片]");
            System.out.println(user.getAccount() + user.getId() + user.getName());
            Dialog dialog = new Dialog(String.valueOf(i), user.getName(), user.getAvatar(),
                    new ArrayList<>(Arrays.asList(user)),
                    message, count);
            dialogs.add(dialog);
            i++;
        }
        Collections.sort(dialogs, (p1, p2) -> p2.getLastMessage().getCreatedAt().compareTo(p1.getLastMessage().getCreatedAt()));
        dialogsAdapter.notifyDataSetChanged();



    }


    @Override
    public String format(Date date) {
        try {
            String s =  DateUtil3.formatDate(date);
            return s;
        } catch (ParseException e) {
            return "";
        }
    }

    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {//在此添加需轮寻的接口
            Log.e("联系人列表轮询","+1");
//            if(hasHandled){
//                hasHandled = false;
//                Log.e("hasHandles置false","..");
//
//            }
            newTest();//getUnreadCount()执行的任务
            mHandler.postDelayed(this, 3 * 1000);
        }
    };
}

