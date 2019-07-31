package com.lpky.taopr.soool.Presenter.Interface;

import android.content.Intent;

import com.lpky.taopr.soool.Presenter.NoticePresenter;

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
