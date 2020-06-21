package com.example.androidapp.fragment.logon;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.request.user.LoginRequest;
import com.example.androidapp.request.user.LogonRequest;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.OptionItems;
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

public class LogonFragment1 extends Fragment {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.logon1_next)
    Button nextButton;

    @BindView(R.id.logon1_type)
    TextView typeSelector;

    @BindView(R.id.logon1_account)
    FormEditText accountEditText;

    @BindView(R.id.logon1_password)
    FormEditText passwordEditText;

    @BindView(R.id.logon1_repeat_password)
    FormEditText repeatPasswordEditText;

    private Unbinder unbinder;

    /******************************
     ************ 方法 ************
     ******************************/
    public LogonFragment1() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logon1, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 添加验证
        accountEditText.addValidator(new Valid.AccountValidator());
        accountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { accountEditText.testValidity(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        passwordEditText.addValidator(new Valid.PasswordValidator());
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { passwordEditText.testValidity(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        repeatPasswordEditText.addValidator(new Valid.PasswordValidator());
        repeatPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { repeatPasswordEditText.testValidity(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.logon1_next)
    void onClickNext() {
        // 进行注册
        String type = "";
        if(typeSelector.getText().toString().equals(OptionItems.optionsType.get(0))){
            type = "T";
        } else if(typeSelector.getText().toString().equals(OptionItems.optionsType.get(1))){
            type = "S";
        }
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        if (!Valid.isAccount(account) || !Valid.isPassword(password) || !Valid.isPassword(repeatPassword)) {
            Hint.showLongCenterToast(getContext(), "格式错误！");
            return;
        }
        if (!password.equals(repeatPassword))
            Hint.showLongCenterToast(getContext(), "重复密码不一致！");
        else
            new LogonRequest(handleLogon, type, account, password, "？").send();
    }

    @OnClick(R.id.logon1_type)
    public void onClickTypeSelector(){

        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(typeSelector.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String type = OptionItems.optionsType.get(options1);
                typeSelector.setText(type);
            }
        })
                .setTitleText("选择类型")
                .setSubmitText("确定")
                .setCancelText("取消")
                .setContentTextSize(18)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(OptionItems.optionsType);
        pvOptions.show();
    }

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleLogon = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "注册失败，请稍后重试！"));
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
                        // 注册成功，进行登录
                        String type = "";
                        if(typeSelector.getText().toString().equals(OptionItems.optionsType.get(0))){
                            type = "T";
                        } else if(typeSelector.getText().toString().equals(OptionItems.optionsType.get(1))){
                            type = "S";
                        }
                        String account = accountEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        new LoginRequest(handleLogin, type, account, password).send();
                    } else {
                        requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), info));
                    }
                }
            } catch (JSONException e) {
                requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "注册失败，请稍后重试！"));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "注册失败，请稍后重试！"));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleLogin = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "登录失败，请稍后重试！"));
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
                        requireActivity().runOnUiThread(((LogonActivity) requireActivity())::onNextPage);
                    } else {
                        requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), info));
                    }
                }
            } catch (JSONException e) {
                requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "登录失败，请稍后重试！"));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "登录失败，请稍后重试！"));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };


}
