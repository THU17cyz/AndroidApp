package com.example.androidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.util.OptionItems;

import java.util.List;

public class EditRecruitmentListAdapter<T> extends MyBaseAdapter {

  public EditRecruitmentListAdapter(List<T> data, Context context){
    super(R.layout.item_edit_enrollment_info, data, context);
  }

  @Override
  protected void initView(BaseViewHolder viewHolder, Object o) {
  }

  @Override
  protected void initData(BaseViewHolder viewHolder, Object o) {
    // 在这里链式赋值就可以了
    RecruitmentInfo data = (RecruitmentInfo) o;
    viewHolder.setText(R.id.direction, data.direction)
            .setText(R.id.number, data.number)
            .setText(R.id.introduction,data.introduction);
    if(data.studentType.equals("UG")){
      viewHolder.setText(R.id.student_type,"本科生");
    } else if(data.studentType.equals("MT")){
      viewHolder.setText(R.id.student_type,"硕士生");
    } else if(data.studentType.equals("DT")) {
      viewHolder.setText(R.id.student_type,"博士生");
    }
    if(data.state.equals("O")){
      viewHolder.setText(R.id.state,"进行");
    } else if(data.state.equals("S")){
      viewHolder.setText(R.id.state,"成功");
    } else if(data.state.equals("F")) {
      viewHolder.setText(R.id.state,"失败");
    }
  }

  @Override
  protected void setListener(BaseViewHolder viewHolder, Object o) {

    viewHolder.addOnClickListener(R.id.delete);
//    viewHolder.addOnClickListener(R.id.direction);
//    viewHolder.addOnClickListener(R.id.number);
//    viewHolder.addOnClickListener(R.id.introduction);

    RecruitmentInfo data = (RecruitmentInfo) o;

    FormEditText direction = viewHolder.getView(R.id.direction);
    direction.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        data.direction = s.toString();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    FormEditText number = viewHolder.getView(R.id.number);
    number.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
          data.number = s.toString();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    FormEditText introduction = viewHolder.getView(R.id.introduction);
    introduction.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        data.introduction = s.toString();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    // 选择器
    TextView state = viewHolder.getView(R.id.state);
    // 选择器
    TextView studentType = viewHolder.getView(R.id.student_type);

    state.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {

            // 隐藏软键盘
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            String s = OptionItems.optionsState.get(options1);
            state.setText(s);
            if(s.equals("进行")){
              data.state = "O";
            } else if(s.equals("成功")){
              data.state = "S";
            } else if(s.equals("失败")) {
              data.state = "F";
            }
            number.requestFocus();
          }


        })
                .setTitleText("选择状态")
                .setSubmitText("确定")
                .setCancelText("取消")
                .setContentTextSize(20)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(OptionItems.optionsState);
        pvOptions.show();

      }
    });


    studentType.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
            String s = OptionItems.optionsDegree.get(options1);
            studentType.setText(s);
            if(s.equals("本科生")){
              data.studentType = "UG";
            } else if(s.equals("硕士生")){
              data.studentType = "MT";
            } else if(s.equals("博士生")){
              data.studentType = "DT";
            }
            number.requestFocus();
          }
        })
                .setTitleText("选择类型")
                .setSubmitText("确定")
                .setCancelText("取消")
                .setContentTextSize(20)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(OptionItems.optionsDegree);
        pvOptions.show();

      }
    });
  }
}
