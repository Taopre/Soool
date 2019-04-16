package com.example.taopr.soool.Presenter;


import com.example.taopr.soool.Object.QnaBoardItem;

import java.util.ArrayList;

public interface QnaInter {
    void setView(QnaPresenter.View view);

    public interface View{
        void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems);

        void getDataFail(String message);
    }
}
