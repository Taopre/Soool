package com.lpky.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable{

    private int accountPoint,accountBc;
    private String accountEmail;
    private String accountNick;
    private String accountImage;

    public  int getAccountPoint() {
        return accountPoint;
    }

    public void setAccountPoint(int accountPoint) {
        this.accountPoint = accountPoint;
    }

    public int getAccountBc() {
        return accountBc;
    }

    public void setAccountBc(int accountBc) {
        this.accountBc = accountBc;
    }

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




    protected UserProfile(Parcel in) {
        readFromParcel(in);
    }

    public final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(accountPoint);
        parcel.writeInt(accountBc);
        parcel.writeString(accountImage);
        parcel.writeString(accountEmail);
        parcel.writeString(accountNick);
    }
    private void readFromParcel(Parcel in){
        accountBc = in.readInt();
        accountEmail = in.readString();
        accountImage = in.readString();
        accountNick = in.readString();
        accountPoint = in.readInt();
    }
}
