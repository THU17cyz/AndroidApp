package com.example.androidapp.fragment.logon;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.LoginActivity;
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.request.user.LogonRequest;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
    Spinner typeSpinner;

    @BindView(R.id.logon1_account)
    EditText accountEditText;

    @BindView(R.id.logon1_password)
    EditText passwordEditText;

    @BindView(R.id.logon1_repeat_password)
    EditText repeatPasswordEditText;

    private Unbinder unbinder;

    /******************************
     ************ 方法 ************
     ******************************/
    public LogonFragment1() {
        // TODO
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logon1, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        String type = typeSpinner.getSelectedItem().toString().equals(getResources().getStringArray(R.array.t_s_type)[0]) ? "T" : "S";
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        if (!password.equals(repeatPassword))
            Hint.showLongBottomToast(getContext(), "两次密码不一致！");
        else
            new LogonRequest(handleLogon, type, account, password, "？").send();
    }

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleLogon = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), "注册失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    boolean status = (Boolean) jsonObject.get("status");
                    String info = (String) jsonObject.get("info");
                    if (status) {
                        requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), info));
                        requireActivity().runOnUiThread(((LogonActivity) requireActivity())::onNextPage);
                    } else {
                        requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), info));
                    }
                }
            } catch (JSONException e) {
                requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), "注册失败..."));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), "注册失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };


}
