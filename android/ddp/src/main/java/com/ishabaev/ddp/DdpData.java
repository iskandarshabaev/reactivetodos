package com.ishabaev.ddp;

/**
 * Created by ishabaev on 17.08.16.
 */
public class DdpData {

    public enum DataType {
        ADDED,
        CHANGED,
        REMOVED
    }

    private DataType mType;
    private String mCollectionName;
    private String mId;
    private String mAddedValue;

    public DdpData(String id, DataType type, String collectionName, String addedValue) {
        mId = id;
        mType = type;
        mCollectionName = collectionName;
        mAddedValue = addedValue;
    }

    public DataType getType() {
        return mType;
    }

    public void setType(DataType mType) {
        this.mType = mType;
    }

    public String getCollectionName() {
        return mCollectionName;
    }

    public void setCollectionName(String mCollectionName) {
        this.mCollectionName = mCollectionName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getAddedValue() {
        return mAddedValue;
    }

    public void setAddedValue(String mAddedValue) {
        this.mAddedValue = mAddedValue;
    }
}
