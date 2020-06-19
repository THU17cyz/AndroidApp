package com.example.androidapp.UI.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.activity.InfoActivity;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.QueryActivity;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.request.information.GetInformationDetailRequest;
import com.example.androidapp.request.information.GetInformationRequest;
import com.example.androidapp.request.information.SetInformationStateRequest;
import com.example.androidapp.util.MyImageLoader;
import com.kingja.loadsir.core.LoadService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class NotificationFragment extends Fragment implements DateFormatter.Formatter{

  @BindView(R.id.imageButton)
  CircleImageView drawerBtn;

  @BindView(R.id.search_view)
  EditText searchView;

  private NotificationViewModel notificationViewModel;

  @BindView(R.id.dialogsList)
  DialogsList dialogsList;

  @BindView(R.id.refreshLayout)
  RefreshLayout refreshLayout;

  private ImageLoader imageLoader;

  private DialogsListAdapter dialogsAdapter;

  private List<Integer> informationIdList;
  private ArrayList<Dialog> dialogs;
  private ArrayList<Message> messages;
  private LoadService loadService;
  private User user;

  private Unbinder unbinder;

  // 全标已读
  @BindView(R.id.btn_all_read)
  TextView btnAllRead;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_notification, container, false);
    unbinder = ButterKnife.bind(this,root);
    MyImageLoader.loadImage(drawerBtn);


    //设置头像
    imageLoader = new ImageLoader() {
      @Override
      public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
        MyImageLoader.loadImage(imageView, url);
      }
    };
    dialogsAdapter = new DialogsListAdapter<>(imageLoader);

    user  =new User("0","","zx",false);

    dialogs = new ArrayList<>();
//    Dialog dialog = new Dialog("0","联系人一","zx",
//            new ArrayList<User>(Arrays.asList(user)),
//            new Message("0",user,"一句话"),0);
//    dialogs.add(dialog);

    dialogsAdapter.setItems(dialogs);
    dialogsAdapter.setDatesFormatter(this);

    dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
      @Override
      public void onDialogClick(IDialog dialog) {
        // todo
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        intent.putExtra("text", "ok");
//        intent.putExtra("text", messages.get(Integer.parseInt(dialog.getId())).getText());
        startActivity(intent);
      }
    });
    dialogsList.setAdapter(dialogsAdapter);



    refreshData(false);


    refreshLayout = (RefreshLayout) root.findViewById(R.id.refreshLayout);
    refreshLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(RefreshLayout refreshlayout) {
        refreshData(true);
        refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
      }
    });

    btnAllRead.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        for(int i=0;i<dialogs.size();i++){
          Dialog dialog = dialogs.get(i);
          // 修改消息状态
          new SetInformationStateRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
              getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  dialog.setUnreadCount(0);
                  dialogsAdapter.notifyDataSetChanged();
                }
              });
            }
          },dialogs.get(i).getLastMessage().getId(),"R").send();

        }
      }
    });

    dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
      @Override
      public void onDialogClick(IDialog dialog) {

        // 更新未读条数
        Dialog d = (Dialog)dialog;
        d.setUnreadCount(0);
        dialogsAdapter.notifyDataSetChanged();

        // 修改消息状态
        new SetInformationStateRequest(new okhttp3.Callback() {
          @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {

          }

          @Override
          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

          }
        },dialog.getLastMessage().getId(),"R").send();

        // 进入页面
        Intent intent = new Intent(getActivity(),InfoActivity.class);
        intent.putExtra("text",dialog.getLastMessage().getText());
        intent.putExtra("dateString",((Message)dialog.getLastMessage()).getDateString());
        startActivity(intent);


      }

    });


    return root;
  }


  private void refreshData(boolean isRefresh){
//    if (!isRefresh) {
//      loadService = LoadSir.getDefault().register(dialogList, (com.kingja.loadsir.callback.Callback.OnReloadListener) v -> {
//      });
//    }
    // 获取消息id列表
    new GetInformationRequest(new okhttp3.Callback() {
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
          JSONArray jsonArray = (JSONArray) jsonObject.get("information_id_list");
          informationIdList = new ArrayList<>();
          for (int i = 0; i < jsonArray.length(); i++) {
            informationIdList.add(jsonArray.getInt(i));
          }

          //在获取id列表的基础上获取每条消息
          if(informationIdList!=null){

            messages = new ArrayList<>();

            for(int i=0;i<informationIdList.size();i++){
              String id = informationIdList.get(i).toString();
              new GetInformationDetailRequest(new okhttp3.Callback(){
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
                      String time = jsonObject.getString("information_time");
                      String state = jsonObject.getString("information_state");
                      // todo 解析byte
                      String content = (String)jsonObject.get("information_content");

                      Message message;
                      SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
                      if(state.equals("N")){
                        message = new Message(id,user,content,sdf.parse(time),false);
                      } else {
                        message = new Message(id,user,content,sdf.parse(time),true);
                      }
                      message.setDateString(time);
                      Log.e("消息内容",sdf.parse(time).toString()+" "+state);

                      getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                          if(message.isRead()){
                            dialogs.add(new Dialog("1","系统通知",null,new ArrayList<User>(Arrays.asList(user)),message,0));
                            dialogsAdapter.notifyDataSetChanged();;
                          } else {
                            dialogs.add(new Dialog("1","系统通知",null,new ArrayList<User>(Arrays.asList(user)),message,1));
                            dialogsAdapter.notifyDataSetChanged();;
                          }
                        }
                      });

                    }else{
                      String info = jsonObject.getString("info");
                      getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                  } catch (JSONException | ParseException e) {

                  }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
              },String.valueOf(informationIdList.get(i))).send();
            }

          }

        } catch (JSONException e) {

        }
      }
    }).send();

    // 根据id列表获取消息

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

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    drawerBtn.setOnClickListener(v -> {
      MainActivity parentActivity = (MainActivity) getActivity();
      parentActivity.openDrawer();
    });
    searchView.setOnClickListener(v -> {
      Intent intent = new Intent(getActivity(), QueryActivity.class);
      startActivity(intent);
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
