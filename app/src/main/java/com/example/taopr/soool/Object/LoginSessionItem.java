package com.example.taopr.soool.Object;

public class LoginSessionItem {
    public String accountNick, accountImage;
    private int accountNo, accountPoint, accountBc, accountCc;
    private boolean accountAutoLogin;

    public LoginSessionItem(int _accountNo, String _accountNick, String _accountImage,
                            int _accountPoint, int _accountBc, int _accountCc, boolean _accountAutoLogin) {
        this.accountNo = _accountNo; //회원 번호
        this.accountNick = _accountNick; //회원 닉넴
        this.accountImage = _accountImage; //회원 이미지
        this.accountPoint = _accountPoint; //회원 포인트
        this.accountBc = _accountBc; //게시판 갯수
        this.accountCc = _accountCc; //댓글 갯수
        this.accountAutoLogin = _accountAutoLogin; //회원 자동로그인 체크여부
    }

    public int getAccountBc() {
        return accountBc;
    }

    public int getAccountCc() {
        return accountCc;
    }

    public String getAccountImage() {
        return accountImage;
    }

    public String getAccountNick() {
        return accountNick;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public int getAccountPoint() {
        return accountPoint;
    }

    public boolean isAccountAutoLogin() {
        return accountAutoLogin;
    }

    public void setAccountNick(String accountNick) {
        this.accountNick = accountNick;
    }
}
