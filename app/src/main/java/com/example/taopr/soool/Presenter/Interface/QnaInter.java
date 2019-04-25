package com.example.taopr.soool.Presenter.Interface;


import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.QnaPresenter;

import java.util.ArrayList;

public interface QnaInter {
    void setView(QnaPresenter.View view);

    public interface View{
        void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems);

        void getDataFail(String message);
    }
}
