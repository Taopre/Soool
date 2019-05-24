package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QnaVoteItem {

    private String result;
    private int qnaVoteStatus; // int로 바꾸고 0이면 텍스트 1이면 이미지
    private int count;
    private int totalVoteCount;
    private int memberIsVoted;
    private ArrayList<String> voteImage;
    private ArrayList<String> voteText;
    private ArrayList<Integer> voteResult;

    public QnaVoteItem() {}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalVoteCount() {
        return totalVoteCount;
    }

    public void setTotalVoteCount(int totalVoteCount) {
        this.totalVoteCount = totalVoteCount;
    }

    public int getMemberIsVoted() {
        return memberIsVoted;
    }

    public void setMemberIsVoted(int memberIsVoted) {
        this.memberIsVoted = memberIsVoted;
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

    public int getQnaVoteStatus() {
        return qnaVoteStatus;
    }

    public void setQnaVoteStatus(int qnaVoteStatus) {
        this.qnaVoteStatus = qnaVoteStatus;
    }

    public ArrayList<Integer> getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(ArrayList<Integer> voteResult) {
        this.voteResult = voteResult;
    }
}
