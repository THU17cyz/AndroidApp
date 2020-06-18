package com.example.androidapp.fragment.homepage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Response;

public class StudyInfoFragment extends Fragment {

  @BindView(R.id.text_direction_or_interest)
  TextView textDirOrInt;

  @BindView(R.id.direction_or_interest)
  TextView dirOrInt;

  @BindView(R.id.text_result_or_experience)
  TextView textResOrExp;

  @BindView(R.id.result_or_experience)
  TextView resOrExp;

  @BindView(R.id.video_player)
  JCVideoPlayerStandard videoPlayer;

  private String url;
  private String type;
  private String direction;
  private String interest;
  private String result;
  private String experience;


  //To do
  public StudyInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_study_info, container, false);
    ButterKnife.bind(this,view);

    // 获取类型
    new GetInfoRequest(new okhttp3.Callback() {
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

            if(jsonObject.getJSONObject("teacher_id")==null){
              type="S";
            }else {
              type="T";
            }
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null);

    // 获取信息
    new GetInfoPlusRequest(new okhttp3.Callback() {
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
            direction = jsonObject.getString("research_fields");
            result = jsonObject.getString("research_achievements");
            interest = jsonObject.getString("research_interest");
            experience = jsonObject.getString("research_experience");
            url = jsonObject.getString("promotional_video_url");
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null);

    if(type.equals("S")){
      textDirOrInt.setText("兴趣方向");
      textResOrExp.setText("研究经历");
      dirOrInt.setText(interest);
      resOrExp.setText(experience);
    }
    else {
      textDirOrInt.setText("研究方向");
      textResOrExp.setText("研究成果");
      resOrExp.setText(direction);
      resOrExp.setText(result);
    }

    videoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "宣传视频");

    return view;
  }

}


