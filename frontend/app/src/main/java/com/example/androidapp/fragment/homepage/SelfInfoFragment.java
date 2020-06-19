package com.example.androidapp.fragment.homepage;

import android.app.Activity;
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

import com.example.androidapp.UI.dashboard.DashboardFragment;
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
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

  private Unbinder unbinder;

  private String type;
  private int id;


  //To do
  public SelfInfoFragment(String type, int id) {
    this.type = type;
    this.id = id;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_self_info, container, false);
    unbinder = ButterKnife.bind(this,view);



    // 显示
//    name.setText(shortProfile.name);
//    if(mGender.equals("M")){
//      gender.setText("男");
//    } else if(mGender.equals("F")){
//      gender.setText("女");
//    } else {
//      gender.setText("未设置");
//    }
//    school.setText(shortProfile.school);
//    department.setText(shortProfile.department);
//    if(BasicInfo.TYPE.equals("T")){
//      title.setText(mTitle);
//      layoutMajor.setVisibility(View.INVISIBLE);
//      layoutDegree.setVisibility(View.INVISIBLE);
//      layoutStudentNumber.setVisibility(View.INVISIBLE);
//    } else {
//      major.setText(mMajor);
//      degree.setText(mDegree);
//      layoutTitle.setVisibility(View.INVISIBLE);
//      layoutTeacherNumber.setVisibility(View.INVISIBLE);
//    }


//    if(BasicInfo.TYPE.equals("T")){
//      teacherNumber.setText(mTeacherNumber);
//    } else {
//      studentNumber.setText(mStudentNumber);
//    }
//    idNumber.setText(mIdNumber);
//    phone.setText(wholeProfile.phone);
//    email.setText(wholeProfile.email);
//    homepage.setText(wholeProfile.homepage);
//    address.setText(wholeProfile.address);
//    introduction.setText(wholeProfile.introduction);

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

//    getInfo();
//    setInfo();
  }

  public void setInfo() {
    Activity activity = getActivity();
    if (activity instanceof MainActivity) {
      DashboardFragment fragment = (DashboardFragment) SelfInfoFragment.this.getParentFragment();
      name.setText(fragment.mName);
      if (fragment.mGender.equals("M")){
        gender.setText("男");
      } else if (fragment.mGender.equals("F")){
        gender.setText("女");
      } else {
        gender.setText("未设置");
      }
      school.setText(fragment.mSchool);
      department.setText(fragment.mDepartment);
      if(type.equals("T")){
        title.setText(fragment.mTitle);
        layoutMajor.setVisibility(View.GONE);
        layoutDegree.setVisibility(View.GONE);
        layoutStudentNumber.setVisibility(View.GONE);
      } else {
        major.setText(fragment.mMajor);
        degree.setText(fragment.mDegree);
        layoutTitle.setVisibility(View.GONE);
        layoutTeacherNumber.setVisibility(View.GONE);
      }
      if(type.equals("T")){
        teacherNumber.setText(fragment.mTeacherNumber);
      } else {
        studentNumber.setText(fragment.mStudentNumber);
      }

      idNumber.setText(fragment.mIdNumber);
      phone.setText(fragment.mPhone);
      email.setText(fragment.mEmail);
      homepage.setText(fragment.mHomepage);
      address.setText(fragment.mAddress);
      introduction.setText(fragment.mIntroduction);
    }
    else {
      VisitHomePageActivity activity_ = (VisitHomePageActivity) activity;
      name.setText(activity_.mName);
      if (activity_.mGender.equals("M")){
        gender.setText("男");
      } else if (activity_.mGender.equals("F")){
        gender.setText("女");
      } else {
        gender.setText("未设置");
      }
      school.setText(activity_.mSchool);
      department.setText(activity_.mDepartment);
      if(type.equals("T")){
        title.setText(activity_.mTitle);
        layoutMajor.setVisibility(View.GONE);
        layoutDegree.setVisibility(View.GONE);
        layoutStudentNumber.setVisibility(View.GONE);
      } else {
        major.setText(activity_.mMajor);
        degree.setText(activity_.mDegree);
        layoutTitle.setVisibility(View.GONE);
        layoutTeacherNumber.setVisibility(View.GONE);
      }
      teacherNumber.setVisibility(View.GONE);
      studentNumber.setVisibility(View.GONE);
      idNumber.setVisibility(View.GONE);

      phone.setText(activity_.mPhone);
      email.setText(activity_.mEmail);
      homepage.setText(activity_.mHomepage);
      address.setText(activity_.mAddress);
      introduction.setText(activity_.mIntroduction);
    }



  }

  protected void getInfo() {
    String type_ = "I";
    String teacher_id = null;
    String student_id = null;
    if (id == -1) {
      type_ = "I";
      teacher_id = null;
      student_id = null;
    } else if (type.equals("S")) {
      type_ = "S";
      teacher_id = null;
      student_id = String.valueOf(id);
    } else {
      type_ = "T";
      teacher_id = String.valueOf(id);
      student_id = null;
    }

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
            mName = jsonObject.getString("name");
            mGender = jsonObject.getString("gender");
            mSchool = jsonObject.getString("school");
            mDepartment = jsonObject.getString("department");
            if(type.equals("S")){
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
                  gender.setText("男");
                } else if(mGender.equals("F")){
                  gender.setText("女");
                } else {
                  gender.setText("未设置");
                }
                school.setText(mSchool);
                department.setText(mDepartment);
                if(type.equals("T")){
                  title.setText(mTitle);
                  layoutMajor.setVisibility(View.GONE);
                  layoutDegree.setVisibility(View.GONE);
                  layoutStudentNumber.setVisibility(View.GONE);
                } else {
                  major.setText(mMajor);
                  degree.setText(mDegree);
                  layoutTitle.setVisibility(View.GONE);
                  layoutTeacherNumber.setVisibility(View.GONE);
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
    }, type_, teacher_id, student_id).send();

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
            mSignature = jsonObject.getString("signature");
            mPhone = jsonObject.getString("phone");
            mEmail = jsonObject.getString("email");
            mHomepage = jsonObject.getString("homepage");
            mAddress = jsonObject.getString("address");
            mIntroduction = jsonObject.getString("introduction");
            mIdNumber = jsonObject.getString("id_number");
            if(type.equals("S")){
              mStudentNumber = jsonObject.getString("student_number");
            }else{
              mTeacherNumber = jsonObject.getString("teacher_number");
            }

            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                if(type.equals("T")){
                  teacherNumber.setText(mTeacherNumber);
                } else {
                  studentNumber.setText(mStudentNumber);
                }

                idNumber.setText(mIdNumber);
                phone.setText(mPhone);
                email.setText(mEmail);
                homepage.setText(mHomepage);
                address.setText(mAddress);
                introduction.setText(mIntroduction);
              }
            });

          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    }, type_, teacher_id, student_id).send();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}

