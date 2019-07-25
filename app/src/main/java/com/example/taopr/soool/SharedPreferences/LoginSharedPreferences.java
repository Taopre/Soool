package com.example.taopr.soool.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.taopr.soool.Object.LoginSessionItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginSharedPreferences {

    private String nameOfShared = "LoginUser";
    private String nameOfPWIv = "pwIv";
    private String nameOfGuide = "guide";
    private String keyOfShared = "LoginAccount";
    private String keyOfLoginId = "LoginId";
    private String TAG = "쉐어드";

    public static void LoginUserSave(Context context, String key, String Value){
        SharedPreferences pref = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, Value);
        editor.commit();
    }


    public static String LoginUserLoad(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        String data = pref.getString(key, "");
        return data;
    }

    public static void LoginUserDelete(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void LoginUserLogOut(Context context, String key){
        String data = LoginSharedPreferences.LoginUserLoad(context,key);
        Gson gson = new GsonBuilder().create();
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        loginSessionItem.setAccountAutoLogin(false);
        // JSON 으로 변환
        String userClass = gson.toJson(loginSessionItem, LoginSessionItem.class);
        LoginSharedPreferences.LoginUserSave(context,"LoginAccount",userClass);

    }


    public static int getAccountNo(Context context,String key){
        int accountNo;
        String data = LoginSharedPreferences.LoginUserLoad(context,key);
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNo = loginSessionItem.getAccountNo();
        return accountNo;
    }

    // 쉐어드에 저장한 유저정보인 loginSessionItem 객체를 가져와서 닉네임만 변경 후 다시 객체를 json 화 해서
    // 쉐어드에 저장
    public void UpdateUserNickname(Context context,String userNickname){
        String data = LoginSharedPreferences.LoginUserLoad(context,this.keyOfShared);
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환

        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);

        loginSessionItem.setAccountNick(userNickname);

        String loginSessionItemSt = gson.toJson(loginSessionItem, LoginSessionItem.class);

        //쉐어드에 객체 저장
        LoginSharedPreferences.LoginUserSave(context, this.keyOfShared, loginSessionItemSt);
    }

    public String getAccountNick(Context context){
        String accountNick;
        String data = LoginSharedPreferences.LoginUserLoad(context,this.keyOfShared);
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();
        return accountNick;
    }

    public void savePWIv(Context context, String key, byte[] iv){
        SharedPreferences pref = context.getSharedPreferences(nameOfPWIv, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, Base64.encodeToString(iv,Base64.DEFAULT));

        Log.i(TAG, "savePWIv: " + Base64.encodeToString(iv,Base64.DEFAULT));
        editor.commit();
    }

    public byte[] getPWIv(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences( nameOfPWIv, Context.MODE_PRIVATE);
        String data = pref.getString(key, "aaaaa");

        Log.i(TAG, "getPWIv: " + data);
        byte[] Iv = Base64.decode(data,Base64.DEFAULT);

        return Iv;
    }

    // 가이드 다시 보지 않기

    public void setHasSeeGuide(Context context){
        SharedPreferences pref = context.getSharedPreferences(nameOfGuide, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("hasSeeGuide",true);
        editor.commit();
    }

    public Boolean getHasSeeGuide(Context context){
        SharedPreferences pref = context.getSharedPreferences( nameOfGuide, Context.MODE_PRIVATE);
        boolean data = pref.getBoolean("hasSeeGuide", false);
        return data;
    }

}
