package com.example.androidapp.UI.notification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapp.R;
import com.example.androidapp.activity.ChatActivity;
import com.example.androidapp.activity.InfoActivity;
import com.example.androidapp.activity.LoginActivity;
import com.example.androidapp.chatTest.fixtures.DialogsFixtures;
import com.example.androidapp.chatTest.model.Dialog;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.chatTest.model.User;
import com.example.androidapp.request.information.GetInformationDetailRequest;
import com.example.androidapp.request.information.GetInformationRequest;
import com.example.androidapp.request.information.SetInformationStateRequest;
import com.example.androidapp.util.Hint;
import com.google.android.material.tabs.TabLayout;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NotificationFragment extends Fragment implements DateFormatter.Formatter{

  private NotificationViewModel notificationViewModel;

  @BindView(R.id.dialogsList)
  DialogsList dialogsList;

  @BindView(R.id.refreshLayout)
  RefreshLayout refreshLayout;

  private ImageLoader imageLoader;

  private DialogsListAdapter dialogsAdapter;

  private List<Integer> informationIdList;
  private ArrayList<Dialog> dialogList;
  private ArrayList<Message> messages;
  private LoadService loadService;
  private User user;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_notification, container, false);
    ButterKnife.bind(this,root);

    //设置头像
    imageLoader = new ImageLoader() {
      @Override
      public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
        Picasso.with(getContext()).load(url).placeholder(R.drawable.ic_person_outline_black_24dp).into(imageView);
      }
    };
    dialogsAdapter = new DialogsListAdapter<>(imageLoader);

    user  =new User("0","","zx",false);

    ArrayList<Dialog> dialogs = new ArrayList<>();
    Dialog dialog = new Dialog("0","联系人一","zx",
            new ArrayList<User>(Arrays.asList(user)),
            new Message("0",user,"一句话"),0);
    dialogs.add(dialog);

    dialogsAdapter.setItems(dialogs);
    dialogsAdapter.setDatesFormatter(this);

    dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
      @Override
      public void onDialogClick(IDialog dialog) {
        // todo

//        new SetInformationStateRequest(new okhttp3.Callback() {
//          @Override
//          public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//          }
//
//          @Override
//          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//          }
//        },String.valueOf(informationIdList.get(Integer.valueOf(dialog.getId()))),"R");

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
        if (isRefresh) {
          refreshLayout.finishRefresh(false);
        } else {
//          getActivity().runOnUiThread(loadService::showSuccess);
        }
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
        } catch (JSONException e) {

        }
      }
    }).send();

    // 根据id列表获取消息
    if(informationIdList!=null){

      messages = new ArrayList<>();

      for(int i=0;i<informationIdList.size();i++){
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

                if(state.equals("N")){
                  messages.add(new Message("",user,content,new Date(time),false));
                } else {
                  messages.add(new Message("",user,content,new Date(time),true));
                }

              }else{
                String info = jsonObject.getString("info");
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
              }
            } catch (JSONException e) {

            }
          }

          @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {
            if (isRefresh) {
              refreshLayout.finishRefresh(false);
            } else {
 //             getActivity().runOnUiThread(loadService::showSuccess);
            }
          }
        },String.valueOf(informationIdList.get(i)));
      }

      dialogList = new ArrayList<>();

      int i=0;
      for(Message message:messages){
        if(message.isRead()){
          dialogList.add(new Dialog(String.valueOf(i),"系统通知",null,new ArrayList<User>(Arrays.asList(user)),message,0));
        } else{
          dialogList.add(new Dialog(String.valueOf(i),"系统通知",null,new ArrayList<User>(Arrays.asList(user)),message,1));
        }
        i++;
      }

      dialogsAdapter.setItems(dialogList);

      if (isRefresh) {
        refreshLayout.finishRefresh(true);
      } else {
 //       getActivity().runOnUiThread(loadService::showSuccess);
      }
    }
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
