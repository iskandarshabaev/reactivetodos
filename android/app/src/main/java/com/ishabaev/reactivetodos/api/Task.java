package com.ishabaev.reactivetodos.api;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    @PrimaryKey
    private String mId;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("createdAt")
    private long createdAt;

    @SerializedName("owner")
    private String owner;

    @SerializedName("useName")
    private String useName;

    @SerializedName("privateField")
    private boolean privateField;

    @SerializedName("checked")
    private boolean checked;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public boolean isPrivateField() {
        return privateField;
    }

    public void setPrivateField(boolean privateField) {
        this.privateField = privateField;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setId(String id){
        mId = id;
    }

}
