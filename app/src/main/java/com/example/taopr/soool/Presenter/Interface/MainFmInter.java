package com.example.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.MainFmPresenter;

import java.util.ArrayList;

public interface MainFmInter {

    void setView(MainFmPresenter.View view);
    void getIntent(Activity activity, int qnaListPosition, QnaBoardItem qnaBoardItem);
    void loadQnaBoardList();
    void loadInfoList();

    interface View{
        void getQnaBoardSuccess(ArrayList<QnaBoardItem> qnaBoardItems);
        void getInfoSuccess();
        void getDataFail(int dataType);
        void moveToPage(Intent intent, int requestCode);
        void showLoading();
        void hideLoading();
    }
}
