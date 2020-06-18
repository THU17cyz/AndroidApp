package com.example.androidapp.entity;

public class EnrollmentInfo {
  public String direction;
  public String studentType;
  public String number;
  public String state;
  public String introduction;

  public int enrollmentId;

  public enum Type{
    ADD,UPDATE,DELETE
  }
  public Type type=Type.UPDATE;

  public void setType(Type type){
    this.type=type;
  }

  public EnrollmentInfo(String direction, String studentType, String number, String state, String introduction) {
    this.direction = direction;
    this.studentType = studentType;
    this.number = number;
    this.state = state;
    this.introduction = introduction;
  }

  public EnrollmentInfo(String direction, String studentType, String number, String state, String introduction, int enrollmentId, Type type) {
    this.direction = direction;
    this.studentType = studentType;
    this.number = number;
    this.state = state;
    this.introduction = introduction;
    this.enrollmentId = enrollmentId;
    this.type = type;
  }
}
