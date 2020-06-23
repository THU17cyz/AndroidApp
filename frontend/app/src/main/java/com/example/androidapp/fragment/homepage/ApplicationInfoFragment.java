package com.example.androidapp.fragment.homepage;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.util.BasicInfo;

import java.util.ArrayList;

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
        if (getActivity() instanceof MainActivity) setInfo();
    }

    public void setInfo() {
        mApplicationList.clear();
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            mApplicationList.addAll(BasicInfo.mApplicationList);
        } else {
            VisitHomePageActivity activity_ = (VisitHomePageActivity) activity;
            mApplicationList.addAll(activity_.mApplicationList);
        }
        adapter.notifyDataSetChanged();
    }

}


