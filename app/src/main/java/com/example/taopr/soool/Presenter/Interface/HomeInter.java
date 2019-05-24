package com.example.taopr.soool.Presenter.Interface;

import android.content.Intent;

import com.example.taopr.soool.Object.QnaBoardItem;

import java.util.ArrayList;

public class HomeInter {
   // void setView(QnaPresenter.View view);

    public interface View{

        void moveToPage(Intent intent, int requestCode);
    }
}
