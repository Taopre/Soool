package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QnaItem implements Parcelable {

    @SerializedName("qnaNo")  // @SerializedName 직렬화, 역직렬화 하기 위해 사용
    public int qnaNo;
    @SerializedName("tag")
    public String tag;
    @SerializedName("qnaCate") // 0 : 투표있음 1 : 투표없음
    public int qnaCate;
    @SerializedName("accountNo")
    public int accountNo;
    @SerializedName("accountNick")
    public String accountNick;
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
    @SerializedName("qnaVoteStatus") // 투표가 이미지인지 텍스트인지 구별을 위해
    public int qnaVoteStatus; // int로 바꾸고 0이면 텍스트 1이면 이미지
    @SerializedName("voteImage")
    public ArrayList<String> voteImage;
    @SerializedName("voteText")
    public ArrayList<String> voteText;

    public QnaItem(){

    }

    // 게시물 이미지 없이 텍스트 투표인 경우
    public QnaItem (int accountNo, String accountNick, int qnaCate, String tag,
                    String title, String content, ArrayList<String> voteText, int qnaVoteStatus) {
        this.accountNick = accountNick;
        this.tag = tag;
        this.qnaCate = qnaCate;
        this.accountNo = accountNo;
        this.title = title;
        this.content = content;
        this.qnaVoteStatus = qnaVoteStatus;
        this.voteText = voteText;
    }

    // 게시물 이미지 없이 이미지 투표인 경우
    public QnaItem (int accountNo, String accountNick, int qnaCate, String tag,
                    String title, String content, int qnaVoteStatus, ArrayList<String> voteImage) {
        this.accountNick = accountNick;
        this.tag = tag;
        this.qnaCate = qnaCate;
        this.accountNo = accountNo;
        this.title = title;
        this.content = content;
        this.qnaVoteStatus = qnaVoteStatus;
        this.voteImage = voteImage;
    }

    // 게시물 이미지 있고 텍스트 투표인 경우
    public QnaItem (int accountNo, String accountNick, int qnaCate, String tag,
                    String title, String content, String image, ArrayList<String> voteText, int qnaVoteStatus) {
        this.accountNick = accountNick;
        this.tag = tag;
        this.qnaCate = qnaCate;
        this.accountNo = accountNo;
        this.image = image;
        this.title = title;
        this.content = content;
        this.qnaVoteStatus = qnaVoteStatus;
        this.voteText = voteText;
    }

    // 게시물 이미지 있고 이미지 투표인 경우
    public QnaItem (int accountNo, String accountNick, int qnaCate, String tag,
                    String title, String content, String image, int qnaVoteStatus, ArrayList<String> voteImage) {
        this.accountNick = accountNick;
        this.tag = tag;
        this.qnaCate = qnaCate;
        this.accountNo = accountNo;
        this.image = image;
        this.title = title;
        this.content = content;
        this.qnaVoteStatus = qnaVoteStatus;
        this.voteImage = voteImage;
    }

    public int getQnaNo() {
        return qnaNo;
    }

    public void setQnaNo(int qnaNo) {
        this.qnaNo = qnaNo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getQnaVoteStatus() {
        return qnaVoteStatus;
    }

    public void setQnaVoteStatus(int qnaVoteStatus) {
        this.qnaVoteStatus = qnaVoteStatus;
    }

    public ArrayList<String> getVoteImage() {
        return voteImage;
    }

    public void setVoteImage(ArrayList<String> voteImage) {
        this.voteImage = voteImage;
    }

    public ArrayList<String> getVoteText() {
        return voteText;
    }

    public void setVoteText(ArrayList<String> voteText) {
        this.voteText = voteText;
    }

    public static Creator<QnaItem> getCREATOR() {
        return CREATOR;
    }

    protected QnaItem(Parcel in) {
        qnaNo = in.readInt();
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
        qnaVoteStatus = in.readInt();
        voteImage = in.createStringArrayList();
        voteText = in.createStringArrayList();

    }

    public static final Creator<QnaItem> CREATOR = new Creator<QnaItem>() {
        @Override
        public QnaItem createFromParcel(Parcel in) {
            return new QnaItem(in);
        }

        @Override
        public QnaItem[] newArray(int size) {
            return new QnaItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(qnaVoteStatus);
        dest.writeStringList(voteImage);
        dest.writeStringList(voteText);
        dest.writeInt(qnaNo);
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
    }
}
