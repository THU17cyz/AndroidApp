package com.example.androidapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidapp.ChatTest.GifSizeFilter;
import com.example.androidapp.ChatTest.fixtures.MessagesFixtures;
import com.example.androidapp.ChatTest.model.Message;
import com.example.androidapp.ChatTest.model.User;
import com.example.androidapp.R;
import com.gyf.immersionbar.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends AppCompatActivity
        implements
        DateFormatter.Formatter,
        DialogInterface.OnClickListener{
  private static final int REQUEST_CODE_CHOOSE = 10;
  private List<Uri> mUris;
  private List<String> mPaths;

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
//        messagesAdapter.enableSelectionMode(new MessagesListAdapter.SelectionListener() {
//            @Override
//            public void onSelectionChanged(int count) {
//
//
//            }
//        });


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
                Toast.makeText(getApplicationContext(),message.getText()+"clilcked",Toast.LENGTH_SHORT).show();
            }
        });

        messagesList.setAdapter(messagesAdapter);



        messageInput = findViewById(R.id.input);
        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                messagesAdapter.addToStart(
                        new Message("0",new User("0","ming",null,true),input.toString())
                        ,true);
                return true;
            }
        });
        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                Toast.makeText(getApplicationContext(),"attachment",Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(ChatActivity.this)
                        .setItems(R.array.view_types_dialog, ChatActivity.this)
                        .show();
//                messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);

            }
        });

    }



    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "今天";
        } else if (DateFormatter.isYesterday(date)) {
            return "昨天";
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


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
//                messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);

              Matisse.from(ChatActivity.this)
                      .choose(MimeType.ofImage(), false)
                      .countable(true)
                      .capture(true)
                      .captureStrategy(
                              new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                      .maxSelectable(9)
                      .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                      .gridExpectedSize(
                              getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                      .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                      .thumbnailScale(0.85f)
                      .imageEngine(new GlideEngine())
                      .setOnSelectedListener((uriList, pathList) -> {
                        Log.e("onSelected", "onSelected: pathList=" + pathList);
                      })
                      .showSingleMediaType(true)
                      .originalEnable(true)
                      .maxOriginalSize(10)
                      .autoHideToolbarOnSingleTap(true)
                      .setOnCheckedListener(isChecked -> {
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                      })
                      .forResult(REQUEST_CODE_CHOOSE);
                break;
            case 1:
//                messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
        mUris = Matisse.obtainResult(data);
        mPaths = Matisse.obtainPathResult(data);
        Log.d("Matisse", "mSelected: " + mUris);

        for(int i=0;i<mUris.size();i++){
          Message message = new Message("0",new User("0","ming",null,true),null);
          message.setImage(new Message.Image(mUris.get(i).toString()));
          Log.d("Matisse print",  mUris.get(i).toString());
          messagesAdapter.addToStart(message, true);
        }
//content://media/external/images/media/27
//        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);

      }

    }
}
