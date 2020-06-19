package com.example.androidapp.UI.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.QueryActivity;
import com.example.androidapp.adapter.ConversationPagerAdapter;
import com.example.androidapp.R;
import com.example.androidapp.util.MyImageLoader;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationFragment extends Fragment implements TabLayout.OnTabSelectedListener
{

    @BindView(R.id.imageButton)
    CircleImageView drawerBtn;

    @BindView(R.id.search_view)
    EditText searchView;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private ViewPager viewPager;
    private ConversationPagerAdapter pagerAdapter;


    private Unbinder unbinder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_conversation, container, false);
        unbinder = ButterKnife.bind(this,root);
        MyImageLoader.loadImage(drawerBtn);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.TUTOR)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.STUDENT)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = root.findViewById(R.id.pager);
        pagerAdapter = new ConversationPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
