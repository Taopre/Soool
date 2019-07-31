package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Presenter.HomePresenter;

public interface HomeInter {
    void setView(HomePresenter.View view);
    void getAccountNo();

    public interface View{
        void getAccountNoSuc(int accountNo);
    }
}
