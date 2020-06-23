package com.example.androidapp.fragment.homepageEdit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.androidapp.R;
import com.example.androidapp.entity.OptionItems;
import com.example.androidapp.request.user.UpdateInfoPlusRequest;
import com.example.androidapp.request.user.UpdateInfoRequest;
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


/**
 * 修改信息界面1
 */
public class EditSelfInfoFragment extends Fragment implements View.OnClickListener {
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

    private Unbinder unbinder;

    public EditSelfInfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_self_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        chooseGender.setOnClickListener(this);
        chooseTitle.setOnClickListener(this);
        chooseDegree.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setInfo();
    }

    public void setInfo() {
        name.setText(BasicInfo.mName);
        if (BasicInfo.mGender.equals("M")) {
            chooseGender.setText("男");
        } else if (BasicInfo.mGender.equals("F")) {
            chooseGender.setText("女");
        } else if (BasicInfo.mGender.equals("U")) {
            chooseGender.setText("保密");
        }
        school.setText(BasicInfo.mSchool);
        department.setText(BasicInfo.mDepartment);
        if (BasicInfo.TYPE.equals("S")) {
            major.setText(BasicInfo.mMajor);
            if (BasicInfo.mDegree.equals("UG")) {
                chooseDegree.setText("本科生");
            } else if (BasicInfo.mDegree.equals("MT")) {
                chooseDegree.setText("硕士生");
            } else if (BasicInfo.mDegree.equals("DT")) {
                chooseDegree.setText("博士生");
            }
            layoutTitle.setVisibility(View.GONE);
            layoutTeacherNumber.setVisibility(View.GONE);
        } else {

            if (BasicInfo.mTitle.equals("TA")) {
                chooseTitle.setText("助理");
            } else if (BasicInfo.mTitle.equals("LT")) {
                chooseTitle.setText("讲师");
            } else if (BasicInfo.mTitle.equals("AP")) {
                chooseTitle.setText("助理教授");
            } else if (BasicInfo.mTitle.equals("PP")) {
                chooseTitle.setText("教授");
            }

            layoutDegree.setVisibility(View.GONE);
            layoutMajor.setVisibility(View.GONE);
            layoutDegree.setVisibility(View.GONE);
        }

        signature.setText(BasicInfo.mSignature);
        phone.setText(BasicInfo.mPhone);
        email.setText(BasicInfo.mEmail);
        homepage.setText(BasicInfo.mHomepage);
        address.setText(BasicInfo.mAddress);
        introduction.setText(BasicInfo.mIntroduction);
        idNumber.setText(BasicInfo.mIdNumber);
        if (BasicInfo.TYPE.equals("S")) {
            studentNumber.setText(BasicInfo.mStudentNumber);
            layoutTeacherNumber.setVisibility(View.GONE);
        } else {
            teacherNumber.setText(BasicInfo.mTeacherNumber);
            layoutStudentNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_gender: {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
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
            case R.id.choose_title: {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
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
            case R.id.choose_degree: {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
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
        }
    }

    public void update() {
        String tmpDegree = "";
        if (chooseDegree.getText().equals("本科生")) {
            tmpDegree = "UG";
        } else if (chooseDegree.getText().equals("硕士生")) {
            tmpDegree = "MT";
        } else if (chooseDegree.getText().equals("博士生")) {
            tmpDegree = "DT";
        }
        String tmpTitle = "";
        if (chooseTitle.getText().toString().equals("助理")) {
            tmpTitle = "TA";
        } else if (chooseTitle.getText().toString().equals("讲师")) {
            tmpTitle = "LT";
        } else if (chooseTitle.getText().toString().equals("助理教授")) {
            tmpTitle = "AP";
        } else if (chooseTitle.getText().toString().equals("教授")) {
            tmpTitle = "PP";
        }
        String tmpGender = "";
        if (chooseGender.getText().toString().equals("男")) {
            tmpGender = "M";
        } else if (chooseGender.getText().toString().equals("女")) {
            tmpGender = "F";
        } else if (chooseGender.getText().toString().equals("保密")) {
            tmpGender = "U";
        }

        BasicInfo.mName = name.getText().toString();
        BasicInfo.mGender = tmpGender;
        BasicInfo.mSchool = school.getText().toString();
        BasicInfo.mDepartment = department.getText().toString();
        BasicInfo.mTitle = tmpTitle;
        BasicInfo.mMajor = major.getText().toString();
        BasicInfo.mDegree = tmpDegree;

        BasicInfo.mSignature = signature.getText().toString();
        BasicInfo.mPhone = phone.getText().toString();
        BasicInfo.mEmail = email.getText().toString();
        BasicInfo.mHomepage = homepage.getText().toString();
        BasicInfo.mAddress = address.getText().toString();
        BasicInfo.mIntroduction = introduction.getText().toString();

        // 发送修改信息的请求
        new UpdateInfoRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();

                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
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
                tmpDegree).send();

        new UpdateInfoPlusRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
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
                null,
                null,
                null,
                null,
                null).send();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
