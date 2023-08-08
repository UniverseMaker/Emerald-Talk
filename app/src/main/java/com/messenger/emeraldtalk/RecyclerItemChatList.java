package com.messenger.emeraldtalk;

import android.graphics.drawable.Drawable;

public class RecyclerItemChatList {
    public Drawable mImage;
    public String id;
    public String mMember;
    public String mTitle;
    public String mContents;
    public String mVariable;
    public String mTime;

    public RecyclerItemChatList(Drawable Image, String id, String Title, String Contents, String Time){
        mImage = Image;
        this.id = id;
        mTitle = Title;
        mContents = Contents;
        mTime = Time;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable mImage) {
        this.mImage = mImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember() {
        return mMember;
    }

    public void setMember(String mMember) {
        this.mMember = mMember;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContents() {
        return mContents;
    }

    public void setContents(String mContents) {
        this.mContents = mContents;
    }

    public String getVariable() {
        return mVariable;
    }

    public void setVariable(String mVariable) {
        this.mVariable = mVariable;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }
}
