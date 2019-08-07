package com.lpky.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class InfoItem implements Parcelable {
    private static final String TAG ="InfoItem" ;

    /* InfoFragment 목록에 필요한 것들

        postNo
        tag
        ------
        accountNo (작성자 id 번호인가)
        writer
        date
        title
        cover (커버 이미지 경로)
        comments
        views

    */

    @SerializedName("postNo")
    public int postNo;
    @SerializedName("postTag")
    public String postTag;
    @SerializedName("accountNo")
    public int accountNo;
    @SerializedName("writer")
    public String writer;
    @SerializedName("date")
    public String date;
    @SerializedName("title")
    public String title;
    @SerializedName("cover")
    public String cover;
    @SerializedName("comments")
    public int comments;
    @SerializedName("views")
    public int views;

    public InfoItem(){
    }

    public InfoItem(int postNo, String postTag, int accountNo, String writer, String date,
                    String title, String cover, int comments, int views) {

        this.postNo = postNo;
        this.postTag = postTag;
        this.accountNo = accountNo;
        this.writer = writer;
        this.date = date;
        this.title = title;
        this.cover = cover;
        this.comments = comments;
        this.views = views;

    }


    protected InfoItem(Parcel in) {

        postNo = in.readInt();
        postTag = in.readString();
        accountNo = in.readInt();
        writer = in.readString();
        date = in.readString();
        title = in.readString();
        cover = in.readString();
        comments = in.readInt();
        views = in.readInt();

    }

    public static final Creator<InfoItem> CREATOR = new Creator<InfoItem>() {
        @Override
        public InfoItem createFromParcel(Parcel in) {
            return new InfoItem(in);
        }

        @Override
        public InfoItem[] newArray(int size) {
            return new InfoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {

        parcel.writeInt(postNo);
        parcel.writeString(postTag);
        parcel.writeInt(accountNo);
        parcel.writeString(writer);
        parcel.writeString(date);
        parcel.writeString(title);
        parcel.writeString(cover);
        parcel.writeInt(comments);
        parcel.writeInt(views);

    }

    public int getPostNo() {
        return postNo;
    }

    public void setPostNo(int postNo) {
        this.postNo = postNo;
    }

    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String tag) {
        this.postTag = tag;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }


}
