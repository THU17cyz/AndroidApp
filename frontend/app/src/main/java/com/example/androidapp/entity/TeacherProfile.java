package com.example.androidapp.entity;

public class TeacherProfile {
    public String name;
    public String affiliation;
    public int fanNum;
    public String url;
    // TODO 照片
    public TeacherProfile(String name, String affiliation, String url, int fanNum) {
        this.name = name;
        this.affiliation = affiliation;
        this.fanNum = fanNum;
        this.url = url;
    }
}
