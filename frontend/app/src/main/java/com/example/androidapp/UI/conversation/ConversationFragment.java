package com.example.androidapp.UI.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.adapter.ConversationPagerAdapter;
import com.example.androidapp.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationFragment
        extends Fragment
        implements TabLayout.OnTabSelectedListener
{

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private ViewPager viewPager;
    private ConversationPagerAdapter pagerAdapter;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this,root);
        initView();
        return root;
    }

    private void initView(){
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.TUTOR)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.STUDENT)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = root.findViewById(R.id.pager);
        pagerAdapter = new ConversationPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

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
}
