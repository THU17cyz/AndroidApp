package com.example.androidapp.fragment.homepage;


import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.UI.dashboard.DashboardFragment;
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EditApplication;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.TeacherProfile;
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ApplicationInfoFragment extends Fragment {

  RecyclerView recyclerView;
  ApplicationListAdapter adapter;
  ArrayList<ApplicationInfo> mApplicationList;

  public ApplicationInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_intention_info, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    mApplicationList = new ArrayList<>();
    adapter = new ApplicationListAdapter(mApplicationList, getContext());
    adapter.setRecyclerManager(recyclerView);
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    setInfo();
  }

  public void setInfo() {
    mApplicationList.clear();
    Activity activity = getActivity();
    if (activity instanceof MainActivity) {
      mApplicationList.addAll(BasicInfo.mApplicationList);
    }
    else {
      VisitHomePageActivity activity_ = (VisitHomePageActivity) activity;
      mApplicationList.addAll(activity_.mApplicationList);
    }
    adapter.notifyDataSetChanged();
  }

}


