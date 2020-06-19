package com.example.androidapp.repository.chathistoryhasread;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.androidapp.util.TimeStampConverter;

import java.util.Date;

@Entity(tableName = "chat_history_has_read")
public class ChatHistoryHasRead {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "user")
  private String user;

  @ColumnInfo(name = "contact")
  private String contact;

  @ColumnInfo(name = "hasRead")
  private Boolean hasRead;

  @ColumnInfo(name = "number")
  private int number;

  @ColumnInfo(name = "lastMessage")
  private String lastMessage;

  public ChatHistoryHasRead(String user, String contact, Boolean hasRead, int number, String lastMessage) {
    this.user = user;
    this.contact = contact;
    this.hasRead = hasRead;
    this.number = number;
    this.lastMessage = lastMessage;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public Boolean getHasRead() {
    return hasRead;
  }

  public void setHasRead(Boolean hasRead) {
    this.hasRead = hasRead;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }
}
