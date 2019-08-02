package com.lpky.taopr.soool.Kakao;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    private static String TAG = "Kakao_API";
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KaKaoSDKAdapter());
    }

    public static GlobalApplication getGlobalApplicationContext() {
        if(obj == null){
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");

        }
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }


    /// 어플리케이션 종료 시 객체 초기화
    @Override
    public void onTerminate() {
        super.onTerminate();
        obj = null;
    }


}