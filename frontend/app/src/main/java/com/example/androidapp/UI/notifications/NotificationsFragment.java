package com.example.androidapp.UI.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.adapter.NotificationPagerAdapter;
import com.example.androidapp.R;
import com.example.androidapp.activity.TeacherHomepageActivity;
import com.google.android.material.tabs.TabLayout;

public class NotificationsFragment extends Fragment {

    private Button all_read;
    private TextView type_text;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        type_text = root.findViewById(R.id.type_text);
        type_text.setText(getString(R.string.INFO_LIST));
        all_read = root.findViewById(R.id.all_read);
        all_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherHomepageActivity.class);
                startActivity(intent);
            }
        });


        TabLayout tabLayout = root.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.INFO)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.CHAT)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = root.findViewById(R.id.pager);
//        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        NotificationPagerAdapter pagerAdapter = new NotificationPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    type_text.setText(getString(R.string.INFO_LIST));
                } else if(tab.getPosition()==1){
                    type_text.setText(getString(R.string.CHAT_LIST));
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;
    }
}
