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

import co.lujun.androidtagview.TagContainerLayout;
import razerdp.basepopup.BasePopupWindow;

public class OrderList extends BasePopupWindow {
    private static final String[] options = {"985", "211", "一本", "二本"};
    private static final Boolean[] selected = {false, false, false, false};
    FlexboxLayout flexboxLayout;

    public OrderList(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {


        // setBackground(0);
        setOutSideTouchable(true);
        setPopupGravity(Gravity.BOTTOM);
        setAlignBackground(true);
        setAlignBackgroundGravity(Gravity.TOP);


        ConstraintLayout view = (ConstraintLayout) createPopupById(R.layout.popup_orderlist);
        TagContainerLayout container = (TagContainerLayout) view.getViewById(R.id.container);
        flexboxLayout = (FlexboxLayout) view.getViewById(R.id.flexbox_layout);
        fillFlexBox(Arrays.asList(options));
//        container.setTags(options);
//        container.setOnTagClickListener(new TagView.OnTagClickListener() {
//            @Override
//            public void onTagClick(int position, String text) {
//                if (selected[position]) {
//                    selected[position] = false;
//                } else {
//                    selected[position] = true;
//                }
//                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
//                container.selectTagView(position);
//            }
//
//            @Override
//            public void onTagLongClick(int position, String text) {
//
//            }
//
//            @Override
//            public void onSelectedTagDrag(int position, String text) {
//
//            }
//
//            @Override
//            public void onTagCrossClick(int position) {
//
//            }
//        });
        ConstraintLayout btns = (ConstraintLayout) view.getViewById(R.id.btns);
        Button enterBtn = (Button) btns.getViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(v -> {
            dismiss();
            QueryResultActivity activity = (QueryResultActivity) getContext();
            activity.filterResult(Arrays.asList(selected));
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (40 * factor));
        for (String query: queries) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(params);
            textView.setClickable(true);
            textView.setBackground(getContext().getDrawable(R.drawable.shape_label));
            textView.setText(query);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(15,0,15,0);
            textView.setTextColor(getContext().getColor(R.color.text_color));
            textView.setOnClickListener(viewIn -> {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                selected[Arrays.asList(options).indexOf(query)] = true;
            });
            flexboxLayout.addView(textView);
        }

    }

}
