package com.example.taopr.soool.Object;

import java.util.List;

import retrofit2.http.QueryMap;

public class QnaBoardItem {

    private String title, content, tag, image;
    private String writer, date;
    private int comments, views;
    private List<QnaBoardItem> qnaBoardItems;

    public QnaBoardItem(){

    }

    public QnaBoardItem(String title, String content, String tag,
                        String image, String writer, String date, int comments, int views, List<QnaBoardItem> qnaBoardItems) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.image = image;
        this.writer = writer;
        this.date = date;
        this.comments = comments;
        this.views = views;
        this.qnaBoardItems = qnaBoardItems;
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

    public List<QnaBoardItem> getQnaBoardItems() {
        return qnaBoardItems;
    }

    public void setQnaBoardItems(List<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
