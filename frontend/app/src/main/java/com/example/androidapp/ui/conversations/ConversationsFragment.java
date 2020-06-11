package com.example.androidapp.ui.conversations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.Adapter.TestAdapter;
import com.example.androidapp.R;
import com.example.androidapp.ui.dashboard.DashboardViewModel;
import com.example.androidapp.ui.query.QueryViewModel;

import java.util.Arrays;
import java.util.List;

public class ConversationsFragment extends Fragment {

  private ConversationsViewModel conversationsViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    conversationsViewModel =
            ViewModelProviders.of(this).get(ConversationsViewModel.class);
    View root = inflater.inflate(R.layout.fragment_conversations, container, false);
    final TextView textView = root.findViewById(R.id.text_query);
    conversationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });
    return root;
  }
}
