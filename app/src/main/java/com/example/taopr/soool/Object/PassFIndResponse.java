package com.example.taopr.soool.Object;

public class PassFIndResponse {

    String result;
    String emailExist;

    public PassFIndResponse (String result, String emailExist) {
        this.emailExist = emailExist;
        this.result = result;
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
}
