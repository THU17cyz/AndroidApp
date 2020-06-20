package com.example.androidapp.UI.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.QueryActivity;
import com.example.androidapp.adapter.RecommendPagerAdapter;
import com.example.androidapp.util.MyImageLoader;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @BindView(R.id.imageButton)
    CircleImageView drawerBtn;

    @BindView(R.id.search_view)
    EditText searchView;

//    @BindView(R.id.refreshLayout)
//    public RefreshLayout refreshLayout;

    private Unbinder unbinder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("推荐导师"));
        tabLayout.addTab(tabLayout.newTab().setText("推荐学生"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = root.findViewById(R.id.pager);
        RecommendPagerAdapter pagerAdapter = new RecommendPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        MyImageLoader.loadImage(drawerBtn);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        drawerBtn.setOnClickListener(v -> {
            MainActivity parentActivity = (MainActivity) getActivity();
            parentActivity.openDrawer();
        });
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QueryActivity.class);
            startActivity(intent);
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}
