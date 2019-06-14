package com.example.taopr.soool.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.LoginItem;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.PassFIndResponse;
import com.example.taopr.soool.Presenter.Interface.PassFindInter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
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

public class PassFindPresenter extends BasePresenter implements PassFindInter {

    private APIService apiService;
    private Context context;
    private PassFindPresenter.View view;
    private String TAG = "PassFindPresenter";

    String result = "", accountNick = "", accountImage = "";
    int accountNo, accountPoint, accountBc, accountCc;

    LoginSessionItem item;
    PassFIndResponse passFIndResponse;
    Gson gson = new Gson();

    public PassFindPresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void EmailCheckReq(String email) {
        Observable.just("")
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
                            apiService = APIClient.getClient1().create(APIService.class);
                            Call<ResponseBody> callServer = apiService.checkEmailInPassFind(email);

                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String message = response.body().string();
                                        message = message.substring(message.indexOf("StartJsonData"), message.indexOf("EndJsonData"));
                                        message = message.substring(13);
//                                        Log.d(TAG, "passFIndResponse: "+message);

                                        passFIndResponse = gson.fromJson(message, PassFIndResponse.class);
                                        view.EmailCheckResp(passFIndResponse);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 콜백 실패
                                    view.EmailCheckResp(passFIndResponse);
                                    Log.d(TAG, "onFailure: 콜백 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            // 통신 실패
                            view.EmailCheckResp(passFIndResponse);
                            Log.d(TAG, "onFailure: 통신 실패");
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
                        Log.d(TAG, "onSubscribe : "+d);
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext: "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : ",e);
                    }
                    @Override
                    public void onComplete()
                    {
                    }
                });
    }

    @Override
    public void login(LoginItem userItem) {
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
                                                view.loginResponse("nee");
                                            } else if (result.equals("false")) {
                                                Log.d(TAG, "onResponse false : false");
                                                //LoginActivity(view)로 결과 전송
                                                view.loginResponse("false");
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
                                                view.loginResponse("true");
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
                                    view.loginResponse("false");
                                    Log.d(TAG, "onFailure: 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            view.loginResponse("false");
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
                        Log.d(TAG, "onSubscribe : "+d);
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext: "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : "+e);
                    }
                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "onComplete : 완료");
                    }
                });
    }

}
