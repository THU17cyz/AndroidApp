package com.example.androidapp.adapter;

import android.app.Activity;
import android.app.Application;
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

public class ApplicationListAdapter<T> extends MyBaseAdapter {


  private TextView direction;
  private TextView state;
  private TextView profile;

  public ApplicationListAdapter(List<T> data, Context context){
    super(R.layout.item_application_info, data, context);
  }

  @Override
  protected void initView(BaseViewHolder viewHolder, Object o) {
    // 这里大多数情况应该不需要初始化
//        mName = viewHolder.getView(R.id.name);
//        mHead = viewHolder.getView(R.id.profile_image);
//        mAffiliation = viewHolder.getView(R.id.affiliation);

    //
  }

  @Override
  protected void initData(BaseViewHolder viewHolder, Object o) {

    ApplicationInfo data = (ApplicationInfo) o;
    viewHolder.setText(R.id.direction, data.direction)
            .setText(R.id.state, data.state)
            .setText(R.id.profile,data.profile);
  }

  @Override
  protected void setListener(BaseViewHolder viewHolder, Object o) {

  }
}
