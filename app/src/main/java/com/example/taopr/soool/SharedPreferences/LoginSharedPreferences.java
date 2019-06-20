package com.example.taopr.soool.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.taopr.soool.Object.LoginSessionItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginSharedPreferences {
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

    public static int getAccountNo(Context context,String key){
        int accountNo;
        String data = LoginSharedPreferences.LoginUserLoad(context,key);
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNo = loginSessionItem.getAccountNo();
        return accountNo;
    }
}
