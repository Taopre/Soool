package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.util.Log;

import com.example.taopr.soool.Model.SignUpModel;

public class SignUpPresenter implements AccountManager {
    private Activity signUpActivity;
    private SignUpModel signUpModel;
    private AccountManager.View signUpView;
    private static boolean enable;
    private static String TAG ="SignUpPresenter";

    public SignUpPresenter(Activity activity){
        this.signUpActivity = activity;
        this.signUpModel = new SignUpModel();
    }
    public void setView(AccountManager.View view) {
        this.signUpView = view;
    }

    // view에서 이메일 혹은 닉네임 값을 전달 받는다
    // 전달 받은 값을 SignUpModel에 전달한다
    // SignUpModel에서 서버에 이메일 혹은 닉네임 값이 중복 확인
    // 중복할 경우 false, 사용가능한 경우 true값을 SignUpModel로부터 전달받는다
    // SignUpModel로부터 전달받은 boolean값을 다시 view에 전달

    @Override
    public boolean clickDuplicity(int separator,String emailorNick) {
        Log.i(TAG, "clickDuplicity: 전달 받은 값  : " + separator +"  , " + emailorNick);
        enable = signUpModel.checkDuplicity(separator,emailorNick);
        return enable;
    }


    // 회원가입
    // 회원정보(accountEmail,accountPW,accountNick)을 view(로 부터 전달 받고
    // model(SignUpModel)에 전달
    // model에서는 회원가입 성공 시 true , 실패 시 false 값을 전달

    public boolean signUpReq(String acconutEmail, String accountPW, String accountNick){
        boolean b = signUpModel.signUpReq(acconutEmail,accountPW,accountNick);
        return b;
    }
}
