package com.lpky.taopr.soool.Object;

public class PassFIndResponse {

    String result;
    String emailExist;
    String fakePwd;

    public PassFIndResponse (String result, String emailExist, String fakePwd) {
        this.emailExist = emailExist;
        this.result = result;
        this.fakePwd = fakePwd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEmailExist() {
        return emailExist;
    }

    public void setEmailExist(String emailExist) {
        this.emailExist = emailExist;
    }

    public String getFakePwd() {
        return fakePwd;
    }

    public void setFakePwd(String fakePwd) {
        this.fakePwd = fakePwd;
    }
}
