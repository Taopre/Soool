package com.lpky.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Presenter.QnaFmPresenter;

import java.util.ArrayList;

public interface QnaFmInter {

    void setView(QnaFmPresenter.View view);
    void getItem(QnaBoardItem qnaBoardItem, Activity activity, int qnaListPosition);

    public interface View{
        void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems,int loadingKind);
        void getDataFail(String message);
        void moveToPage(Intent intent,int requestCode);
        void showLoading();
        void hideLoading();
    }
}
