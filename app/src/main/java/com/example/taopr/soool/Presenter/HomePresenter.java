package com.example.taopr.soool.Presenter;

import android.content.Context;

import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Presenter.Interface.HomeInter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HomePresenter implements HomeInter{

    private View view;
    private Context context;

    public HomePresenter(Context context){
        this.context = context;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void getAccountNo() {
        String data = LoginSharedPreferences.LoginUserLoad(context, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);

        int accountNo = loginSessionItem.getAccountNo();

        view.getAccountNoSuc(accountNo);
    }

}
