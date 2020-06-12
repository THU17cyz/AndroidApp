package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.Arrays;

public class StudentEditIntentionActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_edit_intention);

    TextView textView = findViewById(R.id.item_1).findViewById(R.id.choose);
    ArrayList<String> options1Items = new ArrayList<>(Arrays.asList("已确定","被拒绝","进行中"));

    textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(StudentEditIntentionActivity.this, new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
            //返回的分别是三个级别的选中位置

            String tx = options1Items.get(options1);
//                    + options2Items.get(options1).get(option2)
//                    + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
            textView.setText(tx);
          }
        })
                .setTitleText("状态选择")
                .setContentTextSize(20)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
      }
    });

  }
}
