package com.example.androidapp.Fragments.QueryResult;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidapp.Popup.OrderList;
import com.example.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Teacher extends Fragment {

    @BindView(R.id.orderSpinner)
    Spinner orderSpinner;

    @BindView(R.id.selectText)
    TextView selectText;

    private ArrayAdapter<String> spinnerAdapter;

    public boolean isFilterOpen = false;

    OrderList orderList;


    private Unbinder unbinder;

    private static final String[] order = {"default", "hot"};

    public Teacher() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, view);

        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, order);

        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        orderSpinner.setAdapter(spinnerAdapter);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Toast.makeText(getActivity(), order[pos], Toast.LENGTH_SHORT).show();
                // new OrderList(getContext()).showPopupWindow(orderSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.selectText)
    public void openSelectWindow() {
//        if (selectText.isActivated()) {
//            orderList.dismiss();
//            selectText.setActivated(false);
//        } else {
//            selectText.setActivated(true);
//            orderList = new OrderList(getContext());
//            orderList.showPopupWindow(orderSpinner);
//            orderList.setBackground(0);
//        }
        if (isFilterOpen) {
            isFilterOpen = false;
            if (orderList != null) orderList.dismiss();
            selectText.setTextColor(Color.BLACK);
        } else {
            isFilterOpen = true;
            selectText.setTextColor(Color.BLUE);
            orderList = new OrderList(getContext());
            orderList.showPopupWindow(orderSpinner);
        }

//        orderList.setOutSideTouchable(true);
//        orderList.setPopupGravity(Gravity.BOTTOM);
//        orderList.setAlignBackground(true);
//        orderList.setAlignBackgroundGravity(Gravity.TOP);
        // orderList.setBackground(0);

    }
}
