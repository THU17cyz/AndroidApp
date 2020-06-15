package com.example.androidapp.entity;

public class StudentProfile {
    public int id;
    public String name;
    public String affiliation;
    public int fanNum;
    public String url;
    // TODO 照片
    public StudentProfile(int id, String name, String affiliation, String url, int fanNum) {
        this.id = id;
        this.name = name;
        this.affiliation = affiliation;
        this.fanNum = fanNum;
        this.url = url;
    }
}
