package com.lpky.taopr.soool.Object;

import android.net.Uri;

public class GridVoteItem {

    private int imageIden;
    private String status;
    private Uri image;
    private String strImage;
    private boolean isSelected = false;
    private int vote = 0;
    private boolean isStringOrUri = false;

    public GridVoteItem (boolean isStringOrUri, String status, Uri image, int imageIden) {
        this.isStringOrUri = isStringOrUri;
        this.status = status;
        this.image = image;
        this.imageIden = imageIden;

    }

    public GridVoteItem (boolean isStringOrUri, String status, String strImage, int imageIden) {
        this.isStringOrUri = isStringOrUri;
        this.status = status;
        this.strImage = strImage;
        this.imageIden = imageIden;
    }

    public GridVoteItem (String status, Uri image, boolean isSelected, boolean isStringOrUri) {
        this.status = status;
        this.image = image;
        this.isSelected = isSelected;
        this.isStringOrUri = isStringOrUri;

    }

    public GridVoteItem (String status, Uri image, boolean isSelected, int vote) {
        this.status = status;
        this.image = image;
        this.isSelected = isSelected;
        this.vote = vote;
    }

    public GridVoteItem (String status, String strImage, boolean isSelected, boolean isStringOrUri) {
        this.status = status;
        this.strImage = strImage;
        this.isSelected = isSelected;
        this.isStringOrUri = isStringOrUri;
    }

    public GridVoteItem (String status, String strImage, int vote, boolean isStringOrUri) {
        this.status = status;
        this.strImage = strImage;
        this.vote = vote;
        this.isStringOrUri = isStringOrUri;
    }

    public int getImageIden() {
        return imageIden;
    }

    public void setImageIden(int imageIden) {
        this.imageIden = imageIden;
    }

    public boolean isStringOrUri() {
        return isStringOrUri;
    }

    public void setStringOrUri(boolean stringOrUri) {
        isStringOrUri = stringOrUri;
    }

    public String getStrImage() {
        return strImage;
    }

    public void setStrImage(String strImage) {
        this.strImage = strImage;
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
