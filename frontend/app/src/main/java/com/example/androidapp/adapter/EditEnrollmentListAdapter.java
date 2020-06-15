package com.example.androidapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.EditEnrollment;
import com.example.androidapp.entity.Follower;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditEnrollmentListAdapter extends RecyclerView.Adapter<EditEnrollmentListAdapter.ViewHolder> {

  private final Activity activity;
  private final LayoutInflater inflater;
  private final List<EditEnrollment> enrollmentList = new ArrayList<>();

  public EditEnrollmentListAdapter(@NonNull Activity activity) {
    this.activity = activity;
    this.inflater = LayoutInflater.from(activity);
  }

  public void clearListAndNotify() {
    enrollmentList.clear();
    notifyDataSetChanged();
  }

  public void setListAndNotify(@NonNull List<EditEnrollment> enrollmentList) {
    this.enrollmentList.clear();
    this.enrollmentList.addAll(enrollmentList);
    notifyDataSetChanged();
  }

  public void appendListAndNotify(@NonNull List<EditEnrollment> enrollmentList) {
    int startPosition = this.enrollmentList.size();
    this.enrollmentList.addAll(enrollmentList);
    notifyItemRangeInserted(startPosition, enrollmentList.size());
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(inflater.inflate(R.layout.item_activity_teacher_edit_intention,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(enrollmentList.get(position));
  }

  @Override
  public int getItemCount() {
    return enrollmentList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.edit_direction)
    FormEditText edit_direction;

    @BindView(R.id.choose_type)
    TextView choose_type;

    @BindView(R.id.edit_number)
    FormEditText edit_number;

    @BindView(R.id.choose)
    TextView choose;

    @BindView(R.id.edit_info)
    FormEditText edit_info;

    private EditEnrollment editEnrollment;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }

    void bind(@NonNull EditEnrollment editEnrollment){
      this.editEnrollment = editEnrollment;
//      image.setBackgroundColor(Color.RED);
    }

  }

}
