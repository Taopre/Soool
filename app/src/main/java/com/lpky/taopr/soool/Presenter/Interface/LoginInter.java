package com.lpky.taopr.soool.Presenter.Interface;

import com.lpky.taopr.soool.Object.LoginItem;
import com.lpky.taopr.soool.Presenter.LoginPresenter;

public interface LoginInter {
    //아래 주석으로 그어놓은 줄 위 아래로 부분을 나눌 수 있다.
    //줄 위쪽 부분은 Presenter에서 선언되며 정의된 부분의 메서드들을 인터페이스인 이곳에서 명시해주는 부분.
    //줄 아래쪽 부분은 View에서 선언되며 정의된 부분의 메서드들을 이곳에서 명시해주는 부분. 으로 구별할 수 있다. 참고하세요.
    void setView(LoginPresenter.View view);

    void login(LoginItem userItem);

    void getPW(LoginItem userItem);

    void loginResponse(String response);

    void confirmPw(LoginItem userItem, String enAccountPw);

//    void loginDataSend(LoginSessionItem item);
//--------------------------------------------------------------------------------------

    public interface View {

        void loginResponseGoToVIew(String response);
//        void loginDataSend(LoginSessionItem item);

        void viewConfirmPw(LoginItem userItem, String enAccountPw);
    }

}
