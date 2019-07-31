package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Object.LoginItem;
import com.lpky.taopr.soool.Object.PassFIndResponse;
import com.lpky.taopr.soool.Presenter.PassFindPresenter;

public interface PassFindInter {

    void setView(PassFindPresenter.View view);
    void EmailCheckReq (String email);
    void login(LoginItem loginItem, String secureAccountPW);

    public interface View {
        void EmailCheckResp(PassFIndResponse passFIndResponse);
        void loginResponse(String response);
    }
}
