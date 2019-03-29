package com.example.taopr.soool.Model;

import android.content.SharedPreferences;
import android.util.Log;


import com.example.taopr.soool.LoginItem;
import com.example.taopr.soool.LoginSessionItem;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Presenter.LoginPresenter;

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
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModel {

    String TAG = "LoginModel";
    String result = "", accountNick = "", accountImage = "";
    int accountNo, accountPoint, accountBc, accountCc;

    private LoginPresenter loginPresenter;
    private LoginSessionItem item;

    public LoginModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public void login(LoginItem userItem) {
//        Log.d("in model.login()", userItem.getId() + "//" + userItem.getPwd());
//        if(userItem.getId().equals("goo428") && userItem.getPwd().equals("123")) {
//            loginPresenter.loginResponse(true);
//        }else {
//            loginPresenter.loginResponse(false);
//        }
        Log.d(TAG, "시작");

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
//                            Retrofit retrofit = new Retrofit.Builder()
//                                    .baseUrl("http://")
////                                    .baseUrl("http://")
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .build();
                            //Retrofit 사용 시 apiservice와 apiclient를 사용하자.
                            APIService service = APIClient.getClient().create(APIService.class);

                            Call<ResponseBody> callServer = service.getUserItem(userItem.getId().toString(), userItem.getPwd().toString());
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
                                            //result는 이메일 존재여부를 받는 변수.
                                            if(result.equals("nee")) {
                                                Log.d(TAG, "onResponse nee : not exist email");
                                            } else if (result.equals("false")) {
                                                Log.d(TAG, "onResponse false : false");
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

                                                item = new LoginSessionItem(accountNo, accountNick, accountImage, accountPoint, accountBc, accountCc);
                                                loginPresenter.loginDataSend(item);
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
                    }
                    @Override
                    public void onNext(Boolean s)
                    {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onComplete()
                    {
                    }
                });
    }
}
