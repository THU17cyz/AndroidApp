package com.example.androidapp.util;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.Arrays;

public class OptionItems {

  public static final ArrayList<String> optionsGender = new ArrayList<>(Arrays.asList("男","女","未知"));

  public static final ArrayList<String> optionsTitle = new ArrayList<>(Arrays.asList("助理","讲师","助理教授","教授"));

  public static final ArrayList<String> optionsDegree = new ArrayList<>(Arrays.asList("本科","硕士","博士"));

  public static final ArrayList<String> optionsState = new ArrayList<>(Arrays.asList("进行中","成功","失败"));

}
