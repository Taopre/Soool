package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.Util.EnCryptor;
import com.example.taopr.soool.Util.Keyboard;
import com.example.taopr.soool.Object.LoginItem;
import com.example.taopr.soool.Object.PassFIndResponse;
import com.example.taopr.soool.Presenter.PassFindPresenter;
import com.example.taopr.soool.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

    private EnCryptor enCryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ButterKnife.bind(this);

        enCryptor = new EnCryptor();

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

                String accountPwEn=null;

                try {
                    accountPwEn = Base64.encodeToString(enCryptor.encryptText("soool_key",et_password.getText().toString()), Base64.DEFAULT);
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
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                passFindPresenter.login(loginItem,accountPwEn);

                break;

        }
    }

    @Override
    public void EmailCheckResp(PassFIndResponse passFIndResponse) {
        switch (passFIndResponse.getEmailExist()) {
            case "true" :
                if (passFIndResponse.getResult().equals("true")) {
                    //et_password.setText(passFIndResponse.getFakePwd());
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
            FileOutputStream test = null;
            String accountNo = String.valueOf(LoginSharedPreferences.getAccountNo(this,"LoginAccount"));
            try {
                test = new FileOutputStream(Environment.getDataDirectory() + "/data/com.example.taopr.soool/files/" + accountNo + ".bin");
                test.write(enCryptor.getIv());
                test.close();

                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.equals("false")) {
            Toast.makeText(this, "이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
        } else if (response.equals("nee")) {
            Toast.makeText(this, "존재하지 않는 이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
