package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.util.Log;

import com.example.taopr.soool.LoginItem;
import com.example.taopr.soool.LoginSessionItem;
import com.example.taopr.soool.Model.LoginModel;

public class LoginPresenter implements LoginInter {

    private Activity activity;
    private LoginPresenter.View view;
    private LoginModel loginModel;

    public LoginPresenter(Activity activity) {
        this.activity = activity;
        this.loginModel = new LoginModel(this);
    }
    //view 공유? 사실 뭔지 잘 모름.
    @Override
    public void setView(LoginPresenter.View view) {
        this.view = view;
        Log.i("login presenter", "setView: ");
    }
    //로그인 기능 (view와 model에게 인자값 전달해주는 파트)
    @Override
    public void login(LoginItem userItem) {
        Log.d("in presenter.login()", userItem.getId() + "//" + userItem.getPwd());
        loginModel.login(userItem);
    }
    //model의 응답을 view에게 보내주는 메서드
    @Override
    public void loginResponse(boolean response) {
        view.loginResponse(response);
    }

    @Override
    public void loginDataSend(LoginSessionItem item) {
        view.loginDataSend(item);
    }

}
