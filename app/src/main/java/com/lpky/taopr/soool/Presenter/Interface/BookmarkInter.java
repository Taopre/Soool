package com.lpky.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.lpky.taopr.soool.Object.InfoItem;

import java.util.ArrayList;

public interface BookmarkInter {

    void setView(BookmarkInter.View view);
    void sendItem(InfoItem infoItem, Activity activity, int infoPosition);
    void loadData(int accountNo, int loadingKind);

    public interface View {
        void getDataSuccess(ArrayList<InfoItem> infoItems, int loadingKind);
        void getDataFail(String msg);
        void showInfoDetail(Intent intent, int requestCode);
    }
}
