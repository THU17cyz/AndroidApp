package com.example.androidapp.entity;

public class ApplicationInfo {
  public String direction;
  public String state;
  public String profile;

  public int applicationId;

  public enum Type{
    ADD,UPDATE,DELETE
  }
  public Type type=Type.UPDATE;

  public void setType(Type type){
    this.type=type;
  }

  public ApplicationInfo(String direction, String state, String profile) {
    this.direction = direction;
    this.state = state;
    this.profile = profile;
  }

  public ApplicationInfo(String direction, String state, String profile, int applicationId, Type type) {
    this.direction = direction;
    this.state = state;
    this.profile = profile;
    this.applicationId = applicationId;
    this.type = type;
  }

  public ApplicationInfo (ApplicationInfo another) {
    this.direction = another.direction;
    this.state = another.state;
    this.profile = another.profile;
    this.applicationId = another.applicationId;
    this.type = another.type;
  }
}
