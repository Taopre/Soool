package com.example.taopr.soool;

public class LoginSessionItem {
    String accountNick, accountImage;
    int accountNo, accountPoint, accountBc, accountCc;

    public LoginSessionItem(int _accountNo, String _accountNick, String _accountImage, int _accountPoint, int _accountBc, int _accountCc) {
        this.accountNo = _accountNo;
        this.accountNick = _accountNick;
        this.accountImage = _accountImage;
        this.accountPoint = _accountPoint;
        this.accountBc = _accountBc; //게시판 갯수
        this.accountCc = _accountCc; //댓글 갯수
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
}
