package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.LoginItem;
import com.example.taopr.soool.LoginSessionItem;

public interface LoginInter {

    void setView(LoginPresenter.View view);

    void login(LoginItem usetItem);

    void loginResponse(boolean response);

    void loginDataSend(LoginSessionItem item);

    public interface View {
        void setConfirmText(String text);
        void loginResponse(boolean response);
        void loginDataSend(LoginSessionItem item);
    }

}