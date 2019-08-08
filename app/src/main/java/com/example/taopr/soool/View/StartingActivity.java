package com.example.taopr.soool.View;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.Facebook.LoginCallback;
import com.example.taopr.soool.Kakao.SessionCallback;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartingActivity extends AppCompatActivity {

    private SessionCallback callback;
    private String TAG = " 시작 화면 ";
    private CheckBox termsOfServiceCheck;
    private ViewGroup customFaceLogin;
    private ViewGroup customKakaoLogin;
    private ViewGroup startingToSignUp;
    private LoginButton kakaoLogin;
    private com.facebook.login.widget.LoginButton faceLogin;
    private LoginCallback mLoginCallback;
    private CallbackManager mCallbackManager;
    private TextView startingToLogIn;

    @BindView(R.id.stHandlingPrivacy)
    TextView stHandlingPrivacy;
    @BindView(R.id.stTos)
    TextView stTos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);

        doBinding();

        checkAutoLogin();

       callback = new SessionCallback(this);

        mCallbackManager = CallbackManager.Factory.create();

        mLoginCallback = new LoginCallback(this);


       // 카카오API 로그인
        customKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(termsOfServiceCheck.isChecked()){

                    // 로그인 Activity가 생성될 때 로그인 버튼을 찾아오고,
                    // 세션의 상태가 변경될 때 불리는 세션 콜백을 추가해 줍니다.
                    // 세션이 닫혀 있는지, 이미 오픈되어 있는지, 갱신이 가능한지에 따라 다른 액션을 취합니다.
                    // -> 세션 상태에 따라서 다르게 처리해줄수 있음
                    kakaoLogin.performClick();
                    Session.getCurrentSession().addCallback(callback);
                    Session.getCurrentSession().checkAndImplicitOpen();

                }
                
                else{
                    Log.i(TAG, "onClick:  동의 안함");
                    Toast.makeText(StartingActivity.this,"약관에 동의 해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 페이스북 API 로그인
        customFaceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(termsOfServiceCheck.isChecked()) {

                    faceLogin.performClick();
                    faceLogin.setReadPermissions(Arrays.asList("public_profile", "email"));

                    faceLogin.registerCallback(mCallbackManager, mLoginCallback);

                }
                else{
                    Log.i(TAG, "onClick: 동의 안함");
                    Toast.makeText(StartingActivity.this,"약관에 동의 해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입을 진행하기 전에 약관에 동의를 했는지 확인 후 진행
        startingToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( termsOfServiceCheck.isChecked()) {
                    Intent intent = new Intent(StartingActivity.this, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
                else{
                    Log.i(TAG, "onClick: 동의 안함");
                    Toast.makeText(StartingActivity.this,"약관에 동의 해주세요",Toast.LENGTH_SHORT).show();

                }
            }
        });


        /// 아이디가 있는 회원의 경우 로그인창으로 이동
        startingToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartingActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    // 자동로그인 설정을 한 경우
    // Home 페이지로 이동
    private void checkAutoLogin() {
        try {
            String data = LoginSharedPreferences.LoginUserLoad(StartingActivity.this, "LoginAccount");
            Gson gson = new GsonBuilder().create();
            LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);

            if (loginSessionItem.isAccountAutoLogin()) {
                Intent intent = new Intent(StartingActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        catch (NullPointerException ex){
            Log.i(TAG, "checkAutoLogin: 자동로그인 설정x");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            Log.i(TAG, "onActivityResult: requestCode : " + requestCode +", resultCode :" + resultCode +
                    "");
            return ;
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: " + resultCode);
            Log.i(TAG, "onActivityResult: " + data.getStringExtra("id"));
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션의 상태가 변경될 때 불리는 세션 콜백을 삭제
        Log.i(TAG, "onDestroy: ");
        Session.getCurrentSession().removeCallback(callback);
    }


    /// 카카오톡 로그인 버튼이 레이아웃에 있어서 그런지 버터나이프 라이브러리 적용이 안되는 문제가 있었음.

    private void doBinding(){
        startingToSignUp = findViewById(R.id.startingToSignUp);
        termsOfServiceCheck = findViewById(R.id.termsOfServiceCheck);
        customFaceLogin = findViewById(R.id.customFaceLogin);
        customKakaoLogin = findViewById(R.id.customKakaoLogin);
        kakaoLogin = findViewById(R.id.kakaoLogin);
        faceLogin = findViewById(R.id.faceLogin);
        startingToLogIn = findViewById(R.id.startingToLogIn);

        drawUnderline();
    }

    private void drawUnderline() {

        // '개인정보 취급방취'와 '이용약관' text 에 밑줄 추가
        SpannableString content = new SpannableString(getString(R.string.starting_handling_privacy));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.starting_handling_privacy).length(), 0);
        stHandlingPrivacy.setText(content);

        content = new SpannableString(getString(R.string.starting_tos));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.starting_tos).length(), 0);
        stTos.setText(content);

        content = new SpannableString(getString(R.string.starting_button_move_to_login));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.starting_button_move_to_login).length(), 0);
        startingToLogIn.setText(content);
    }
}
