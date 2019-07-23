package com.example.taopr.soool.Object;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class RecommentItem
{
    @SerializedName("commentNo")
    public int commentNo;
    @SerializedName("recommentNo")
    public int recommentNo;
    @SerializedName("accountNo")
    public int accountNo;
    @SerializedName("commentWriter")
    public String commentWriter;
    @SerializedName("date")
    public String date;
    @SerializedName("commentContent")
    public String commentContent;
    @SerializedName("likeCount")
    public int likeCount;
    @SerializedName("likeList")
    public int[] likeList;


    //likeList / likeCount

    String TAG = "RecommentItem";


    public RecommentItem(int commentNo,int recommentNo, int accountNo, String commentWriter, String date, String commentContent,int likeCount,int[] likeList)
    {
        this.commentNo = commentNo;
        this.recommentNo = recommentNo;
        this.accountNo = accountNo;
        this.commentWriter = commentWriter;
        this.date = date;
        this.commentContent = commentContent;
        this.likeCount = likeCount;
        this.likeList = likeList;

        Log.d(TAG, "생성완료!");
    }

    public int getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(int commentNo) {
        this.commentNo = commentNo;
    }

    public int getRecommentNo()
    {
        return recommentNo;
    }

    public void setRecommentNo(int recommentNo) {
        this.recommentNo = recommentNo;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public String getCommentWriter() {
        return commentWriter;
    }

    public void setCommentWriter(String commentWriter) {
        this.commentWriter = commentWriter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int[] getLikeList() {
        return likeList;
    }

    public void setLikeList(int[] likeList) {
        this.likeList = likeList;
    }
}
