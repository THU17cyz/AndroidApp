package com.example.androidapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.entity.EditApplication;
import com.example.androidapp.entity.EditEnrollment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditApplicationListAdapter extends RecyclerView.Adapter<EditApplicationListAdapter.ViewHolder> {

  private final Activity activity;
  private final LayoutInflater inflater;
  private final List<EditApplication> applicationList = new ArrayList<>();

  public EditApplicationListAdapter(@NonNull Activity activity) {
    this.activity = activity;
    this.inflater = LayoutInflater.from(activity);
  }

  public void clearListAndNotify() {
    applicationList.clear();
    notifyDataSetChanged();
  }

  public void setListAndNotify(@NonNull List<EditApplication> applicationList) {
    this.applicationList.clear();
    this.applicationList.addAll(applicationList);
    notifyDataSetChanged();
  }

  public void appendListAndNotify(@NonNull List<EditEnrollment> enrollmentList) {
    int startPosition = this.applicationList.size();
    this.applicationList.addAll(applicationList);
    notifyItemRangeInserted(startPosition, enrollmentList.size());
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(inflater.inflate(R.layout.item_activity_student_edit_intention,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(applicationList.get(position));
  }

  @Override
  public int getItemCount() {
    return applicationList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.edit_direction)
    FormEditText edit_direction;

    @BindView(R.id.choose)
    TextView choose;

    @BindView(R.id.edit_info)
    FormEditText edit_info;

    private EditApplication editApplication;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }

    void bind(@NonNull EditApplication editApplication){
      this.editApplication = editApplication;
//      image.setBackgroundColor(Color.RED);
    }

  }

}

