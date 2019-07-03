package com.example.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.MainFmPresenter;
import com.example.taopr.soool.Presenter.NoticePresenter;

import java.util.ArrayList;

public interface NoticeInter {
    //public void setView();
    void setView(NoticePresenter.View view);
    //void getIntentMovePage(Activity activity, int infoListPosition, InfoItem infoItem);
    void loadNoticeList();

    interface View{
        void getNoticeListSuccess();
        void getDataFail(int dataType);
        void moveToPage(Intent intent, int requestCode);
        void showLoading();
        void hideLoading();
    }
}
