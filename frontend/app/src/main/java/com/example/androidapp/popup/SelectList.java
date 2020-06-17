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
import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.R;
import com.example.androidapp.fragment.QueryResult.Base;
import com.example.androidapp.fragment.QueryResult.Student;
import com.example.androidapp.fragment.QueryResult.Teacher;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class SelectList extends BasePopupWindow {
    private final int num = 4;
    private static final String[] options = {"男 ", "女 ", "一本", "二本"};
    private boolean[] selected;
    FlexboxLayout flexboxLayout;

    public SelectList(Context context, boolean[] filters) {
        super(context);
//        int i = 0;
//        for (boolean filter: filters) {
//            selected[i] = filter;
//            i++;
//        }
    }

    @Override
    public View onCreateContentView() {


        // setBackground(0);
        setOutSideTouchable(true);
        setPopupGravity(Gravity.BOTTOM | Gravity.RIGHT);
        setAlignBackground(true);
        setAlignBackgroundGravity(Gravity.TOP);

        selected = new boolean[]{false, false, false, false};
        boolean[] filters = ((Base)((QueryResultActivity) getContext()).getCurrentFragment()).getFilters();
        int i = 0;
        for (boolean filter: filters) {
            selected[i] = filter;
            i++;
        }

        ConstraintLayout view = (ConstraintLayout) createPopupById(R.layout.popup_orderlist);
        flexboxLayout = (FlexboxLayout) view.getViewById(R.id.flexbox_layout);
        fillFlexBox(Arrays.asList(options));

        ConstraintLayout btns = (ConstraintLayout) view.getViewById(R.id.btns);
        Button enterBtn = (Button) btns.getViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(v -> {
            dismiss();
            QueryResultActivity activity = (QueryResultActivity) getContext();
            Fragment fragment = activity.getCurrentFragment();
            if (fragment instanceof Teacher) {
                ((Teacher) fragment).filterResult(selected);
            } else if (fragment instanceof Student) {
                ((Student) fragment).filterResult(selected);
            }

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

//        if (selected == null) {
//            selected = new boolean[]{false, false, false, false};
//        }
        int i = 0;
        for (String query: queries) {
            TextView textView = new TextView(getContext());
//            textView.setWidth((int) (60 * factor));
            // textView.setLayoutParams(params);
            textView.setClickable(true);
//            textView.setBackground(getContext().getDrawable(R.drawable.shape_label));
            textView.setText(query);
            textView.setGravity(Gravity.CENTER);
//            textView.setPadding(20, 20, 20, 20);
//            textView.setTextColor(getContext().getColor(R.color.text_color));
            if (!selected[i]) {
                textView.setBackground(getContext().getDrawable(R.drawable.shape_label));
                textView.setTextColor(getContext().getColor(R.color.text_color));
                textView.setPadding(20, 20, 20, 20);
                textView.setWidth((int) (60 * factor));
            } else {
                textView.setBackground(getContext().getDrawable(R.drawable.shape_label_pressed));
                textView.setTextColor(getContext().getColor(R.color.label_pressed_text));
                textView.setPadding(20, 20, 20, 20);
                textView.setWidth((int) (60 * factor));
            }
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
            i++;
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

    @Override
    public void onDismiss() {
        QueryResultActivity activity = (QueryResultActivity) getContext();
        Fragment fragment = activity.getCurrentFragment();
        if (fragment instanceof Base) {
            ((Base) fragment).setFilterClosed();
        }
    }

}
