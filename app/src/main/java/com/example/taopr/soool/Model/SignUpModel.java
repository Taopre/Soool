package com.example.taopr.soool.Model;

import android.util.Log;

public class SignUpModel {

    String TAG = "SignUpModel";

    // presenter를 통해 전달 받은 구분자 값과 이메일 혹은 닉네임 값을 서버에 전달.
    // 서버는 중복여부에 관한 result값을 전달 받고
    public boolean checkDuplicity(int separator, String emailOrNick) {
        Log.i(TAG, "checkDuplicity: 구분자 값 : " + separator + " , 중복체크 값 : " + emailOrNick);

        return true;
    }

    public boolean signUpReq(String accountEmail, String accountPW, String accountNick){

        return true;
    }

}
