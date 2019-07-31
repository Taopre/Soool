package com.lpky.taopr.soool.View;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lpky.taopr.soool.Presenter.SignUpPresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.lpky.taopr.soool.Util.EnCryptor;


import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import android.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.View{

    // 변수명 규칙
    // 첫단어의 첫자는 소문자 그다음 단어들의 첫자는 대문자

    @BindView(R.id.accountNick)
    EditText accountNick;
    @BindView(R.id.accountNickDup)
    Button accountNickDup;
    @BindView(R.id.accountEmail)
    EditText accountEmail;
    @BindView(R.id.accountEmailDup)
    Button accountEmailDup;
    @BindView(R.id.accountPW)
    EditText accountPW;
    @BindView(R.id.confirmPW)
    EditText confirmPW;
    @BindView(R.id.signUpToLogIn)
    TextView signUpToLogIn;
    @BindView(R.id.signUp)
    Button signUp;
    @BindView(R.id.signUpProgress)
    ProgressBar signUpProgress;
    @BindView(R.id.signUpNoticeEnablePw)
    TextView signUpNoticeEnablePw;
    @BindView(R.id.signUpNoticeEnableEmail)
    TextView signUpNoticeEnableEmail;
    @BindView(R.id.signUpNoticeEnableNick)
    TextView signUpNoticeEnableNick;

    private SignUpPresenter signUpPresenter;
    private Boolean emailEnable=false; // 이메일 값 사용 불가능 시 false, 이메일 값 사용 가능 시 true
    private Boolean nickEnable=false;  // false = 닉네임값 사용 불가능 , true = 닉네임 값 사용 가능
    private Boolean pwEnable=false; // 비밀번호와 비밀번호 확인 칸에 입력한 값 false=불일치 true=일치

    // enableEmail,enableNick 두가지를 만든 이유는 중복체크를 했을 때 사용가능하다고 나왔음에도
    // 사용자가 입력한 이메일이나 닉네임을 변경 했다가 다시 중복체크를 통해서 사용가능하다고 했던 아이디로 돌아왔을 때
    // 다시 중복체크를 하지 않게 해주기 위해서

    private String enableEmailValue=null; // 이메일 중복 체크 결과 사용가능한 이메일
    private String enableNickValue=null;  // 닉네임 중복 체크 결과 사용가능한 이메일


    private Boolean clickEmailDupBool = false; // 이메일 중복체크 버튼 클릭여부 false=클릭x , true=클릭o
    private Boolean clickNickDupBool = false; // 닉네임 중복체크 버튼 클릭여부 false=클릭x , true=클릭o


    private String TAG = "SignUpActivity";
    private byte[] accountPWIv;
    private EnCryptor enCryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);

        enCryptor = new EnCryptor();
        signUpPresenter = new SignUpPresenter(SignUpActivity.this, this);
        signUpPresenter.setView(this);


        drawUnderline();
    }

    // sns를 통해 가입하는 사람들의 경우
    // intent에 Boolean throughSNS값을 입력하여
    // true면 sns가입자, false이면 일반 가입자로 구분

    // true일 경우 이메일 주소를 받아왔는지 확인
    // 이유는, 사용자가 sns에서 이메일 공개여부를 거절했을 경우 이메일 주소를 받아올 수 없기 때문에
    // 이메일 주소를 받아온 경우 회원가입 예외처리에 걸리지 않게 하기 위하여
    // emailEnable , clickEmailDupBool의 값을 true로 변경

    private void signUpThroughSNS() {

        Intent intent = getIntent();
        Boolean throughSNS = intent.getBooleanExtra("throughSNS", false);

        if(throughSNS) {
            if(intent.getStringExtra("snsAccountEmail").length() > 0){
                String snsAccountEmail = intent.getStringExtra("snsAccountEmail");
                accountEmail.setText(snsAccountEmail);

                emailEnable = true;
                clickEmailDupBool = true;

                accountPW.requestFocus();
                // 이메일을 받아왔을 경우 디비에 중복한 값이 있는지 확인
                // 중복한 값이 있다면 사용불가능한 이메일이란 메세지 유저에게 표시
                // --> 이부분 예외처리 안해주기로함

            }
        }

    }

    // 이메일 중복 확인

    // 1.입력하지 않았을 경우 값을 입력해달라는 메시지를 표시
    // 2.이메일을 정규식에 맞춰 작성을 했는지 확인
    // 정규식을 지키지 않은 경우 올바르게 작성해 달라는 메시지를 유저에게 표시
    // 3.입력했을 경우 이메일값과 보내는 값이 이메일인지 닉네임인지를 구별 해주는 구분자 값을 서버에 전달
    // 디비에서 입력한 이메일의 중복여부를 파악
    // 중복하는 경우 이메일을 사용할 수 없다는 메세지를 유저에게 표시
    // 중복하지 않는 경우 이메일을 사용할 수 있다는 메세지를 표시


    //// 회의해야 될 사항
    //// 중복체크를 했을 때 사용한 가능하다는 메시지를 유저에게 다이얼로그를 띄워서 알려줄지
    //// 아니면 아이콘의 변화를 주거나 문구를 표시함으로써 지속적으로 나타내줄지

    @OnClick(R.id.accountEmailDup)
    void emailDupClick(){

        boolean ismail = isEmail(accountEmail.getText().toString());

        // 이메일 값을 아무것도 입력하지 않은 경우
        if(accountEmail.getText().length()==0){
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
        }

        // 이메일 값을 정규식에 맞춰 작성하지 않은 경우
        else if(ismail == false){
            Toast.makeText(this, "양식에 맞게 작성해주세요", Toast.LENGTH_SHORT).show();
        }

        // 이메일 값을 정규식에 맞춰 입력한 경우
        else {

            // 보내는 값일 이메일인 경우 separator=0, 닉네임인 경우 separator=1
            signUpPresenter.clickDuplicity(0,accountEmail.getText().toString());

        }
    }

    //이메일, 닉네임 중복 체크에 대한 응답을 View에서 처리하기 위해 만든 함수.
    public void clickDuplicityResponseGoToVIew(int separator, String emailorNick, boolean response) {
        // separator 이메일일때 0, 닉네임일때 1
        // response 값 중복 false, 중복x true
        if (separator == 0) {

            if (response){

                enableEmailValue = emailorNick;
                accountNick.requestFocus();

           } else{
              /*  Toast.makeText(this, "사용 불가능", Toast.LENGTH_SHORT).show();

                signUpNoticeEnableEmail.setText(getString(R.string.sign_up_disable_email));
                signUpNoticeEnableEmail.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.redMain));*/
           }
            emailEnable = response;
            clickEmailDupBool = true;

          //  accountPW.requestFocus();
        } else {
            if (response){
                accountPW.requestFocus();
                enableNickValue = emailorNick;


            }else{
               /* Toast.makeText(this, "사용 불가능", Toast.LENGTH_SHORT).show();
                signUpNoticeEnableNick.setText(getString(R.string.sign_up_disable_nickname));
                signUpNoticeEnableNick.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.redMain));*/
            }

            nickEnable = response;
            clickNickDupBool = true;
        }
        noticeEnableOrDisable(separator,response);
    }


    // 이메일 정규식에 맞춰 입력했는지 확인
    // 올바르게 작성했을 경우 true
    // 그렇지 않은 경우 false

    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches(
                "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",
                email.trim());
        return b;
    }


    // 비밀번호와 비밀번호 확인 값 일치 확인
    // confirmPW 값이나 accountPW값 입력 시 서로의 값이 일치하는지 확인
    // 일치하는 경우 pwEnable 값을 true로 불일치하는 경우 pwEnable 값을 false로 변경해줌을써
    // 회원가입시 pwEnable 값으로 비밀번호와 비밀번호 확인 값이 일치하는 경우에만 서버에 전달하는 예외처리
    // 둘 중 한가지 값만 입력했을 때 일치하지 않는다고 표시해주는 것은 불필요하다 생각해서
    // 두 값을 모두 입력했을 때만 표시하도록 예외처리

    @OnTextChanged({R.id.confirmPW, R.id.accountPW})
    void editConfirmPW(){

        if(accountPW.getText().length() >0 && confirmPW.getText().length()>0) {
            if (accountPW.getText().toString().equals(confirmPW.getText().toString())) {
                signUpNoticeEnablePw.setText(getString(R.string.sign_up_match_password));
                signUpNoticeEnablePw.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.greenMain));
                signUpNoticeEnablePw.setVisibility(View.VISIBLE);
                pwEnable = true;
            } else {
                signUpNoticeEnablePw.setText(getString(R.string.sign_up_not_match_password));
                signUpNoticeEnablePw.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.redMain));
                signUpNoticeEnablePw.setVisibility(View.VISIBLE);
                pwEnable = false;
            }
        }else{
            signUpNoticeEnablePw.setVisibility(View.GONE);
        }
    }

    // 닉네임이나 이메일을 중복 체크한 이후에 수정을 하게 될 경우
    // 중복 체크를 검사했던 값이 아닌 수정된 값으로 회원가입이 진행되게 된다
    // 위와 같은 현상을 방지하기 위해 닉네임이나 이메일 값을 수정하게 될 경우
    // 그 값을 EmailEnable이나 PWEnable 값을 false로 설정해준다
    // 그리고 위 두 값은 중복체크를 통해 사용가능한 경우에만 true로 설정해준다.
    // 중복 체크를 통해 사용가능한 결과를 받았음에도 수정을 하게되면 사용불가능하다는 문구를 띄워주게 된다
    // 하지만 다시 수정을 통해 중복체크를 통해 사용가능하다는 결과를 받았던 이메일이나 닉네임으로 수정할 경우
    // 다시 사용가능하다는 문구를 띄워준다

    @OnTextChanged(R.id.accountEmail)
    void editAccountEmail(){
        if (enableEmailValue!=null) {
            if (accountEmail.getText().toString().equals(enableEmailValue)) {
                emailEnable = true;
                clickEmailDupBool = true;
                noticeEnableOrDisable(0, true);
            } else {
                emailEnable = false;
                clickEmailDupBool = false;
                noticeEnableOrDisable(0, false);
            }
        }
    }

    @OnTextChanged(R.id.accountNick)
    void editAccountNick(){
        if (enableNickValue!=null) {
            if (accountNick.getText().toString().equals(enableNickValue)) {
                nickEnable = true;
                clickNickDupBool = true;
                noticeEnableOrDisable(1, true);
            }
            else{
                nickEnable = false;
                clickNickDupBool = false;
                noticeEnableOrDisable(1, false);
            }
        }
    }

    // 이메일이나 닉네임이 사용가능한지 불가능한지 사용자에게 보여주는 부분
    // enableValue 은 사용가능 할때 true, 사용불가능할때 false
    private void noticeEnableOrDisable(int emailOrNick , boolean enableValue){
        switch (emailOrNick){
            case 0:
                signUpNoticeEnableEmail.setVisibility(View.VISIBLE);
                if (enableValue){
                    signUpNoticeEnableEmail.setText(getString(R.string.sign_up_usable_email));
                    signUpNoticeEnableEmail.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.greenMain));
                }else{
                    signUpNoticeEnableEmail.setText(getString(R.string.sign_up_disable_email));
                    signUpNoticeEnableEmail.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.redMain));
                }
                break;
            case 1:
                signUpNoticeEnableNick.setVisibility(View.VISIBLE);
                if (enableValue){
                    signUpNoticeEnableNick.setText(getString(R.string.sign_up_usable_nickname));
                    signUpNoticeEnableNick.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.greenMain));
                }else{
                    signUpNoticeEnableNick.setText(getString(R.string.sign_up_disable_nickname));
                    signUpNoticeEnableNick.setTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.redMain));
                }
                break;
        }
    }

    // 닉네임 중복 확인
    // 닉네임 중복 확인 버튼을 클릭 시 닉네임값을 입력했는지 확인 후
    // 1.입력하지 않았을 경우 값을 입력해달라는 메시지를 유저에게 표시
    // 2.입력한 닉네임이 6자를 넘어갈 경우 6자 이내로 작성해달라는 메세지 표시
    // 2.입력했을 경우 닉네임값과 보내는 값이 닉네임인지 이메일인지를 구별 해주는 구분자 값을 서버에 전달
    // 디비에서 입력한 닉네임의 중복여부를 파악
    // 중복하는 경우 닉네임을 사용할 수 없다는 메세지를 유저에게 표시
    // 중복하지 않는 경우 닉네임을 사용할 수 있다는 메세지를 표시

    @OnClick(R.id.accountNickDup)
    void nickDupClick(){


        // 이메일 값을 아무것도 입력하지 않은 경우
        if(accountNick.getText().length()==0){
            Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
        }

        else if(accountNick.getText().length()>6){
            Toast.makeText(this, "닉네임은 6자 이내로 작성해주세요", Toast.LENGTH_SHORT).show();
        }
        else {

            // 보내는 값일 이메일인 경우 separator=0, 닉네임인 경우 separator=1
            signUpPresenter.clickDuplicity(1,accountNick.getText().toString());

        }
    }

    //회원가입에 대한 서버로 부터의 응답을 View에서 처리하기 위해 만든 메서드.
    public void signUpReqResponseGoToVIew (boolean response,String accountNo) {
        if( response == false){
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
        else{
            // 암호화 IV 바이너리 파일로 저장
            try {
                FileOutputStream test = null;
                //test = new FileOutputStream(Environment.getDataDirectory() +"/data/com.example.taopr.soool/files/"+accountNo+".bin");
                test = new FileOutputStream(Environment.getDataDirectory() +"/data/com.lpky.taopr.soool/files/"+accountNo+".bin");
                test.write(enCryptor.getIv());
                test.close();

                Toast.makeText(this, "가입 성공! 환영합니다 :)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void showLoading() {
        signUpProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        signUpProgress.setVisibility(View.GONE);
    }

    // 회원가입 요청
    // 회원가입 버튼 클릭 시
    // 1. 모든 Edittext의 값을 입력했는지 확인
    //    입력하지 않았을 경우 값을 입력해달라는 메세지를 유저에게 표시
    // 2. 이메일 중복 체크 버튼을 눌렀는지 확인
    // 3. 이메일 중복 체크를 했을 때 사용 가능한 값이었는지 확인
    // 4. 비밀번호와 비밀번호확인칸에 입력한 두 값이 일치하는지 확인
    //    일치하지 않은 경우 비밀번호가 일치하지 않는다는 메세지를 유저에게 표시
    // 5. 닉네임 중복 체크 버튼을 눌렀는지 확인
    // 6. 닉네임 중복 체크를 했을 때 사용 가능한 값이었는지 확인
    // 7. 1,2,3,4의 예외처리를 통과한 경우 서버에 이메일주소와 비밀번호 닉네임 값을 전달
    // 8. 서버에서 회원가입에 실패했을 경우 다시 시도해 달라는 메시지를 유저에게 전달
    // 9. 성공 했을 경우에는 가입한 유저의 회원번호를 전달 받는다.
    // 10. 회원번호, 이메일값 그리고 자동로그인 값을 쉐어드에 저장 후 메인 페이지로 이동

    //회원가입 버튼 클릭 리스너 부분.
    @OnClick(R.id.signUp)
    void clickSignUp(){

        String accountEmailSt = accountEmail.getText().toString();
        String accountPWSt = accountPW.getText().toString();
        String accountNickSt = accountNick.getText().toString();

        // 1번 , 모든 Edittext 값 입력했는지
        if(accountEmailSt.length()==0 || accountNickSt.length()==0 ||
                accountPWSt.length()==0 || confirmPW.getText().length()==0){

            Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show();

        }

        // 2번 , 3번
        else if(emailEnable == false){
            if(clickEmailDupBool){
                Toast.makeText(this,getString(R.string.sign_up_disable_email), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "이메일 중복 체크를 부탁드립니다", Toast.LENGTH_SHORT).show();
            }
        }

        // 4번 , 비밀번호와 비밀번호확인카에 입력한 두 값이 일치하는지
        else if(pwEnable == false){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
        }

        // 5번 , 6번
        else if(nickEnable == false){
            if (clickNickDupBool){
                Toast.makeText(this,getString(R.string.sign_up_disable_nickname), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "닉네임 중복 체크를 부탁드립니다", Toast.LENGTH_SHORT).show();
            }
        }

        // 상위의 조건을 모두 만족한 경우 서버에 accountEmail, accountPW, accountNick 전달.

        else{

            // EnCryptor enCryptor = new EnCryptor();
            LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences();

            String accountPwEn=null;

            try {

               // FileOutputStream test = null;

                accountPwEn = Base64.encodeToString(enCryptor.encryptText("soool_key",accountPWSt), Base64.DEFAULT);
                //loginSharedPreferences.savePWIv(this,accountEmailSt,enCryptor.getIv());
                /*test = new FileOutputStream(Environment.getDataDirectory() +"/data/com.example.taopr.soool/files/"+accountEmailSt+".bin");
                test.write(enCryptor.getIv());
                test.close();*/
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



            signUpPresenter.signUpReq(accountEmailSt,accountPwEn,accountNickSt);
        }
    }

    // 회원 가입 완료 후
    // 회원 이메일(accountEmail)과 계정번호(accountNo)의 값와 autoLogin변수에 true값 쉐어드에 저장
    // 저장 후 메인 페이지로 이동


    //로그인 페이지로 이동의 클릭리스너 부분.
    @OnClick(R.id.signUpToLogIn)
    public void setSignUpToLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void drawUnderline() {

        // '개인정보 취급방취'와 '이용약관' text 에 밑줄 추가
        SpannableString content = new SpannableString(getString(R.string.sign_up_button_move_to_login));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.sign_up_button_move_to_login).length(), 0);
        signUpToLogIn.setText(content);


    }
}
