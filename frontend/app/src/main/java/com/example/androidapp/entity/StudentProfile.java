package com.example.androidapp.entity;

public class StudentProfile extends ShortProfile {

    // TODO 照片
    public StudentProfile(int id, String name, String affiliation, String url, int fanNum) {
        this.id = id;
        this.name = name;
        this.affiliation = affiliation;
        this.fanNum = fanNum;
        this.url = url;
    }
}
