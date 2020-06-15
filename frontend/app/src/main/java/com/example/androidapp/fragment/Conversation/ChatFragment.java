package com.example.androidapp.fragment.Conversation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.ChatActivity;
import com.example.androidapp.chatTest.fixtures.DialogsFixtures;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment implements DateFormatter.Formatter{
    private DialogsList dialogsList;
    private List<ChatItem> mNameList;
    private DialogsListAdapter dialogsAdapter;
    private ImageLoader imageLoader;


    public ChatFragment() {

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

        return root;

    }

    @Override
    public String format(Date date) {

//        SimpleDateFormat bjSdf = new SimpleDateFormat();     // 北京
//        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
//        try {
//            date = bjSdf.parse(bjSdf.format(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
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
