package com.lpky.taopr.soool.Presenter.Interface;

import android.app.Activity;
import android.content.Intent;

import com.lpky.taopr.soool.Object.QnaBoardItem;

import java.util.ArrayList;

public interface MyBoardInter {

    void setView(MyBoardInter.View view);
    void getItem(QnaBoardItem qnaBoardItem, Activity activity, int qnaListPosition);
    void loadData(int accountNo,int loadingKind,int lastPostNo);


    public interface View{
        void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems,int loadingKind);
        void getDataFail(String message);
        void moveToPage(Intent intent,int requestCode);

    }
}
