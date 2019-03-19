package com.example.taopr.soool.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taopr.soool.R;

import butterknife.BindView;

public class SignUpActivity extends AppCompatActivity{

    // 변수명 규칙
    // 첫단어의 첫자는 소문자 그다음 단어들의 첫자는 대문자

    @BindView(R.id.confirmPW)
    EditText confirmPW;
    @BindView(R.id.accountNick)
    EditText accountNick;
    @BindView(R.id.accountNickDup)
    EditText accountNickDup;
    @BindView(R.id.accountID)
    EditText accountId;
    @BindView(R.id.accountPW)
    EditText accountPW;
    @BindView(R.id.linkToLogin)
    TextView linkToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }


}
