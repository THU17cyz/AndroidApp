package com.example.androidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.fragment.QueryResult.Teacher;
import com.example.androidapp.util.OptionItems;

import java.util.List;

public class EditApplicationListAdapter<T> extends MyBaseAdapter {

  public EditApplicationListAdapter(List<T> data, Context context){
    super(R.layout.item_edit_application_info, data, context);
  }

  @Override
  protected void initView(BaseViewHolder viewHolder, Object o) {


  }

  @Override
  protected void initData(BaseViewHolder viewHolder, Object o) {
    // 在这里链式赋值就可以了
    ApplicationInfo data = (ApplicationInfo) o;
    viewHolder.setText(R.id.direction, data.direction)
            .setText(R.id.state, data.state)
            .setText(R.id.profile,data.profile);



  }

  @Override
  protected void setListener(BaseViewHolder viewHolder, Object o) {
    viewHolder.addOnClickListener(R.id.state);
    viewHolder.addOnClickListener(R.id.delete);
    ApplicationInfo data = (ApplicationInfo) o;
    // 删除按钮
    ImageView delete = viewHolder.getView(R.id.delete);
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // todo 删除该栏
        data.setType(ApplicationInfo.Type.DELETE);
        viewHolder.setVisible(R.id.card,false);
      }
    });

    // 选择器
    TextView state = viewHolder.getView(R.id.state);
    state.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
            String s = OptionItems.optionsState.get(options1);
            state.setText(s);
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


  }
}
