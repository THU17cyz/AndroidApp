package com.example.androidapp.fragment.HomepageEdit;

import android.app.Activity;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.androidapp.R;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.request.user.UpdateInfoPlusRequest;
import com.example.androidapp.request.user.UpdateInfoRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.OptionItems;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class EditSelfInfoFragment extends Fragment
        implements View.OnClickListener
{

  @BindView(R.id.btn_concern)
  Button btnConcern;

  @BindView(R.id.edit_name)
  FormEditText name;

  @BindView(R.id.edit_signature)
  FormEditText signature;

  @BindView(R.id.edit_school)
  FormEditText school;

  @BindView(R.id.edit_department)
  FormEditText department;

  @BindView(R.id.layout_major)
  LinearLayout layoutMajor;

  @BindView(R.id.edit_major)
  FormEditText major;

  @BindView(R.id.layout_teacher_number)
  LinearLayout layoutTeacherNumber;

  @BindView(R.id.edit_teacher_number)
  FormEditText teacherNumber;

  @BindView(R.id.layout_student_number)
  LinearLayout layoutStudentNumber;

  @BindView(R.id.edit_student_number)
  FormEditText studentNumber;

  @BindView(R.id.layout_id_number)
  LinearLayout layoutIdNumber;

  @BindView(R.id.edit_id_number)
  FormEditText idNumber;

  @BindView(R.id.edit_phone)
  FormEditText phone;

  @BindView(R.id.edit_email)
  FormEditText email;

  @BindView(R.id.edit_homepage)
  FormEditText homepage;

  @BindView(R.id.edit_address)
  FormEditText address;

  @BindView(R.id.edit_intro)
  FormEditText introduction;


  @BindView(R.id.choose_gender)
  TextView chooseGender;

  @BindView(R.id.layout_title)
  LinearLayout layoutTitle;

  @BindView(R.id.choose_title)
  TextView chooseTitle;

  @BindView(R.id.layout_degree)
  LinearLayout layoutDegree;

  @BindView(R.id.choose_degree)
  TextView chooseDegree;

  private ShortProfile shortProfile;
  private WholeProfile wholeProfile;

  private String mTitle;
  private String mMajor;
  private String mDegree;
  private String mTeacherNumber;
  private String mStudentNumber;
  private String mIdNumber;
  private String mGender;

  //To do
  public EditSelfInfoFragment() { }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_self_info, container, false);
    ButterKnife.bind(this, view);

    chooseGender.setOnClickListener(this);
    chooseTitle.setOnClickListener(this);
    chooseDegree.setOnClickListener(this);

    // 获取目前数据

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

            if(BasicInfo.TYPE.equals("S")){
              shortProfile = new ShortProfile(jsonObject,false);
            }else {
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

    // 显示目前数据
    name.setText(shortProfile.name);
    if(mGender.equals("M")){
      chooseGender.setText("男");
    } else if(mGender.equals("F")){
      chooseGender.setText("女");
    } else {
      chooseGender.setText("未设置");
    }
    school.setText(shortProfile.school);
    department.setText(shortProfile.department);
    if(BasicInfo.TYPE.equals("T")){
      chooseTitle.setText(mTitle);
      teacherNumber.setText(mTeacherNumber);
      layoutMajor.setVisibility(View.INVISIBLE);
      layoutDegree.setVisibility(View.INVISIBLE);
      layoutStudentNumber.setVisibility(View.INVISIBLE);
    } else {
      studentNumber.setText(mStudentNumber);
      major.setText(mMajor);
      chooseDegree.setText(mDegree);
      layoutTitle.setVisibility(View.INVISIBLE);
      layoutTeacherNumber.setVisibility(View.INVISIBLE);
    }
    idNumber.setText(mIdNumber);
    phone.setText(wholeProfile.phone);
    email.setText(wholeProfile.email);
    homepage.setText(wholeProfile.homepage);
    address.setText(wholeProfile.address);
    introduction.setText(wholeProfile.introduction);


    btnConcern.setOnClickListener(this);
    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.choose_gender:
        {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
            String gender = OptionItems.optionsGender.get(options1);
            chooseGender.setText(gender);
          }
        })
        .setTitleText("选择性别")
        .setSubmitText("确定")
        .setCancelText("取消")
        .setContentTextSize(20)
        .setDividerColor(Color.GRAY)
        .setSelectOptions(0)
        .build();
        pvOptions.setPicker(OptionItems.optionsGender);
        pvOptions.show();
        break;
      }
      case R.id.choose_title:
        {
         OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
            String gender = OptionItems.optionsTitle.get(options1);
            chooseTitle.setText(gender);
          }
        })
                .setTitleText("选择职务")
                .setSubmitText("确定")
                .setCancelText("取消")
                .setContentTextSize(20)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(OptionItems.optionsTitle);
        pvOptions.show();
        break;
      }
      case R.id.choose_degree:
      {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
            String gender = OptionItems.optionsDegree.get(options1);
            chooseDegree.setText(gender);
          }
        })
                .setTitleText("选择学位")
                .setSubmitText("确定")
                .setCancelText("取消")
                .setContentTextSize(20)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(OptionItems.optionsDegree);
        pvOptions.show();
        break;
      }
      case R.id.btn_concern:
      {
        // 发送修改信息的请求
        new UpdateInfoRequest(new okhttp3.Callback() {
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
                name.getText().toString(),
                chooseGender.getText().toString(),
                school.getText().toString(),
                department.getText().toString(),
                chooseTitle.getText().toString(),
                major.getText().toString(),
                chooseDegree.getText().toString() );

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
                signature.getText().toString(),
                phone.getText().toString(),
                email.getText().toString(),
                homepage.getText().toString(),
                address.getText().toString(),
                introduction.getText().toString(),
                wholeProfile.research_fields,
                wholeProfile.research_achievements,
                wholeProfile.research_interest,
                wholeProfile.research_experience,
                wholeProfile.promotional_video_url);
      }
    }
  }

}
