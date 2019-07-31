package com.lpky.taopr.soool.Object;

import java.util.ArrayList;


// QnaBoardItem 객체를 리스트로 담고 있을 객체

public class QnaBoardList {
    private ArrayList<QnaBoardItem> qnaBoardItem;

    public ArrayList<QnaBoardItem> getQnaBoardItems() {
        return qnaBoardItem;
    }

    public void setQnaBoardItems(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItem = qnaBoardItems;
    }

}
