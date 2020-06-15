package com.example.androidapp.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class SelectList extends BasePopupWindow {
    private final int num = 4;
    private static final String[] options = {"男 ", "女 ", "一本", "二本"};
    private static final Boolean[] selected = {false, false, false, false};
    FlexboxLayout flexboxLayout;

    public SelectList(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {


        // setBackground(0);
        setOutSideTouchable(true);
        setPopupGravity(Gravity.BOTTOM | Gravity.RIGHT);
        setAlignBackground(true);
        setAlignBackgroundGravity(Gravity.TOP);


        ConstraintLayout view = (ConstraintLayout) createPopupById(R.layout.popup_orderlist);
        flexboxLayout = (FlexboxLayout) view.getViewById(R.id.flexbox_layout);
        fillFlexBox(Arrays.asList(options));

        ConstraintLayout btns = (ConstraintLayout) view.getViewById(R.id.btns);
        Button enterBtn = (Button) btns.getViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(v -> {
            dismiss();
            QueryResultActivity activity = (QueryResultActivity) getContext();
            activity.filterResult(Arrays.asList(selected));
        });

        Button clearBtn = (Button) btns.getViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(v -> {
            resetFlexBox();
        });
        return view;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        Animation showAnimation = new ScaleAnimation(0, 1f, 0, 1f);
        showAnimation.setDuration(500);
        return showAnimation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation showAnimation = new ScaleAnimation(1f, 0, 1f, 0);
        showAnimation.setDuration(500);
        return showAnimation;
    }

    private void fillFlexBox(List<String> queries) {
        float factor = getContext().getResources().getDisplayMetrics().density;
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                (int) (40 * factor));

        for (String query: queries) {
            TextView textView = new TextView(getContext());
            textView.setWidth((int) (60 * factor));
            // textView.setLayoutParams(params);
            textView.setClickable(true);
            textView.setBackground(getContext().getDrawable(R.drawable.shape_label));
            textView.setText(query);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(20, 20, 20, 20);
            textView.setTextColor(getContext().getColor(R.color.text_color));

            textView.setOnClickListener(viewIn -> {
                int idx = Arrays.asList(options).indexOf(query);
                if (selected[idx]) {
                    selected[idx] = false;
                    textView.setBackground(getContext().getDrawable(R.drawable.shape_label));
                    textView.setTextColor(getContext().getColor(R.color.text_color));
                    textView.setPadding(20, 20, 20, 20);
                    textView.setWidth((int) (60 * factor));
                } else {
                    selected[idx] = true;
                    textView.setBackground(getContext().getDrawable(R.drawable.shape_label_pressed));
                    textView.setTextColor(getContext().getColor(R.color.label_pressed_text));
                    textView.setPadding(20, 20, 20, 20);
                    textView.setWidth((int) (60 * factor));
                }

            });
            flexboxLayout.addView(textView);
        }
    }

    private void resetFlexBox() {
        float factor = getContext().getResources().getDisplayMetrics().density;
        for (int i = 0; i < num; i++) {

            TextView textView = ((TextView)flexboxLayout.getFlexItemAt(i));
            textView.setTextColor(getContext().getColor(R.color.text_color));
            textView.setBackground(getContext().getDrawable(R.drawable.shape_label));
            textView.setPadding(20, 20, 20, 20);
            textView.setWidth((int) (60 * factor));
            selected[i] = false;

        }

    }

}
