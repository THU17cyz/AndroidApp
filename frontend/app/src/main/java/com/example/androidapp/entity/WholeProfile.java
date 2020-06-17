package com.example.androidapp.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class WholeProfile extends ShortProfile {
    public String signature;
    public String phone;
    public String email;
    public String homepage;
    public String address;
    public String introduction;
    public String research_fields;
    public String research_achievements;
    public String research_interest;
    public String research_experience;
    public String promotional_video_url;


//    // TODO 照片
//    public WholeProfile(int id, String name, String school, String url, int fanNum, boolean isValidated, boolean isFan) {
//        this.id = id;
//        this.name = name;
//        this.school = school;
//        this.fanNum = fanNum;
//        this.url = url;
//        this.isValidated = isValidated;
//        this.isFan = isFan;
//    }

    public WholeProfile(ShortProfile shortProfile, JSONObject jsonObject) throws JSONException {
        super(shortProfile);
        this.signature = jsonObject.getString("signature");
        this.phone = jsonObject.getString("phone");
        this.email = jsonObject.getString("email");
        this.homepage = jsonObject.getString("homepage");
        this.address = jsonObject.getString("address");
        this.introduction = jsonObject.getString("introduction");
        this.promotional_video_url = jsonObject.getString("promotional_video_url");

        if (this.isTeacher) {
            this.research_fields = jsonObject.getString("research_fields");
            this.research_achievements = jsonObject.getString("research_achievements");
        } else {
            this.research_interest = jsonObject.getString("research_interest");
            this.research_experience = jsonObject.getString("research_experience");
        }


    }
}
