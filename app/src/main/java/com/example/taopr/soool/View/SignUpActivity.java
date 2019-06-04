package com.example.taopr.soool.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taopr.soool.Presenter.SignUpPresenter;
import com.example.taopr.soool.R;

import java.util.regex.Pattern;

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

    private SignUpPresenter signUpPresenter;
    private Boolean emailEnable=false; // 이메일 값 사용 불가능 시 false, 이메일 값 사용 가능 시 true
    private Boolean nickEnable=false;  // false = 닉네임값 사용 불가능 , true = 닉네임 값 사용 가능
    private Boolean pwEnable=false; // 비밀번호와 비밀번호 확인 칸에 입력한 값 false=불일치 true=일치


    private Boolean clickEmailDupBool = false; // 이메일 중복체크 버튼 클릭여부 false=클릭x , true=클릭o
    private Boolean clickNickDupBool = false; // 닉네임 중복체크 버튼 클릭여부 false=클릭x , true=클릭o

    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);
        signUpPresenter = new SignUpPresenter(SignUpActivity.this, this);
        signUpPresenter.setView(this);

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

        Log.i(TAG, "signUpThroughSNS: " + intent.getBooleanExtra("throughSNS", false) +
                intent.getStringExtra("snsAccountEmail") );

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
            Log.i(TAG, "emailDupClick: 이메일 값 입력해주세요");
        }

        // 이메일 값을 정규식에 맞춰 작성하지 않은 경우
        else if(ismail == false){
            Log.i(TAG, "emailDupClick: 이메일 정규식 불일치");
        }

        // 이메일 값을 정규식에 맞춰 입력한 경우
        else {

            // 보내는 값일 이메일인 경우 separator=0, 닉네임인 경우 separator=1
//            Boolean b = signUpPresenter.clickDuplicity(0,accountEmail.getText().toString());
            signUpPresenter.clickDuplicity(0,accountEmail.getText().toString());
//            Log.i(TAG, "emailDupClick: email = " + accountEmail.getText().toString() + ", 사용 가능 여부 : " + b);
//
//            // 중복 false, 중복x true
//
//            emailEnable = b;
//            clickEmailDupBool = true;
//
//            accountPW.requestFocus();
        }
    }

    //이메일, 닉네임 중복 체크에 대한 응답을 View에서 처리하기 위해 만든 함수.
    public void clickDuplicityResponseGoToVIew(int separator, String emailorNick, boolean response) {
        // 중복 false, 중복x true
        if (separator == 0) {
            Log.i(TAG, "emailDupClick: email = " + emailorNick + ", 사용 가능 여부 : " + response);

            emailEnable = response;
            clickEmailDupBool = true;

            accountPW.requestFocus();
        } else {
            Log.i(TAG, "nickDupClick: nick = " + accountNick.getText().toString() + ", 중복 여부 : " + response);

            // 중복 false, 중복x true

            nickEnable = response;
            clickNickDupBool = true;
        }
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

        Log.i(TAG, "editConfirmPW: 입력");
        if(accountPW.getText().length() >0 && confirmPW.getText().length()>0) {
            if (accountPW.getText().toString().equals(confirmPW.getText().toString())) {
                Log.i(TAG, "editConfirmPW: 일치");
                pwEnable = true;
            } else {
                Log.i(TAG, "editConfirmPW: 불일치");
                pwEnable = false;
            }
        }
    }

    // 닉네임이나 이메일을 중복 체크한 이후에 수정을 하게 될 경우
    // 중복 체크를 검사했던 값이 아닌 수정된 값으로 회원가입이 진행되게 된다
    // 위와 같은 현상을 방지하기 위해 닉네임이나 이메일 값을 수정하게 될 경우
    // 그 값을 EmailEnable이나 PWEnable 값을 false로 설정해준다
    // 그리고 위 두 값은 중복체크를 통해 사용가능한 경우에만 true로 설정해준다.

    @OnTextChanged(R.id.accountEmail)
    void editAccountEmail(){
        emailEnable = false;
        clickEmailDupBool = false;
    }

    @OnTextChanged(R.id.accountNick)
    void editAccountNick(){
        nickEnable = false;
        clickNickDupBool = false;
    }

    // 닉네임 중복 확인
    // 닉네임 중복 확인 버튼을 클릭 시 닉네임값을 입력했는지 확인 후
    // 1.입력하지 않았을 경우 값을 입력해달라는 메시지를 유저에게 표시
    // 2.입력했을 경우 닉네임값과 보내는 값이 닉네임인지 이메일인지를 구별 해주는 구분자 값을 서버에 전달
    // 디비에서 입력한 닉네임의 중복여부를 파악
    // 중복하는 경우 닉네임을 사용할 수 없다는 메세지를 유저에게 표시
    // 중복하지 않는 경우 닉네임을 사용할 수 있다는 메세지를 표시

    @OnClick(R.id.accountNickDup)
    void nickDupClick(){

        Log.i(TAG, "nickDupClick: , 텍스트 길이 : " + accountNick.getText().length());

        // 이메일 값을 아무것도 입력하지 않은 경우
        if(accountNick.getText().length()==0){
            Log.i(TAG, "emailDupClick: nick 값 입력해주세요");
        }

        else {

            // 보내는 값일 이메일인 경우 separator=0, 닉네임인 경우 separator=1
//            Boolean b = signUpPresenter.clickDuplicity(1,accountNick.getText().toString());
            signUpPresenter.clickDuplicity(1,accountNick.getText().toString());
//            Log.i(TAG, "nickDupClick: nick = " + accountNick.getText().toString() + ", 중복 여부 : " + b);
//
//            // 중복 false, 중복x true
//
//            nickEnable = b;
//            clickNickDupBool = true;
        }
    }

    //회원가입에 대한 서버로 부터의 응답을 View에서 처리하기 위해 만든 메서드.
    public void signUpReqResponseGoToVIew (boolean response) {
        if( response == false){
            Log.i(TAG, "clickSignUp: 다시 시도해주세요 ");
        }
        else{
            Log.i(TAG, "clickSignUp: 회원가입 성공 ");
            Intent intent = new Intent(this, HomeActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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

            Log.i(TAG, "clickSignUp: 모든 텍스트를 입력해주세요");

        }

        // 2번 , 3번
        else if(emailEnable == false){
            if(clickEmailDupBool){
                Log.i(TAG, "clickSignUp: 사용불가능한 이메일입니다");
            }
            else{
                Log.i(TAG, "clickSignUp: 중복 체크를 부탁드립니다");
            }
        }

        // 4번 , 비밀번호와 비밀번호확인카에 입력한 두 값이 일치하는지
        else if(pwEnable == false){
            Log.i(TAG, "clickSignUp: 비밀번호 불일치 ");
        }

        // 5번 , 6번
        else if(nickEnable == false){
            if (clickNickDupBool){
                Log.i(TAG, "clickSignUp: 사용 불가능한 닉네임입니다");
            }
            else{
                Log.i(TAG, "clickSignUp: 중복 체크를 부탁드립니다");
            }
        }

        // 상위의 조건을 모두 만족한 경우 서버에 accountEmail, accountPW, accountNick 전달.

        else{
//            boolean signUpSuccess = signUpPresenter.signUpReq(accountEmailSt,accountPWSt,accountNickSt);
            //회원가입 가능한지 model로 요청 하는 부분.
            signUpPresenter.signUpReq(accountEmailSt,accountPWSt,accountNickSt);
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

        Log.i(TAG, "setLinkToLogin: 로그인 페이지 이동");
    }

}
