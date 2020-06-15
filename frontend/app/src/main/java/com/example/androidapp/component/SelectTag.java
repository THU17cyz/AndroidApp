//package com.example.androidapp.component;
//
//import android.content.Context;
//import android.graphics.drawable.GradientDrawable;
//import android.view.Gravity;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.androidapp.R;
//
//import java.util.Arrays;
//
//public class SelectTag extends androidx.appcompat.widget.AppCompatTextView {
//
//    public SelectTag(Context context) {
//        super(context);
//        init();
//    }
//
//    public void init() {
//        float factor = getContext().getResources().getDisplayMetrics().density;
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                (int) (40 * factor));
//
//        setLayoutParams(params);
//        setClickable(true);
//        setBackground(getContext().getDrawable(R.drawable.shape_label));
//        setText(query);
//        textView.setGravity(Gravity.CENTER);
//        textView.setPadding(15,0,15,0);
//        textView.setTextColor(getContext().getColor(R.color.text_color));
//        textView.setOnClickListener(viewIn -> {
//            Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
//            selected[Arrays.asList(options).indexOf(query)] = true;
//        });
//        flexboxLayout.addView(textView);
//    }
//
//
//}
