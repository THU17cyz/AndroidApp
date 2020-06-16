package com.example.androidapp.entity;

public class TeacherProfile extends ShortProfile {

    // TODO 照片
    public TeacherProfile(int id, String name, String affiliation, String url, int fanNum) {
        this.id = id;
        this.name = name;
        this.school = affiliation;
        this.fanNum = fanNum;
        this.url = url;
    }
}
