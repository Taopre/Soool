package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InfoContentText implements Parcelable {
    private static final String TAG = "InfoContentText";

    /*
     * 받아오는 목록
     *
     * type (int)
     * text (String)
     * ----------
     * bookmark (Array)
     *   bookmarkNo (int)
     *   hasBookmarked (boolean)
     *
     * */

    @SerializedName("type")
    public int type;
    @SerializedName("text")
    public String text;



    public InfoContentText(){
        //Log.e(TAG, "소환이 되나1 "+ type+text);
    }
    public InfoContentText(int type, String text){

        this.type = type;
        this.text = text;

    }

    protected InfoContentText(Parcel in) {

        type = in.readInt();
        text = in.readString();

    }

    public static final Creator<InfoContentText> CREATOR = new Creator<InfoContentText>() {
        @Override
        public InfoContentText createFromParcel(Parcel in) {
            return new InfoContentText(in);
        }

        @Override
        public InfoContentText[] newArray(int size) {
            return new InfoContentText[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(type);
        parcel.writeString(text);
       // parcel.writeList(bookmarks);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
