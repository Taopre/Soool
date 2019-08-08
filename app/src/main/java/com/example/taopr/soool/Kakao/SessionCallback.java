package com.example.taopr.soool.Kakao;

import android.content.Context;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class SessionCallback implements ISessionCallback {
    private static String TAG = "Kakao_API";
    private static Context startingContext;
    private static KakaoRequest kakaoRequest;

    public SessionCallback(Context startingContext){
        this.startingContext = startingContext;
        kakaoRequest = new KakaoRequest();
    }

    @Override
    public void onSessionOpened() {
        // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태.

        kakaoRequest.requestMe(startingContext);

    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        //memory와 cache에 session 정보가 전혀 없는 상태

        if(exception != null) {
            Logger.e(exception);
        }
    }

}