package com.example.taopr.soool.Presenter.Interface;

import android.content.Intent;

import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.HomePresenter;

import java.util.ArrayList;

public interface HomeInter {
    void setView(HomePresenter.View view);
    void getAccountNo();

    public interface View{
        void getAccountNoSuc(int accountNo);
    }
}