package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.HistoryAdapter;
import com.example.androidapp.request.search.DeleteRecordRequest;
import com.example.androidapp.request.search.SearchHotRecordRequest;
import com.example.androidapp.request.search.SearchRecordRequest;
import com.google.android.flexbox.FlexboxLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class QueryActivity extends BaseActivity {

    @BindView(R.id.top_bar)
    Toolbar toolbar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.historyList)
    RecyclerView historyList;

    @BindView(R.id.query_flexbox_container)
    LinearLayout linearLayout;

    @BindView(R.id.flexbox_layout)
    FlexboxLayout flexboxLayout;

    private HistoryAdapter historyAdapter;

    private List<String> records;

    private List<String> hot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        ButterKnife.bind(this);

        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .init();

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getApplicationContext(), QueryResultActivity.class);
                intent.putExtra("query", s);
                startActivity(intent);
                QueryActivity.this.finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

//        fillFlexBox(Arrays.asList("清华大学","清华大学软件学院","北京大学","小花","小海","小林","小叶","小虎","小柔"));

        historyList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        records = new ArrayList<>();
        hot = new ArrayList<>();
        // records.addAll(Arrays.asList("小明","小红","小芳","小花","小海","小林","小叶","小虎","小柔"));
        historyAdapter = new HistoryAdapter(records, this);//初始化NameAdapter
        historyAdapter.setRecyclerManager(historyList);//设置RecyclerView特性
        // historyAdapter.openLeftAnimation();//设置加载动画


        historyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 用position获取点击的是什么
            deleteSearchHistory(records.get(position));
            adapter.remove(position);

        });

        historyAdapter.setOnItemClickListener((adapter, view, position) -> {
            searchView.setQuery((String) adapter.getData().get(position), true);
//            Toast.makeText(this, "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });
        getSearchHistory();
        getHotSearch();

        toolbar.setNavigationOnClickListener(v -> this.finish());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }


    private void fillFlexBox(List<String> queries) {
        float factor = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (40 * factor));
        for (String query : queries) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setClickable(true);
            textView.setBackground(getDrawable(R.drawable.shape_label));
            textView.setText(query);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(15, 0, 15, 0);
            textView.setTextColor(getColor(R.color.text_color));
            textView.setOnClickListener(viewIn -> {
                searchView.setQuery(query, true);
            });
            flexboxLayout.addView(textView);
        }

    }

//    @OnClick(R.id.returnButton)
//    public void returnToParent() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }

    private void deleteSearchHistory(String key) {
        new DeleteRecordRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                } catch (JSONException e) {
                }
            }
        }, key).send();
    }

    private void getSearchHistory() {
        loadService = LoadSir.getDefault().register(historyList, (Callback.OnReloadListener) v -> {

        });
        new SearchRecordRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                loadService.showSuccess();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                // runOnUiThread(() -> Toast.makeText(QueryActivity.this, resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("search_record_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        records.add(jsonArray.getString(i));
                    }
                    loadService.showSuccess();
                } catch (JSONException e) {
                    loadService.showSuccess();
                }
            }
        }).send();
    }

    private void getHotSearch() {
//        loadService = LoadSir.getDefault().register(linearLayout, (Callback.OnReloadListener) v -> {
//
//        });
        new SearchHotRecordRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                // loadService.showSuccess();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                // runOnUiThread(() -> Toast.makeText(QueryActivity.this, resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("search_record_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        hot.add(jsonArray.getString(i));
                    }
                    runOnUiThread(() -> fillFlexBox(hot));
                    // loadService.showSuccess();
                } catch (JSONException e) {
                    // loadService.showSuccess();
                }
            }
        }).send();
    }
}
