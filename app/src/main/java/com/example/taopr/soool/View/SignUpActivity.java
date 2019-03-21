package com.example.taopr.soool.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.Presenter.SignUpPresenter;
import com.example.taopr.soool.R;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SignUpActivity extends AppCompatActivity{

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
    @BindView(R.id.linkToLogin)
    TextView linkToLogin;
    @BindView(R.id.signUp)
    Button signUp;

    private SignUpPresenter signUpPresenter;
    private Boolean emailEnable=false; // 이메일 값 사용 불가능 시 false, 이메일 값 사용 가능 시 true
    private Boolean nickEnable=false;  // false = 닉네임값 사용 불가능 , true = 닉네임 값 사용 가능
    private Boolean pwEnable=false; // 비밀번호와 비밀번호 확인 칸에 입력한 값 false=불일치 true=일치

    /*
    private Boolean emailDupClickBool = false; // 이메일 중복체크 버튼 클릭여부 false=클릭x , true=클릭o
    private Boolean nickDupClickBool = false; // 닉네임 중복체크 버튼 클릭여부 false=클릭x , true=클릭o
    */

    private static String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        signUpPresenter = new SignUpPresenter(this);

        // sns를 통해 가입하는 사람들의 경우
        // 가져온 값을 Edittext의 값에 넣어주고
        // 넣어준 Edittext에 관해서는 사용자에게 보이지 않게한다.
        // 위와 같은 방법으로 한 이유는 회원가입을 할 때 예외처리를 편하게 하기 위함인데
        // 이유는, 위와 같은 방법으로 할 경우는 회원가입을 할 때
        // Edittext의 값을 입력을 했는지 안했는지만 확인하면 되지만
        // 위와 같은 방법을 하지 않을 경우
        // sns를 통해서 가입을 하는 것인지에 대한 구분도 해줘야 하며
        // 추가로 sns api를 통해 이메일주소를 가져오지 못하는 경우 이 부분에 대해서도 처리를 해줘야 한다

    }




    // 이메일 중복 확인

    // 1.입력하지 않았을 경우 값을 입력해달라는 메시지를 표시
    // 2.이메일을 정규식에 맞춰 작성을 했는지 확인
    // 정규식을 지키지 않은 경우 올바르게 작성해 달라는 메시지를 유저에게 표시
    // 3.입력했을 경우 이메일값과 보내는 값이 이메일인지 닉네임인지를 구별 해주는 구분자 값을 서버에 전달
    // 디비에서 입력한 이메일의 중복여부를 파악
    // 중복하는 경우 이메일을 사용할 수 없다는 메세지를 유저에게 표시
    // 중복하지 않는 경우 이메일을 사용할 수 있다는 메세지를 표시

    ////////////////////// 회의해야 될 사항
    // 중복체크를 했을 때 사용한 가능하다는 메시지를 유저에게 다이얼로그를 띄워서 알려줄지
    // 아니면 아이콘의 변화를 주거나 문구를 표시함으로써 지속적으로 나타내줄지

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

            emailEnable = signUpPresenter.clickDuplicity(0,accountEmail.getText().toString());

            Log.i(TAG, "emailDupClick: email = " + accountEmail.getText().toString() + ", 사용 가능 여부 : " + emailEnable);

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
    // confirmPW 값을 입력 시 accountPW와 일치하는지 확인
    // 일치하는 경우 pwEnable 값을 true로 불일치하는 경우 pwEnable 값을 false로 변경해줌을써
    // 회원가입시 pwEnable 값으로 비밀번호와 비밀번호 확인 값이 일치하는 경우에만 서버에 전달하는 예외처리

    @OnTextChanged(R.id.confirmPW)
    void editConfirmPW(){

        Log.i(TAG, "editConfirmPW: 입력");

        if(accountPW.getText().toString().equals(confirmPW.getText().toString())){
            Log.i(TAG, "editConfirmPW: 일치");
            pwEnable = true;
        }
        else{
            Log.i(TAG, "editConfirmPW: 불일치");
            pwEnable = false;
        }
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
            Log.i(TAG, "emailDupClick: 이메일 값 입력해주세요");
        }

        else {

            // 보내는 값일 이메일인 경우 separator=0, 닉네임인 경우 separator=1
            nickEnable = signUpPresenter.clickDuplicity(1,accountNick.getText().toString());
            Log.i(TAG, "nickDupClick: nick = " + accountNick.getText().toString() + ", 중복 여부 : " + nickEnable);

        }
    }


    // 회원가입 요청
    // 회원가입 버튼 클릭 시
    // 1. 모든 Edittext의 값을 입력했는지 확인
    //    입력하지 않았을 경우 값을 입력해달라는 메세지를 유저에게 표시
    // 2. 이메일 중복 체크 확인 후 사용가능한 아이디인지 확인
    // 3. 비밀번호와 비밀번호확인칸에 입력한 두 값이 일치하는지 확인
    //    일치하지 않은 경우 비밀번호가 일치하지 않는다는 메세지를 유저에게 표시
    // 4. 닉네임 값 사용가능한지 확인
    // 5. 1,2,3,4의 예외처리를 통과한 경우 서버에 이메일주소와 비밀번호 닉네임 값을 전달
    // 6. 서버에서 회원가입에 실패했을 경우 다시 시도해 달라는 메시지를 유저에게 전달
    //    성공 했을 경우


    //// 고려,회의 해야되는 사항
    //// 이메일 값, 닉네임 값 예외처리에서 중복체크를 했는지, 중복체크를 했다면 가능한 값인지 불가능한 값인지
    //// -> 일단은 중복체크 버튼을 누르지 않은 것도, 중복체크를 했지만 사용불가능 한 경우 둘다
    ////    사용할 수 업다는 메세지를 띄우는 방식으로 통일

    @OnClick(R.id.signUp)
    void signUpClick(){

        String acconutEmailSt = accountEmail.getText().toString();
        String accountPWSt = accountPW.getText().toString();
        String accountNickSt = accountNick.getText().toString();

        // 1번 , 모든 Edittext 값 입력했는지
        if(acconutEmailSt.length()==0 || accountNickSt.length()==0 ||
                accountPWSt.length()==0 || confirmPW.getText().length()==0){

            Log.i(TAG, "signUpClick: 1번 예외처리");

        }

        // 2번 , 이메일 값 사용가능한지 확인
        else if(emailEnable == false){
            Log.i(TAG, "signUpClick: 2번 예외처리 ");
        }

        // 3번 , 비밀번호와 비밀번호확인카에 입력한 두 값이 일치하는지
        else if(pwEnable == false){
            Log.i(TAG, "signUpClick: 3번 예외처리 ");
        }

        // 4번 , 닉네임 값 사용가능한지 확인
        else if(nickEnable == false){
            Log.i(TAG, "signUpClick: 4번 예외처리 ");
        }

        // 상위의 조건을 모두 만족한 경우 서버에 accountEmail, accountPW, accountNick 전달.
        else{
            boolean signUpSuccess = signUpPresenter.signUpReq(accountNickSt,accountPWSt,accountNickSt);
            if( signUpSuccess == false){
                Log.i(TAG, "signUpClick: 다시 시도해주세요 ");
            }
            else{
                Log.i(TAG, "signUpClick: 회원가입 성공 / 로그인 페이지로 이동");
            }
        }
    }
}
