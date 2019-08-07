package com.lpky.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lpky.taopr.soool.Object.LoginItem;
import com.lpky.taopr.soool.Object.LoginSessionItem;
import com.lpky.taopr.soool.Presenter.LoginPresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.lpky.taopr.soool.Util.DeCryptor;
import com.lpky.taopr.soool.Util.EnCryptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View, Button.OnClickListener {
    String TAG = "LoginActivity log : ";

    LoginPresenter loginPresenter;
    LoginSessionItem loginSessionItem;

    @BindView(R.id.accountFindPwd) TextView tv_findpwd;
    @BindView(R.id.accountSignup) TextView tv_signup;
    @BindView(R.id.accountId)
    EditText et_id;
    @BindView(R.id.accountPass) EditText et_pwd;
    @BindView(R.id.accountLoginBtn) Button btn_login;
    @BindView(R.id.accountAutoLoginCheck)
    CheckBox cb_autologin;
    private EnCryptor encryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);
        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

        encryptor = new EnCryptor();


        //id 와 pwd를 입력하고
        //id가 있다면 login 없다면 wrong 메시지를 textview에 띄워주는 과정
        //1. id 입력
        //2. pwd 입력
        //3. 버튼 클릭의 경우 model로 클릭의 상황과 함께 id와 pwd를 보낸다.
        //4. model로부터 일치여부를 view로 전달된다.
        //5. 일치할 경우 login 일치하지 않을 경우 wrong을 textview로 보여준다.

        drawUnderline();
        btn_login.setOnClickListener(this);
        tv_findpwd.setOnClickListener(this);
        tv_signup.setOnClickListener(this);


    }

    private void DoBinding() {
        loginPresenter = new LoginPresenter(LoginActivity.this, this);
        loginPresenter.setView(this);

        et_id = findViewById(R.id.accountId);
        et_pwd = findViewById(R.id.accountPass);
        btn_login = findViewById(R.id.accountLoginBtn);
        tv_findpwd = findViewById(R.id.accountFindPwd);
        tv_signup = findViewById(R.id.accountSignup);
        cb_autologin = findViewById(R.id.accountAutoLoginCheck);
    }



    //loginResponseGoToView 이 함수는 LoginModel(model)에서 서버로부터 넘어온 응답을 LoginActivity(view)로 보낼 때 LoginPresenter(presenter)가 먼저 받고 LoginActivity(view)로 보낼 때 사용되는 함수입니다.
    //데이터 이동 경로 : model -> preseneter (loginResponse 함수 사용된다.) presenter -> view (loginResponseGoToVIew 함수 사용된다.)
    @Override
    public void loginResponseGoToVIew(String response) {
        //리스폰스 결과에 따라 메시지 처리 어떤식으로 보여줄건지 정해야해.
        //예를 들어 nee일 경우 이메일 존재하지않은 다는걸 toast메시지로 띄워줄지 이런거
        //내가 어떻게 하기로 했는지 기억못하는건지 뭔지 몰라서 주석 남겨놓음.
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

    // 유저가 입력한 비밀번호와 디비에 암호화되서 저장된 값과 일치하지는 확인
    @Override
    public void viewConfirmPw(LoginItem userItem, String enAccountPw) {

        try {
            DeCryptor deCryptor = new DeCryptor();

            String accountNoSt = String.valueOf(LoginSharedPreferences.getAccountNo(LoginActivity.this,"LoginAccount"));
            //Path ttt = Paths.get(Environment.getDataDirectory() +"/data/com.example.taopr.soool/files/"+ accountNoSt +".bin");
            Path ttt = Paths.get(Environment.getDataDirectory() +"/data/com.lpky.taopr.soool/files/"+ accountNoSt +".bin");
            Log.d(TAG, "viewConfirmPw: " + String.valueOf(ttt));
            byte[] pwIv;
            pwIv = Files.readAllBytes(ttt);

            //LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences();
            //byte[] iv = loginSharedPreferences.getPWIv(this,userItem.getId());
            String decPw = null;

            decPw = deCryptor.decryptData("soool_key",
                    Base64.decode(enAccountPw,Base64.DEFAULT),pwIv);


            if (userItem.getPwd().equals(decPw)){
                loginPresenter.login(userItem);
            }

            else{
                Toast.makeText(this, "존재하지 않는 이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }

        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.accountLoginBtn :
                //로그인 버튼 리스너
                //이메일 형식인지 아닌지 예외처리해서 맞다면 실행하게 처리해야함

                LoginItem loginItem = new LoginItem();
                loginItem.setId(et_id.getText().toString());
                loginItem.setPwd(et_pwd.getText().toString());

                loginItem.setAutologinStatus(cb_autologin.isChecked());
                loginPresenter.getPW(loginItem);
                break;
            case R.id.accountFindPwd :
                //비밀번호 찾기 텍뷰 리스너
                Toast.makeText(LoginActivity.this, "비밀번호 찾기 화면으로 가기.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, PassFindActivity.class);
                startActivity(intent);
                break;
            case R.id.accountSignup :
                //회원가입하기 텍뷰 리스터
                Toast.makeText(LoginActivity.this, "이용약관 동의 후, 회원가입을 진행해주세요.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, StartingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.accountAutoLoginCheck :

                break;
        }
    }



    private void drawUnderline() {

        SpannableString content = new SpannableString(getString(R.string.login_button_find_password));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.login_button_find_password).length(), 0);
        tv_findpwd.setText(content);

        content = new SpannableString(getString(R.string.login_button_sign_up));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.login_button_sign_up).length(), 0);
        tv_signup.setText(content);

    }
}

