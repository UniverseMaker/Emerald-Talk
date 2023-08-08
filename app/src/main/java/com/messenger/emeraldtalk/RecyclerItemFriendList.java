package com.messenger.emeraldtalk;

import android.graphics.drawable.Drawable;

public class RecyclerItemFriendList {
    public Drawable mImage;
    public String mName;
    public String mPhone;
    public String mProfile;

    public RecyclerItemFriendList(Drawable Image, String Name, String Phone, String Profile){
        mImage = Image;
        mName = Name;
        mPhone = Phone;
        mProfile = Profile;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable mImage) {
        this.mImage = mImage;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getProfile() {
        return mProfile;
    }

    public void setProfile(String mProfile) {
        this.mProfile = mProfile;
    }
}
