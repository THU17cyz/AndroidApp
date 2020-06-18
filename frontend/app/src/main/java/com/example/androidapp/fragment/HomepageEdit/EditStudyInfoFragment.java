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

  private String signature;
  private String phone;
  private String email;
  private String homepage;
  private String address;
  private String introduction;

  WholeProfile wholeProfile;
  private String mTeacherNumber;
  private String mStudentNumber;
  private String mIdNumber;

  //To do
  public EditStudyInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_study_info, container, false);
    ButterKnife.bind(this,view);

    // 获取当前信息
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
            wholeProfile = new WholeProfile(null,jsonObject);
            mIdNumber = jsonObject.getString("id_number");
            mTeacherNumber = jsonObject.getString("teacher_number");
            mStudentNumber = jsonObject.getString("student_number");
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null);


    // 显示当前信息
    if(BasicInfo.TYPE.equals("S")){
      textDirOrInt.setText("兴趣方向");
      dirOrInt.setText(wholeProfile.research_interest);
      textResOrExp.setText("研究经历");
      resOrExp.setText(wholeProfile.research_experience);
      url.setText(wholeProfile.promotional_video_url);
    } else {
      textDirOrInt.setText("研究方向");
      dirOrInt.setText(wholeProfile.research_fields);
      textResOrExp.setText("研究成果");
      resOrExp.setText(wholeProfile.research_achievements);
      url.setText(wholeProfile.promotional_video_url);
    }

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
                  wholeProfile.signature,
                  wholeProfile.phone,
                  wholeProfile.email,
                  wholeProfile.homepage,
                  wholeProfile.address,
                  wholeProfile.introduction,
                  wholeProfile.research_fields,
                  wholeProfile.research_achievements,
                  dirOrInt.getText().toString(),
                  resOrExp.getText().toString(),
                  url.getText().toString());
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
                  wholeProfile.signature,
                  wholeProfile.phone,
                  wholeProfile.email,
                  wholeProfile.homepage,
                  wholeProfile.address,
                  wholeProfile.introduction,
                  dirOrInt.getText().toString(),
                  resOrExp.getText().toString(),
                  wholeProfile.research_interest,
                  wholeProfile.research_experience,
                  url.getText().toString());
        }
      }
    }
  }
}
