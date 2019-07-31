package com.lpky.taopr.soool.Model;

import android.content.Context;
import android.util.Log;


import com.lpky.taopr.soool.Object.LoginItem;
import com.lpky.taopr.soool.Object.LoginSessionItem;
import com.lpky.taopr.soool.Networking.APIClient;
import com.lpky.taopr.soool.Networking.APIService;
import com.lpky.taopr.soool.Presenter.LoginPresenter;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginModel {

    String TAG = "LoginModel";
    String result = "", accountNick = "", accountImage = "";
    int accountNo, accountPoint, accountBc, accountCc;

    private LoginPresenter loginPresenter;
    private LoginSessionItem item;

    Context context;

    //생성자 shared를 위해 context를 사용.
    public LoginModel(LoginPresenter loginPresenter, Context context) {
        this.loginPresenter = loginPresenter;
        this.context = context;
    }

    public void getPw(LoginItem userItem){
        Observable.just(userItem.getId())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>()
                {
                    @Override
                    public Boolean apply(String s) throws Exception
                    {
                        try
                        {

                            //Retrofit 사용 시 apiservice와 apiclient를 사용하자.
                            //Retrofit 객체 불러와서 선언하는 부분.
                            APIService service = APIClient.getClient().create(APIService.class);

                            //Call함수로 LoginActivity(view)로부터 받은 인자를 서버로 넘기는 부분.
                            Call<ResponseBody> callServer = service.getaccountPw(userItem.getId());

                            //서버로 부터 응답을 받는 부분.
                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();

                                        Log.i(TAG, "onResponse: " + msg);
                                        // 복호화

                                        // 로그인 시 작성한 이메일이 가입된 이메일이 아닐경우
                                        if (msg.equals("false")){
                                            loginPresenter.loginResponse("nee");
                                        }
                                        // 이메일이 있는 경우
                                        else{
                                            loginPresenter.confirmPw(userItem,msg);
                                        }
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    Log.d(TAG, "onFailure: 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onFailure: 실패2");
                            Log.e(TAG, "apply: ", e);
                        }

                        return true;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.d(TAG, "onSubscribe : wfpowjefpwfepowfjwpfojwfepojfe");
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext: wfpowjefpwfepowfjwpfojwfepojfe");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : wfpowjefpwfepowfjwpfojwfepojfe");
                    }
                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "onComplete : wfpowjefpwfepowfjwpfojwfepojfe");
                    }
                });
    }

    //LoginActvity(view)로부터 입력받은 값을 model에서 로그인 처리를 위해 만든 메서드.
    public void login(LoginItem userItem) {
//        Log.d("in model.login()", userItem.getId() + "//" + userItem.getPwd());
//        if(userItem.getId().equals("goo428") && userItem.getPwd().equals("123")) {
//            loginPresenter.loginResponse(true);
//        }else {
//            loginPresenter.loginResponse(false);
//        }
        Log.d(TAG, "시작");

        //RxJava 사용 아래 보이는 observable. 책으로 더 공부해봐야할 듯. 아직 설명을 못하겠음.
        Observable.just(userItem.getId(), userItem.getPwd())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>()
                {
                    @Override
                    public Boolean apply(String s) throws Exception
                    {
                        try
                        {
                            Log.d(TAG, "시작 바로 전");
                            //Retrofit 사용 시 apiservice와 apiclient를 사용하자.
                            //Retrofit 객체 불러와서 선언하는 부분.
                            APIService service = APIClient.getClient().create(APIService.class);

                            //Call함수로 LoginActivity(view)로부터 받은 인자를 서버로 넘기는 부분.
                            Call<ResponseBody> callServer = service.getUserItem(userItem.getId().toString(), userItem.getPwd().toString());

                            //서버로 부터 응답을 받는 부분.
                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();
                                        Log.d(TAG, "onResponse 1st : "+msg);
                                        JSONArray jsonArray = new JSONArray(msg);
                                        for (int i=0; i<jsonArray.length(); i++) {
                                            JSONObject returnData = jsonArray.getJSONObject(i);

                                            result = returnData.getString("result");

                                            if(result.equals("nee")) {
                                                Log.d(TAG, "onResponse nee : not exist email");
                                                //LoginActivity(view)로 결과 전송
                                                loginPresenter.loginResponse("nee");
                                            } else if (result.equals("false")) {
                                                Log.d(TAG, "onResponse false : false");
                                                //LoginActivity(view)로 결과 전송
                                                loginPresenter.loginResponse("false");
                                            } else if (result.equals("true")){
                                                //이메일 비밀번호 맞았을 경우 응답 값들을 shared에 저장해야함.
                                                accountNo  = returnData.getInt("accountNo");
                                                accountNick = returnData.getString("accountNick");
                                                accountImage = returnData.getString("accountImage");
                                                accountPoint = returnData.getInt("accountPoint");
                                                accountBc = returnData.getInt("accountBc");
                                                accountCc = returnData.getInt("accountCc");

                                                Log.d(TAG, "onResponse true : "+accountNo);
                                                Log.d(TAG, "onResponse true : "+accountNick);
                                                Log.d(TAG, "onResponse true : "+accountImage);
                                                Log.d(TAG, "onResponse true : "+accountPoint);
                                                Log.d(TAG, "onResponse true : "+accountBc);
                                                Log.d(TAG, "onResponse true : "+accountCc);

                                                item = new LoginSessionItem(accountNo, accountNick, accountImage, accountPoint, accountBc, accountCc, userItem.isAutologinStatus());
                                                // Gson 인스턴스 생성
                                                Gson gson = new GsonBuilder().create();
                                                // JSON 으로 변환
                                                String userClass = gson.toJson(item, LoginSessionItem.class);
                                                //shared에 객체 저장
                                                LoginSharedPreferences.LoginUserSave(context, "LoginAccount", userClass);
                                                //LoginActivity(view)로 결과 전송
                                                loginPresenter.loginResponse("true");
                                            }
                                        }
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    Log.d(TAG, "onFailure: 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onFailure: 실패2");
                            Log.e(TAG, "apply: ", e);
                        }

                        return true;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.d(TAG, "onSubscribe : wfpowjefpwfepowfjwpfojwfepojfe");
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext: wfpowjefpwfepowfjwpfojwfepojfe");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : wfpowjefpwfepowfjwpfojwfepojfe");
                    }
                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "onComplete : wfpowjefpwfepowfjwpfojwfepojfe");
                    }
                });
    }
}
