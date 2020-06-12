package com.example.androidapp.UI.query;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapp.R;

public class QueryFragment extends Fragment {
    private QueryViewModel queryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        queryViewModel =
                ViewModelProviders.of(this).get(QueryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_query, container, false);
        final TextView textView = root.findViewById(R.id.text_query);
        queryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}