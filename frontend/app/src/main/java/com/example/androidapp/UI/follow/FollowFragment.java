package com.example.androidapp.UI.follow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ConversationPagerAdapter;
import com.example.androidapp.adapter.FollowPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FollowFragment extends Fragment {
  private FollowViewModel followViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_follow_nav, container, false);

//    followViewModel =
//            ViewModelProviders.of(this).get(FollowViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_query);
//    followViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//      @Override
//      public void onChanged(@Nullable String s) {
//        textView.setText(s);
//      }
//    });

    TabLayout tabLayout = root.findViewById(R.id.tab_layout);
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.WATCH)));
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.FOLLOWER)));
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


    final ViewPager viewPager = root.findViewById(R.id.pager);
    FollowPagerAdapter pagerAdapter = new FollowPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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


    return root;
  }
}
