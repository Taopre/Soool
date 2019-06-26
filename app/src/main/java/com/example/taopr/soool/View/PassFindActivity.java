package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taopr.soool.Util.Keyboard;
import com.example.taopr.soool.Object.LoginItem;
import com.example.taopr.soool.Object.PassFIndResponse;
import com.example.taopr.soool.Presenter.PassFindPresenter;
import com.example.taopr.soool.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassFindActivity extends AppCompatActivity implements PassFindPresenter.View{

    @BindView(R.id.email)
    EditText et_email;
    @BindView(R.id.password)
    EditText et_password;

    PassFindPresenter passFindPresenter;
    Keyboard keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ButterKnife.bind(this);

        passFindPresenter = new PassFindPresenter(this);
        passFindPresenter.setView(this);

        keyboard = new Keyboard(this);

        et_email.requestFocus();
        keyboard.showKeyboard(et_email);
    }

    @OnClick({R.id.receiveMail,R.id.login})
    public void passActBtnClick(View view) {
        switch (view.getId()) {
            case R.id.receiveMail:

                if (et_email.getText().length() > 0) {
                    passFindPresenter.EmailCheckReq(et_email.getText().toString());
                    keyboard.hideKeyboard(et_email);
                } else {
                    Toast.makeText(view.getContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.login:

                LoginItem loginItem = new LoginItem();
                loginItem.setId(et_email.getText().toString());
                loginItem.setPwd(et_password.getText().toString());
                loginItem.setAutologinStatus(true);
                passFindPresenter.login(loginItem);

                break;

        }
    }

    @Override
    public void EmailCheckResp(PassFIndResponse passFIndResponse) {
        switch (passFIndResponse.getEmailExist()) {
            case "true" :
                if (passFIndResponse.getResult().equals("true")) {
                    et_password.setText(passFIndResponse.getFakePwd());
//                    Toast.makeText(this, "임시 비밀번호를 확인 후 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "메일 확인 절차를 다시 한번 부탁드립니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "false" :
                Toast.makeText(this, "이메일이 틀렸습니다. 확인 후 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void loginResponse(String response) {
        if (response.equals("true")) {
            Intent intent = new Intent(this, HomeActivity.class);
            //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (response.equals("false")) {
            Toast.makeText(this, "이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
        } else if (response.equals("nee")) {
            Toast.makeText(this, "존재하지 않는 이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
