package com.example.androidapp.fragment.logon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.request.user.UpdateInfoRequest;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogonFragment2 extends Fragment {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.logon2_next)
    Button nextButton;

    @BindView(R.id.logon2_name)
    EditText nameEditText;

    @BindView(R.id.logon2_gender)
    EditText genderEditText;

    @BindView(R.id.logon2_school)
    EditText schoolEditText;

    @BindView(R.id.logon2_department)
    EditText departmentEditText;

    private Unbinder unbinder;

    /******************************
     ************ 方法 ************
     ******************************/
    public LogonFragment2() {
        // TODO
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logon2, container, false);
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
    @OnClick(R.id.logon2_next)
    void onClickNext() {
        // 进行更新
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String school = schoolEditText.getText().toString();
        String department = departmentEditText.getText().toString();
        new UpdateInfoRequest(handleUpdate, name, gender, school, department, null, null, null).send();
    }

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleUpdate = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), "更新失败..."));
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
                requireActivity().runOnUiThread(() -> Hint.showLongBottomToast(getContext(), "更新失败..."));
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
