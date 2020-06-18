package com.example.androidapp.fragment.homepage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import okhttp3.Call;
import okhttp3.Response;

public class SelfInfoFragment extends Fragment {

  @BindView(R.id.name)
  TextView name;

  @BindView(R.id.gender)
  TextView gender;

  @BindView(R.id.school)
  TextView school;

  @BindView(R.id.department)
  TextView department;

  @BindView(R.id.layout_title)
  LinearLayout layoutTitle;

  @BindView(R.id.title)
  TextView title;

  @BindView(R.id.layout_major)
  LinearLayout layoutMajor;

  @BindView(R.id.major)
  TextView major;

  @BindView(R.id.layout_degree)
  LinearLayout layoutDegree;

  @BindView(R.id.degree)
  TextView degree;

  @BindView(R.id.layout_teacher_number)
  LinearLayout layoutTeacherNumber;

  @BindView(R.id.teacher_number)
  TextView teacherNumber;

  @BindView(R.id.layout_student_number)
  LinearLayout layoutStudentNumber;

  @BindView(R.id.student_number)
  TextView studentNumber;

  @BindView(R.id.layout_id_number)
  LinearLayout layoutIdNumber;

  @BindView(R.id.id_number)
  TextView idNumber;

  @BindView(R.id.phone)
  TextView phone;

  @BindView(R.id.email)
  TextView email;

  @BindView(R.id.homepage)
  TextView homepage;

  @BindView(R.id.address)
  TextView address;

  @BindView(R.id.introduction)
  TextView introduction;

  private ShortProfile shortProfile;
  private WholeProfile wholeProfile;

  private String mTitle;
  private String mMajor;
  private String mDegree;
  private String mTeacherNumber;
  private String mStudentNumber;
  private String mIdNumber;
  private String mGender;

  private String type;
  //To do
  public SelfInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_self_info, container, false);
    ButterKnife.bind(this,view);

    // 获取用户名和类型
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
              shortProfile = new ShortProfile(jsonObject,false);
            }else {
              type="T";
              shortProfile = new ShortProfile(jsonObject,true);
            }
            mTitle = jsonObject.getString("title");
            mMajor = jsonObject.getString("major");
            mDegree = jsonObject.getString("degree");
            mGender = jsonObject.getString("gender");
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null);

    // 获取个性签名
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
            wholeProfile = new WholeProfile(shortProfile,jsonObject);
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

    // 显示
    name.setText(shortProfile.name);
    if(mGender.equals("M")){
      gender.setText("男");
    } else if(mGender.equals("F")){
      gender.setText("女");
    } else {
      gender.setText("未设置");
    }
    school.setText(shortProfile.school);
    department.setText(shortProfile.department);
    if(type.equals("T")){
      title.setText(mTitle);
      teacherNumber.setText(mTeacherNumber);
      layoutMajor.setVisibility(View.INVISIBLE);
      layoutDegree.setVisibility(View.INVISIBLE);
      layoutStudentNumber.setVisibility(View.INVISIBLE);
    } else {
      studentNumber.setText(mStudentNumber);
      major.setText(mMajor);
      degree.setText(mDegree);
      layoutTitle.setVisibility(View.INVISIBLE);
      layoutTeacherNumber.setVisibility(View.INVISIBLE);
    }
    idNumber.setText(mIdNumber);
    phone.setText(wholeProfile.phone);
    email.setText(wholeProfile.email);
    homepage.setText(wholeProfile.homepage);
    address.setText(wholeProfile.address);
    introduction.setText(wholeProfile.introduction);

    return view;
  }

}

