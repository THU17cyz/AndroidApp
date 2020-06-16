package com.example.androidapp.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ShortProfile {
    public boolean isTeacher;
    public int id;
    public String name;
    public String school;
    public String department;
    public int fanNum;
    public String url;
    public boolean isMale;
    public boolean isValidated;
    public boolean isFan;

    public ShortProfile() {
        
    }
    // TODO 照片
    public ShortProfile(int id, String name, String school, String url, int fanNum, boolean isValidated, boolean isFan) {
        this.id = id;
        this.name = name;
        this.school = school;
        this.fanNum = fanNum;
        this.url = url;
        this.isValidated = isValidated;
        this.isFan = isFan;
    }

    public ShortProfile(JSONObject jsonObject, boolean isTeacher) throws JSONException {
        this.isTeacher = isTeacher;
        this.name = jsonObject.getString("name");
        this.isMale = jsonObject.getString("gender").equalsIgnoreCase("M");
        this.school = jsonObject.getString("school");
        this.department = jsonObject.getString("department");
        this.isValidated = jsonObject.getString("auth_state").equalsIgnoreCase("UQ");
        this.fanNum = jsonObject.getInt("fans_number");
        this.isFan = jsonObject.getBoolean("is_followed");
        if (isTeacher) {
            this.id = jsonObject.getInt("teacher_id");
        } else {
            this.id = jsonObject.getInt("student_id");
        }
    }
}
