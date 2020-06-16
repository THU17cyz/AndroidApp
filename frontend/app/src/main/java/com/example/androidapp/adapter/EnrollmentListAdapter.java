package com.example.androidapp.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EnrollmentInfo;

import java.util.List;

public class EnrollmentListAdapter<T> extends MyBaseAdapter {


  private TextView direction;
  private TextView studentType;
  private TextView number;
  private TextView state;
  private TextView introduction;

  public EnrollmentListAdapter(List<T> data, Context context){
    super(R.layout.item_activity_teacher_edit_intention, data, context);
  }

  @Override
  protected void initView(BaseViewHolder viewHolder, Object o) {
  }

  @Override
  protected void initData(BaseViewHolder viewHolder, Object o) {

    EnrollmentInfo data = (EnrollmentInfo) o;
    viewHolder.setText(R.id.direction, data.direction)
              .setText(R.id.student_type,data.studentType)
              .setText(R.id.number,data.number)
              .setText(R.id.state,data.state)
              .setText(R.id.introduction,data.introduction);
  }

  @Override
  protected void setListener(BaseViewHolder viewHolder, Object o) {

  }
}

