package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Object.LoginItem;
import com.example.taopr.soool.Model.LoginModel;

public class LoginPresenter implements LoginInter {

    private Activity activity;
    private LoginPresenter.View view;
    private LoginModel loginModel;

    //생성자.
    public LoginPresenter(Activity activity, Context context) {
        this.activity = activity;
        this.loginModel = new LoginModel(this, context);
    }
    //자세하게는 설명을 못하지만 내 느낌으로는 아래의 setView 함수는 LoginActivity(view)와 LoginPresenter(presenter)를 연결하기 위해 사용한 함수이다.
    @Override
    public void setView(LoginPresenter.View view) {
        this.view = view;
        Log.i("login presenter", "setView: ");
    }
    //LoginActvity(view)에서 LoginModel(model)로 id와 pw를 보내기 위해 사용한 함수.
    @Override
    public void login(LoginItem userItem) {
        Log.d("in presenter.login()", userItem.getId() + "//" + userItem.getPwd());
        //view로부터 받은 값을 model로 보내기 위한 부분.
        loginModel.login(userItem);
    }
    //LoginModel(model)의 응답을 LoginActivity(view)에게 보내주는 메서드.
    @Override
    public void loginResponse(String response) {
        view.loginResponseGoToVIew(response);
    }

//    @Override
//    public void loginDataSend(LoginSessionItem item) {
//        view.loginDataSend(item);
//    }

}
