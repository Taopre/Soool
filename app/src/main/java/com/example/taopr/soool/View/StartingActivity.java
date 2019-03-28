package com.example.taopr.soool.View;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.taopr.soool.Kakao.SessionCallback;
import com.example.taopr.soool.Model.SignUpModel;
import com.example.taopr.soool.R;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;

import java.security.MessageDigest;
import java.security.Signature;

import butterknife.BindView;
import butterknife.OnClick;

public class StartingActivity extends AppCompatActivity {

    private SessionCallback callback;
    private static String TAG = "Starting";
    private CheckBox termsOfServiceCheck;

  /// 카카오톡 로그인 버튼이 레이아웃에 있어서 그런지 버터나이프 라이브러리 적용이 안되는 문제가 있었음.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        // 로그인 Activity가 생성될 때 로그인 버튼을 찾아오고,
        // 세션의 상태가 변경될 때 불리는 세션 콜백을 추가해 줍니다.
        // 세션이 닫혀 있는지, 이미 오픈되어 있는지, 갱신이 가능한지에 따라 다른 액션을 취합니다.
        // -> 세션 상태에 따라서 다르게 처리해줄수 있음
        callback = new SessionCallback(this);
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        Button startingToSignUp = findViewById(R.id.startingToSignUp);
        termsOfServiceCheck = findViewById(R.id.termsOfServiceCheck);


        // 웹에 시작하기에 앞서 동의를 했는지 확인
        startingToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( termsOfServiceCheck.isChecked()) {
                    Intent intent = new Intent(StartingActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.i(TAG, "onClick: 동의 안함");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: ");
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            Log.i(TAG, "onActivityResult: requestCode : " + requestCode +", resultCode :" + resultCode +
                    "");
            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션의 상태가 변경될 때 불리는 세션 콜백을 삭제

        Log.i(TAG, "onDestroy: ");
        Session.getCurrentSession().removeCallback(callback);
    }

}
