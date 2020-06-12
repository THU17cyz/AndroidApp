package com.example.androidapp.UI.query;

import androidx.lifecycle.LiveData;

public class Info extends LiveData<Info> {
    String name;
    String time;

    public Info(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public void setName(String name){this.name=name;postValue(this);}
    public String getName(String name){return this.name;}
    public void setTime(String time){this.time=time;postValue(this);}
    public String getTime(String time){return this.time;}

}