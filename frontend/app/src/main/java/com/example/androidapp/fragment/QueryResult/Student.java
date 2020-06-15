package com.example.androidapp.fragment.QueryResult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidapp.entity.queryInfo.StudentQueryInfo;

import java.util.ArrayList;
import java.util.List;

public class Student extends Base {

    public Student() {
        order = new String[]{"最相关（默认）", "关注人数最多"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState); // inflater.inflate(R.layout.fragment_teacher_result, container, false);
        return root;

    }


    public List<StudentQueryInfo> loadQueryInfo() {
        return new ArrayList<>();
    }

}
