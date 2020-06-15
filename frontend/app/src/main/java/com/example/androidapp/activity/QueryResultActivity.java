package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.androidapp.fragment.QueryResult.Teacher;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
}
