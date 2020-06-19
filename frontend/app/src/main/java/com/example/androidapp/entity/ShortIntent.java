package com.example.androidapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ShortIntent implements Parcelable {
    public boolean isTeacher;
    public int id;
    public String name;
    public String school;
    public String department;
    public int recruitNum;
    public String intentionState;
    public String recruitType;
    public String target;
    public int relate;
    public boolean isFan;

    public ShortIntent(JSONObject jsonObject, boolean isTeacher) throws JSONException {
        this.isTeacher = isTeacher;

        if (isTeacher) {
            this.id = jsonObject.getInt("teacher_id");
            this.target = jsonObject.getString("research_fields");
            this.name = jsonObject.getString("teacher_name");
            this.school = jsonObject.getString("teacher_school");
            this.department = jsonObject.getString("teacher_department");
            this.recruitNum = jsonObject.getInt("recruitment_number");
            this.recruitType = jsonObject.getString("recruitment_type");
        } else {
            this.id = jsonObject.getInt("student_id");
            this.target = jsonObject.getString("research_interests");
            this.name = jsonObject.getString("student_name");
            this.school = jsonObject.getString("student_school");
            this.department = jsonObject.getString("student_department");
        }
//        this.isFan = jsonObject.getBoolean("is_followed");
        this.intentionState = jsonObject.getString("intention_state");
        this.relate = jsonObject.getInt("match_degree");
    }

    protected ShortIntent(Parcel in) {
        isTeacher = in.readByte() != 0;
        id = in.readInt();
        name = in.readString();
        school = in.readString();
        department = in.readString();
        recruitNum = in.readInt();
        intentionState = in.readString();
        recruitType = in.readString();
        target = in.readString();
        relate = in.readInt();
    }

    public static final Creator<ShortIntent> CREATOR = new Creator<ShortIntent>() {
        @Override
        public ShortIntent createFromParcel(Parcel in) {
            return new ShortIntent(in);
        }

        @Override
        public ShortIntent[] newArray(int size) {
            return new ShortIntent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isTeacher ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(school);
        dest.writeString(department);
        dest.writeInt(recruitNum);
        dest.writeString(intentionState);
        dest.writeString(recruitType);
        dest.writeString(target);
        dest.writeInt(relate);
    }
}
