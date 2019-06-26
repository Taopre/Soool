package com.example.taopr.soool.Object;


import android.util.Log;

import java.util.ArrayList;

public class InfoContent {

    private static final String TAG = "InfoContent";

    private ArrayList<InfoContentText> infoText;

    public ArrayList<InfoContentText> getInfoText() {
        //Log.e(TAG, "getInfoText: infoContent 소환되는거니"+infoText);
        return infoText;
    }

    public void setInfoText(ArrayList<InfoContentText> infoText) {
        this.infoText = infoText;
    }

    private ArrayList<InfoBookmark> infoBookmark;

    public ArrayList<InfoBookmark> getInfoBookmark() {
        return infoBookmark;
    }

    public void setInfoBookmark(ArrayList<InfoBookmark> infoBookmark) {
        this.infoBookmark = infoBookmark;
    }

}
