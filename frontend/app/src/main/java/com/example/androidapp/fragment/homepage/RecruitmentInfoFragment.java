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
import com.example.androidapp.adapter.EnrollmentListAdapter;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.util.BasicInfo;

import java.util.ArrayList;

public class RecruitmentInfoFragment extends Fragment {

    RecyclerView recyclerView;
    EnrollmentListAdapter adapter;
    ArrayList<RecruitmentInfo> mRecruitmentList;



    public RecruitmentInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intention_info, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecruitmentList = new ArrayList<>();
        adapter = new EnrollmentListAdapter(mRecruitmentList, getContext());//初始化NameAdapter
        adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setInfo();
    }

    public void setInfo() {
        mRecruitmentList.clear();
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
//            DashboardFragment fragment = (DashboardFragment) RecruitmentInfoFragment.this.getParentFragment();
            mRecruitmentList.addAll(BasicInfo.mRecruitmentList);
        }
        else {
            VisitHomePageActivity activity_ = (VisitHomePageActivity) activity;
            mRecruitmentList.addAll(activity_.mRecruitmentList);
        }
        adapter.notifyDataSetChanged();
    }

}
