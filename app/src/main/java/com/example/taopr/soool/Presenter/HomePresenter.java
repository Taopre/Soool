package com.example.taopr.soool.Presenter;

import android.content.Context;

import com.example.taopr.soool.Presenter.Interface.HomeInter;

public class HomePresenter implements HomeInter{

    private View view;
    private Context context;

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void UserProfile() {

    }
}
