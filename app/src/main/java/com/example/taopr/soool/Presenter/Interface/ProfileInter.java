package com.example.taopr.soool.Presenter.Interface;

import android.content.Context;
import android.net.Uri;

import com.example.taopr.soool.Object.ProfileInfo;
import com.example.taopr.soool.Presenter.ProfilePresenter;

public interface ProfileInter {
    public void setView(ProfilePresenter.View view,Context context);
    public void getProfileInfo(int accountNo);
    void changeProfileImage(Uri uri);
    void changeProfileInfo(ProfileInfo profileInfo);
    void deleteProfileImg();
    void deleteAccount(int accountNo); // 회원탈퇴

    public interface View{
        void getProfileInfoSuccess(ProfileInfo profileInfo);
        void getDataFail(int dataKind);
        void changeProfileImgSuccess(String accountImage);
        void changeProfileInfoSuccess(ProfileInfo profileInfo);
        void deleteProfileImgSuccess();
        void deleteAccountSuccess();
        void showLoading();
        void hideLoading();
    }
}
