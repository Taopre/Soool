package com.example.taopr.soool.Object;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoOfSoool {

    @SerializedName("제목")
    private String title;

    @SerializedName("작성날짜")
    private String date;

    @SerializedName("이미지")
    private String image;
   
    @SerializedName("조회수")
    private int views;
    
    @SerializedName("스크랩수")
    private int scraps;
    
    @SerializedName("정보 컨텐츠")
    private List<InfoOfSoool> minfoOfSoools;
    
    
    public InfoOfSoool(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public InfoOfSoool(String image, String title, String date, int views, int scraps) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.views = views;
        this.scraps = scraps;
    }

    public void setInfoOfSoools(List<InfoOfSoool> infoOfSoools) {
        this.minfoOfSoools = infoOfSoools;
    }

    public List<InfoOfSoool> getInfoOfSoools(){ return minfoOfSoools; }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getScraps() {
        return scraps;
    }

    public void setScraps(int scraps) {
        this.scraps = scraps;
    }
}
