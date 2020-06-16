package com.example.androidapp.fragment.HomepageEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidapp.R;

public class EditStudyInfoFragment extends Fragment {

  //To do
  public EditStudyInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_study_info, container, false);
    return view;
  }

}
