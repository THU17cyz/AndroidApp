package com.example.androidapp.fragment.logon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.activity.LoginActivity;
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.request.user.UserAuthRequest;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.Valid;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogonFragment3 extends Fragment {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.logon3_next)
    Button nextButton;

    @BindView(R.id.logon3_ts_number)
    FormEditText tsNumberEditText;

    @BindView(R.id.logon3_id_number)
    FormEditText idNumberEditText;

    private Unbinder unbinder;
    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleVerify = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "验证失败，请先注册！"));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    boolean status = (Boolean) jsonObject.get("status");
                    String info = (String) jsonObject.get("info");
                    if (status) {
                        requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), info));
                        // 认证成功后返回登录界面
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), info));
                    }
                }
            } catch (JSONException e) {
                requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "验证失败，请稍后重试！"));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "验证失败，请稍后重试！"));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    /******************************
     ************ 方法 ************
     ******************************/
    public LogonFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logon3, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 添加验证
        tsNumberEditText.addValidator(new Valid.NumberValidator());
        tsNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tsNumberEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        idNumberEditText.addValidator(new Valid.NumberValidator());
        idNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idNumberEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.logon3_next)
    void onClickNext() {
        // 进行验证
        String tsNumber = tsNumberEditText.getText().toString();
        String idNumber = idNumberEditText.getText().toString();
        if (!Valid.isNumber(tsNumber) || !Valid.isNumber(idNumber)) {
            Hint.showLongCenterToast(getContext(), "格式错误！");
            return;
        }
        new UserAuthRequest(handleVerify, tsNumber, tsNumber, idNumber).send();
    }
}
