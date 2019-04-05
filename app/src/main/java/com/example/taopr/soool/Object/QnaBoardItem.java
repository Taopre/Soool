package com.example.taopr.soool.Object;

import android.net.Uri;

import java.util.List;

public class QnaBoardItem {

    private String title, content, tag, image;
    private int views,comments;
    private String date,writer;
    private List<QnaBoardItem> qnaBoardItems;

    public QnaBoardItem(String title, String content, String tag,
                        String image, int views, int comments, String date, String writer, List<QnaBoardItem> qnaBoardItems) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.image = image;
        this.views = views;
        this.comments = comments;
        this.date = date;
        this.writer = writer;
        this.qnaBoardItems = qnaBoardItems;
    }

    public QnaBoardItem(){

    }

    public List<QnaBoardItem> getQnaBoardItems() {
        return qnaBoardItems;
    }

    public void setQnaBoardItems(List<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;
    }


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getcomments() {
        return comments;
    }

    public void setcomments(int comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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
