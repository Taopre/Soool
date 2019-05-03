package com.example.taopr.soool.Object;

import android.net.Uri;

import java.util.ArrayList;

public class GridVoteItem {

    private String status;
    private Uri image;

    public GridVoteItem (String status) {
        this.status = status;
    };

    public GridVoteItem (String status, Uri image) {
        this.status = status;
        this.image = image;
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
