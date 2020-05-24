package com.example.androidapp.Fragments.ForgetPassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.androidapp.ForgetPasswordActivity;
import com.example.androidapp.LogonActivity;
import com.example.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ForgetPasswordFragment1 extends Fragment {

    @BindView(R.id.nextStep)
    Button nextStepButton;
    private Unbinder unbinder;

    //To do
    public ForgetPasswordFragment1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password1, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nextStepButton.setOnClickListener(v -> {
            ForgetPasswordActivity activity = (ForgetPasswordActivity) getActivity();
            activity.nextPage();
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
