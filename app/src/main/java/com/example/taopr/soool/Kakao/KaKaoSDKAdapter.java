package com.example.taopr.soool.Kakao;

import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class KaKaoSDKAdapter extends KakaoAdapter {

    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                //로그인시 인증받을 타입을 지정한다. 지정하지 않을 시 가능한 모든 옵션이 지정된다.

                return new AuthType[]{
                        AuthType.KAKAO_LOGIN_ALL
                };
            }

            @Override
            public boolean isUsingWebviewTimer() {
                //	SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
                // true 를 리턴할경우 webview로그인을 사용하는 화면서 모든 webview에 onPause와 onResume 시에 Timer를 설정해 주어야 한다.
                // 지정하지 않을 시 false로 설정된다.
                return false;
            }

            @Override
            public boolean isSecureMode() {
                //	로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
                return false;
            }

            @Override
            public ApprovalType getApprovalType() {
                // 일반 사용자가 아닌 Kakao와 제휴된 앱에서 사용되는 값으로,
                // 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                //Kakao SDK 에서 사용되는 WebView에서 email 입력폼에서 data를 save할지여부를 결정한다. Default true
                return false;
            }
        };
    }


    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Context getApplicationContext() {
                return GlobalApplication.getGlobalApplicationContext();
            }
        };
    }
}


