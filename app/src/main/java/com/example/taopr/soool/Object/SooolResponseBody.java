package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

public class  SooolResponseBody implements Parcelable{

    String result;

    public SooolResponseBody(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    protected SooolResponseBody(Parcel in) {
        result = in.readString();
    }

    public static final Creator<SooolResponseBody> CREATOR = new Creator<SooolResponseBody>() {
        @Override
        public SooolResponseBody createFromParcel(Parcel in) {
            return new SooolResponseBody(in);
        }

        @Override
        public SooolResponseBody[] newArray(int size) {
            return new SooolResponseBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(result);
    }
}
