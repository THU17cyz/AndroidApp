package com.example.androidapp.entity;

public class ShortProfile {
    public int id;
    public String name;
    public String affiliation;
    public int fanNum;
    public String url;
    public boolean isValidated;
    public boolean isFan;

    public ShortProfile() {
        
    }
    // TODO 照片
    public ShortProfile(int id, String name, String affiliation, String url, int fanNum, boolean isValidated, boolean isFan) {
        this.id = id;
        this.name = name;
        this.affiliation = affiliation;
        this.fanNum = fanNum;
        this.url = url;
        this.isValidated = isValidated;
        this.isFan = isFan;
    }
}
