package com.example.androidapp.fragment.homepageEdit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.request.user.UpdateInfoPlusRequest;
import com.example.androidapp.util.BasicInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class EditStudyInfoFragment extends Fragment {

    @BindView(R.id.text_direction_or_interest)
    TextView textDirOrInt;

    @BindView(R.id.edit_direction_or_interest)
    FormEditText dirOrInt;

    @BindView(R.id.text_result_or_experience)
    TextView textResOrExp;

    @BindView(R.id.edit_result_or_experience)
    FormEditText resOrExp;

    @BindView(R.id.edit_url)
    FormEditText url;

    private Unbinder unbinder;

    public EditStudyInfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_study_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setInfo();
    }

    public void setInfo() {
        if (BasicInfo.TYPE.equals("S")) {
            textDirOrInt.setText("兴趣方向");
            textResOrExp.setText("研究经历");
            dirOrInt.setText(BasicInfo.mInterest);
            resOrExp.setText(BasicInfo.mExperience);
            url.setText(BasicInfo.mUrl);
        } else {
            textDirOrInt.setText("研究方向");
            textResOrExp.setText("研究成果");
            dirOrInt.setText(BasicInfo.mDirection);
            resOrExp.setText(BasicInfo.mResult);
            url.setText(BasicInfo.mUrl);
        }
    }


    public boolean checkContent() {
        if (BasicInfo.TYPE.equals("S")) {
            return dirOrInt.getText().toString() != null && dirOrInt.getText().toString().length() != 0;
        } else {
            return dirOrInt.getText().toString() != null && dirOrInt.getText().toString().length() != 0;
        }
    }

    public void update() {
        if (BasicInfo.TYPE.equals("S")) {
            BasicInfo.mInterest = dirOrInt.getText().toString();
            BasicInfo.mExperience = resOrExp.getText().toString();
            BasicInfo.mUrl = url.getText().toString();
            // 提交信息
            new UpdateInfoPlusRequest(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.e("response", resStr);
                    try {
                        JSONObject jsonObject = new JSONObject(resStr);
                        Boolean status = jsonObject.getBoolean("status");
                    } catch (JSONException e) {

                    }
                }
            },
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    dirOrInt.getText().toString(),
                    resOrExp.getText().toString(),
                    url.getText().toString()).send();

        } else {
            BasicInfo.mDirection = dirOrInt.getText().toString();
            BasicInfo.mResult = resOrExp.getText().toString();
            BasicInfo.mUrl = url.getText().toString();
            new UpdateInfoPlusRequest(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.e("response", resStr);
                    try {
                        JSONObject jsonObject = new JSONObject(resStr);
                        Boolean status = jsonObject.getBoolean("status");
                    } catch (JSONException e) {

                    }
                }
            },
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    dirOrInt.getText().toString(),
                    resOrExp.getText().toString(),
                    null,
                    null,
                    url.getText().toString()).send();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
