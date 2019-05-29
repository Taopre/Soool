package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.ChangeProfile;
import com.example.taopr.soool.Presenter.ProfilePresenter;

public interface ProfileInter {
    public void setView(ProfilePresenter.View view);
    public void getProfileInfo(int accountNo);

    public interface View{
        void getChangeProfileSuccess(ChangeProfile changeProfile);
        void getDataFail(int dataKind);
    }
}
