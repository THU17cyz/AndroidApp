package com.example.androidapp.Popup;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidapp.QueryResultActivity;
import com.example.androidapp.R;

import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import razerdp.basepopup.BasePopupWindow;

public class OrderList extends BasePopupWindow {
    private static final String[] options = {"术士", "重装"};
    private static final Boolean[] selected = {false, false};

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
        container.setTags(options);
        container.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (selected[position]) {
                    selected[position] = false;
                } else {
                    selected[position] = true;
                }
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        ConstraintLayout btns = (ConstraintLayout) view.getViewById(R.id.btns);
        Button enterBtn = (Button) btns.getViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(v -> {
            dismiss();
            QueryResultActivity activity = (QueryResultActivity) getContext();
            activity.filterResult();
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

}
