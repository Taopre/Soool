package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Object.UserProfile;
import com.lpky.taopr.soool.Presenter.MypageFmPresenter;

public interface MypageFmInter {
    void setView(MypageFmPresenter.View view);
    void loadMypageData(int accountNo);

    public interface View{
        void getDataFail(boolean response, int i);
        void getUserProfileSuccess(UserProfile userProfile);
    }
}
