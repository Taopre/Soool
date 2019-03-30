package com.example.taopr.soool.Presenter;

// accountManager는 회원정보 관련 페이지 presenter에서
// 공통적으로 사용할 메서드를 상속하게 하려는 용도

public interface AccountManager {

    void setView(SignUpPresenter.View view);

    // 중복확인 버튼을 눌렀을 때, View에서 SignUpPresenter로 이메일 or 닉네임 값을 전달
    // 전달 받은 값이 이메일인지 닉네임인지 구분하게 도와주는 구분자(separate) 값도 같이 전달 받는다.
    // View로부터 전달받은 값을 model에 전달해서 이메일 or 닉네임이 중복된 값인지 확인한다.

//    boolean clickDuplicity(int separator,String emailOrNick);
    void clickDuplicity(int separator,String emailOrNick);

    void clickDuplicityResponse(int separator, String emailorNick, boolean response);

    void signUpReqResponse(boolean response);

//    boolean signUpReq(String acconutEmail, String accountPW, String accountNick);

    void signUpReq(String acconutEmail, String accountPW, String accountNick);

   // boolean checkDuplicity(int separator,String emailOrNick);

    public interface View {
       // void setConfirmText(String text);
        void clickDuplicityResponseGoToVIew(int separator, String emailorNick, boolean response);
        void signUpReqResponseGoToVIew(boolean response);
    }
}
