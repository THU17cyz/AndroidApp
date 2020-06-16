package com.example.androidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreabaccega.widget.FormEditText;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EditEnrollment;
import com.example.androidapp.entity.EnrollmentInfo;
import com.example.androidapp.entity.Follower;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditEnrollmentListAdapter<T> extends MyBaseAdapter {

  public EditEnrollmentListAdapter(List<T> data, Context context){
    super(R.layout.item_activity_teacher_edit_intention, data, context);
  }

  @Override
  protected void initView(BaseViewHolder viewHolder, Object o) {
  }

  @Override
  protected void initData(BaseViewHolder viewHolder, Object o) {
    // 在这里链式赋值就可以了
    EnrollmentInfo data = (EnrollmentInfo) o;
    viewHolder.setText(R.id.direction, data.direction)
            .setText(R.id.student_type, data.studentType)
            .setText(R.id.number, data.number)
            .setText(R.id.state, data.state)
            .setText(R.id.introduction,data.introduction);
  }

  @Override
  protected void setListener(BaseViewHolder viewHolder, Object o) {
  }
}
