package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.androidapp.R;
import com.example.androidapp.adapter.EditEnrollmentListAdapter;
import com.example.androidapp.adapter.FollowListAdapter;
import com.example.androidapp.entity.EditEnrollment;
import com.example.androidapp.entity.Follower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherEditIntentionActivity extends BaseActivity {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  private EditEnrollmentListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_teacher_edit_intention);
    ButterKnife.bind(this);


    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new EditEnrollmentListAdapter(this);
    recyclerView.setAdapter(adapter);

    List<EditEnrollment> editEnrollments = new ArrayList<>(Arrays.asList(
            new EditEnrollment(),
            new EditEnrollment()
    ));
    adapter.setListAndNotify(editEnrollments);


  }
}
