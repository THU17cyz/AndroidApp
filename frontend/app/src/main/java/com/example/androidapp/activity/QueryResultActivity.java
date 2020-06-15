package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.adapter.QueryResultPageAdapter;
import com.example.androidapp.entity.ApplyQueryInfo;
import com.example.androidapp.entity.RecruitQueryInfo;
import com.example.androidapp.entity.StudentQueryInfo;
import com.example.androidapp.entity.TeacherQueryInfo;
import com.example.androidapp.fragment.QueryResult.Student;
import com.example.androidapp.fragment.QueryResult.Teacher;
import com.example.androidapp.request.search.SearchStudentRequest;
import com.example.androidapp.request.search.SearchTeacherRequest;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
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

public class QueryResultActivity extends BaseActivity {
    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;

    QueryResultPageAdapter pagerAdapter;

    String[] tabs = {"教师", "学生", "招生意向", "报考意向"};

    List<Integer> teacherIdList;
    List<Integer> studentIdList;
    List<Integer> applyIdList;
    List<Integer> recruitIdList;
    List<TeacherQueryInfo> teacherQueryInfoList;
    List<StudentQueryInfo> studentQueryInfoList;
    List<ApplyQueryInfo> applyQueryInfoList;
    List<RecruitQueryInfo> recruitQueryInfoList;
    LoadService loadService;

    private String query;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        ButterKnife.bind(this);

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .init();

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        searchView.setIconifiedByDefault(false);
        searchView.setQuery(query, true);
//        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // TODO
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        for (String tab: tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pagerAdapter = new QueryResultPageAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // viewPager.set
        viewPager.setOffscreenPageLimit(4);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                loadQueryInfo(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadService = LoadSir.getDefault().register(viewPager, (Callback.OnReloadListener) v -> {

        });

        new SearchTeacherRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                runOnUiThread(() -> Toast.makeText(QueryResultActivity.this, resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("teacher_id_list");
                    teacherIdList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        teacherIdList.add(jsonObject2.getInt("id"));

                    }
                    // loadService.showSuccess();
                } catch (JSONException e) {

                }
            }
        }, "烦").send();
        new SearchStudentRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                runOnUiThread(() -> Toast.makeText(QueryResultActivity.this, resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    loadService.showSuccess();
                } catch (JSONException e) {

                }
            }
        }, "烦").send();
    }

    public void filterResult(List<Boolean> filters) {
        Toast.makeText(this, "66666", Toast.LENGTH_SHORT).show();
        int pos = viewPager.getCurrentItem();
        if (pos == 0) {
            Teacher teacher = (Teacher) pagerAdapter.getRegisteredFragment(0);
            teacher.isFilterOpen = false;
            for (Boolean item: filters) {
                Toast.makeText(this, String.valueOf(item), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void loadQueryInfo(int position) {
        switch (position) {
            case 0: {
                if (teacherQueryInfoList != null) return;
                Teacher teacher = (Teacher) pagerAdapter.getRegisteredFragment(0);
                teacherQueryInfoList = teacher.loadQueryInfo();
                break;
            }
            case 1: {
                if (studentQueryInfoList != null) return;
                Student student = (Student) pagerAdapter.getRegisteredFragment(1);
                studentQueryInfoList = student.loadQueryInfo();
                break;
            }
            case 2: {
//                if (recruitQueryInfoList != null) return;
//                Teacher teacher = (Teacher) pagerAdapter.getRegisteredFragment(0);
//                recruitQueryInfoList = teacher.loadQueryInfo();
                break;
            }
            default: {
//                if (teacherQueryInfoList != null) return;
//                Teacher teacher = (Teacher) pagerAdapter.getRegisteredFragment(0);
//                teacherQueryInfoList = teacher.loadQueryInfo();
                break;
            }
        }
    }

    public List<Integer> getApplyIdList() {
        return applyIdList;
    }

    public List<Integer> getRecruitIdList() {
        return recruitIdList;
    }

    public List<Integer> getStudentIdList() {
        return studentIdList;
    }

    public List<Integer> getTeacherIdList() {
        return teacherIdList;
    }
}
