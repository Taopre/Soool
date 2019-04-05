package com.example.taopr.soool.Object;

import java.util.List;

public class QnaVoteItem {
    private String title, content, first_image, second_image;
    private String writer, date;
    private int comments, views;
    private List<QnaVoteItem> qnaVoteItems;

    public QnaVoteItem(){

    }

    public QnaVoteItem(String title, String content, String first_image,
                        String second_image, String writer, String date, int comments, int views, List<QnaVoteItem> qnaVoteItems) {
        this.title = title;
        this.content = content;
        this.first_image = first_image;
        this.second_image = second_image;
        this.writer = writer;
        this.date = date;
        this.comments = comments;
        this.views = views;
        this.qnaVoteItems = qnaVoteItems;
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

    public List<QnaVoteItem> qnaVoteItems() {
        return qnaVoteItems;
    }

    public void QnaVoteItem(List<QnaVoteItem> qnaVoteItems) {
        this.qnaVoteItems = qnaVoteItems;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getFirst_image() {
        return first_image;
    }

    public String getSecond_image() {
        return second_image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFirst_image(String first_image) {
        this.first_image = first_image;
    }

    public void setSecond_image(String second_image) {
        this.second_image = second_image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
