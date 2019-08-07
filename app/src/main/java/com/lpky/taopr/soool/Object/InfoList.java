package com.lpky.taopr.soool.Object;


import android.util.Log;

import java.util.ArrayList;


public class InfoList {
    private static final String TAG = "InfoList" ;

    // InfoItem 객체를 리스트로 담고 있는 객체 ???

    private ArrayList<InfoItem> infoItem;

    public ArrayList<InfoItem> getInfoItems() {
        return infoItem;
    }

    public void setInfoItems(ArrayList<InfoItem> infoItems) {
        this.infoItem = infoItems;
    }
}
