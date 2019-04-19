package com.example.taopr.soool.Object;

import java.util.ArrayList;

public class MypageList {
    private ArrayList<MypageBookmarkItem> mypageBookmarkItems;
    private ArrayList<MypageMyboardItem> mypageMyboardItems;
    private ArrayList<QnaBoardItem> qnaBoardItems;

    public ArrayList<MypageBookmarkItem> getMypageBookmarkItems() {
        return mypageBookmarkItems;
    }

    public void setMypageBookmarkItems(ArrayList<MypageBookmarkItem> mypageBookmarkItems) {
        this.mypageBookmarkItems = mypageBookmarkItems;
    }

    public ArrayList<MypageMyboardItem> getMypageMyboardItems() {
        return mypageMyboardItems;
    }

    public void setMypageMyboardItems(ArrayList<MypageMyboardItem> mypageMyboardItems) {
        this.mypageMyboardItems = mypageMyboardItems;
    }

    public ArrayList<QnaBoardItem> getQnaBoardItems() {
        return qnaBoardItems;
    }

    public void setQnaBoardItems(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;
    }
}
