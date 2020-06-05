package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;

public class TeacherEditIntentionActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_teacher_edit_intention);

    LinearLayout linearLayout = findViewById(R.id.linearLayout);
    LayoutInflater layoutInflater = getLayoutInflater();
    View view = layoutInflater.inflate(R.layout.item_activity_teacher_edit_intention,null);
    linearLayout.addView(view);
    View view2 = layoutInflater.inflate(R.layout.item_activity_teacher_edit_intention,null);
    linearLayout.addView(view2);
    View view3 = layoutInflater.inflate(R.layout.item_activity_teacher_edit_intention,null);
    linearLayout.addView(view3);
    View view4 = layoutInflater.inflate(R.layout.item_activity_teacher_edit_intention,null);
    linearLayout.addView(view4);
    view4.findViewById(R.id.text_direction).setBackgroundColor(Color.GREEN);

  }
}
