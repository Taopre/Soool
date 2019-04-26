package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QnaVoteItem implements Parcelable {

    @SerializedName("qnaVoteStatus") // 투표가 이미지인지 텍스트인지 구별을 위해
    public String qnaVoteStatus;
    @SerializedName("voteImage")
    public ArrayList<String> voteImage;
    @SerializedName("voteText")
    public ArrayList<String> voteText;

    public QnaVoteItem() {}

    protected QnaVoteItem(Parcel in) {
        qnaVoteStatus = in.readString();
        voteImage = in.createStringArrayList();
        voteText = in.createStringArrayList();
    }

    public static final Creator<QnaVoteItem> CREATOR = new Creator<QnaVoteItem>() {
        @Override
        public QnaVoteItem createFromParcel(Parcel in) {
            return new QnaVoteItem(in);
        }

        @Override
        public QnaVoteItem[] newArray(int size) {
            return new QnaVoteItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(qnaVoteStatus);
        dest.writeStringList(voteImage);
        dest.writeStringList(voteText);
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

    public String getQnaVoteStatus() {
        return qnaVoteStatus;
    }

    public void setQnaVoteStatus(String qnaVoteStatus) {
        this.qnaVoteStatus = qnaVoteStatus;
    }
}
