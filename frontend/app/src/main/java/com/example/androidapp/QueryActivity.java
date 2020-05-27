package com.example.androidapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Adapter.HistoryAdapter;
import com.example.androidapp.Adapter.TestAdapter;
import com.example.androidapp.Fragments.TabFragment1;
import com.google.android.flexbox.FlexboxLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryActivity extends AppCompatActivity {

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.historyList)
    RecyclerView historyList;

    @BindView(R.id.flexbox_layout)
    FlexboxLayout flexboxLayout;

    private HistoryAdapter historyAdapter;

    private List<String> records;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        ButterKnife.bind(this);

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .init();

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        fillFlexBox(Arrays.asList("清华大学","清华大学软件学院","万人称我美食家","小花","小海","小林","小叶","小虎","小柔"));

        historyList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        records = new ArrayList<>();
        records.addAll(Arrays.asList("小明","小红","小芳","小花","小海","小林","小叶","小虎","小柔"));
        historyAdapter=new HistoryAdapter(records, this);//初始化NameAdapter
        historyAdapter.setRecyclerManager(historyList);//设置RecyclerView特性
        // historyAdapter.openLeftAnimation();//设置加载动画


        historyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 用position获取点击的是什么
            System.out.println(view instanceof Button);
            adapter.remove(position);
        });

        historyAdapter.setOnItemClickListener((adapter, view, position) -> {
            Toast.makeText(this, "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });
    }


    private void fillFlexBox(List<String> queries) {
        float factor = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (40 * factor));
        for (String query: queries) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setClickable(true);
            textView.setBackground(getDrawable(R.drawable.shape_label));
            textView.setText(query);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(15,0,15,0);
            textView.setTextColor(getColor(R.color.text_color));
            textView.setOnClickListener(viewIn -> {
                searchView.setQuery(query, true);
            });
            flexboxLayout.addView(textView);
        }

    }
}
