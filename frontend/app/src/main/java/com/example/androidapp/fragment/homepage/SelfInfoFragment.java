package com.example.androidapp.fragment.homepage;

import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelfInfoFragment extends Fragment {

  //To do
  public SelfInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_self_info, container, false);
    return view;
  }

}

