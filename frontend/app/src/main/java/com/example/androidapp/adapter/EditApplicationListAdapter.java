package com.example.androidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreabaccega.widget.FormEditText;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EditApplication;
import com.example.androidapp.entity.EditEnrollment;
import com.example.androidapp.entity.ShortProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditApplicationListAdapter<T> extends MyBaseAdapter {

  private FormEditText direction;
  private FormEditText state;
  private FormEditText profile;


  public EditApplicationListAdapter(List<T> data, Context context){
    super(R.layout.item_activity_student_edit_intention, data, context);
  }

  @Override
  protected void initView(BaseViewHolder viewHolder, Object o) {
  }

  @Override
  protected void initData(BaseViewHolder viewHolder, Object o) {
    // 在这里链式赋值就可以了
    ApplicationInfo data = (ApplicationInfo) o;
    viewHolder.setText(R.id.direction, data.direction)
            .setText(R.id.state, data.state)
            .setText(R.id.profile,data.profile);
  }

  @Override
  protected void setListener(BaseViewHolder viewHolder, Object o) {
  }
}
