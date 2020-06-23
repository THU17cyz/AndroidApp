package com.example.androidapp.entity;

public class RecruitmentInfo {
    public String direction;
    public String studentType;
    public String number;
    public String state;
    public String introduction;

    public int enrollmentId;
    public Type type = Type.UPDATE;

    public RecruitmentInfo(String direction, String studentType, String number, String state, String introduction) {
        this.direction = direction;
        this.studentType = studentType;
        this.number = number;
        this.state = state;
        this.introduction = introduction;
    }

    public RecruitmentInfo(String direction, String studentType, String number, String state, String introduction, int enrollmentId, Type type) {
        this.direction = direction;
        this.studentType = studentType;
        this.number = number;
        this.state = state;
        this.introduction = introduction;
        this.enrollmentId = enrollmentId;
        this.type = type;
    }

    public RecruitmentInfo(RecruitmentInfo another) {
        this.direction = another.direction;
        this.studentType = another.studentType;
        this.number = another.number;
        this.state = another.state;
        this.introduction = another.introduction;
        this.enrollmentId = another.enrollmentId;
        this.type = another.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ADD, UPDATE, DELETE
    }
}
