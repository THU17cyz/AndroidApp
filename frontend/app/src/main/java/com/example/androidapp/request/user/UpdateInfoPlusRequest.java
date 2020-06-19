package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class UpdateInfoPlusRequest extends BasePostRequest {
    public UpdateInfoPlusRequest(Callback callback, String signature, String phone, String email, String homepage, String address,
                                 String introduction, String research_fields, String research_achievements, String research_interest,
                                 String research_experience, String promotional_video_url) {
        // 设置请求URL
        this.to("/api/user/update_info_plus");
        // 设置请求参数
        if (signature != null) this.put("signature", signature);
        if (phone != null) this.put("phone", phone);
        if (email != null) this.put("email", email);
        if (homepage != null) this.put("homepage", homepage);
        if (address != null) this.put("address", address);
        if (introduction != null) this.put("introduction", introduction);
        if (research_fields != null) this.put("research_fields", research_fields);
        if (research_achievements != null) this.put("research_achievements", research_achievements);
        if (research_interest != null) this.put("research_interest", research_interest);
        if (research_experience != null) this.put("research_experience", research_experience);
        if (promotional_video_url != null) this.put("promotional_video_url", promotional_video_url);
        // 设置回调函数
        this.call(callback);
    }
}
