package com.example.androidapp.fragment.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.UI.dashboard.DashboardFragment;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.util.BasicInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    setInfo();
  }

  public void setInfo() {
    Activity activity = getActivity();
    if (activity instanceof MainActivity) {
      if (type.equals("S")) {
        textDirOrInt.setText("兴趣方向");
        textResOrExp.setText("研究经历");
        dirOrInt.setText(BasicInfo.mInterest);
        resOrExp.setText(BasicInfo.mExperience);
      }
      else {
        textDirOrInt.setText("研究方向");
        textResOrExp.setText("研究成果");
        dirOrInt.setText(BasicInfo.mDirection);
        resOrExp.setText(BasicInfo.mResult);
      }

      videoPlayer.setUp(BasicInfo.mUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "宣传视频");
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
        dirOrInt.setText(activity_.mDirection);
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


