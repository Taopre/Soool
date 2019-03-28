package com.example.taopr.soool.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSharedPreferences {
    public static void LoginUserSave(Context context, String Name, String Value){
        SharedPreferences pref = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Name, Value);
        editor.commit();
    }


    public static String LoginUserLoad(Context context, String Name){
        SharedPreferences pref = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        String data = pref.getString(Name, "");
        return data;
    }
}
