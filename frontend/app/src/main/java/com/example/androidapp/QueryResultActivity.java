package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryResultActivity extends AppCompatActivity {
    @BindView(R.id.search_view)
    SearchView searchView;

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
    }
}
