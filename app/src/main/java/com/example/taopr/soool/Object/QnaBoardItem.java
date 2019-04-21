package com.example.taopr.soool.Object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QnaBoardItem implements Serializable{

    // 테스트를 위해 tag ,date, content를 주석 처리해논 상황


    @SerializedName("qnaNo")  // @SerializedName 직렬화, 역직렬화 하기 위해 사용
    public int qnaNo;
    @SerializedName("tag")
    public String tag;
    @SerializedName("qnaCate") // 게시글이 투표인지 아닌지
    public String qnaCate;
    @SerializedName("qnaVoteExistence") // 투표가 존재하는지 아닌지 구별을 위해
    public boolean qnaVoteExistence;
    @SerializedName("qnaVoteStatus") // 투표가 이미지인지 텍스트인지 구별을 위해
    public String qnaVoteStatus;
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
    @SerializedName("voteImage")
    public ArrayList<String> voteImage;
    @SerializedName("voteText")
    public ArrayList<String> voteText;


    public QnaBoardItem(){}

    public QnaBoardItem(int qnaNo, String tag, String qnaCate, int accountNo, String writer, String date,
                        String title, String content, String image, int goods, int bads, int comments, int views) {
        this.qnaNo = qnaNo;
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
    }

    public int getQnaNo() {
        return qnaNo;
    }

    public void setQnaNo(int qnaNo) {
        this.qnaNo = qnaNo;
    }

    public String getQnaCate() {
        return qnaCate;
    }

    public void setQnaCate(String qnaCate) {
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

    public boolean isQnaVoteExistence() {
        return qnaVoteExistence;
    }

    public void setQnaVoteExistence(boolean qnaVoteExistence) {
        this.qnaVoteExistence = qnaVoteExistence;
    }

    public String getQnaVoteStatus() {
        return qnaVoteStatus;
    }

    public void setQnaVoteStatus(String qnaVoteStatus) {
        this.qnaVoteStatus = qnaVoteStatus;
    }
}
