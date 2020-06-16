package com.example.androidapp.fragment.HomepageEdit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.androidapp.R;
import com.example.androidapp.util.OptionItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditSelfInfoFragment extends Fragment
        implements View.OnClickListener
{

  @BindView(R.id.choose_gender)
  TextView chooseGender;

  @BindView(R.id.choose_title)
  TextView chooseTitle;

  @BindView(R.id.choose_degree)
  TextView chooseDegree;

  //To do
  public EditSelfInfoFragment() { }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_self_info, container, false);
    ButterKnife.bind(this, view);

    chooseGender.setOnClickListener(this);
    chooseTitle.setOnClickListener(this);
    chooseDegree.setOnClickListener(this);



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

    }
  }

}
