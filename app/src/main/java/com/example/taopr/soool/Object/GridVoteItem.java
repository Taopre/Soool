package com.example.taopr.soool.Object;

import android.net.Uri;

import java.util.ArrayList;

public class GridVoteItem {

    private String status;
    private Uri image;
    private boolean isSelected = false;
    private int vote = 0;

    public GridVoteItem (String status) {
        this.status = status;
    };

    public GridVoteItem (String status, Uri image) {
        this.status = status;
        this.image = image;
    }

    public GridVoteItem (String status, Uri image, boolean isSelected) {
        this.status = status;
        this.image = image;
        this.isSelected = isSelected;
    }

    public GridVoteItem (String status, Uri image, boolean isSelected, int vote) {
        this.status = status;
        this.image = image;
        this.isSelected = isSelected;
        this.vote = vote;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
