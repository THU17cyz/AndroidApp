package com.example.androidapp.fragment.homepage;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidapp.UI.dashboard.DashboardFragment;
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.fragment.QueryResult.Base;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Signature;

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
  private String direction;
  private String interest;
  private String result;
  private String experience;

  private Unbinder unbinder;

  private String type;
  private int id;


  //To do
  public StudyInfoFragment(String type, int id) {
    this.type = type;
    this.id = id;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_study_info, container, false);
    unbinder = ButterKnife.bind(this,view);
    System.out.println("?????????");

//    // 获取信息
//    new GetInfoPlusRequest(new okhttp3.Callback() {
//      @Override
//      public void onFailure(@NotNull Call call, @NotNull IOException e) {
//        Log.e("error", e.toString());
//      }
//
//      @Override
//      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//        String resStr = response.body().string();
//        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
//        Log.e("response", resStr);
//        try {
//          // 解析json，然后进行自己的内部逻辑处理
//          JSONObject jsonObject = new JSONObject(resStr);
//
//          Boolean status = jsonObject.getBoolean("status");
//          if(status){
//            if(BasicInfo.TYPE.equals("S")){
//              interest = jsonObject.getString("research_interest");
//              experience = jsonObject.getString("research_experience");
//              url = jsonObject.getString("promotional_video_url");
//            }else {
//              direction = jsonObject.getString("research_fields");
//              result = jsonObject.getString("research_achievements");
//              url = jsonObject.getString("promotional_video_url");
//            }
//            getActivity().runOnUiThread(new Runnable() {
//              @Override
//              public void run() {
//                if(BasicInfo.TYPE.equals("S")){
//                  textDirOrInt.setText("兴趣方向");
//                  textResOrExp.setText("研究经历");
//                  dirOrInt.setText(interest);
//                  resOrExp.setText(experience);
//                }
//                else {
//                  textDirOrInt.setText("研究方向");
//                  textResOrExp.setText("研究成果");
//                  resOrExp.setText(direction);
//                  resOrExp.setText(result);
//                }
//
//                videoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "宣传视频");
//
//              }
//            });
//
//          }else{
//            String info = jsonObject.getString("info");
//            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
//          }
//        } catch (JSONException e) {
//
//        }
//      }
//    },"I",null,null).send();

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
//    setInfo();
  }

  public void setInfo() {
    Activity activity = getActivity();
    if (activity instanceof MainActivity) {
      DashboardFragment fragment = (DashboardFragment) StudyInfoFragment.this.getParentFragment();
      if (type.equals("S")) {
        textDirOrInt.setText("兴趣方向");
        textResOrExp.setText("研究经历");
        dirOrInt.setText(fragment.mInterest);
        resOrExp.setText(fragment.mExperience);
      }
      else {
        textDirOrInt.setText("研究方向");
        textResOrExp.setText("研究成果");
        resOrExp.setText(fragment.mDirection);
        resOrExp.setText(fragment.mResult);
      }

      videoPlayer.setUp(fragment.mUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "宣传视频");
    }
    else {
      VisitHomePageActivity activity_ = (VisitHomePageActivity) activity;
      if (type.equals("S")) {
        textDirOrInt.setText("兴趣方向");
        textResOrExp.setText("研究经历");
        dirOrInt.setText(activity_.mInterest);
        resOrExp.setText(activity_.mExperience);
      }
      else {
        textDirOrInt.setText("研究方向");
        textResOrExp.setText("研究成果");
        resOrExp.setText(activity_.mDirection);
        resOrExp.setText(activity_.mResult);
      }

      videoPlayer.setUp(activity_.mUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "宣传视频");
    }



  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

}


