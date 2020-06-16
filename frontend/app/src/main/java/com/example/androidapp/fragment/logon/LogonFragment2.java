package com.example.androidapp.fragment.logon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.Unbinder;

public class LogonFragment2 extends Fragment {
    @BindView(R.id.nextStep2)
    Button nextStepButton;
    private Unbinder unbinder;


    //To do
    public LogonFragment2() {

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
        nextStepButton.setOnClickListener(v -> {
            LogonActivity activity = (LogonActivity) getActivity();
            activity.onNextPage();
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
