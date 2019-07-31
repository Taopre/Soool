package com.lpky.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class InfoBookmark implements Parcelable {



    private static final String TAG = "InfoBookmark";
    @SerializedName("postNo")
    public int postNo;
    @SerializedName("bookmarkNo")
    public int bookmarkNo;
    @SerializedName("hasBookmarked")
    public String hasBookmarked;

    // https://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface

    public InfoBookmark(){
        Log.e(TAG, "InfoBookmark 소환되는거니");
    }

    public InfoBookmark(int postNo, int bookmarkNo, String hasBookmarked) {
        this.postNo = postNo;
        this.bookmarkNo = bookmarkNo;
        this.hasBookmarked = hasBookmarked;
    }

    protected InfoBookmark(Parcel in) {
        postNo = in.readInt();
        bookmarkNo = in.readInt();
        hasBookmarked = in.readString();
    }

    public static final Creator<InfoBookmark> CREATOR = new Creator<InfoBookmark>() {
        @Override
        public InfoBookmark createFromParcel(Parcel in) {
            return new InfoBookmark(in);
        }

        @Override
        public InfoBookmark[] newArray(int size) {
            return new InfoBookmark[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(postNo);
        parcel.writeInt(bookmarkNo);
        parcel.writeString(hasBookmarked);
    }

    public int getPostNo() {
        return postNo;
    }

    public void setPostNo(int postNo) {
        this.postNo = postNo;
    }

    public int getBookmarkNo() {
        return bookmarkNo;
    }

    public void setBookmarkNo(int bookmarkNo) {
        this.bookmarkNo = bookmarkNo;
    }

    public String getHasBookmarked() {
        return hasBookmarked;
    }

    public void setHasBookmarked(String hasBookmarked) {
        this.hasBookmarked = hasBookmarked;
    }

}
