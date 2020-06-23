package com.example.androidapp.fragment.logon;

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
import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.request.user.UpdateInfoRequest;
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


/**
 * 注册界面2
 */
public class LogonFragment2 extends Fragment {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.logon2_next)
    Button nextButton;

    @BindView(R.id.logon2_name)
    FormEditText nameEditText;

    @BindView(R.id.logon2_gender)
    FormEditText genderEditText;

    @BindView(R.id.logon2_school)
    FormEditText schoolEditText;

    @BindView(R.id.logon2_department)
    FormEditText departmentEditText;

    private Unbinder unbinder;
    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleUpdate = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "更新失败，请先注册！"));
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
                requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "更新失败，请稍后重试！"));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            requireActivity().runOnUiThread(() -> Hint.showLongCenterToast(getContext(), "更新失败，请稍后重试！"));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    /******************************
     ************ 方法 ************
     ******************************/
    public LogonFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logon2, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 添加验证
        nameEditText.addValidator(new Valid.NotBlankValidator());
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        genderEditText.addValidator(new Valid.GenderValidator());
        genderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                genderEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        schoolEditText.addValidator(new Valid.NotBlankValidator());
        schoolEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                schoolEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        departmentEditText.addValidator(new Valid.NotBlankValidator());
        departmentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                departmentEditText.testValidity();
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
    @OnClick(R.id.logon2_next)
    void onClickNext() {
        // 进行更新
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String school = schoolEditText.getText().toString();
        String department = departmentEditText.getText().toString();
        if (Valid.isBlank(name) || !Valid.isGender(gender) || Valid.isBlank(school) || Valid.isBlank(department)) {
            Hint.showLongCenterToast(getContext(), "格式错误！");
            return;
        }
        if (gender.equals("男"))
            gender = "M";
        else if (gender.equals("女"))
            gender = "F";
        else
            gender = "U";
        new UpdateInfoRequest(handleUpdate, name, gender, school, department, null, null, null).send();
    }
}
