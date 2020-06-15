package com.example.androidapp.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;

import com.example.androidapp.R;

import butterknife.OnClick;

/**
 * usage:
 *   xml:
 *     <com.example.androidapp.component.FocusButton
 *         android:layout_width="100dp"
 *         android:layout_height="50dp"
 *         android:textSize="25dp"
 *         app:radius="40dp"
 *         app:border_width="10"
 *         app:border_color="@color/green"
 *         app:border_color_pressed="@color/blue"
 *         app:bg_color="@color/colorPrimary"
 *         app:bg_color_pressed="@color/black"
 *         app:text_color="@color/md_yellow_50"
 *         app:text_color_pressed="@color/md_pink_100"
 *         app:text="收藏"
 *         app:text_pressed="已收藏"
 *         >
 *     </com.example.androidapp.component.FocusButton>
 *
 *  java:
 *      boolean isPressed()
 */


public class FocusButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {

  private int bg_color_pressed = Color.DKGRAY;
  private int bg_color = Color.RED;
  private int text_color_pressed = Color.WHITE;
  private int text_color = Color.WHITE;
  private String text_pressed = "已关注";
  private String text = "关注";
  private float radius = 15;
  private int border_color_pressed = Color.TRANSPARENT;
  private int border_color = Color.TRANSPARENT;
  private int border_width = 0;

  private GradientDrawable drawable_pressed;
  private GradientDrawable drawable;

  private boolean pressed = false;// true：已关注

  public FocusButton(Context context) {
    super(context);
    init();
  }

  public FocusButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    initParams(context,attrs);
    init();
  }

  public FocusButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initParams(context,attrs);
    init();
  }

  private void init(){
    setOnClickListener(FocusButton.this);

    drawable = new GradientDrawable();
    drawable.setColor(bg_color);
    drawable.setCornerRadius(radius);
    drawable.setStroke(border_width, border_color);

    drawable_pressed = new GradientDrawable();
    drawable_pressed.setColor(bg_color_pressed);
    drawable_pressed.setCornerRadius(radius);
    drawable_pressed.setStroke(border_width,border_color_pressed);

    if(pressed){
      setBackgroundDrawable(drawable_pressed);
      setText(text_pressed);
      setTextColor(text_color_pressed);
    } else {
      setBackgroundDrawable(drawable);
      setTextColor(text_color);
      setText(text);
    }
  }

  private void initParams(Context context,AttributeSet attrs){

    Toast.makeText(context,"ok",Toast.LENGTH_LONG).show();

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusButton);
    if (typedArray != null) {
      bg_color_pressed = typedArray.getColor(R.styleable.FocusButton_bg_color_pressed,Color.DKGRAY);
      bg_color = typedArray.getColor(R.styleable.FocusButton_bg_color,Color.RED);
      text_color_pressed = typedArray.getColor(R.styleable.FocusButton_text_color_pressed,Color.WHITE);
      text_color = typedArray.getColor(R.styleable.FocusButton_text_color,Color.WHITE);
      text_pressed = typedArray.getString(R.styleable.FocusButton_text_pressed);
      if (text_pressed == null) {
        text_pressed = "已关注";
      }
      text = typedArray.getString(R.styleable.FocusButton_text);
      if (text == null) {
        text = "关注";
      }
      radius = typedArray.getDimension(R.styleable.FocusButton_radius, 15);
      border_width = typedArray.getInteger(R.styleable.FocusButton_border_width, 0);
      border_color_pressed = typedArray.getColor(R.styleable.FocusButton_border_color_pressed, Color.TRANSPARENT);
      border_color = typedArray.getColor(R.styleable.FocusButton_border_color, Color.TRANSPARENT);
      typedArray.recycle();
    }
  }


  @Override
  public void onClick(View v) {
    if(!pressed){
      pressed = true;
      setBackgroundDrawable(drawable_pressed);
      setText(text_pressed);
      setTextColor(text_color_pressed);

    } else {
      pressed = false;
      setBackgroundDrawable(drawable);
      setTextColor(text_color);
      setText(text);
    }
  }

  public boolean isPressed(){
    return pressed;
  }
}
