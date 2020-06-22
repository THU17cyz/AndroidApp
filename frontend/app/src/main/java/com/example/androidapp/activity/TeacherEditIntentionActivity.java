package com.example.androidapp.activity;

import android.os.Bundle;

import com.example.androidapp.R;
import com.example.androidapp.adapter.EditRecruitmentListAdapter;
import com.example.androidapp.entity.EditEnrollment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherEditIntentionActivity extends BaseActivity {
  private EditRecruitmentListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_teacher_edit_intention);

    List<EditEnrollment> editEnrollments = new ArrayList<>(Arrays.asList(
            new EditEnrollment(),
            new EditEnrollment()
    ));
//    adapter.setListAndNotify(editEnrollments);


  }
}
