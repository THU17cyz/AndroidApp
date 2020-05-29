package com.example.androidapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidapp.ChatTest.fixtures.MessagesFixtures;
import com.example.androidapp.ChatTest.model.Message;
import com.gyf.immersionbar.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity
        implements
        DateFormatter.Formatter{
    private MessagesList messagesList;
    private MessagesListAdapter messagesAdapter;
    private MessageInput messageInput;
    private Date lastLoadedDate;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .init();

        messagesList = findViewById(R.id.messagesList);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);
                Log.d("url",url);
            }
        };


        messagesAdapter = new MessagesListAdapter<>("0", imageLoader);
        messagesAdapter.enableSelectionMode(new MessagesListAdapter.SelectionListener() {
            @Override
            public void onSelectionChanged(int count) {


            }
        });



        messagesAdapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
                if (totalItemsCount < 100) {
                    loadMessages();
                }
            }
        });
        messagesAdapter.setDateHeadersFormatter(this);

        messagesAdapter.setOnMessageClickListener(new MessagesListAdapter.OnMessageClickListener() {
            @Override
            public void onMessageClick(IMessage message) {
                Toast.makeText(getApplicationContext(),"clilcked",Toast.LENGTH_SHORT).show();
            }
        });

        messagesList.setAdapter(messagesAdapter);



        messageInput = findViewById(R.id.input);
        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                messagesAdapter.addToStart(
                        MessagesFixtures.getTextMessage(input.toString()), true);
                return true;
            }
        });
        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
            }
        });

    }



    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "today";
        } else if (DateFormatter.isYesterday(date)) {
            return "yes";
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
    }


}
