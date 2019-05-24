package com.example.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.QnaFmPresenter;

import java.util.ArrayList;

public interface QnaFmInter {

    void setView(QnaFmPresenter.View view);
    void getItem(QnaBoardItem qnaBoardItem, Activity activity, int qnaListPosition);

    public interface View{
        void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems);
        void getDataFail(String message);
        void moveToPage(Intent intent,int requestCode);
    }
}
