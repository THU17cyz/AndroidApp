package com.example.androidapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryActivity extends AppCompatActivity {

    @BindView(R.id.search_view)
    SearchView searchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        ButterKnife.bind(this);

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .init();

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
    }


}
