package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.LoginItem;
import com.example.taopr.soool.Object.PassFIndResponse;
import com.example.taopr.soool.Presenter.PassFindPresenter;

public interface PassFindInter {

    void setView(PassFindPresenter.View view);
    void EmailCheckReq (String email);
    void login(LoginItem loginItem);

    public interface View {
        void EmailCheckResp(PassFIndResponse passFIndResponse);
        void loginResponse(String response);
    }
}
