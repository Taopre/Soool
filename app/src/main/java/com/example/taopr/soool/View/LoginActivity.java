package com.example.taopr.soool.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.LoginItem;
import com.example.taopr.soool.LoginSessionItem;
import com.example.taopr.soool.Presenter.LoginPresenter;
import com.example.taopr.soool.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View, Button.OnClickListener {
    String TAG = "LoginActivity log : ";

    LoginPresenter loginPresenter;

    @BindView(R.id.textView)
    TextView tv_loginStatus;
    @BindView(R.id.accountFindPwd) TextView tv_findpwd;
    @BindView(R.id.accountSignup) TextView tv_signup;
    @BindView(R.id.accountId)
    EditText et_id;
    @BindView(R.id.accountPass) EditText et_pwd;
    @BindView(R.id.accountLoginBtn) Button btn_login;
    @BindView(R.id.accountAutoLoginCheck)
    CheckBox cb_autologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

        //id 와 pwd를 입력하고
        //id가 있다면 login 없다면 wrong 메시지를 textview에 띄워주는 과정
        //1. id 입력
        //2. pwd 입력
        //3. 버튼 클릭의 경우 model로 클릭의 상황과 함께 id와 pwd를 보낸다.
        //4. model로부터 일치여부를 view로 전달된다.
        //5. 일치할 경우 login 일치하지 않을 경우 wrong을 textview로 보여준다.

        btn_login.setOnClickListener(this);
        tv_findpwd.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginItem loginItem = new LoginItem();
//                Log.d("onClick", et_id.getText().toString() + "//" + et_pwd.getText().toString());
//                loginItem.setId(et_id.getText().toString());
//                loginItem.setPwd(et_pwd.getText().toString());
//                loginPresenter.login(loginItem);
//            }
//        });

        //아래껀 rxjava 람다식 테스트용 소스.
        Observable<String> simpleObservable = Observable.just("Hello Lambda! (test rxjava)");
        simpleObservable
                .map(text -> text + " - goo")
                .subscribe(text -> tv_loginStatus.setText(text));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //자동 로그인 체크박스 부분.
        if (cb_autologin.isChecked()) {
            //자동 로그인 체크했을 경우

        }else {
            //자동 로그인 체크 안 할 경우

        }
    }

    private void DoBinding() {
        loginPresenter = new LoginPresenter(LoginActivity.this);
        loginPresenter.setView(this);

        tv_loginStatus = findViewById(R.id.textView);
        et_id = findViewById(R.id.accountId);
        et_pwd = findViewById(R.id.accountPass);
        btn_login = findViewById(R.id.accountLoginBtn);
        tv_findpwd = findViewById(R.id.accountFindPwd);
        tv_signup = findViewById(R.id.accountSignup);
        cb_autologin = findViewById(R.id.accountAutoLoginCheck);
    }

    @Override
    public void setConfirmText(String text) {
        tv_loginStatus.setText(text);
    }

    @Override
    public void loginResponse(boolean response) {
        if (response) {
            setConfirmText("Login!");
        }else {
            setConfirmText("Fail!");
        }
    }

    @Override
    public void loginDataSend(LoginSessionItem item) {
        setConfirmText("From DB userdata :" + item.getAccountNo() + ", " + item.getAccountNick() + ", " + item.getAccountImage() + ", " + item.getAccountPoint() + ", " + item.getAccountBc() + ", " + item.getAccountCc());

        Log.d(TAG, "loginDataSend: " + item.getAccountNo());
        Log.d(TAG, "loginDataSend: " + item.getAccountNick());
        Log.d(TAG, "loginDataSend: " + item.getAccountImage());
        Log.d(TAG, "loginDataSend: " + item.getAccountPoint());
        Log.d(TAG, "loginDataSend: " + item.getAccountBc());
        Log.d(TAG, "loginDataSend: " + item.getAccountCc());

        SharedPreferences preferences = getSharedPreferences("LoginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        
    }

    //버터나이프 테스트해봐야할듯
//    @OnClick({R.id.accountLoginBtn,R.id.accountFindPwd,R.id.accountSignup})
//    public void onButtonClick(View view) {
//        switch (view.getId()) {
//            case R.id.accountLoginBtn :
//                //로그인 버튼 리스너
//                //이메일 형식인지 아닌지 예외처리해서 맞다면 실행하게 처리해야함
//                LoginItem loginItem = new LoginItem();
//                Log.d("onClick", et_id.getText().toString() + "//" + et_pwd.getText().toString());
//                loginItem.setId(et_id.getText().toString());
//                loginItem.setPwd(et_pwd.getText().toString());
//                loginPresenter.login(loginItem);
//                break;
//            case R.id.accountFindPwd :
//                //비밀번호 찾기 텍뷰 리스너
//                tv_loginStatus.setText("비밀번호 찾을래?");
//                break;
//            case R.id.accountSignup :
//                //회원가입하기 텍뷰 리스터
//                tv_loginStatus.setText("회원가입 할래?");
//                break;
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accountLoginBtn :
                //로그인 버튼 리스너
                //이메일 형식인지 아닌지 예외처리해서 맞다면 실행하게 처리해야함
                LoginItem loginItem = new LoginItem();
                Log.d(TAG, "login btn click: " + et_id.getText().toString() + "//" + et_pwd.getText().toString());
                loginItem.setId(et_id.getText().toString());
                loginItem.setPwd(et_pwd.getText().toString());
                loginPresenter.login(loginItem);
                break;
            case R.id.accountFindPwd :
                //비밀번호 찾기 텍뷰 리스너
                Log.d(TAG, "onClick: 비밀번호 찾기 클릭");
                Toast.makeText(LoginActivity.this, "비밀번호 찾기 화면으로 가기.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accountSignup :
                //회원가입하기 텍뷰 리스터
                Log.d(TAG, "onClick: 회원가입하기 클릭");
                Toast.makeText(LoginActivity.this, "회원가입 화면으로 가기.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accountAutoLoginCheck :
                Log.d(TAG, "onClick: 체크박스 클릭");
                Toast.makeText(LoginActivity.this, "체크박스 클릭.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

