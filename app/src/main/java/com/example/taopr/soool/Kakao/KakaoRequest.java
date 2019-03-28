package com.example.taopr.soool.Kakao;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.taopr.soool.View.SignUpActivity;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class KakaoRequest {
    private static String TAG = "Kakao";

    public void requestMe(final Context startingContext) {
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("properties.profile_image");
        keys.add("kakao_account.email");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.i(TAG, "onSessionClosed: ");
                // redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Log.i(TAG, "onSuccess: ");
                if(response == null){
                    Log.i(TAG, "onSuccess:  null");
                }
                else {
                    Log.i(TAG, "onSuccess: not null");
                    Log.i(TAG, "onSuccess: getId()" + response.getId() );
                    Log.i(TAG, "onSuccess: email" + response.getKakaoAccount().getEmail());
                    Log.i(TAG, "onSuccess: image" + response.getProfileImagePath());

                    Logger.d("user id : " + response.getId());
                    Logger.d("email: " + response.getKakaoAccount().getEmail());
                    //Logger.d("profile image: " + response.getKakaoAccount().get);

                    // 나중에 보내는 데이터가 많아질 경우에는 객체로 전달 하는 것이 더 용이
                    String snsAcccountEmail = response.getKakaoAccount().getEmail();

                    // 이메일 주소를 가져왔는지 구분하는 값은 ""공백을 전달해서 string.length()로 구분
                    if(snsAcccountEmail == null && snsAcccountEmail.length()==0){
                        redirectSignupActivity(startingContext,"");
                    }
                    else {
                        redirectSignupActivity(startingContext, response.getKakaoAccount().getEmail());
                    }
                }
            }


           /* @Override
            public void onNotSignedUp() {
                //showSignup();
            }*/
        });
    }

    // 카카오계정에서 받아온 이메일 주소(snsAccountEmail) 값과 throughSNS에 true값을 SignUp 페이지로 전달

    private void redirectSignupActivity(Context startingContext,String snsAccountEmail) {
        onClickLogout();
        final Intent intent = new Intent(startingContext, SignUpActivity.class);
        intent.putExtra("throughSNS",true); /*송신*/
        intent.putExtra("snsAccountEmail",snsAccountEmail);
        startingContext.startActivity(intent);

    }


    // 카카오 로그아웃 처리
    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(Long result) {
            }

            @Override
            public void onDidEnd() {
//        Session.getCurrentSession().close();
                onCompleteLogout();
            }
        });
    }
}
