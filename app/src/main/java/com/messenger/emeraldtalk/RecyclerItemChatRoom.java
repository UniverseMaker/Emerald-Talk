package com.messenger.emeraldtalk;

import android.graphics.drawable.Drawable;

public class RecyclerItemChatRoom {
    public Drawable mImage;
    public String mOwner;
    public String mData;
    public String mTime;

    public RecyclerItemChatRoom(Drawable Image, String Owner, String Data, String Time){
        mImage = Image;
        mOwner = Owner;
        mData = Data;
        mTime = Time;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable mImage) {
        this.mImage = mImage;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }
}
