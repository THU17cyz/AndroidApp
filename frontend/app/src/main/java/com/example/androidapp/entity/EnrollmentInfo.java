package com.example.androidapp.entity;

public class EnrollmentInfo {
  public String direction;
  public String studentType;
  public String number;
  public String state;
  public String introduction;

  public EnrollmentInfo(String direction, String studentType, String number, String state, String introduction) {
    this.direction = direction;
    this.studentType = studentType;
    this.number = number;
    this.state = state;
    this.introduction = introduction;
  }
}
