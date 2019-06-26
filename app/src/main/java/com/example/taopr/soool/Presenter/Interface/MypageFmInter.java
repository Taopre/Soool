package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.UserProfile;
import com.example.taopr.soool.Presenter.MypageFmPresenter;

public interface MypageFmInter {
    void setView(MypageFmPresenter.View view);
    void loadMypageData(int accountNo);

    public interface View{
        void getDataFail(boolean response, int i);
        void getUserProfileSuccess(UserProfile userProfile);
    }
}
