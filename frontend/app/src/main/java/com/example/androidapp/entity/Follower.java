package com.example.androidapp.entity;

public class Follower {
  private String imageUrl;
  private String name;

  public Follower(String imageUrl, String name) {
    this.imageUrl = imageUrl;
    this.name = name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getName() {
    return name;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setName(String name) {
    this.name = name;
  }
}
