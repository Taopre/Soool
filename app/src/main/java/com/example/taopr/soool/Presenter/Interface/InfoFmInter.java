package com.example.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Presenter.InfoFmPresenter;


import java.util.ArrayList;

public interface InfoFmInter {

    void setView(InfoFmPresenter.View view);
    void sendItem(InfoItem infoItem, Activity activity, int infoPosition);
    void loadData(int loadingKind, int accountNo, int lastPostNo);

    public interface View {
        void getDataSuccess(ArrayList<InfoItem> infoItems, int loadingKind);
        void getDataFail(String message);
        void showInfoDetail(Intent intent, int requestCode);
        void showLoading();
        void hideLoading();
    }
}
