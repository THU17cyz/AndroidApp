package com.example.androidapp.repository.chathistory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.androidapp.util.TimeStampConverter;

import java.util.Date;

@Entity(tableName = "chat_history")
public class ChatHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "time")
    @TypeConverters({TimeStampConverter.class})
    private Date time;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "type")
    private String type;

    // 是否为当前用户发送
    @ColumnInfo(name = "send")
    private String send;

    // 当前用户用户名
    @ColumnInfo(name = "user")
    private String user;

    // 联系人用户名
    @ColumnInfo(name = "contact")
    private String contact;

    public ChatHistory(Date time, String content, String type, String send, String user, String contact) {
        this.time = time;
        this.content = content;
        this.type = type;
        this.send = send;
        this.user = user;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
