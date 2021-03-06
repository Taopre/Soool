package com.lpky.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QnaBoardItem implements Parcelable{

    // 테스트를 위해 tag ,date, content를 주석 처리해논 상황

    @SerializedName("postNo")  // @SerializedName 직렬화, 역직렬화 하기 위해 사용
    public int postNo;
    @SerializedName("tag")
    public String tag;
    @SerializedName("qnaCate") // 0 : 투표있음 1 : 투표없음
    public int qnaCate;
    @SerializedName("accountNo")
    public int accountNo;
    @SerializedName("writer")
    public String writer;
    @SerializedName("date")
    public String date;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("image")
    public String image;
    @SerializedName("goods")
    public int goods;
    @SerializedName("bads")
    public int bads;
    @SerializedName("comments")
    public int comments;
    @SerializedName("views")
    public int views;
    @SerializedName("isLike")
    public int isLike;
    @SerializedName("isBad")
    public int isBad;

    public QnaBoardItem(){}

    public QnaBoardItem(int postNo, String tag, int qnaCate, int accountNo, String writer, String date,
                        String title, String content, String image, int goods, int bads, int comments, int views, int isLike, int isBad) {
        this.postNo = postNo;
        this.tag = tag;
        this.qnaCate = qnaCate;
        this.accountNo = accountNo;
        this.writer = writer;
        this.date = date;
        this.title = title;
        this.content = content;
        this.image = image;
        this.goods = goods;
        this.bads = bads;
        this.comments = comments;
        this.views = views;
        this.isLike = isLike;
        this.isBad = isBad;
    }
    protected QnaBoardItem(Parcel in) {
        postNo = in.readInt();
        tag = in.readString();
        qnaCate = in.readInt();
        accountNo = in.readInt();
        writer = in.readString();
        date = in.readString();
        title = in.readString();
        content = in.readString();
        image = in.readString();
        goods = in.readInt();
        bads = in.readInt();
        comments = in.readInt();
        views = in.readInt();
        isLike = in.readInt();
        isBad = in.readInt();
    }

    public static final Creator<QnaBoardItem> CREATOR = new Creator<QnaBoardItem>() {
        @Override
        public QnaBoardItem createFromParcel(Parcel in) {
            return new QnaBoardItem(in);
        }

        @Override
        public QnaBoardItem[] newArray(int size) {
            return new QnaBoardItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postNo);
        dest.writeString(tag);
        dest.writeInt(qnaCate);
        dest.writeInt(accountNo);
        dest.writeString(writer);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeInt(goods);
        dest.writeInt(bads);
        dest.writeInt(comments);
        dest.writeInt(views);
        dest.writeInt(isLike);
        dest.writeInt(isBad);
    }

    public int getPostNo() {
        return postNo;
    }

    public void setPostNo(int postNo) {
        this.postNo = postNo;
    }

    public int getQnaCate() {
        return qnaCate;
    }

    public void setQnaCate(int qnaCate) {
        this.qnaCate = qnaCate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGoods() {
        return goods;
    }

    public void setGoods(int goods) {
        this.goods = goods;
    }

    public int getBads() {
        return bads;
    }

    public void setBads(int bads) {
        this.bads = bads;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsBad() {
        return isBad;
    }

    public void setIsBad(int isBad) {
        this.isBad = isBad;
    }
}
