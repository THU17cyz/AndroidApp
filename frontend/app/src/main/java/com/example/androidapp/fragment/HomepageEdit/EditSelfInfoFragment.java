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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
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

  private String mTitle;
  private String mMajor;
  private String mDegree;
  private String mTeacherNumber;
  private String mStudentNumber;
  private String mIdNumber;
  private String mGender;

  private String mName;
  private String mSchool;
  private String mDepartment;

  private String mSignature;
  private String mPhone;
  private String mEmail;
  private String mHomepage;
  private String mAddress;
  private String mIntroduction;

  private String mDirection;
  private String mResult;
  private String mInterest;
  private String mExperience;
  private String mUrl;

  private Unbinder unbinder;


  //To do
  public EditSelfInfoFragment() { }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_self_info, container, false);
    unbinder = ButterKnife.bind(this, view);

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

            mName = jsonObject.getString("name");
            mGender = jsonObject.getString("gender");
            mSchool = jsonObject.getString("school");
            mDepartment = jsonObject.getString("department");
            if(BasicInfo.TYPE.equals("S")){
              mMajor = jsonObject.getString("major");
              mDegree = jsonObject.getString("degree");
            }else {
              mTitle = jsonObject.getString("title");
            }

            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                name.setText(mName);
                if(mGender.equals("M")){
                  chooseGender.setText("男");
                } else if(mGender.equals("F")){
                  chooseGender.setText("女");
                } else if(mGender.equals("U")){
                  chooseGender.setText("未知");
                }
                school.setText(mSchool);
                department.setText(mDepartment);
                if(BasicInfo.TYPE.equals("S")){
                  major.setText(mMajor);
                  if(mDegree.equals("UG")){
                    chooseDegree.setText("本科生");
                  } else if(mDegree.equals("MT")){
                    chooseDegree.setText("硕士生");
                  } else if(mDegree.equals("DT")){
                    chooseDegree.setText("博士生");
                  }
                  layoutTitle.setVisibility(View.GONE);
                  layoutTeacherNumber.setVisibility(View.GONE);
                }else {

                  if(mTitle.equals("TA")){
                    chooseTitle.setText("助理");
                  } else if(mTitle.equals("LT")){
                    chooseTitle.setText("讲师");
                  } else if(mTitle.equals("AP")){
                    chooseTitle.setText("助理教授");
                  } else if(mTitle.equals("PP")){
                    chooseTitle.setText("教授");
                  }

                  layoutDegree.setVisibility(View.GONE);
                  layoutMajor.setVisibility(View.GONE);
                  layoutDegree.setVisibility(View.GONE);
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
    },"I","","").send();

    new GetInfoPlusRequest(new Callback() {
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
                signature.setText(mSignature);
                phone.setText(mPhone);
                email.setText(mEmail);
                homepage.setText(mHomepage);
                address.setText(mAddress);
                introduction.setText(mIntroduction);
                idNumber.setText(mIdNumber);
                if(BasicInfo.TYPE.equals("S")){
                  studentNumber.setText(mStudentNumber);
                  layoutTeacherNumber.setVisibility(View.GONE);
                } else {
                  teacherNumber.setText(mTeacherNumber);
                  layoutStudentNumber.setVisibility(View.GONE);
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

    // 显示目前数据
//    name.setText(shortProfile.name);
//    if(mGender.equals("M")){
//      chooseGender.setText("男");
//    } else if(mGender.equals("F")){
//      chooseGender.setText("女");
//    } else {
//      chooseGender.setText("未设置");
//    }
//    school.setText(shortProfile.school);
//    department.setText(shortProfile.department);
//    if(BasicInfo.TYPE.equals("T")){
//      chooseTitle.setText(mTitle);
//      teacherNumber.setText(mTeacherNumber);
//      layoutMajor.setVisibility(View.INVISIBLE);
//      layoutDegree.setVisibility(View.INVISIBLE);
//      layoutStudentNumber.setVisibility(View.INVISIBLE);
//    } else {
//      studentNumber.setText(mStudentNumber);
//      major.setText(mMajor);
//      chooseDegree.setText(mDegree);
//      layoutTitle.setVisibility(View.INVISIBLE);
//      layoutTeacherNumber.setVisibility(View.INVISIBLE);
//    }
//    idNumber.setText(mIdNumber);
//    phone.setText(wholeProfile.phone);
//    email.setText(wholeProfile.email);
//    homepage.setText(wholeProfile.homepage);
//    address.setText(wholeProfile.address);
//    introduction.setText(wholeProfile.introduction);


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

        String tmpDegree = "";
        if(chooseDegree.getText().equals("本科生")){
          tmpDegree = "UG";
        } else if(chooseDegree.getText().equals("硕士生")){
          tmpDegree = "MT";
        } else if(chooseDegree.getText().equals("博士生")) {
          tmpDegree = "DT";
        }
        String tmpTitle = "";
        if(chooseTitle.getText().toString().equals("助理")){
          tmpTitle="TA";
        } else if(chooseTitle.getText().toString().equals("讲师")){
          tmpTitle="LT";
        } else if(chooseTitle.getText().toString().equals("助理教授")){
          tmpTitle="AP";
        } else if(chooseTitle.getText().toString().equals("教授")){
          tmpTitle="PP";
        }
        String tmpGender="";
        if(chooseGender.getText().toString().equals("男")){
          tmpGender = "M";
        } else if(chooseGender.getText().toString().equals("女")){
          tmpGender = "F";
        } else if(chooseGender.getText().toString().equals("未知")){
          tmpGender = "U";
        }

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
                tmpGender,
                school.getText().toString(),
                department.getText().toString(),
                tmpTitle,
                major.getText().toString(),
                tmpDegree ).send();

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
                mDirection,
                mResult,
                mInterest,
                mExperience,
                mUrl).send();
      }
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

}
