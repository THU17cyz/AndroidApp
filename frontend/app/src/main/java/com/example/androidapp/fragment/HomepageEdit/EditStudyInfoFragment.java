package com.example.androidapp.fragment.HomepageEdit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.UpdateInfoPlusRequest;
import com.example.androidapp.util.BasicInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class EditStudyInfoFragment extends Fragment implements View.OnClickListener{

  @BindView(R.id.btn_concern)
  Button btnConcern;

  @BindView(R.id.text_direction_or_interest)
  TextView textDirOrInt;

  @BindView(R.id.edit_direction_or_interest)
  FormEditText dirOrInt;

  @BindView(R.id.text_result_or_experience)
  TextView textResOrExp;

  @BindView(R.id.edit_result_or_experience)
  FormEditText resOrExp;

  @BindView(R.id.edit_url)
  FormEditText url;

  private String mSignature;
  private String mPhone;
  private String mEmail;
  private String mHomepage;
  private String mAddress;
  private String mIntroduction;
  private String mTeacherNumber;
  private String mStudentNumber;
  private String mIdNumber;

  private String mDirection;
  private String mResult;
  private String mInterest;
  private String mExperience;
  private String mUrl;

  private Unbinder unbinder;

  //To do
  public EditStudyInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_study_info, container, false);
    unbinder = ButterKnife.bind(this,view);

    // 获取并显示当前信息
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
            mSignature = jsonObject.getString("signature");
            mPhone = jsonObject.getString("phone");
            mEmail = jsonObject.getString("email");
            mHomepage = jsonObject.getString("homepage");
            mAddress = jsonObject.getString("address");
            mIntroduction = jsonObject.getString("introduction");
            mIdNumber = jsonObject.getString("id_number");
            mUrl = jsonObject.getString("promotional_video_url");
            if(BasicInfo.TYPE.equals("S")){
              mStudentNumber = jsonObject.getString("student_number");
              mInterest = jsonObject.getString("research_interest");
              mExperience = jsonObject.getString("research_experience");
            } else {
              mTeacherNumber = jsonObject.getString("teacher_number");
              mDirection = jsonObject.getString("research_fields");
              mResult = jsonObject.getString("research_achievements");
            }

            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                if(BasicInfo.TYPE.equals("S")){
                  textDirOrInt.setText("兴趣方向");
                  textResOrExp.setText("研究经历");
                  dirOrInt.setText(mInterest);
                  resOrExp.setText(mExperience);
                  url.setText(mUrl);
                } else {
                  textDirOrInt.setText("研究方向");
                  textResOrExp.setText("研究成果");
                  dirOrInt.setText(mDirection);
                  resOrExp.setText(mResult);
                  url.setText(mUrl);
                }
              }
            });

          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null).send();

//
//    // 显示当前信息
//    if(BasicInfo.TYPE.equals("S")){
//      textDirOrInt.setText("兴趣方向");
//      dirOrInt.setText(wholeProfile.research_interest);
//      textResOrExp.setText("研究经历");
//      resOrExp.setText(wholeProfile.research_experience);
//      url.setText(wholeProfile.promotional_video_url);
//    } else {
//      textDirOrInt.setText("研究方向");
//      dirOrInt.setText(wholeProfile.research_fields);
//      textResOrExp.setText("研究成果");
//      resOrExp.setText(wholeProfile.research_achievements);
//      url.setText(wholeProfile.promotional_video_url);
//    }

    btnConcern.setOnClickListener(this);


    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_concern:
      {

        if(BasicInfo.TYPE.equals("S")){
          // 提交信息
          new UpdateInfoPlusRequest(new okhttp3.Callback() {
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

                }else{
                }
              } catch (JSONException e) {

              }
            }
          },
                  mSignature,
                  mPhone,
                  mEmail,
                  mHomepage,
                  mAddress,
                  mIntroduction,
                  mDirection,
                  mResult,
                  dirOrInt.getText().toString(),
                  resOrExp.getText().toString(),
                  url.getText().toString()).send();
        } else {
          new UpdateInfoPlusRequest(new okhttp3.Callback() {
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

                }else{
                }
              } catch (JSONException e) {

              }
            }
          },
                  mSignature,
                  mPhone,
                  mEmail,
                  mHomepage,
                  mAddress,
                  mIntroduction,
                  dirOrInt.getText().toString(),
                  resOrExp.getText().toString(),
                  mInterest,
                  mExperience,
                  url.getText().toString()).send();
        }
      }
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
