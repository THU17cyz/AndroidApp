package com.example.androidapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ShortProfile implements Parcelable {
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
    public int relate;

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
        try {
            this.relate = jsonObject.getInt("match_degree");
        } catch (JSONException e) {

        }

    }

    public ShortProfile(ShortProfile shortProfile) {
        this.isTeacher = shortProfile.isTeacher;
        this.id = shortProfile.id;
        this.name = shortProfile.name;
        this.department = shortProfile.department;
        this.school = shortProfile.school;
        this.fanNum = shortProfile.fanNum;
        this.url = shortProfile.url;
        this.isMale = shortProfile.isMale;
        this.isValidated = shortProfile.isValidated;
        this.isFan = shortProfile.isFan;
    }

    protected ShortProfile(Parcel in) {
        isTeacher = in.readByte() != 0;
        id = in.readInt();
        name = in.readString();
        school = in.readString();
        department = in.readString();
        fanNum = in.readInt();
        url = in.readString();
        isMale = in.readByte() != 0;
        isValidated = in.readByte() != 0;
        isFan = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isTeacher ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(school);
        dest.writeString(department);
        dest.writeInt(fanNum);
        dest.writeString(url);
        dest.writeByte((byte) (isMale ? 1 : 0));
        dest.writeByte((byte) (isValidated ? 1 : 0));
        dest.writeByte((byte) (isFan ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShortProfile> CREATOR = new Creator<ShortProfile>() {
        @Override
        public ShortProfile createFromParcel(Parcel in) {
            return new ShortProfile(in);
        }

        @Override
        public ShortProfile[] newArray(int size) {
            return new ShortProfile[size];
        }
    };
}
