package com.example.androidapp.fragment.logon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LogonFragment2 extends Fragment {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.logon2_next)
    Button nextButton;

    @BindView(R.id.logon2_name)
    EditText nameEditText;

    @BindView(R.id.logon2_gender)
    EditText genderEditText;

    @BindView(R.id.logon2_school)
    EditText schoolEditText;

    @BindView(R.id.logon2_department)
    EditText departmentEditText;

    private Unbinder unbinder;

    /******************************
     ************ 方法 ************
     ******************************/
    public LogonFragment2() {
        // TODO
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logon2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.logon2_next)
    public void onClickNext() {
        LogonActivity activity = (LogonActivity) getActivity();
        activity.onNextPage();
    }
}
