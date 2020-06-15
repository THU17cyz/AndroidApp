package com.example.androidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.Follower;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {

  private final Activity activity;
  private final LayoutInflater inflater;
  private final List<Follower> followerList = new ArrayList<>();

  public FollowListAdapter(@NonNull Activity activity) {
    this.activity = activity;
    this.inflater = LayoutInflater.from(activity);
  }

  public void clearTopicListAndNotify() {
    followerList.clear();
    notifyDataSetChanged();
  }

  public void setTopicListAndNotify(@NonNull List<Follower> followerList) {
    this.followerList.clear();
    this.followerList.addAll(followerList);
    notifyDataSetChanged();
  }

  public void appendTopicListAndNotify(@NonNull List<Follower> followerList) {
    int startPosition = this.followerList.size();
    this.followerList.addAll(followerList);
    notifyItemRangeInserted(startPosition, followerList.size());
  }


  @NonNull
  @Override
  public FollowListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(inflater.inflate(R.layout.item_follower,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull FollowListAdapter.ViewHolder holder, int position) {
    holder.bind(followerList.get(position));
  }

  @Override
  public int getItemCount() {
    return followerList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.image)
    CircleImageView image;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.button)
    FocusButton button;

    private Follower follower;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }

    void bind(@NonNull Follower follower){
      this.follower = follower;

//      image.setBackgroundColor(Color.RED);
      name.setText("WANG");
    }

    @OnClick(R.id.image)
    void onImageClick(){

    }

    @OnClick(R.id.name)
    void onNameClick(){
      Toast.makeText(activity,"ok",Toast.LENGTH_LONG).show();
    }
  }
}
