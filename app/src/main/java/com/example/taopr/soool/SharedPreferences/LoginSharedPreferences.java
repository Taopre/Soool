package com.example.taopr.soool.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

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
}
