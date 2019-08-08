package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Model.SignUpModel;
import com.example.taopr.soool.Presenter.Interface.AccountManager;

public class SignUpPresenter implements AccountManager {
    private Activity signUpActivity;
    private SignUpModel signUpModel;
    private SignUpPresenter.View view;
    private static boolean enable;
    private static String TAG ="SignUpPresenter";

    public SignUpPresenter(Activity activity, Context context){
        this.signUpActivity = activity;
        this.signUpModel = new SignUpModel(this, context);
    }

    //Presenter와 View를 연결하기 위해 사용한 함수임. setVIew
    public void setView(SignUpPresenter.View view) {
        this.view = view;
    }

    // view에서 이메일 혹은 닉네임 값을 전달 받는다
    // 전달 받은 값을 SignUpModel에 전달한다
    // SignUpModel에서 서버에 이메일 혹은 닉네임 값이 중복 확인
    // 중복할 경우 false, 사용가능한 경우 true값을 SignUpModel로부터 전달받는다
    // 전달 받는 함수 => clickDuplicityResponse
    @Override
    public void clickDuplicity(int separator,String emailorNick) {
        Log.i(TAG, "clickDuplicity: 전달 받은 값  : " + separator +"  , " + emailorNick);
        view.showLoading();
        signUpModel.checkDuplicity(separator,emailorNick);
    }

    //이메일, 닉네임 중복 체크의 결과를 View에게 전송하기 위해 사용한 함수.
    public void clickDuplicityResponse(int separator, String emailorNick, boolean response) {
        view.hideLoading();
        Log.d(TAG, "clickDuplicityResponse: 모델로부터 넘어온 결과" + separator + " // " + emailorNick + " // " + response);
        view.clickDuplicityResponseGoToVIew(separator, emailorNick, response);
    }

    // 회원가입
    // 회원정보(accountEmail,accountPW,accountNick)을 view(로 부터 전달 받고
    // model(SignUpModel)에 전달
    // model에서는 회원가입 성공 시 true , 실패 시 false 값을 전달

    //테스틀 위한 주석처리
//    public boolean signUpReq(String acconutEmail, String accountPW, String accountNick){
//        boolean b = signUpModel.signUpReq(acconutEmail,accountPW,accountNick);
//        return b;
//    }

    //회원가입 버튼을 클릭했을 때 서버로 보낼 정보를 위해 사용한 함수.
    public void signUpReq(String acconutEmail, String accountPW, String accountNick){
        view.showLoading();
        signUpModel.signUpReq(acconutEmail,accountPW,accountNick);

    }

    // 회원가입 결과를 view에게 전송하기 위해 사용한 함수.
    // 서버로 response 를 받았다면 프로그래스바 숨기기
    public void signUpReqResponse (boolean response) {
        view.hideLoading();
        view.signUpReqResponseGoToVIew(response);

    }
}
