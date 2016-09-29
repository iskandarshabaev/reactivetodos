package com.ishabaev.reactivetodos.api;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Date extends RealmObject {

    @SerializedName("$date")
    private long date;

    public Date(){}

    public Date(long date){
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
