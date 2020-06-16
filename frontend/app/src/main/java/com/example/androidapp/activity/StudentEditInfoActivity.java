package com.example.androidapp.activity;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.Validator;
import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.gyf.immersionbar.ImmersionBar;

public class StudentEditInfoActivity extends BaseActivity {

  private FormEditText formEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_edit_info);

    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    TextView textView = findViewById(R.id.logon1_type).findViewById(R.id.text);
    textView.setText("个人姓名");

    formEditText = findViewById(R.id.test_edit);

    formEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        Log.d("testdsf", String.valueOf(formEditText.testValidity()));
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
//        Log.d("testdsf", String.valueOf(formEditText.testValidity()));
      }

      @Override
      public void afterTextChanged(Editable s) {
        Log.d("testdsf", String.valueOf(formEditText.testValidity()));
      }
    });



    formEditText.addValidator(new myValidator(""));

    Log.d("test", String.valueOf(formEditText.testValidity()));
  }

  public void onClickValidate(View v) {

    if (formEditText.testValidity()) {
      Toast.makeText(this, ":)", Toast.LENGTH_LONG).show();
    }
  }


  public void onClickNext(View v) {
    FormEditText formEditText = findViewById(R.id.logon1_type).findViewById(R.id.edit);
    formEditText.testValidity();
  }

}

class myValidator extends Validator{


  public myValidator(String _customErrorMessage) {
    super("fuck wrong");
  }

  @Override
  public boolean isValid(EditText et) {
    return false;
  }
}