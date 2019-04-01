package com.example.taopr.soool;

public class LoginItem {
    //로그인 시 mvp간에 인자 넘겨줄 객체입니다.
    private String loginid, loginpwd;
    private boolean autologinStatus;

    public boolean isAutologinStatus() {
        return autologinStatus;
    }

    public String getId() {
        return loginid;
    }

    public String getPwd() {
        return loginpwd;
    }

    public void setAutologinStatus(boolean autologinStatus) {
        this.autologinStatus = autologinStatus;
    }

    public void setId(String id) {
        this.loginid = id;
    }

    public void setPwd(String pwd) {
        this.loginpwd = pwd;
    }
}
