
package com.example.taopr.soool.Facebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.taopr.soool.View.SignUpActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class LoginCallback implements FacebookCallback<LoginResult> {
    // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.
    private static String TAG = "FACEBOOK_API";
    
    Context startingContext;

    public LoginCallback(Context context){
        Log.i(TAG, "LoginCallback: ");
        this.startingContext = context;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        Log.i(TAG, "onSuccess");

        requestMe(loginResult.getAccessToken());
    }


    // 로그인 창을 닫을 경우, 호출됩니다.
    @Override
    public void onCancel() {
        Log.i(TAG, "onCancel: ");
    }


    // 로그인 실패 시에 호출됩니다.
    @Override
    public void onError(FacebookException error) {
        Log.i(TAG, "onError: " +error.getMessage());

    }

    // 사용자 정보 요청

    public void requestMe(AccessToken token) {

        GraphRequest graphRequest = GraphRequest.newMeRequest(token,

                new GraphRequest.GraphJSONObjectCallback() {

                    @Override

                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.i(TAG,object.toString());
                        try {

                            // facebook 계정을 통해 이메일을 받아왔는지 확인

                            String snsAccountEmail = "";
                            Iterator i = object.keys();
                            while(i.hasNext())
                            {
                                String b = i.next().toString();
                                if(b.equals("email")){
                                    snsAccountEmail = object.getString("email");
                                }
                            }
                            redirectSignupActivity(startingContext,snsAccountEmail);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    private void redirectSignupActivity(Context startingContext,String snsAccountEmail) {

        facebookLogout();
        final Intent intent = new Intent(startingContext, SignUpActivity.class);
        intent.putExtra("throughSNS",true);

        intent.putExtra("snsAccountEmail",snsAccountEmail);
        startingContext.startActivity(intent);

    }

    private void facebookLogout() {
        LoginManager.getInstance().logOut();
    }
}
