package com.example.androidapp.fragment.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.activity.ChatActivity;
import com.example.androidapp.entity.chat.Dialog;
import com.example.androidapp.entity.chat.Message;
import com.example.androidapp.entity.chat.User;
import com.example.androidapp.repository.ChatHistory;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.DateUtil3;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 聊天列表界面（其实就是主界面聊天子页，不过分成两个fragment）
 */
public class ChatFragment extends Fragment implements DateFormatter.Formatter {
    @BindView(R.id.btn_all_read)
    TextView btnAllRead;

    private DialogsList dialogsList;
    private DialogsListAdapter dialogsAdapter;
    private ImageLoader imageLoader;
    private int currentMessageId;
    private int messageId;
    private List<Integer> messageIdList;
    private String userAccount;
    private ArrayList<Dialog> dialogs;
    private ArrayList<ChatHistory> tmpChatHistoryList;
    private Unbinder unbinder;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            mHandler.postDelayed(this, 2 * 1000);
        }
    };

    public ChatFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        unbinder = ButterKnife.bind(this, root);

        dialogsList = root.findViewById(R.id.dialogsList);
        imageLoader = (imageView, url, payload) -> {
            Picasso.get().load(url).placeholder(R.drawable.ic_avatarholder).into(imageView);
        };

        tmpChatHistoryList = new ArrayList<>();
        userAccount = BasicInfo.ACCOUNT;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        currentMessageId = sharedPreferences.getInt(BasicInfo.ACCOUNT, 0);

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
                BasicInfo.subFromBadgeChat(dialog.getUnreadCount());

                ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(contact.getAccount());
                for (Message m : msgs) {
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


        btnAllRead.setOnClickListener(v -> {
            for (int i = 0; i < dialogs.size(); i++) {
                Dialog dialog = dialogs.get(i);
                BasicInfo.subFromBadgeChat(dialog.getUnreadCount());
                dialog.setUnreadCount(0);
                User contact = dialog.getUsers().get(0);
                ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(contact.getAccount());
                for (Message m : msgs) {
                    m.setRead();
                }
                dialogsAdapter.notifyDataSetChanged();
            }
        });

        return root;

    }


    @Override
    public void onStart() {
        super.onStart();
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

    public void refresh() {
        // 初步构造联系人列表
        dialogs.clear();
        int i = 0;
        for (String account : BasicInfo.CHAT_HISTORY.keySet()) {
            ArrayList<Message> msgs = BasicInfo.CHAT_HISTORY.get(account);
            if (msgs.isEmpty()) continue;

            int size = msgs.size() - 1;
            int count = 0;
            Message message = msgs.get(size);
            while (size >= 0) {
                Message tmp = msgs.get(size);
                boolean sent = tmp.getUser().getId().equals("1");
                if (tmp.isRead() && sent) break;
                else if (sent) count++;
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
            String s = DateUtil3.formatDate(date);
            return s;
        } catch (ParseException e) {
            return "";
        }
    }
}

