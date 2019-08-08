package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileInfo implements Parcelable {
    private String accountEmail;
    private String accountNick;
    private String accountImage;
    private String accountPw;
    private int capacity,favorite,reason;

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountNick() {
        return accountNick;
    }

    public void setAccountNick(String accountNick) {
        this.accountNick = accountNick;
    }

    public String getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(String accountImage) {
        this.accountImage = accountImage;
    }

    public String getAccountPw() {
        return accountPw;
    }

    public void setAccountPw(String accountPw) {
        this.accountPw = accountPw;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public ProfileInfo(String accountEmail, String accountNick, String accountImage, String accountPw, int capacity, int favorite, int reason) {
        this.accountEmail = accountEmail;
        this.accountNick = accountNick;
        this.accountImage = accountImage;
        this.accountPw = accountPw;
        this.capacity = capacity;
        this.favorite = favorite;
        this.reason = reason;
    }

    protected ProfileInfo(Parcel in) {
        accountEmail = in.readString();
        accountNick = in.readString();
        accountImage = in.readString();
        accountPw = in.readString();
        capacity = in.readInt();
        favorite = in.readInt();
        reason = in.readInt();
    }

    public static final Creator<ProfileInfo> CREATOR = new Creator<ProfileInfo>() {
        @Override
        public ProfileInfo createFromParcel(Parcel in) {
            return new ProfileInfo(in);
        }

        @Override
        public ProfileInfo[] newArray(int size) {
            return new ProfileInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accountEmail);
        parcel.writeString(accountNick);
        parcel.writeString(accountImage);
        parcel.writeString(accountPw);
        parcel.writeInt(capacity);
        parcel.writeInt(favorite);
        parcel.writeInt(reason);
    }
}
