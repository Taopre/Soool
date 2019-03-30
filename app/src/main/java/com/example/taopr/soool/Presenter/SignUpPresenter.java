package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Model.SignUpModel;

import retrofit2.Retrofit;

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

    public void setView(SignUpPresenter.View view) {
        this.view = view;
    }

    // view에서 이메일 혹은 닉네임 값을 전달 받는다
    // 전달 받은 값을 SignUpModel에 전달한다
    // SignUpModel에서 서버에 이메일 혹은 닉네임 값이 중복 확인
    // 중복할 경우 false, 사용가능한 경우 true값을 SignUpModel로부터 전달받는다
    // SignUpModel로부터 전달받은 boolean값을 다시 view에 전달

    //잠시 주석처리했음. 내가 테스트할라고.
//    @Override
//    public boolean clickDuplicity(int separator,String emailorNick) {
//        Log.i(TAG, "clickDuplicity: 전달 받은 값  : " + separator +"  , " + emailorNick);
////        enable =
//        signUpModel.checkDuplicity(separator,emailorNick);
//        return enable;
//    }

    @Override
    public void clickDuplicity(int separator,String emailorNick) {
        Log.i(TAG, "clickDuplicity: 전달 받은 값  : " + separator +"  , " + emailorNick);
//        enable =
        signUpModel.checkDuplicity(separator,emailorNick);
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
    public void signUpReq(String acconutEmail, String accountPW, String accountNick){
        signUpModel.signUpReq(acconutEmail,accountPW,accountNick);
    }

    public void signUpReqResponse (boolean response) {
        view.signUpReqResponseGoToVIew(response);
    }

    public void clickDuplicityResponse(int separator, String emailorNick, boolean response) {
        Log.d(TAG, "clickDuplicityResponse: 모델로부터 넘어온 결과" + separator + " // " + emailorNick + " // " + response);
        view.clickDuplicityResponseGoToVIew(separator, emailorNick, response);
    }

}
